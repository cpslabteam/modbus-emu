package modbusemu.memorymap.databaseconnector;

/**
 * The Class PSQLConnector.
 */
public class PSQLConnector extends
        DBConnector {


    public PSQLConnector() {
    }

    @Override
    protected String getName() {
        return "PostgreSQL";
    }

    @Override
    protected void runDriver() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
    }

 

}
