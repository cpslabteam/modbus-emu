package modbusemu.memorymap.databaseconnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import modbusemu.exception.DatabaseConnectionException;

/**
 * The Interface for a generic database connector.
 */
public abstract class DBConnector {

    private Properties properties;
    private Connection conn;

    /**
     * Gets JDBC connection.
     *
     * @throws DatabaseConnectionException the database connection exception
     * @return operation predicate stating the operation result
     */
    public boolean startConnection() throws DatabaseConnectionException {
        try {
            final long initTime = System.currentTimeMillis();
            runDriver();
            conn = DriverManager.getConnection(getURL(properties), properties);
            System.err.println(getName()
                    + " Database connection was established sucessfully taking "
                    + (System.currentTimeMillis() - initTime) + " miliseconds");
            return true;
        } catch (ClassNotFoundException e) {
            throw new DatabaseConnectionException("load jdbc driver");
        } catch (SQLException e) {
            throw new DatabaseConnectionException("establish connection");
        }
    }

    protected abstract void runDriver() throws ClassNotFoundException;

    protected abstract String getName();

    public void closeConnection() throws DatabaseConnectionException {
        try {
            if (conn != null) {
                conn.close();
            } else {
                throw new DatabaseConnectionException("close connection");
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("close connection");
        }
    }

    public PreparedStatement getPreparedStatement(String query) throws DatabaseConnectionException {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(query);
        } catch (SQLException e) {
            throw new DatabaseConnectionException("process query statement");
        }
        return statement;
    };

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    /**
     * Gets JDBC specific database URL.
     * 
     * @param props connection properties
     * @return the URL
     */
    private String getURL(Properties props) {
        final String db = props.getProperty("database");
        final String host = props.getProperty("host");
        final String prefix = props.getProperty("url_prefix");
        return prefix + host + "/" + db;
    }
}
