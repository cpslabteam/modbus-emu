package modbus.master.exception;

/**
 * The Class InvalidPropertiesException.
 */
public class InvalidPropertiesException extends
        Exception {

    private static final long serialVersionUID = 1L;

    @Override
    public String getMessage() {
        return "Properties on properties file are invalid or file could not be read";
    }

}
