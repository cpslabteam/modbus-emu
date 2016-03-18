package modbus.slave;

import com.serotonin.modbus4j.code.DataType;

/**
 * The Enum ModbusDataType responsible for mapping modbus data types into java data types.
 */
public enum ModbusDataType {

    /**
     * The short data type corresponds to modbus 2 byte int signed. It requires 1 modbus
     * registers to store a value.
     */
    SHORT(DataType.TWO_BYTE_INT_SIGNED),

    /**
     * The int data type corresponds to modbus 4 byte int signed. It requires 2 modbus
     * registers to store a value.
     */
    INT(DataType.FOUR_BYTE_INT_SIGNED),

    /**
     * The float data type corresponds to modbus 4 byte float signed. It requires 2 modbus
     * registers to store a value.
     */
    FLOAT(DataType.FOUR_BYTE_FLOAT),

    /**
     * The long data type corresponds to modbus 8 byte int signed. It requires 4 modbus
     * registers to store a value.
     */
    LONG(DataType.EIGHT_BYTE_INT_SIGNED),


    /**
     * The double data type corresponds to modbus 8 byte float signed.It requires 4 modbus
     * registers to store a value.
     */
    DOUBLE(DataType.EIGHT_BYTE_FLOAT);


    private final int dataType;

    ModbusDataType(int dataType) {
        this.dataType = dataType;
    }

    public int getType() {
        return dataType;
    }

}
