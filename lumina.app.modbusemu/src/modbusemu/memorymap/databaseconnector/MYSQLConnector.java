package modbusemu.memorymap.databaseconnector;

/**
 * The Class MYSQLConnector.
 */
public class MYSQLConnector extends
        DBConnector {

    @Override
    protected String getName() {
        return "MYSQL";
    }

    @Override
    protected void runDriver() throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
    }

}
