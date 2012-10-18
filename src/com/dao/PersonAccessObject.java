/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.sql_generator.PersonSQLGenerator;
import com.person.Person;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Miguel
 */
public class PersonAccessObject extends DataAccessObject<Person> {

    public static final String ID_COL = "IDPerson";
    public static final String FIRSTNAME_COL = "PersonFirstName";
    public static final String LASTNAME_COL = "PersonLastName";
    public static final String REGISTRY_DATE_COL = "RegistryDate";

    private PersonSQLGenerator sqlGenerator;
    
    public PersonAccessObject(String in_databaseTable) {
        super(in_databaseTable);
        sqlGenerator = new PersonSQLGenerator(
                in_databaseTable,
                ID_COL,
                FIRSTNAME_COL,
                LASTNAME_COL,
                REGISTRY_DATE_COL);
    }

    public int getMaxID(){
        DatabaseConnectionManager connManager = getConnectionManager();
        String maxQuery = sqlGenerator.createSelectMaxIDStatement(ID_COL);
        connManager.openConnection();
        Connection dbConnection = connManager.getConnection();
        int maxID = 0;
        try{
        Statement maxStatement = dbConnection.createStatement();
        ResultSet result = maxStatement.executeQuery(maxQuery);
        result.next();
        maxID = result.getInt("MAX");
        }catch(SQLException ex){
            
        }finally{
            connManager.closeConnection();
        }
        return maxID;
    }
    
    @Override
    public int insertObject(Person object) {
        DatabaseConnectionManager connManager = null;
        try {
            String sqlQuery = sqlGenerator.createInsertStatement(object);
            connManager = getConnectionManager();
            connManager.openConnection();
            Connection dbConnection = connManager.getConnection();
            Statement statement = dbConnection.createStatement();
            int result = statement.executeUpdate(sqlQuery);
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(PersonAccessObject.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            connManager.closeConnection();
        }
        return ERROR_EXECUTING_OPERATION;
    }

    @Override
    public int updateObject(Person prevObject,Person newObject) {
        DatabaseConnectionManager connManager = getConnectionManager();
        String updateQuery = sqlGenerator.createUpdateStatement(prevObject, newObject);
        connManager.openConnection();
        Connection dbConnection = connManager.getConnection();
        try{
        Statement updateStatement = dbConnection.createStatement();
        int result = updateStatement.executeUpdate(updateQuery);
        return result;
        }catch(SQLException ex){
        
        }finally{
            connManager.closeConnection();
        }
        return ERROR_EXECUTING_OPERATION;
    }

    @Override
    public int deleteObject(Person object) {
        DatabaseConnectionManager connManager = getConnectionManager();
        try {
            String sqlDeleteQuery = sqlGenerator.createDeleteStatement(object);
            connManager.openConnection();
            Connection dbConnection = connManager.getConnection();
            Statement deleteStatement = dbConnection.createStatement();
            int result = deleteStatement.executeUpdate(sqlDeleteQuery);
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(PersonAccessObject.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            connManager.closeConnection();
        }
        return ERROR_EXECUTING_OPERATION;
    }

    @Override
    public ArrayList<Person> selectDataFromDatabase(String[] tableValues,String condition) {
        ArrayList<Person> personsList = new ArrayList<Person>();
        DatabaseConnectionManager connManager = getConnectionManager();
        try {
            String selectQuery = sqlGenerator.createSelectStatement(tableValues, condition);
            connManager.openConnection();
            Connection dbConnection = connManager.getConnection();
            Statement selectStatement = dbConnection.createStatement();
            ResultSet selectResult = selectStatement.executeQuery(selectQuery);
            personsList = createPersonArrayList(selectResult);
        } catch (SQLException ex) {
            Logger.getLogger(PersonAccessObject.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            connManager.closeConnection();
        }
        return personsList;
    }

    private ArrayList<Person> createPersonArrayList(ResultSet inDBResult) {
        ArrayList<Person> personsLists = new ArrayList<Person>();
        try {
            while (inDBResult.next()) {
                int personID = inDBResult.getInt(ID_COL);
                String firstName = inDBResult.getString(FIRSTNAME_COL);
                String lastName = inDBResult.getString(LASTNAME_COL);
                Date registryDate = inDBResult.getDate(REGISTRY_DATE_COL);
                
                Person person = new Person();
                person.setPersonID(personID);
                person.setFirstName(firstName);
                person.setLastName(lastName);
                person.setRegistrationDate(registryDate);
                personsLists.add(person);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PersonAccessObject.class.getName()).log(Level.SEVERE, null, ex);
        }
        return personsLists;
    }

    /*
    private String createSelectQuery(String[] values) {
        String selectQuery = "SELECT ";
        selectQuery += values[0];
        for (int i = 1; i < values.length; i++) {
            selectQuery += "," + values[i];
        }
        selectQuery += " FROM " + getDatabaseTable() + ";";
        return selectQuery;
    }

    private String createInsertQuery(Person in_InsertingPerson) {
        String sqlQuery = "";
        sqlQuery = "INSERT INTO " + getDatabaseTable();
        sqlQuery += "( " + ID_COL + "," + FIRSTNAME_COL + ","
                + LASTNAME_COL + "," + REGISTRY_DATE_COL + ")"
                + "VALUES(nextval('ID_Person_Increment'), '" + in_InsertingPerson.getFirstName() + "'"
                + "," + "'" + in_InsertingPerson.getLastName() + "','"
                + in_InsertingPerson.getRegistrationDate().toGMTString() + "'"
                + ");";
        return sqlQuery;
    }

    private String createDeleteQuery(Person inDeletingPerson) {
        String deleteSQLQuery = "DELETE FROM " + getDatabaseTable();
        deleteSQLQuery += "WHERE " + FIRSTNAME_COL + " = '" + inDeletingPerson.getFirstName() + "'";
        deleteSQLQuery += " && " + LASTNAME_COL + " = '" + inDeletingPerson.getLastName() + "';";
        return deleteSQLQuery;
    }*/
}
