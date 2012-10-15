/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.dao.ContactInformationAccessObject;
import com.dao.DatabaseConnectionManager;
import com.dao.DatabaseTables;
import com.dao.PersonAccessObject;
import com.person.ContactInformation;
import com.person.Person;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Miguel
 */
public class PersonContactRelationship {
    private PersonAccessObject personDAO;
    private ContactInformationAccessObject contactInfoDAO;
    
    public PersonContactRelationship(){
        personDAO = new PersonAccessObject(DatabaseTables.PERSON_TABLE);
        contactInfoDAO = new ContactInformationAccessObject(DatabaseTables.CONTACT_INFORMATION_TABLE);
    }
    public void linkPersonWithContactInformation(Person inPerson,ContactInformation inContactInfo){
        int personID = personDAO.getMaxID();
        int contactID = contactInfoDAO.getMaxID();
        String sqlQuery = "INSERT INTO PERSON_HAS_INFO(IDPerson,ContactID) VALUES("+personID+","+contactID+");";
        DatabaseConnectionManager connManager = DatabaseConnectionManager.getInstance();
        connManager.openConnection();
        Connection dbConnection = connManager.getConnection();
        try{
            Statement insertStatement = dbConnection.createStatement();
            insertStatement.executeUpdate(sqlQuery);
        }catch(SQLException e){
            
        }finally{
            connManager.closeConnection();
        }
    }
}
