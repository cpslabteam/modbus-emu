package modbusemu.exception;

/**
 * The Class PropertiesLoadingException.
 */
public class PropertiesLoadingException extends
        Exception {

    private static final long serialVersionUID = 1L;

    @Override
    public String getMessage() {
        return "Configuration properties could not be read";
    }

}
