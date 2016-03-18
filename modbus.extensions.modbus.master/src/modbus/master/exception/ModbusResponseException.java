package modbus.master.exception;

/**
 * The Class ModbusResponseException.
 */
public class ModbusResponseException extends
        Exception {

    private final String responseError;

    public ModbusResponseException(String responseError) {
        this.responseError = responseError;
    }

    private static final long serialVersionUID = 1L;

    @Override
    public String getMessage() {
        return "modbus response includes the following error " + responseError;
    }
}
