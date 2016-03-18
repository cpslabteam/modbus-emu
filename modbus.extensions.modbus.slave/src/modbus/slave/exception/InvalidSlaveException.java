package modbus.slave.exception;

/**
 * The Class InvalidSlaveException.
 */
public class InvalidSlaveException extends
        Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /* (non-Javadoc)
     * @see java.lang.Throwable#getMessage()
     */
    @Override
    public String getMessage() {
        return "Slave ID zero is not valid, it has to be higher than zero";
    }
}
