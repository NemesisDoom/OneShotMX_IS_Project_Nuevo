/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sql_generator;

import com.person.Person;
import com.table_projection.DatabaseTableProjectionGenerator;

/**
 *
 * @author Miguel
 */
public class PersonSQLGenerator extends SQLStatementGenerator<Person> {
    
    private final String ID_COL = "IDPerson";
    private final String FIRSTNAME_COL = "PersonFirstname";
    private final String LASTNAME_COL = "PersonLastname";
    private final String REGISTRY_DATE_COL = "RegistryDate";
    
    public PersonSQLGenerator(String inDBTable){
        super(inDBTable);
    }
    
    @Override
    public String createSelectStatement(DatabaseTableProjectionGenerator tableProjection,String condition) {
        String sqlQuery = "SELECT ";
        sqlQuery += tableProjection.getTableProjection();
        sqlQuery += " FROM " + getDatabaseTable() + " ";
        sqlQuery += condition == null ? ";" : "WHERE " + condition + ";";
        return sqlQuery;
    }

    @Override
    public String createUpdateStatement(Person prevObject, Person newObject) {
        String sqlQuery = "";
        sqlQuery += "UPDATE " + getDatabaseTable() + "SET ";
        sqlQuery += "PersonFirstName = '"+newObject.getFirstName()+"',";
        sqlQuery += "PersonLastName = '"+newObject.getLastName() +"'";
        sqlQuery += " WHERE IDPerson = " + prevObject.getPersonID();
        return sqlQuery;
    }

    @Override
    public String createDeleteStatement(Person deletingObject) {
        String sqlQuery = "DELETE FROM " + getDatabaseTable() + " ";
        sqlQuery += "WHERE IDPerson = " + deletingObject.getPersonID();
        return sqlQuery;
    }

    @Override
    public String createInsertStatement(Person insertingObject) {
        String sqlQuery = "";
        sqlQuery = "INSERT INTO " + getDatabaseTable();
        sqlQuery += "( " + ID_COL + "," + FIRSTNAME_COL + ","
                + LASTNAME_COL + "," + REGISTRY_DATE_COL + ")"
                + "VALUES(nextval('ID_Person_Increment'), '" + insertingObject.getFirstName() + "'"
                + "," + "'" + insertingObject.getLastName() + "','"
                + insertingObject.getRegistrationDate().toGMTString() + "'"
                + ");";
        return sqlQuery;
    }
}
