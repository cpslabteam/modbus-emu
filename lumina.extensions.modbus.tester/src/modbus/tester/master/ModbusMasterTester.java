package modbus.tester.master;

import java.util.Arrays;

import modbus.master.ModbusDataType;
import modbus.master.ModbusMasterLib;
import modbus.master.exception.ModbusCommunicationException;
import modbus.master.exception.ModbusConnectionException;
import modbus.master.exception.ModbusResponseException;

/**
 * The Class ModbusMasterEmu.
 */
public final class ModbusMasterTester {

    private static final ModbusMasterLib MASTER = new ModbusMasterLib();

    private static final int SHORT_REG = 10;
    private static final int INT_REG = 11;
    private static final int FLOAT_REG = 13;
    private static final int LONG_REG = 15;
    private static final int DOUBLE_REG = 19;

    /**
     * Instantiates a new modbus master emu.
     */
    private ModbusMasterTester() {
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
            final int initialRegister = 10;
            final int nRegisters = 6;
            final String address = "127.0.0.1";
            final int port = 1502;

            MASTER.createModbusTcpMaster(address, port, true);

            /**
             * Test Coil registers
             */

            System.out.println("--------------------");
            System.out.println("Test Coil registers");
            readCoils(slaveId2, 1, nRegisters);
            writeCoilsOneByOne(slaveId2, 1, nRegisters);
            readCoils(slaveId2, 1, nRegisters);
            writeManyCoils(slaveId2, 1, nRegisters, new boolean[] { false, true, false, true,
                    false, true });
            readCoils(slaveId2, 1, nRegisters);
            System.out.println("--------------------");

            /**
             * Test discrete input
             */

            System.out.println("--------------------");
            System.out.println("Test discrete input");
            readDiscreteInput(slaveId2, 1, nRegisters);
            System.out.println("--------------------");

            /**
             * Test Holding registers
             */

            System.out.println("--------------------");
            System.out.println("Test holding registers");
            readHoldingRegisters(slaveId3);
            writeHoldingRegisters(slaveId3);
            readHoldingRegisters(slaveId3);
            final short[] values = new short[] { 0, 1, 3, 4, 5, 6 };
            writeManyHoldingRegisters(slaveId3, initialRegister, values);
            readManyHoldingRegisters(slaveId3, initialRegister, nRegisters);
            System.out.println("--------------------");

            /**
             * Test Input registers
             */

            System.out.println("--------------------");
            System.out.println("Test input registers");
            readInputRegisters(slaveId4);
            readManyInputRegisters(slaveId4, initialRegister, nRegisters);
            System.out.println("--------------------");

            MASTER.destroyModbusMaster();
        } catch (ModbusConnectionException e) {
            System.err.println(e.getMessage());
        } catch (ModbusCommunicationException e) {
            e.printStackTrace();
        } catch (ModbusResponseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write coils one by one.
     *
     * @param slaveId the slave id
     * @param initialRegister the initial register
     * @param nRegisters the number of registers
     * @throws ModbusResponseException the modbus response exception
     * @throws ModbusCommunicationException the modbus communication exception
     */
    public static void writeCoilsOneByOne(int slaveId, int initialRegister, int nRegisters) throws ModbusResponseException,
            ModbusCommunicationException {
        for (int i = 1; i < nRegisters + 1; i++) {
            MASTER.writeCoil(slaveId, i, true);
        }
    }

    /**
     * Write many coils.
     *
     * @param slaveId the slave id
     * @param initialRegister the initial register
     * @param nRegisters the number of registers
     * @param values the values
     * @throws ModbusResponseException the modbus response exception
     * @throws ModbusCommunicationException the modbus communication exception
     */
    public static void writeManyCoils(int slaveId,
                                      int initialRegister,
                                      int nRegisters,
                                      boolean[] values) throws ModbusResponseException,
            ModbusCommunicationException {
        MASTER.writeCoils(slaveId, initialRegister, values);
    }

    /**
     * Read coils.
     *
     * @param slaveId the slave id
     * @param initialRegister the initial register
     * @param nRegisters the number of registers
     * @throws ModbusResponseException the modbus response exception
     * @throws ModbusCommunicationException the modbus communication exception
     */
    public static void readCoils(int slaveId, int initialRegister, int nRegisters) throws ModbusResponseException,
            ModbusCommunicationException {
        System.out.println(Arrays.toString(MASTER.readCoils(slaveId, initialRegister, nRegisters)));
    }

    /**
     * Read discrete input.
     *
     * @param slaveId the slave id
     * @param initialRegister the initial register
     * @param nRegisters the number of registers
     * @throws ModbusResponseException the modbus response exception
     * @throws ModbusCommunicationException the modbus communication exception
     */
    public static void readDiscreteInput(int slaveId, int initialRegister, int nRegisters) throws ModbusResponseException,
            ModbusCommunicationException {
        System.out.println(Arrays.toString(MASTER.readDiscreteInput(slaveId, initialRegister,
                nRegisters)));
    }



    /**
     * Read holding registers.
     *
     * @param slaveId the slave id
     * @throws ModbusCommunicationException the modbus communication exception
     * @throws ModbusResponseException the modbus response exception
     */
    public static void readHoldingRegisters(int slaveId) throws ModbusCommunicationException,
            ModbusResponseException {
        System.out.print(" SHORT "
                + MASTER.readHoldingRegister(slaveId, SHORT_REG, ModbusDataType.SHORT));
        System.out
                .print(" INT " + MASTER.readHoldingRegister(slaveId, INT_REG, ModbusDataType.INT));
        System.out.print(" FLOAT "
                + MASTER.readHoldingRegister(slaveId, FLOAT_REG, ModbusDataType.FLOAT));
        System.out.print(" LONG "
                + MASTER.readHoldingRegister(slaveId, LONG_REG, ModbusDataType.LONG));
        System.out.println(" DOUBLE "
                + MASTER.readHoldingRegister(slaveId, DOUBLE_REG, ModbusDataType.DOUBLE));
    }

    /**
     * Read input registers.
     *
     * @param slaveId the slave id
     * @throws ModbusCommunicationException the modbus communication exception
     * @throws ModbusResponseException the modbus response exception
     */
    public static void readInputRegisters(int slaveId) throws ModbusCommunicationException,
            ModbusResponseException {
        System.out.print(" SHORT "
                + MASTER.readInputRegister(slaveId, SHORT_REG, ModbusDataType.SHORT));
        System.out.print(" INT " + MASTER.readInputRegister(slaveId, INT_REG, ModbusDataType.INT));
        System.out.print(" FLOAT "
                + MASTER.readInputRegister(slaveId, FLOAT_REG, ModbusDataType.FLOAT));
        System.out.print(" LONG "
                + MASTER.readInputRegister(slaveId, LONG_REG, ModbusDataType.LONG));
        System.out.println(" DOUBLE "
                + MASTER.readInputRegister(slaveId, DOUBLE_REG, ModbusDataType.DOUBLE));
    }

    /**
     * Write holding registers.
     *
     * @param slaveId the slave id
     * @throws ModbusResponseException the modbus response exception
     * @throws ModbusCommunicationException the modbus communication exception
     */
    public static void writeHoldingRegisters(int slaveId) throws ModbusResponseException,
            ModbusCommunicationException {
        int value = 0;
        MASTER.writeHoldingRegister(slaveId, SHORT_REG, value++, ModbusDataType.SHORT);
        MASTER.writeHoldingRegister(slaveId, INT_REG, value++, ModbusDataType.INT);
        MASTER.writeHoldingRegister(slaveId, FLOAT_REG, value++, ModbusDataType.FLOAT);
        MASTER.writeHoldingRegister(slaveId, LONG_REG, value++, ModbusDataType.LONG);
        MASTER.writeHoldingRegister(slaveId, DOUBLE_REG, value++, ModbusDataType.DOUBLE);
    }

    /**
     * Write many holding registers.
     *
     * @param slaveId the slave id
     * @param initialRegister the initial register
     * @param values the values
     * @throws ModbusResponseException the modbus response exception
     * @throws ModbusCommunicationException the modbus communication exception
     */
    public static void writeManyHoldingRegisters(int slaveId, int initialRegister, short[] values) throws ModbusResponseException,
            ModbusCommunicationException {
        MASTER.writeHoldingRegisters(slaveId, initialRegister, values);
    }

    /**
     * Read many holding registers.
     *
     * @param slaveId the slave id
     * @param initialRegister the initial register
     * @param len the length
     * @throws ModbusResponseException the modbus response exception
     * @throws ModbusCommunicationException the modbus communication exception
     */
    public static void readManyHoldingRegisters(int slaveId, int initialRegister, int len) throws ModbusResponseException,
            ModbusCommunicationException {
        System.out.println(Arrays.toString(MASTER.readHoldingRegisters(slaveId, initialRegister,
                len)));
    }

    /**
     * Read many input registers.
     *
     * @param slaveId the slave id
     * @param initialRegister the initial register
     * @param len the length
     * @throws ModbusResponseException the modbus response exception
     * @throws ModbusCommunicationException the modbus communication exception
     */
    public static void readManyInputRegisters(int slaveId, int initialRegister, int len) throws ModbusResponseException,
            ModbusCommunicationException {
        System.out
                .println(Arrays.toString(MASTER.readInputRegisters(slaveId, initialRegister, len)));
    }
}
