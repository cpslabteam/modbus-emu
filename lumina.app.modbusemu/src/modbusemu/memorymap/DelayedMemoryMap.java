package modbusemu.memorymap;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;

import modbusemu.PropertiesLoader;
import modbusemu.exception.PropertiesLoadingException;
import modbusemu.memorymap.databaseconnector.DBConnector;
import modbusemu.memorymap.dataoperator.DataLoader;
import modbusemu.memorymap.dataoperator.DataWriter;
import modbusemu.memorymap.dataoperator.IDataOperator;

/**
 * The Class DelayedMemoryMap.
 */
public class DelayedMemoryMap {

    /**
     * An Hash map with one entry per datapoint id, which has a list of readings.
     */
    private final ConcurrentHashMap<String, DelayQueue<SensorReading>> readings;

    /** The modbus writing listeners. */
    private final List<IWriteListener> listeners;

    /** The memory map which stores written values. */
    private final Map<String, Long> memoryMap;

    /**
     * The address map, which has the modbus slave address of each datapoint.
     */
    private Map<String, Integer> addressMap;
    /**
     * The address map which has the modbus register address of each datapoint.
     */
    private Map<String, Integer> registerMap;
    /**
     * Initial modbus written value.
     */
    private final int initValue = 0;

    /**
     * The listener interface for receiving IWrite events. The class that is interested in
     * processing a IWrite event implements this interface, and the object created with
     * that class is registered with a component using the component's
     * <code>addIWriteListener</code> method. When the IWrite event occurs, that object's
     * appropriate method is invoked.
     * 
     * @see IWriteEvent
     */
    public interface IWriteListener {

        /**
         * On write.
         * 
         * @param slaveId the slave id
         * @param nRegister the number of registers
         * @param value the value
         * @param client the client
         */
        void onWrite(int slaveId, int nRegister, long value, DelayedMemoryMap client);
    }


    /**
     * Adds a listener.
     * 
     * @param l the listener
     */
    public void addListener(IWriteListener l) {
        listeners.add(l);
    }


    /**
     * Instantiates a new delayed memory map.
     */
    public DelayedMemoryMap() {
        readings = new ConcurrentHashMap<String, DelayQueue<SensorReading>>();
        listeners = new LinkedList<IWriteListener>();
        memoryMap = new HashMap<String, Long>();
    }

    /**
     * Writes a value on modbus slave.
     * 
     * @param address the address
     * @param value the value
     */
    public void write(String address, long value) {
        memoryMap.put(address, value);

        for (IWriteListener l : listeners) {
            l.onWrite(addressMap.get(address), registerMap.get(address), value, this);
        }
    }

    /**
     * Writes delayed sensor readings, for later processing.
     * 
     * @param dpid the datapoint id
     * @param timestamp the timestamp
     * @param reading the reading
     */
    public void writeDelayed(String dpid, long timestamp, long reading) {
        final DelayQueue<SensorReading> queue = readings.get(dpid);

        if (queue != null) {
            queue.add(new SensorReading(System.nanoTime() + timestamp, reading));
        } else {
            DelayQueue<SensorReading> delayedQueue = new DelayQueue<SensorReading>();
            write(dpid, reading);
            //delayedQueue.add(new SensorReading(System.nanoTime() + timestamp, reading));
            readings.put(dpid, delayedQueue);
        }
    }

    /**
     * The Class DataOperatorThread.
     */
    private class DataOperator extends
            Thread {
        private final IDataOperator operator;

        public DataOperator(IDataOperator operator) {
            super(operator.getName());
            this.operator = operator;
        }

        @Override
        public void run() {
            operator.start();
        }
    }

    /**
     * Initiates the delayed memory, running threads and loading properties.
     *
     * @param dbConnector the database connector
     * @param props emulator configuration properties
     * @throws PropertiesLoadingException properties loading failed
     */
    public void open(DBConnector dbConnector, Properties props) throws PropertiesLoadingException {
        addressMap = PropertiesLoader.getJSONMap("conf/modbusSlaveAddresses.json");
        registerMap = PropertiesLoader.getJSONMap("conf/modbusRegisterAddresses.json");

        for (Entry<String, Integer> address : addressMap.entrySet()) {
            write(address.getKey(), initValue);
        }

        dbConnector.setProperties(props);
        final long minTS = Long.parseLong(props.getProperty("min_ts"));
        final long maxTS = Long.parseLong(props.getProperty("max_ts"));
        final float simulatedTime = Float.parseFloat(props.getProperty("simulated_time"));
        final float realTime = Float.parseFloat(props.getProperty("real_time"));
        final int loadRate = Integer.parseInt(props.getProperty("load_rate"));
        final float timeRate = simulatedTime / realTime;

        final DataWriter dataWriter = new DataWriter(this, readings.entrySet());
        final DataLoader dataLoader = new DataLoader(this, dbConnector, loadRate, timeRate, minTS,
                maxTS);

        final DataOperator writeOperator = new DataOperator(dataWriter);
        final DataOperator loadOperator = new DataOperator(dataLoader);
        writeOperator.setDaemon(true);
        loadOperator.setDaemon(true);
        loadOperator.start();
        writeOperator.start();
        System.err.print("Loading datapoints...");
        System.err.println("Transfering datapoints...");
    }
}
