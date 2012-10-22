/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.management;

import com.dao.ContactInformationAccessObject;
import com.dao.DataAccessObject;
import database_tables.DatabaseTables;
import com.dao.PersonAccessObject;
import com.dao.PersonContactInfoRelationshipAccessObject;
import com.person.ContactInformation;
import com.person.Person;
import com.table_projection.DatabaseTableProjectionGenerator;
import database_tables.ContactInformationTable;
import database_tables.PersonHasInfoTable;
import java.util.ArrayList;

/**
 *
 * @author Miguel
 */
public class PersonManager implements ResourceManager<Person> {

    private PersonAccessObject personDAO;
    private ContactInformationAccessObject contactInfoDAO;
    private PersonContactInfoRelationshipAccessObject personContactDAO;

    public PersonManager() {
        personDAO = new PersonAccessObject(DatabaseTables.PERSON_TABLE);
        contactInfoDAO = new ContactInformationAccessObject(DatabaseTables.CONTACT_INFORMATION_TABLE);
        personContactDAO = new PersonContactInfoRelationshipAccessObject(DatabaseTables.PERSON_HAS_INFO_TABLE);
    }

    @Override
    public void registerResource(Person inRegisteringObject) {
        int personID = registerPerson(inRegisteringObject);
        ContactInformation personContactInfo = inRegisteringObject.getContactInformation();
        int contactInfoID = registerContactInformation(personContactInfo);
        linkPersonWithContactInformation(personID, contactInfoID);
    }

    @Override
    public void modifyResource(Person inModifyingObject,Person inModifiedObject) {
        ContactInformation prevContactInfo = inModifyingObject.getContactInformation();
        ContactInformation newContactInfo = inModifiedObject.getContactInformation();
        contactInfoDAO.updateObject( prevContactInfo, newContactInfo );
    }

    @Override
    public void unRegisterResource(Person inUnregisteringObject) {
        int personID = -1;
        personID = getPersonID(inUnregisteringObject);
        int contactID = -1;
        contactID = getContactInformationID(inUnregisteringObject);
        unLinkPersonWithContactInformation(personID,contactID);
        deletePerson(personID);
        deleteContactInformation(contactID);
    }

    @Override
    public ArrayList<Person> obtainResourceList(String inCondition) {
        ArrayList<Person> personList = null;
        DatabaseTableProjectionGenerator personProjection = createTableProjectionWithAllAttributes();
        personList = personDAO.selectDataFromDatabase(personProjection, inCondition);
        linkPersonWithContactInformation(personList);
        return personList;
    }

    private void linkPersonWithContactInformation(ArrayList<Person> list){
        for(Person actualPerson : list){
            DatabaseTableProjectionGenerator tableProjection = new DatabaseTableProjectionGenerator();
            tableProjection.addAttribute(DatabaseTableProjectionGenerator.ALL_ATTRIBUTES);
            String condition = ContactInformationTable.CONTACT_ID_COL + "=" + actualPerson.getPersonID();
            ArrayList<ContactInformation> contactInfo = contactInfoDAO.selectDataFromDatabase(tableProjection, condition);
            actualPerson.setContactInformation(contactInfo.get(0));
        }
    }
    
    private void deletePerson(int personID){
        Person tempPerson = new Person();
        tempPerson.setPersonID(personID);
        personDAO.deleteObject(tempPerson);
    }
    
    private void deleteContactInformation(int contactID){
        ContactInformation tempContactInfo = new ContactInformation();
        tempContactInfo.setContactInformationID(contactID);
        contactInfoDAO.deleteObject(tempContactInfo);
    }
    
    private int registerPerson(Person inRegisteringPerson) {
        int result = personDAO.insertObject(inRegisteringPerson);
        int maxID = personDAO.getMaxID();
        return selectOperationReturningValue(result, maxID);
    }

    private int getPersonID(Person inPerson) {
        String condition = PersonAccessObject.FIRSTNAME_COL + " = '" + inPerson.getFirstName() +
                "' AND " + PersonAccessObject.LASTNAME_COL + " = '"
                + inPerson.getLastName() + "'";
        DatabaseTableProjectionGenerator personProjection = createTableProjectionWithAllAttributes();
        ArrayList<Person> personList = personDAO.selectDataFromDatabase(personProjection, condition);
        Person actualPerson = personList.get(0);
        return actualPerson.getPersonID();
    }

    private int getContactInformationID(Person inPerson){
        int personID = getPersonID(inPerson);
        String condition = PersonAccessObject.ID_COL + " = " + personID;
        DatabaseTableProjectionGenerator contactProjection = createTableProjectionWithAllAttributes();
        ArrayList<ArrayList<Integer>> contactList = personContactDAO.selectDataFromDatabase(contactProjection, condition);
        ArrayList<Integer> listWithValues = contactList.get(0);
        int contactID = listWithValues.get(1);
        return contactID;
    }
    
    private int registerContactInformation(ContactInformation inRegisteringContactInfo) {
        int result = contactInfoDAO.insertObject(inRegisteringContactInfo);
        int maxID = contactInfoDAO.getMaxID();
        return selectOperationReturningValue(result, maxID);
    }

    private int selectOperationReturningValue(int result, int maxID) {
        return result == DataAccessObject.ERROR_EXECUTING_OPERATION ? result : maxID;
    }

    private DatabaseTableProjectionGenerator createTableProjectionWithAllAttributes(){
        DatabaseTableProjectionGenerator dbProjection = new DatabaseTableProjectionGenerator();
        dbProjection.addAttribute(DatabaseTableProjectionGenerator.ALL_ATTRIBUTES);
        return dbProjection;
    }
    
    private int linkPersonWithContactInformation(int inPersonID, int inContactInfoID) {
        ArrayList<Integer> relationshipList = new ArrayList<Integer>();
        relationshipList.add(inPersonID);
        relationshipList.add(inContactInfoID);
        int result = personContactDAO.insertObject(relationshipList);
        return result;
    }
    
    private int unLinkPersonWithContactInformation(int inPersonID, int inContactID){
        ArrayList<Integer> relationshipList = new ArrayList<Integer>();
        relationshipList.add(inPersonID);
        relationshipList.add(inContactID);
        int result = DataAccessObject.ERROR_EXECUTING_OPERATION;
        result = personContactDAO.deleteObject(relationshipList);
        return result;
    }
}
