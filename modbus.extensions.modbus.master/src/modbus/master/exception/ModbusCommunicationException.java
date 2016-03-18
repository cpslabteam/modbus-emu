package modbus.master.exception;

/**
 * The Class ModbusCommunicationError.
 */
public class ModbusCommunicationException extends
        Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    @Override
    public String getMessage() {
        return "Communication between modbus master and slave failed";
    }

}
