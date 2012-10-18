/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.sql_generator.PersonContactRelationshipSQLGenerator;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Miguel
 */
public class PersonContactInfoRelationshipAccessObject extends DataAccessObject< ArrayList<Integer> >{
    
    private static final String ID_PERSON_COL = "IDPerson";
    private static final String CONTACT_ID_COL = "ContactID";
    
    private PersonContactRelationshipSQLGenerator personContactSQLGenerator;
    
    public PersonContactInfoRelationshipAccessObject(String inDBTable){
        super(inDBTable);
        personContactSQLGenerator = new PersonContactRelationshipSQLGenerator(inDBTable);
    }
    
    @Override
    public int insertObject(ArrayList<Integer> object) {
        String insertQuery = personContactSQLGenerator.createInsertStatement(object);
        DatabaseConnectionManager connManager = getConnectionManager();
        connManager.openConnection();
        Connection dbConnection = connManager.getConnection();
        int result = ERROR_EXECUTING_OPERATION;
        try{
            Statement insertStatement = dbConnection.createStatement();
            result = insertStatement.executeUpdate(insertQuery);
        }catch(SQLException ex){
            ex.printStackTrace();
        }finally{
            connManager.closeConnection();
        }
        return result;
    }

    @Override
    public int updateObject(ArrayList<Integer> prevObject, ArrayList<Integer> newObject) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<ArrayList<Integer>> selectDataFromDatabase(String[] tableValues,String condition) {
        ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();
        String selectQuery = personContactSQLGenerator.createSelectStatement(tableValues, condition);
        DatabaseConnectionManager connManager = getConnectionManager();
        connManager.openConnection();
        Connection dbConnection = connManager.getConnection();
        try{
            Statement selectStatement = dbConnection.createStatement();
            ResultSet statementResult = selectStatement.executeQuery(selectQuery);
            while(statementResult.next()){
                ArrayList<Integer> actualList = new ArrayList<Integer>();
                int personID = statementResult.getInt(ID_PERSON_COL);
                int contactID = statementResult.getInt(CONTACT_ID_COL);
                actualList.add(personID);
                actualList.add(contactID);
                list.add(actualList);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            connManager.closeConnection();
        }
        return list;
    }

    @Override
    public int deleteObject(ArrayList<Integer> object) {
        String deleteQuery = personContactSQLGenerator.createDeleteStatement(object);
        DatabaseConnectionManager connManager = getConnectionManager();
        connManager.openConnection();
        Connection dbConnection = connManager.getConnection();
        int result = ERROR_EXECUTING_OPERATION;
        try{
            Statement insertStatement = dbConnection.createStatement();
            result = insertStatement.executeUpdate(deleteQuery);
        }catch(SQLException ex){
            ex.printStackTrace();
        }finally{
            connManager.closeConnection();
        }
        return result;
    }
}
