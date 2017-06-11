package com.study;


// Use the JDBC driver
import java.sql.*;
import java.util.ArrayList;

import com.microsoft.sqlserver.jdbc.*;

import javax.swing.*;
import javax.xml.crypto.Data;

public class DatabaseController {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private static String databaseConnectionURL = "jdbc:sqlserver://localhost;database=TSPLocations;integratedSecurity=true;";

    public static String getDatabaseConnectionURL(){
        return databaseConnectionURL;
    }

    public static void setDatabaseConnectionURL(String newURL){
        databaseConnectionURL = newURL;
    }

    public Connection getConnection() {
        return connection;
    }

    public DatabaseController(String databaseConnectionURL){
        this.connection = null;
        this.statement = null;
        this.resultSet = null;

        try{
            connection = DriverManager.getConnection(databaseConnectionURL);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR: CONNECTION URL");
        }
        finally {
            if (connection != null) try {
                System.out.println("CONNECTED TO SQL SERVER"); } catch(Exception e) {}
        }

    }

    public void closeConnection(){
        try{
            this.connection.close();
            System.out.println("CONNECTION TO SQL SERVER CLOSED");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void query(String query){
        try{
            this.statement = this.connection.createStatement();
            this.resultSet = statement.executeQuery(query);
            while (this.resultSet.next())
            {
                int resultSetColumnCount = resultSet.getMetaData().getColumnCount();
                String resultRowString = "";
                for(int i = 1; i <= resultSetColumnCount; i++) {
                    resultRowString = resultRowString+resultSet.getString(i)+ " ";
                }
                System.out.println(resultRowString);
            }
        }
        catch(Exception e){
        }
        finally {
            if (resultSet != null) try { resultSet.close(); } catch(Exception e) {}
            if (statement != null) try { statement.close(); } catch(Exception e) {}
        }
    }

    public ArrayList<String[]> getDatasetTableContent(){
        ArrayList<String[]> content = new ArrayList<>();

        try{
            String query = "SELECT * FROM DataManager.Dataset";
            this.statement = this.connection.createStatement();
            this.resultSet = statement.executeQuery(query);
            int resultSetColumnCount = resultSet.getMetaData().getColumnCount();
            while (this.resultSet.next())
            {
                String[] resultRowString = new String[resultSetColumnCount];
                for(int i = 1; i <= resultSetColumnCount; i++){
                    resultRowString[i-1] = resultSet.getString(i);
                }
                content.add(resultRowString);
            }
        }
        catch(Exception e){
        }
        finally {
            if (resultSet != null){
                try { resultSet.close(); } catch(Exception e) {}
            }
            if (statement != null){
                try { statement.close(); } catch(Exception e) {}
            }
        }
        return content;
    }

    public ArrayList<String[]> getLocationsTableContent(){
        ArrayList<String[]> content = new ArrayList<>();

        try{
            String query = "SELECT * FROM DataManager.Locations";
            this.statement = this.connection.createStatement();
            this.resultSet = statement.executeQuery(query);
            int resultSetColumnCount = resultSet.getMetaData().getColumnCount();
            while (this.resultSet.next())
            {
                String[] resultRowString = new String[resultSetColumnCount];
                for(int i = 1; i <= resultSetColumnCount; i++){
                    resultRowString[i-1] = resultSet.getString(i);
                }
                content.add(resultRowString);
            }
        }
        catch(Exception e){
        }
        finally {
            if (resultSet != null){
                try { resultSet.close(); } catch(Exception e) {}
            }
            if (statement != null){
                try { statement.close(); } catch(Exception e) {}
            }
        }
        return content;
    }

    public ArrayList<String[]> getDatasetTableContent(String datasetName){
        ArrayList<String[]> content = new ArrayList<>();

        try{
            String query = "SELECT * FROM DataManager.Dataset WHERE NAME ='"+datasetName+"';";
            this.statement = this.connection.createStatement();
            this.resultSet = statement.executeQuery(query);
            int resultSetColumnCount = resultSet.getMetaData().getColumnCount();
            while (this.resultSet.next())
            {
                String[] resultRowString = new String[resultSetColumnCount];
                for(int i = 1; i <= resultSetColumnCount; i++){
                    resultRowString[i-1] = resultSet.getString(i);
                }
                content.add(resultRowString);
            }
        }
        catch(Exception e){
        }
        finally {
            if (resultSet != null){
                try { resultSet.close(); } catch(Exception e) {}
            }
            if (statement != null){
                try { statement.close(); } catch(Exception e) {}
            }
        }
        return content;
    }

    public ArrayList<String[]> getLocationsTableContent(String datasetName){
        ArrayList<String[]> content = new ArrayList<>();

        try{
            String query = "SELECT L.ID, L.X,L.Y,L.TagName\n" +
                    "FROM TSPlocations.DataManager.Dataset AS D\n" +
                    "JOIN TSPlocations.DataManager.Locations AS L\n" +
                    "ON D.DatasetID = L.DatasetID\n" +
                    "WHERE D.Name = '"+datasetName+"';";
            this.statement = this.connection.createStatement();
            this.resultSet = statement.executeQuery(query);
            int resultSetColumnCount = resultSet.getMetaData().getColumnCount();
            while (this.resultSet.next())
            {
                String[] resultRowString = new String[resultSetColumnCount];
                for(int i = 1; i <= resultSetColumnCount; i++){
                    resultRowString[i-1] = resultSet.getString(i);
                }
                content.add(resultRowString);
            }
        }
        catch(Exception e){
        }
        finally {
            if (resultSet != null){
                try { resultSet.close(); } catch(Exception e) {}
            }
            if (statement != null){
                try { statement.close(); } catch(Exception e) {}
            }
        }
        return content;
    }

    public ArrayList<String[]> getJoinedDatasetAndLocations(String datasetName){
        ArrayList<String[]> content = new ArrayList<>();

        try{
            String query = "SELECT D.DatasetID,D.Name,D.LocationsCount,L.ID, L.X,L.Y,L.TagName\n" +
                    "FROM TSPlocations.DataManager.Dataset AS D\n" +
                    "JOIN TSPlocations.DataManager.Locations AS L\n" +
                    "ON D.DatasetID = L.DatasetID\n" +
                    "WHERE D.Name = '"+datasetName+"';";
            this.statement = this.connection.createStatement();
            this.resultSet = statement.executeQuery(query);
            int resultSetColumnCount = resultSet.getMetaData().getColumnCount();
            while (this.resultSet.next())
            {
                String[] resultRowString = new String[resultSetColumnCount];
                for(int i = 1; i <= resultSetColumnCount; i++){
                    resultRowString[i-1] = resultSet.getString(i);
                }
                content.add(resultRowString);
            }
        }
        catch(Exception e){
        }
        finally {
            if (resultSet != null){
                try { resultSet.close(); } catch(Exception e) {}
            }
            if (statement != null){
                try { statement.close(); } catch(Exception e) {}
            }
        }
        return content;
    }

    public int getDatasetID(String datasetName){
        Integer datasetID = -1;
        try{
            String query = "SELECT DatasetID FROM DataManager.Dataset WHERE Name = '"+datasetName+"';";
            this.statement = this.connection.createStatement();
            this.resultSet = statement.executeQuery(query);
            int resultSetColumnCount = resultSet.getMetaData().getColumnCount();
            while (this.resultSet.next())
            {
                String[] resultRowString = new String[resultSetColumnCount];
                datasetID = resultSet.getInt("DatasetID");
            }
        }
        catch(Exception e){
        }
        finally {
            if (resultSet != null){
                try { resultSet.close(); } catch(Exception e) {}
            }
            if (statement != null){
                try { statement.close(); } catch(Exception e) {}
            }
        }
        return datasetID;
    }

    public double[][] getLocations(String datasetName){
        ArrayList<String[]> content = new ArrayList<>();
        double output[][];
        try{
            String query = "SELECT L.X,L.Y\n" +
                    "FROM TSPlocations.DataManager.Dataset AS D\n" +
                    "JOIN TSPlocations.DataManager.Locations AS L\n" +
                    "ON D.DatasetID = L.DatasetID\n" +
                    "WHERE D.Name = '"+datasetName+"';";
            this.statement = this.connection.createStatement();
            this.resultSet = statement.executeQuery(query);
            int resultSetColumnCount = resultSet.getMetaData().getColumnCount();
            while (this.resultSet.next())
            {
                String[] resultRowString = new String[resultSetColumnCount];
                for(int i = 1; i <= resultSetColumnCount; i++){
                    resultRowString[i-1] = resultSet.getString(i);
                }
                content.add(resultRowString);
            }
        }
        catch(Exception e){
        }
        finally {
            if (resultSet != null){
                try { resultSet.close(); } catch(Exception e) {}
            }
            if (statement != null){
                try { statement.close(); } catch(Exception e) {}
            }
        }
        output = new double[content.size()][2];
        int i = 0;
        for(String[] row : content){
            output[i][0] = Double.parseDouble(row[0]);
            output[i][1] = Double.parseDouble(row[1]);
            i++;
        }
        return output;
    }

    public void insertLocationsRow(int datasetId, int id, double X, double Y){

        PreparedStatement prepsInsertProduct = null;

        try {
            String insertSql = "INSERT INTO DataManager.Locations (DatasetID, ID, X, Y) VALUES "
                    + "("+datasetId+","+id+","+X+","+Y+");";
            prepsInsertProduct = connection.prepareStatement(insertSql);
            prepsInsertProduct.execute();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (prepsInsertProduct != null) try { prepsInsertProduct.close(); } catch(Exception e) {}
        }
    }

    public void insertDatasetRow(String name, int locationsCount){

        PreparedStatement prepsInsertProduct = null;

        try {
            String insertSql = "INSERT INTO DataManager.Dataset (Name, LocationsCount) VALUES "
                    + "('"+name+"',"+locationsCount+");";
            prepsInsertProduct = connection.prepareStatement(insertSql);
            prepsInsertProduct.execute();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (prepsInsertProduct != null) try { prepsInsertProduct.close(); } catch(Exception e) {}
        }
    }

    public void updateDatasetTableLocationsCount(){
        try{
            String query = "UPDATE DataManager.Dataset\n" +
                "SET LocationsCount =\n" +
                "(SELECT COUNT (*) FROM DataManager.Locations\n" +
                "where DataManager.Dataset.DatasetID = DataManager.Locations.DatasetID);";Statement statement = null;
            statement = this.connection.createStatement();
            statement.executeQuery(query);
        }
        catch (Exception e){
        }
        finally{
            if(statement != null){
                try { statement.close(); } catch(Exception e) {}
            }
        }
    }

    public void removeDataset(String datasetName){
        Statement statement = null;
        try{
            String query = "DELETE DataManager.Locations\n" +
                    "                    FROM DataManager.Locations a\n" +
                    "                           INNER JOIN DataManager.Dataset b\n" +
                    "                                   ON b.DatasetID = a.DatasetID\n" +
                    "                                      AND b.Name = '"+datasetName+"';\n" +
                    "DELETE DataManager.Dataset FROM DataManager.Dataset\n" +
                    "WHERE DataManager.Dataset.Name = '"+datasetName+"';";
            statement = this.connection.createStatement();
            statement.executeQuery(query);
        }
        catch (Exception e){
        }
        finally{
            if(statement != null){
                try { statement.close(); } catch(Exception e) {}
            }
        }
    }

    public void clearDatasetTable(){
        Statement statement = null;
        try{
            String query = "DELETE FROM DataManager.Dataset;";
            statement = this.connection.createStatement();
            statement.executeQuery(query);
        }
        catch (Exception e){
        }
        finally{
            if(statement != null){
                try { statement.close(); } catch(Exception e) {}
            }
        }
    }

    public void clearLocationsTable(){
        Statement statement = null;
        try{
            String query = "DELETE FROM DataManager.Locations;";
            statement = this.connection.createStatement();
            statement.executeQuery(query);
        }
        catch (Exception e){
        }
        finally{
            if(statement != null){
                try { statement.close(); } catch(Exception e) {}
            }
        }
    }
    /*
    Removes from DataManager.Locations locations attached to 'datasetName'
     */
    public void clearLocationsTable(String datasetName){
        Statement statement = null;
        try{
            String query = "DELETE DataManager.Locations\n" +
                    "FROM DataManager.Locations a\n" +
                    "       INNER JOIN DataManager.Dataset b\n" +
                    "               ON b.DatasetID = a.DatasetID\n" +
                    "                  AND b.Name = '"+datasetName+"';";
            statement = this.connection.createStatement();
            statement.executeQuery(query);
        }
        catch (Exception e){
        }
        finally{
            if(statement != null){
                try { statement.close(); } catch(Exception e) {}
            }
        }
    }
}