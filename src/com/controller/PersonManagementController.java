/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.dao.PersonAccessObject;
import com.person.ContactInformation;
import com.person.Person;
import com.visual.PersonTableModel;
import java.util.Date;
import javax.swing.JTable;

/**
 *
 * @author Miguel
 */
public class PersonManagementController {

    private PersonAccessObject personDAO;
    public static final String PERSON_TABLE = "Persons";

    public PersonManagementController() {
        personDAO = new PersonAccessObject(PERSON_TABLE);
    }

    public Person createPerson(String inFirstName, String inLastName, ContactInformation contactInfo, Date registryDate) {
        Person newPerson = new Person();

        newPerson.setFirstName(inFirstName);
        newPerson.setLastName(inLastName);
        newPerson.setContactInformation(contactInfo);
        newPerson.setRegistrationDate(registryDate);

        return newPerson;
    }

    public void insertPersonToDatabase(Person person){
        personDAO.insertObject(person);
    }
    
    public void deletePerson(JTable inPersonTable){
        PersonTableModel tableModel = (PersonTableModel)inPersonTable.getModel();
        int selectedRow = inPersonTable.getSelectedRow();
        String personIDString = (String)tableModel.getValueAt(selectedRow, 0);
        int personID = Integer.parseInt(personIDString);

        Person deletingPerson = new Person();
        deletingPerson.setPersonID(personID);
        
        personDAO.deleteObject(deletingPerson);
    }
}
