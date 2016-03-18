package modbus.master;

import modbus.master.exception.ModbusCommunicationException;
import modbus.master.exception.ModbusConnectionException;
import modbus.master.exception.ModbusResponseException;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusLocator;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.RegisterRange;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.modbus4j.msg.ReadCoilsRequest;
import com.serotonin.modbus4j.msg.ReadCoilsResponse;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsRequest;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsResponse;
import com.serotonin.modbus4j.msg.ReadExceptionStatusRequest;
import com.serotonin.modbus4j.msg.ReadExceptionStatusResponse;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersResponse;
import com.serotonin.modbus4j.msg.ReadInputRegistersRequest;
import com.serotonin.modbus4j.msg.ReadInputRegistersResponse;
import com.serotonin.modbus4j.msg.ReportSlaveIdRequest;
import com.serotonin.modbus4j.msg.ReportSlaveIdResponse;
import com.serotonin.modbus4j.msg.WriteCoilRequest;
import com.serotonin.modbus4j.msg.WriteCoilsRequest;
import com.serotonin.modbus4j.msg.WriteRegistersRequest;
import com.serotonin.modbus4j.msg.WriteRegistersResponse;

/**
 * The Class ModbusMasterEmulator.
 */
public class ModbusMasterLib {

    /** The timeout. */
    private final int timeout = 2000;

    /** The n retry. */
    private final int nRetry = 0;

    /** The master. */
    private ModbusMaster master;

    /**
     * Gets the ip parameters.
     * 
     * @return the ip parameters
     */
    private IpParameters getIpParameters(String address, int port) {
        final IpParameters ipParameters = new IpParameters();
        ipParameters.setHost(address);
        ipParameters.setPort(port);
        ipParameters.setEncapsulated(false);
        return ipParameters;
    }

    /**
     * Creates the modbus master.
     *
     * @param address modbus slave address
     * @param port modbus slave listening port
     * @param keepAlive predicate to define if the modbus master should end after timeout.
     * @throws ModbusConnectionException the modbus connection exception
     */
    public void createModbusTcpMaster(String address, int port, boolean keepAlive) throws ModbusConnectionException {
        try {
            final ModbusFactory modbusFactory = new ModbusFactory();
            final IpParameters params = getIpParameters(address, port);
            master = modbusFactory.createTcpMaster(params, keepAlive);
            master.setTimeout(timeout);
            master.setRetries(nRetry);
            master.init();
        } catch (ModbusInitException e) {
            throw new ModbusConnectionException();
        }
    }

    /**
     * Destroy modbus master.
     */
    public void destroyModbusMaster() {
        master.destroy();
    }

    /**
     * Read coils - read write registers, boolean.
     *
     * @param slaveId the slave id
     * @param offset register address
     * @param len n registers read
     * @return array of boolean type results
     * @throws ModbusResponseException modbus response exception
     * @throws ModbusCommunicationException the modbus communication exception
     */
    public boolean[] readCoils(int slaveId, int offset, int len) throws ModbusResponseException,
            ModbusCommunicationException {
        ReadCoilsResponse response = null;
        try {
            final ReadCoilsRequest request = new ReadCoilsRequest(slaveId, offset, len);
            response = (ReadCoilsResponse) master.send(request);
            checkResponse(response);
        } catch (ModbusTransportException e) {
            throw new ModbusCommunicationException();
        }
        return response.getBooleanData();
    }

    /**
     * Read discrete input - read only registers, boolean.
     *
     * @param slaveId the slave id
     * @param offset register address
     * @param len n registers read
     * @return array of boolean type results
     * @throws ModbusResponseException modbus transport exception
     * @throws ModbusCommunicationException the modbus communication exception
     */
    public boolean[] readDiscreteInput(int slaveId, int offset, int len) throws ModbusResponseException,
            ModbusCommunicationException {
        ReadDiscreteInputsResponse response = null;
        try {
            final ReadDiscreteInputsRequest request = new ReadDiscreteInputsRequest(slaveId,
                    offset, len);
            response = (ReadDiscreteInputsResponse) master.send(request);
            checkResponse(response);
        } catch (ModbusTransportException e) {
            throw new ModbusCommunicationException();
        }
        return response.getBooleanData();
    }

    /**
     * Read holding register - read only registers, any numeric data type.
     *
     * @param slaveId the slave id
     * @param offset register address
     * @param dataType the data type format
     * @return the object read
     * @throws ModbusCommunicationException the modbus communication exception
     * @throws ModbusResponseException the modbus response exception
     */
    public Number readHoldingRegister(int slaveId, int offset, ModbusDataType dataType) throws ModbusCommunicationException,
            ModbusResponseException {
        return readRegister(slaveId, offset, RegisterRange.HOLDING_REGISTER, dataType);
    }

    /**
     * Read holding registers - read write registers, short.
     *
     * @param slaveId the slave id
     * @param offset register address
     * @param len n registers read
     * @return array of short type results
     * @throws ModbusResponseException modbus response exception
     * @throws ModbusCommunicationException the modbus communication exception
     */
    public short[] readHoldingRegisters(int slaveId, int offset, int len) throws ModbusResponseException,
            ModbusCommunicationException {
        ReadHoldingRegistersResponse response = null;
        try {
            final ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest(slaveId,
                    offset, len);
            response = (ReadHoldingRegistersResponse) master.send(request);
            checkResponse(response);
        } catch (ModbusTransportException e) {
            throw new ModbusCommunicationException();
        }
        return response.getShortData();
    }

    /**
     * Read input register - read only register, any numeric data type.
     *
     * @param slaveId the slave id
     * @param offset register address
     * @param dataType the data type format
     * @return the object read
     * @throws ModbusCommunicationException the modbus communication exception
     * @throws ModbusResponseException the modbus response exception
     */
    public Number readInputRegister(int slaveId, int offset, ModbusDataType dataType) throws ModbusCommunicationException,
            ModbusResponseException {
        return readRegister(slaveId, offset, RegisterRange.INPUT_REGISTER, dataType);
    }

    /**
     * Read input registers - read only register, short.
     *
     * @param slaveId the slave id
     * @param offset register address
     * @param len n registers read
     * @return array of short type results
     * @throws ModbusResponseException modbus response exception
     * @throws ModbusCommunicationException the modbus communication exception
     */
    public short[] readInputRegisters(int slaveId, int offset, int len) throws ModbusResponseException,
            ModbusCommunicationException {
        ReadInputRegistersResponse response = null;
        try {
            final ReadInputRegistersRequest request = new ReadInputRegistersRequest(slaveId,
                    offset, len);
            response = (ReadInputRegistersResponse) master.send(request);
            checkResponse(response);
        } catch (ModbusTransportException e) {
            throw new ModbusCommunicationException();
        }
        return response.getShortData();
    }

    /**
     * Read input or holding registers - any numeric data type.
     * 
     * @param slaveId the slave id
     * @param offset register address
     * @param range register type
     * @param dataType data type format
     * @return value read
     * @throws ModbusCommunicationException the modbus communication exception
     * @throws ModbusResponseException modbus response exception
     */
    private Number readRegister(int slaveId, int offset, int range, ModbusDataType dataType) throws ModbusCommunicationException,
            ModbusResponseException {
        Number result;
        try {
            final ModbusLocator locator = new ModbusLocator(slaveId, range, offset,
                    dataType.getType());
            result = (Number) master.getValue(locator);
        } catch (ModbusTransportException e) {
            throw new ModbusCommunicationException();
        } catch (ErrorResponseException e) {
            throw new ModbusResponseException(e.getErrorResponse().getExceptionMessage());
        }
        return result;
    }

    /**
     * Read exception status.
     * 
     * @param slaveId the slave id
     * @return the byte type result
     * @throws ModbusResponseException modbus response exception
     * @throws ModbusCommunicationException
     * @throws ModbusTransportException modbus transport exception
     */
    @SuppressWarnings("unused")
    private byte readExceptionStatus(int slaveId) throws ModbusResponseException,
            ModbusCommunicationException {
        ReadExceptionStatusResponse response;
        try {
            final ReadExceptionStatusRequest request = new ReadExceptionStatusRequest(slaveId);
            response = (ReadExceptionStatusResponse) master.send(request);
            checkResponse(response);
        } catch (ModbusTransportException e) {
            throw new ModbusCommunicationException();
        }
        return response.getExceptionStatus();
    }

    /**
     * Report slave id.
     * 
     * @param slaveId the slave id
     * @return the array of byte type results
     * @throws ModbusTransportException modbus transport exception
     * @throws ModbusResponseException modbus response exception
     * @throws ModbusCommunicationException
     */
    @SuppressWarnings("unused")
    private byte[] reportSlaveId(int slaveId) throws ModbusResponseException,
            ModbusCommunicationException {
        ReportSlaveIdResponse response = null;
        try {
            final ReportSlaveIdRequest request = new ReportSlaveIdRequest(slaveId);
            response = (ReportSlaveIdResponse) master.send(request);
            checkResponse(response);
        } catch (ModbusTransportException e) {
            throw new ModbusCommunicationException();
        }
        return response.getData();
    }

    /**
     * Write coil - read write registers, boolean.
     *
     * @param slaveId the slave id
     * @param offset register address
     * @param value the boolean type result
     * @throws ModbusResponseException modbus response exception
     * @throws ModbusCommunicationException the modbus communication exception
     */
    public void writeCoil(int slaveId, int offset, boolean value) throws ModbusResponseException,
            ModbusCommunicationException {
        try {
            final WriteCoilRequest request = new WriteCoilRequest(slaveId, offset, value);
            checkResponse(master.send(request));
        } catch (ModbusTransportException e) {
            throw new ModbusCommunicationException();
        }
    }

    /**
     * Write coils - read write registers, boolean.
     *
     * @param slaveId the slave id
     * @param offset register address
     * @param values the array of boolean type results
     * @throws ModbusResponseException modbus response exception
     * @throws ModbusCommunicationException the modbus communication exception
     */
    public void writeCoils(int slaveId, int offset, boolean[] values) throws ModbusResponseException,
            ModbusCommunicationException {
        try {
            final WriteCoilsRequest request = new WriteCoilsRequest(slaveId, offset, values);
            checkResponse(master.send(request));
        } catch (ModbusTransportException e) {
            throw new ModbusCommunicationException();
        }
    }

    /**
     * Write register - read write registers, any numeric data type.
     *
     * @param slaveId the slave id
     * @param offset register address
     * @param value boolean type result
     * @param dataType the data type
     * @throws ModbusResponseException modbus response exception
     * @throws ModbusCommunicationException the modbus communication exception
     */
    public void writeHoldingRegister(int slaveId, int offset, int value, ModbusDataType dataType) throws ModbusResponseException,
            ModbusCommunicationException {
        try {
            final ModbusLocator locator = new ModbusLocator(slaveId,
                    RegisterRange.HOLDING_REGISTER, offset, dataType.getType());
            master.setValue(locator, value);
        } catch (ModbusTransportException e) {
            throw new ModbusCommunicationException();
        } catch (ErrorResponseException e) {
            throw new ModbusResponseException(e.getErrorResponse().getExceptionMessage());
        }
    }

    /**
     * Write registers - read write registers, short.
     *
     * @param slaveId the slave id
     * @param offset the start
     * @param values the array of short type results
     * @throws ModbusResponseException modbus response exception
     * @throws ModbusCommunicationException the modbus communication exception
     */
    public void writeHoldingRegisters(int slaveId, int offset, short[] values) throws ModbusResponseException,
            ModbusCommunicationException {
        WriteRegistersResponse response;
        try {
            final WriteRegistersRequest request = new WriteRegistersRequest(slaveId, offset, values);
            response = (WriteRegistersResponse) master.send(request);
            checkResponse(response);
        } catch (ModbusTransportException e) {
            throw new ModbusCommunicationException();
        }
    }

    /**
     * Check response.
     * 
     * @param response the response
     * @throws ModbusResponseException modbus response exception
     */
    private void checkResponse(ModbusResponse response) throws ModbusResponseException {
        if (response.isException())
            throw new ModbusResponseException(response.getExceptionMessage());
    }
}
