package se.su.dsv.pvt.utemaning.backend;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.*;

/**
 * this is a class to handle the connection to a database. it takes a SQL query as input and creates a connection.
 * returns the result in a cachedrowset that will survive the connection.
 * <p>
 * Comments are above the code
 * <p>
 * REMEMBER
 * close connection or crash
 * close statement or crash
 *
 * @author Michel
 */
public class ConnectionToPasiDB {

    private final String driverName = ("org.mariadb.jdbc.Driver");
    private final String urlName = ("jdbc:mariadb://mysql.dsv.su.se:3306/pasi2645");
    private final String databaseName = ("pasi2645");
    private final String password = ("aijee1mau7Ip");

    private String errorMessage;

    /**
     * method for creating connection to retrive data, passes the cachedrowset to the databasemanagement class
     * @param sqlQuery query to be executed on the database
     * @return the results in the form of a table
     */
    public CachedRowSet getData(String sqlQuery) {
        CachedRowSet crs = null;
        try {
            //finds the driver
            Class.forName(driverName);
            //creates connection
            Connection con = DriverManager.getConnection(urlName, databaseName, password);
            //creates statement
            Statement stmt = con.createStatement();
            //primary result, will not survive closing of connection
            ResultSet rs = stmt.executeQuery(sqlQuery);
            //put into cachedrow... will survive closing of connection
            crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.populate(rs);
            stmt.close();
            con.close();
            //handles errors, dont know why printStackTrace
        } catch (ClassNotFoundException e) {
            errorMessage = e.getMessage();
            e.printStackTrace();
        } catch (SQLException e) {
            errorMessage = e.getMessage();
            e.printStackTrace();
        }
        return crs;
    }

    /**
     *     method for inserting data into the database returns a boolean, true if all ok, false if all not ok, also takes
     *     sql exception and stores in String, basiclly the error message to why and what went wrong.
     * @param sqlQuery query to be executed on the database
     * @return a true or false walue depending on success or not
     */

    public Boolean insertData(String sqlQuery) {
        boolean success = true;
        Exception e = null;
        try {
            //finds the driver
            Class.forName(driverName);
            //creates connection
            Connection con = DriverManager.getConnection(urlName, databaseName, password);
            //creates statement
            Statement stmt = con.createStatement();
            //execution of the statement. nothing gets returned as exception will be trown if something goes wrong
            stmt.executeQuery(sqlQuery);
            stmt.close();
            con.close();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            errorMessage = ex.getMessage();
            success = false;
        } catch (SQLException ex) {
            ex.printStackTrace();
            //gets sqlexception into string
            errorMessage = ex.getMessage();
            success = false;
        }
        return success;
    }


    /**
     *     this is a method for inserting a new workoutSpot into the database and returns that workoutSpotID
     *      that ID is needed to insert the location and outdoorGym as if is the PrimaryKey for those tables
     * @param sqlQuery query to be executed on the database
     * @return the ID nummer (primary key) of the newly created row
     */
    public int addAndReturnIncrementValue(String sqlQuery) {
        int i = 0;
        try {
            //finds the driver
            Class.forName(driverName);
            //creates connection
            Connection con = DriverManager.getConnection(urlName, databaseName, password);
            //creates a prepared statement
            PreparedStatement pstm = con.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            //execution of the statement and returns the generated keys in the resultSet
            pstm.execute();
            ResultSet rs = pstm.getGeneratedKeys();
            while (rs.next()) {
                i = rs.getInt("WorkoutSpotId");
            }
            pstm.close();
            con.close();
        } catch (ClassNotFoundException e) {
            errorMessage = e.getMessage();
            e.printStackTrace();
        } catch (SQLException e) {
            errorMessage = e.getMessage();
            e.printStackTrace();
        }return i;
    }

    public int addAndReturnIncrementValueChallenge (String sqlQuery) {
        int i = 0;
        try {
            //finds the driver
            Class.forName(driverName);
            //creates connection
            Connection con = DriverManager.getConnection(urlName, databaseName, password);
            //creates a prepared statement
            PreparedStatement pstm = con.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            //execution of the statement and returns the generated keys in the resultSet
            pstm.execute();
            ResultSet rs = pstm.getGeneratedKeys();
            while (rs.next()) {
                i = rs.getInt("ChallengeID");
            }
            pstm.close();
            con.close();
        } catch (ClassNotFoundException e) {
            errorMessage = e.getMessage();
            e.printStackTrace();
        } catch (SQLException e) {
            errorMessage = e.getMessage();
            e.printStackTrace();
        }return i;
    }

    /**
     * method for retriving the error message if it is needed.
     * @return exception in the form of a string.
     */
    public String getErrorMessage () {
        return errorMessage;
    }
}