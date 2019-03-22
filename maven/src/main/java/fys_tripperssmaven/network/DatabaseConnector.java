
package fys_tripperssmaven.network;

import java.sql.*;
import java.util.Enumeration;


public class DatabaseConnector {
    private static final String DB_DEFAULT_DATABASE = "fys";
    private static final String DB_DEFAULT_SERVER_URL = "localhost:3306";
    private static final String DB_DEFAULT_ACCOUNT = "root";
    private static final String DB_DEFAULT_PASSWORD = "root";
    
    private final static String DB_DRIVER_URL = "com.mysql.jdbc.Driver";
    private final static String DB_DRIVER_PREFIX = "jdbc:mysql://";
    private final static String DB_DRIVER_PARAMETERS = "?useSSL=false";
    
    public static Connection connection = null;
    
    // set for verbose logging of all queries
    private boolean verbose = true;
    
    // remembers the first error message on the connection 
    private String errorMessage = null; 
    
    // constructors
    public DatabaseConnector() {
        this(DB_DEFAULT_DATABASE, DB_DEFAULT_SERVER_URL, DB_DEFAULT_ACCOUNT, DB_DEFAULT_PASSWORD);
    }
    
    public DatabaseConnector(String dbName) {
        this(dbName, DB_DEFAULT_SERVER_URL, DB_DEFAULT_ACCOUNT, DB_DEFAULT_PASSWORD);
    }
    
    public DatabaseConnector(String dbName, String account, String password) {
        this(dbName, DB_DEFAULT_SERVER_URL, account, password);
    }
    
    public DatabaseConnector(String dbName, String serverURL, String account, String password) {

        try {
            // verify that a proper JDBC driver has been installed and linked
            if (!selectDriver(DB_DRIVER_URL)) {
                System.out.println("CONECTION IS GEEN NULL MEER?: " + String.valueOf(this.connection));

                return;
            }

            if (password == null) {
                password = "";
            }

            // establish a connection to a named database on a specified server	
            String connStr = DB_DRIVER_PREFIX + serverURL + "/" + dbName;
            log("Connecting " + connStr);
            DatabaseConnector.connection = DriverManager.getConnection(connStr, account, password);
            
            System.out.println("CONECTION IS GEEN NULL MEER?: " + String.valueOf(this.connection));
            
        } catch (SQLException eSQL) {
            error(eSQL);
            this.close();
        }
    }
    
    public final void close() {
        
        if (DatabaseConnector.connection == null) {
            // db has been closed earlier already
            return;
        }
        try {
            DatabaseConnector.connection.close();
            DatabaseConnector.connection = null;
            this.log("Data base has been closed");
        } catch (SQLException eSQL) {
            error(eSQL);
        }
    }
   
    /***
     * elects proper loading of the named driver for database connections.
     * This is relevant if there are multiple drivers installed that match the JDBC type
     * @param driverName    the name of the driver to be activated.    
     * @return              indicates whether a suitable driver is available
     */
    private Boolean selectDriver(String driverName) 
    {
        try {
            Class.forName(driverName);
            // Put all non-prefered drivers to the end, such that driver selection hits the first
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                Driver d = drivers.nextElement();
                if (!d.getClass().getName().equals(driverName)) {   // move the driver to the end of the list
                    DriverManager.deregisterDriver(d);
                    DriverManager.registerDriver(d);
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            error(ex);
            return false;
        }
        return true;
    }

    /***
     * Executes a DDL, DML or DCL query that does not yield a result set
     * @param sql   the full sql text of the query.      
     * @return      the number of rows that have been impacted, -1 on error
     */
    public int executeUpdateQuery(String sql) {
        try {
            Statement s = DatabaseConnector.connection.createStatement();
            log(sql);
            int n = s.executeUpdate(sql);
            s.close();
            return (n);
        } catch (SQLException ex) {
            // handle exception
            error(ex);
            return -1;
        }
    }

    /***
     * Executes an SQL query that yields a ResultSet with the outcome of the
     * query. This outcome may be a single row with a single column in case of
     * a scalar outcome.
     * @param sql   the full sql text of the query. 
     * @return      a ResultSet object that can iterate along all rows
     * @throws SQLException 
     */
    public ResultSet executeResultSetQuery(String sql) throws SQLException { 
        Statement s = DatabaseConnector.connection.createStatement();
        log(sql);
        ResultSet rs = s.executeQuery(sql);
        // cannot close the statement, because that also closes the resultset
        return rs;
    }
    
    /***
     * Executes query that is expected to return a single String value
     * @param sql   the full sql text of the query.       
     * @return      the string result, null if no result or error
     */
    public String executeStringQuery(String sql) {
        String result = null;
        try {
            Statement s = DatabaseConnector.connection.createStatement();
            log(sql);
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()) {
                result = rs.getString(1);              
            }
            // close both statement and resultset
            s.close();
        } catch (SQLException ex) {
            error(ex);
        }
        
        return result;
    }
    
    /***
     * Executes query that is expected to return a list of String values
     * @param sql   the full sql text of the query.     
     * @return      the string result, null if no result or error
     */
    public String executeStringListQuery(String sql) {
        String result = null;
        try {
            Statement s = DatabaseConnector.connection.createStatement();
            log(sql);
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()) {
                result = rs.getString(1);              
            }
            // close both statement and resultset
            s.close();
        } catch (SQLException ex) {
            error(ex);
        }
        
        return result;
    }

    /***
     * echoes a message on the system console, if run in verbose mode
     * @param message
     */
    public void log(String message) {
        if (isVerbose()) {
            System.out.println("MyJDBC: " + message);
        }
    }

    /***
     * echoes an exception and its stack trace on the system console.
     * remembers the message of the first error that occurs for later reference.
     * closes the connection such that no further operations are possible.
     * @param e
     */
    public final void error(Exception e) {
        String msg = "MyJDBC-" + e.getClass().getName() + ": " + e.getMessage();
        
        // capture the message of the first error of the connection
        if (this.errorMessage == null) {
            this.errorMessage = msg;
        }
        System.out.println(msg);
        e.printStackTrace();

        // if an error occurred, close the connection to prevent further operations
        this.close();
    }
    
    public static Connection getConnection(){
        return DatabaseConnector.connection;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
