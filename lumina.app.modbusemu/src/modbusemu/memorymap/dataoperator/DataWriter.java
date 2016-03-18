package modbusemu.memorymap.dataoperator;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.DelayQueue;

import modbusemu.memorymap.DelayedMemoryMap;
import modbusemu.memorymap.SensorReading;

/**
 * The Class DataTransferThread.
 */
public class DataWriter
        implements IDataOperator {

    /** Set of datapoint address(key) and corresponding delayQueue(value). */
    private final Set<Entry<String, DelayQueue<SensorReading>>> readingQueues;
    /** The delayed memory. */
    private final DelayedMemoryMap memoryMap;
    /** Operator name. */
    private final String name = "Data Writer";


    public DataWriter(DelayedMemoryMap memoryMap,
                      Set<Entry<String, DelayQueue<SensorReading>>> readingQueues) {
        this.readingQueues = readingQueues;
        this.memoryMap = memoryMap;
    }

    /** Starts the data writer. */
    public void start() {
        while (true) {
            for (Entry<String, DelayQueue<SensorReading>> entry : readingQueues) {
                final SensorReading reading = entry.getValue().poll();
                if (reading != null) {
                    memoryMap.write(entry.getKey(), reading.getReading());
                }
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
