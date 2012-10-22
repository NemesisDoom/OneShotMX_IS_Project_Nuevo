/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sql_generator;

import com.table_projection.DatabaseTableProjectionGenerator;

/**
 *
 * @author Miguel
 */
public abstract class SQLStatementGenerator<T> {
    
    public SQLStatementGenerator(String inDBTable){
        setDatabaseTable(inDBTable);
    }
    
    private void setDatabaseTable(String inDbTable){
        databaseTable = inDbTable;
    }
    
    protected String getDatabaseTable(){
        return databaseTable;
    }
    
    public String createSelectMaxIDStatement(String inMaxValue){
        String maxIDQuery = "SELECT MAX(" +inMaxValue + ") FROM " + getDatabaseTable() + ";";
        return maxIDQuery;
    }
    
    public abstract String createSelectStatement(DatabaseTableProjectionGenerator tableProjection,String condition);
    public abstract String createUpdateStatement(T prevObject,T newObject);
    public abstract String createDeleteStatement(T deletingObject);
    public abstract String createInsertStatement(T insertingObject);
    
    private String databaseTable;
    
}
