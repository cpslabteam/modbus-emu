package modbusemu.exception;

/**
 * The Class DatabaseConnectionException.
 */
public class DatabaseConnectionException extends
        Exception {

    private static final long serialVersionUID = 1L;
    private final String cause;

    public DatabaseConnectionException(String cause) {
        this.cause = cause;
    }

    @Override
    public String getMessage() {
        return "Database connection failed when trying to: " + cause;
    }

}
