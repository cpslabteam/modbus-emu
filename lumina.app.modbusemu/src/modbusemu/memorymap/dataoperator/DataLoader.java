package modbusemu.memorymap.dataoperator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import modbusemu.exception.DatabaseConnectionException;
import modbusemu.memorymap.DelayedMemoryMap;
import modbusemu.memorymap.databaseconnector.DBConnector;

/**
 * The Class DataLoader.
 */
public class DataLoader
        implements IDataOperator {
    /** highest timestamp considered - seconds. */
    private final long maxTS;
    /** lowest timestamp considered - seconds. */
    private final long minTS;
    /** time rate between simulated time and real time being simulated . */
    private final float timeRate;
    /** the number of different timestamps loaded from the database on every query. */
    private final long loadRate;
    /** current timestamp. */
    private long currentTS;
    /** database connection. */
    private final DBConnector dbConn;
    /** delayed memory. */
    private final DelayedMemoryMap memMap;
    /** the amount of nano seconds corresponding to one second. */
    private final long oneSecondInNano = 1000000000L;
    private final long oneHundredMili = 100;
    /** Operator name. */
    private final String name = "Data Loader";

    public DataLoader(DelayedMemoryMap memMap,
                      DBConnector dbConn,
                      long loadRate,
                      float timeRate,
                      long minTS,
                      long maxTS) {
        currentTS = minTS;
        this.maxTS = maxTS;
        this.dbConn = dbConn;
        this.memMap = memMap;
        this.loadRate = loadRate;
        this.timeRate = timeRate;
        this.minTS = minTS;
    }

    /**
     * Starts the data loader.
     */
    public void start() {
        boolean suceeded = false;
        
        while (!suceeded) {
            try {
                suceeded = dbConn.startConnection();
                Thread.sleep(oneHundredMili);
            } catch (InterruptedException e) {
                System.err.println("Data Loader thread was interrupted");
            } catch (DatabaseConnectionException e) {
                System.err.println("Connection establishment failed, retrying...");
            }
        }

        try {
            while (currentTS < maxTS) {
                loadDatapoints();
                currentTS += loadRate;
            }
            dbConn.closeConnection();
        } catch (DatabaseConnectionException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Load datapoints from a database.
     *
     * @throws DatabaseConnectionException the database connection exception
     */
    public void loadDatapoints() throws DatabaseConnectionException {
        try {
            final long initTime = System.currentTimeMillis();
            final String query = "SELECT dpid,rts,reading FROM read_data_record WHERE rts >= ? AND rts < ? ORDER BY rts";
            final PreparedStatement st = dbConn.getPreparedStatement(query);
            st.setLong(1, currentTS);
            st.setLong(2, Math.min(currentTS + loadRate, maxTS));
            final ResultSet rs = st.executeQuery();
            while (rs.next()) {
                final long timestamp = (long) (secondToNano(rs.getLong("rts") - minTS) * timeRate);
                memMap.writeDelayed(rs.getString("dpid"), timestamp, rs.getLong("reading"));
            }
            rs.close();
            st.close();
            System.err.println("Data Loaded in " + (System.currentTimeMillis() - initTime)
                    + " miliseconds");
        } catch (SQLException e) {
            throw new DatabaseConnectionException("process query statement");
        }
    }

    /**
     * Converts seconds to nano seconds.
     * 
     * @param second the amount of seconds
     * @return the amount of nano seconds
     */
    private long secondToNano(long second) {
        return second * oneSecondInNano;
    }

    @Override
    public String getName() {
        return name;
    }
}
