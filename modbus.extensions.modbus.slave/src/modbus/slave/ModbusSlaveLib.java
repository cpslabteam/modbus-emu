package modbus.slave;

import java.util.HashMap;

import modbus.slave.exception.InvalidSlaveException;

import com.serotonin.modbus4j.BasicProcessImage;
import com.serotonin.modbus4j.ModbusSlaveSet;
import com.serotonin.modbus4j.ProcessImage;
import com.serotonin.modbus4j.code.RegisterRange;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.ip.tcp.TcpSlave;

/**
 * The Class ModbusSlave.
 */
public class ModbusSlaveLib {

    /** The listener. */
    private final ModbusSlaveSet listener;

    /** The slave values. */
    private final HashMap<Integer, ProcessImage> slaveValues;

    /** The wait time. */
    private final int waitTime = 200;

    public ModbusSlaveLib(int port) {
        this.slaveValues = new HashMap<Integer, ProcessImage>();
        this.listener = new TcpSlave(port, false);
    }

    /**
     * Run modbus tcp slave.
     * 
     * @throws InterruptedException interruped exception
     * @throws InvalidPropertiesException
     */
    public void runModbusTcpSlave() throws InterruptedException {
        new Thread(new Runnable() {
            public void run() {
                try {
                    listener.start();
                } catch (ModbusInitException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        while (true) {
            synchronized (listener) {
                listener.wait(waitTime);
            }
        }
    }

    /**
     * Sets the coil - read write register, boolean.
     * 
     * @param slaveId the slave id
     * @param offset the register address
     * @param value the value to write
     * @throws InvalidSlaveException invalid slave exception
     */
    public void setCoil(int slaveId, int offset, boolean value) throws InvalidSlaveException {
        getProcessImage(slaveId).setCoil(offset, value);
    }

    /**
     * Sets the discrete input - read only register, boolean.
     * 
     * @param slaveId the slave id
     * @param offset the register address
     * @param value the value to write
     * @throws InvalidSlaveException invalid slave exception
     */
    public void setDiscreteInput(int slaveId, int offset, boolean value) throws InvalidSlaveException {
        getProcessImage(slaveId).setInput(offset, value);
    }

    /**
     * Sets the holding register - read write register, short.
     * 
     * @param slaveId the slave id
     * @param offset the register address
     * @param value the value to write
     * @throws InvalidSlaveException invalid slave exception
     */
    public void setHoldingRegister(int slaveId, int offset, short value) throws InvalidSlaveException {
        getProcessImage(slaveId).setHoldingRegister(offset, value);
    }

    /**
     * Sets the holding register - read write register, any numeric type.
     *
     * @param slaveId the slave id
     * @param offset the register address
     * @param value the value to write
     * @param mdType the modbus data type format
     * @throws InvalidSlaveException the invalid slave exception
     */
    public void setHoldingRegister(int slaveId, int offset, Number value, ModbusDataType mdType) throws InvalidSlaveException {
        setRegister(slaveId, offset, value, RegisterRange.HOLDING_REGISTER, mdType);
    }

    /**
     * Sets the input register - read only register, short.
     * 
     * @param slaveId the slave id
     * @param offset the register address
     * @param value the value to write
     * @throws InvalidSlaveException invalid slave exception
     */
    public void setInputRegister(int slaveId, int offset, short value) throws InvalidSlaveException {
        getProcessImage(slaveId).setInputRegister(offset, value);
    }

    /**
     * Sets the input register - read only register, any numeric type.
     *
     * @param slaveId the slave id
     * @param offset the register address
     * @param value the value to write
     * @param mdType the modbus data type format
     * @throws InvalidSlaveException the invalid slave exception
     */
    public void setInputRegister(int slaveId, int offset, Number value, ModbusDataType mdType) throws InvalidSlaveException {
        setRegister(slaveId, offset, value, RegisterRange.INPUT_REGISTER, mdType);
    }

    /**
     * Sets the register.
     *
     * @param slaveId the slave id
     * @param offset the register address
     * @param value the value
     * @param range the register type
     * @param mdType the modbus datatype format
     * @throws InvalidSlaveException the invalid slave exception
     */
    private void setRegister(int slaveId, int offset, Number value, int range, ModbusDataType mdType) throws InvalidSlaveException {
        ((BasicProcessImage) getProcessImage(slaveId)).setRegister(range, offset, mdType.getType(),
                value);
    }

    /**
     * Gets the process image.
     * 
     * @param slaveId the slave id
     * @return the process image
     * @throws InvalidSlaveException the invalid slave exception
     */
    private ProcessImage getProcessImage(int slaveId) throws InvalidSlaveException {
        if (slaveId == 0) {
            throw new InvalidSlaveException();
        } else if (slaveValues.containsKey(slaveId)) {
            return slaveValues.get(slaveId);
        } else {
            final BasicProcessImage processImage = new BasicProcessImage(slaveId);
            processImage.setAllowInvalidAddress(false);
            processImage.setInvalidAddressValue(Short.MIN_VALUE);
            slaveValues.put(slaveId, processImage);
            listener.addProcessImage(processImage);
            return processImage;
        }
    }
}
