package modbus.master.exception;

/**
 * The Class ModbusConnectionException.
 */
public class ModbusConnectionException extends
        Exception {

    private static final long serialVersionUID = 1L;

    @Override
    public String getMessage() {
        return "The attempt of modbus master to contact modbus slave failed.";
    }

}
