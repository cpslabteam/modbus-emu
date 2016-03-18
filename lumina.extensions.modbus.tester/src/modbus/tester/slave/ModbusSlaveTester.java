package modbus.tester.slave;

import modbus.slave.ModbusDataType;
import modbus.slave.ModbusSlaveLib;
import modbus.slave.exception.InvalidSlaveException;

/**
 * The Class ModbusSlaveEmu.
 */
public final class ModbusSlaveTester {

    private static final int PORT = 1502;
    private static final ModbusSlaveLib SLAVE = new ModbusSlaveLib(PORT);
    private static final long LONG_VALUE = 1234567891011121314L;
    private static final double DOUBLE_VALUE = 0.12345678910111213;
    private static final float FLOAT_VALUE = 1968.1968f;
    private static final int INT_VALUE = -123456789;
    private static final short SHORT_VALUE = -19689;

    private static final int SHORT_REG = 10;
    private static final int INT_REG = 11;
    private static final int FLOAT_REG = 13;
    private static final int LONG_REG = 15;
    private static final int DOUBLE_REG = 19;


    /**
     * Instantiates a new modbus slave emu.
     */
    private ModbusSlaveTester() {

    }

    /**
     * The main method.
     * 
     * @param args the arguments
     */
    public static void main(String[] args) {
        try {
            final int slaveId2 = 2;
            final int slaveId3 = 3;
            final int slaveId4 = 4;
            final int nRegisters = 500;

            /**
             * Writes discrete input and coil registers
             */
            writeBinaryRegisters(slaveId2, nRegisters);

            /**
             * Writes holding registers
             */
            writeHoldingRegisters(slaveId3);

            /**
             * Writes input registers
             */
            writeInputRegisters(slaveId4);

            SLAVE.runModbusTcpSlave();
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        } catch (InvalidSlaveException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write binary registers.
     *
     * @param slaveId the slave id
     * @param nRegisters the number of registers
     * @throws InvalidSlaveException the invalid slave exception
     */
    public static void writeBinaryRegisters(int slaveId, int nRegisters) throws InvalidSlaveException {
        for (int i = 0; i < nRegisters; i++) {
            if (i % 2 == 0) {
                SLAVE.setCoil(slaveId, i, true);
                SLAVE.setDiscreteInput(slaveId, i, false);
            } else {
                SLAVE.setCoil(slaveId, i, false);
                SLAVE.setDiscreteInput(slaveId, i, true);
            }
        }
    }

    /**
     * Write holding registers.
     *
     * @param slaveId the slave id
     * @throws InvalidSlaveException the invalid slave exception
     */
    public static void writeHoldingRegisters(int slaveId) throws InvalidSlaveException {
        SLAVE.setHoldingRegister(slaveId, SHORT_REG, SHORT_VALUE, ModbusDataType.SHORT);
        SLAVE.setHoldingRegister(slaveId, INT_REG, INT_VALUE, ModbusDataType.INT);
        SLAVE.setHoldingRegister(slaveId, FLOAT_REG, FLOAT_VALUE, ModbusDataType.FLOAT);
        SLAVE.setHoldingRegister(slaveId, LONG_REG, LONG_VALUE, ModbusDataType.LONG);
        SLAVE.setHoldingRegister(slaveId, DOUBLE_REG, DOUBLE_VALUE, ModbusDataType.DOUBLE);
    }

    /**
     * Write input registers.
     *
     * @param slaveId the slave id
     * @throws InvalidSlaveException the invalid slave exception
     */
    public static void writeInputRegisters(int slaveId) throws InvalidSlaveException {
        SLAVE.setInputRegister(slaveId, SHORT_REG, SHORT_VALUE, ModbusDataType.SHORT);
        SLAVE.setInputRegister(slaveId, INT_REG, INT_VALUE, ModbusDataType.INT);
        SLAVE.setInputRegister(slaveId, FLOAT_REG, FLOAT_VALUE, ModbusDataType.FLOAT);
        SLAVE.setInputRegister(slaveId, LONG_REG, LONG_VALUE, ModbusDataType.LONG);
        SLAVE.setInputRegister(slaveId, DOUBLE_REG, DOUBLE_VALUE, ModbusDataType.DOUBLE);
    }
}
