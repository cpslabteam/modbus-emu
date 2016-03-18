package modbusemu;

import java.util.Properties;

import modbus.slave.ModbusDataType;
import modbus.slave.ModbusSlaveLib;
import modbus.slave.exception.InvalidSlaveException;
import modbusemu.exception.PropertiesLoadingException;
import modbusemu.memorymap.DelayedMemoryMap;
import modbusemu.memorymap.DelayedMemoryMap.IWriteListener;
import modbusemu.memorymap.databaseconnector.PSQLConnector;

/**
 * The Data Transfer Application class.
 */
public final class ModbusEmu {

    private ModbusEmu() {
    }

    public static void main(String[] args) {
        try {
            final Properties props = PropertiesLoader.getProperties("conf/emu_config.properties");
            final int port = Integer.parseInt(props.getProperty("md_port"));
                        
            final DelayedMemoryMap memoryMap = new DelayedMemoryMap();
            final ModbusSlaveLib slaveLib = new ModbusSlaveLib(port);

            memoryMap.addListener(new IWriteListener() {
                @Override
                public void onWrite(int slaveId, int nRegister, long value, DelayedMemoryMap client) {
                    try {
                        slaveLib.setInputRegister(slaveId, nRegister, value, ModbusDataType.LONG);
                    } catch (InvalidSlaveException e) {
                        System.err.println("Modbus registry could not be written");
                    }
                }
            });

            memoryMap.open(new PSQLConnector(), props);
            slaveLib.runModbusTcpSlave();
        } catch (InterruptedException e) {
            System.err.println("Modbus slave was interrupted");
        } catch (PropertiesLoadingException e) {
            System.err.println(e.getMessage());
        }
    }
}
