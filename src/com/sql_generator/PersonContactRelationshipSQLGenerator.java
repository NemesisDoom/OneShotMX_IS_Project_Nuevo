/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sql_generator;

import java.util.ArrayList;

/**
 *
 * @author Miguel
 */
public class PersonContactRelationshipSQLGenerator extends SQLStatementGenerator< ArrayList<Integer> >{
    private final static String ID_PERSON_COL = "IDPerson";
    private final static String CONTACT_ID_COL = "ContactID";
    
    public PersonContactRelationshipSQLGenerator(String inDBTable){
        super(inDBTable);
    }
    
    @Override
    public String createSelectStatement(String[] tableValues, String condition) {
        String selectQuery = "SELECT ";
        selectQuery += tableValues[0] + " ";
        for(int i=1;i<tableValues.length;i++){
            selectQuery += "," + tableValues[i];
        }
        selectQuery += " FROM " + getDatabaseTable() + " ";
        selectQuery += condition == null ? ";" : "WHERE " + condition + ";";
        return selectQuery;
    }

    @Override
    public String createUpdateStatement(ArrayList<Integer> prevObject, ArrayList<Integer> newObject) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String createDeleteStatement(ArrayList<Integer> deletingObject) {
        String deleteStatement = "DELETE FROM " + getDatabaseTable() + " ";
        deleteStatement += "WHERE " + ID_PERSON_COL + " = " + deletingObject.get(0);
        deleteStatement += " AND " + CONTACT_ID_COL + " = " + deletingObject.get(1) + ";";
        return deleteStatement;
    }

    @Override
    public String createInsertStatement(ArrayList<Integer> insertingObject) {
        String insertStatement = "INSERT INTO " + getDatabaseTable() + " ";
        insertStatement += "(" + ID_PERSON_COL + "," + CONTACT_ID_COL + ")";
        insertStatement += "VALUES(" +insertingObject.get(0) + "," + insertingObject.get(1) + ")";
        return insertStatement;
    }
}
