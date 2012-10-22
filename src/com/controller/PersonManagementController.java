/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.management.PersonManager;
import com.person.ContactInformation;
import com.person.Person;
import com.visual.AddPersonForm;
import com.visual.ModifyPersonForm;
import com.visual.PersonManagementForm;
import com.visual.PersonTableModel;
import com.visual.ViewPersonForm;
import database_tables.PersonTable;
import java.awt.Dialog.ModalityType;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 *
 * @author Miguel
 */
public class PersonManagementController {

    private PersonManager personManager;
    private static PersonManagementController INSTANCE;

    private PersonManagementController() {
        personManager = new PersonManager();
    }

    public static PersonManagementController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PersonManagementController();
        }
        return INSTANCE;
    }

    public Person createPerson(String inFirstName, String inLastName,
            ContactInformation contactInfo, Date registryDate) {
        Person newPerson = new Person();

        newPerson.setFirstName(inFirstName);
        newPerson.setLastName(inLastName);
        newPerson.setContactInformation(contactInfo);
        newPerson.setRegistrationDate(registryDate);

        return newPerson;
    }

    public void registerPerson(Person inPerson, AddPersonForm inPersonForm) {
        inPersonForm.setModalityType(ModalityType.APPLICATION_MODAL);
        boolean isRegisterConfirmed = showConfirmDialog("¿Desea agregar a la Persona?", inPersonForm);
        if (isRegisterConfirmed) {
            personManager.registerResource(inPerson);
            String msg = "Se ha registrado a la persona " + inPerson.getName();
            showMessageDialog(msg, inPersonForm);
            inPersonForm.dispose();
        }
    }

    public void viewPersonsList(JTable table) {
        PersonTableModel tableModel = createPersonTableModel();
        ArrayList<Person> personsList = personManager.obtainResourceList(null);
        addPersonToTable(personsList, tableModel);
        table.setModel(tableModel);
    }

    public void showViewPersonForm(JTable personTable, PersonManagementForm inPersonForm) {
        if (!isPersonSelected(personTable)) {
            showMessageDialog("Favor de Seleccionar una persona para Ver", inPersonForm);
        } else {
            Person selectedPerson = getSelectedPersonFromTable(personTable);
            Person personFromDatabase = getPersonFromDatabase(selectedPerson);
            ViewPersonForm personForm = createViewPersonForm(personFromDatabase);
            personForm.setVisible(true);
        }
    }

    public void searchPerson(JTable personTable, PersonManagementForm inPersonForm) {
        if (!isSearchTextFieldEmpty(inPersonForm.getSearchTextField())) {
            boolean isFirstNameSelected = inPersonForm.isFirstNameSearchSelected();
            String sqlCondition = "";
            if (isFirstNameSelected) {
                sqlCondition += PersonTable.PERSON_FIRSTNAME_COL;
            } else {
                sqlCondition += PersonTable.PERSON_LASTNAME_COL;
            }
            JTextField searchField = inPersonForm.getSearchTextField();
            sqlCondition += " LIKE '%" + searchField.getText() + "%'";
            ArrayList<Person> searchPersonList = personManager.obtainResourceList(sqlCondition);
            PersonTableModel personModel = (PersonTableModel) personTable.getModel();
            clearTableModel(personModel);
            addPersonToTable(searchPersonList, personModel);
        } else {
            PersonTableModel personModel = (PersonTableModel) personTable.getModel();
            clearTableModel(personModel);
            viewPersonsList(personTable);
        }
    }

    public void showModifyPersonForm(JTable personTable, PersonManagementForm inPersonForm) {
        if (!isPersonSelected(personTable)) {
            showMessageDialog("Favor de seleccionar una persona para modificar", inPersonForm);
        } else {
            Person selectedPerson = getSelectedPersonFromTable(personTable);
            Person personFromDatabase = getPersonFromDatabase(selectedPerson);
            ModifyPersonForm modifyForm = createModifyPersonForm(personFromDatabase);
            modifyForm.setVisible(true);
        }
    }

    public void modifyPerson(Person inModifyingPerson, Person inModifiedPerson, ModifyPersonForm inModifyForm) {
        personManager.modifyResource(inModifyingPerson,inModifiedPerson);
        showMessageDialog("Se han modificado los datos de contacto",inModifyForm);
        inModifyForm.dispose();
    }

    public void deletePerson(JTable inPersonTable, PersonManagementForm inPersonFrame) {
        if (!isPersonSelected(inPersonTable)) {
            showMessageDialog("Favor de seleccionar una persona a Eliminar", inPersonFrame);
        } else {
            boolean confirmErase = showConfirmDialog(
                    "¿Desea eliminar a la Persona seleccionada?",
                    inPersonFrame);
            if (confirmErase) {
                int selectedIndex = inPersonTable.getSelectedRow();
                Person selectedPerson = getSelectedPerson(inPersonTable, selectedIndex);
                personManager.unRegisterResource(selectedPerson);
                viewPersonsList(inPersonTable);
            }
        }
    }

    private String convertDateToString(Date formattingDate) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.ENGLISH);
        String format = dateFormat.format(formattingDate);
        return format;
    }

    private Person getPersonFromDatabase(Person inSelectedPerson) {
        String selectionCondition = PersonTable.ID_PERSON_COL + "=" + inSelectedPerson.getPersonID();
        ArrayList<Person> person = personManager.obtainResourceList(selectionCondition);
        Person actualPerson = person.get(0);
        return actualPerson;
    }

    private Person getSelectedPersonFromTable(JTable personTable) {
        PersonTableModel tableModel = (PersonTableModel) personTable.getModel();
        int selectedIndex = personTable.getSelectedRow();
        Person selectedPerson = tableModel.getValueAt(selectedIndex);
        return selectedPerson;
    }

    private boolean isSearchTextFieldEmpty(JTextField searchTextField) {
        String searchTextFieldString = searchTextField.getText();
        return searchTextFieldString.isEmpty();
    }

    private boolean isPersonSelected(JTable personTable) {
        int selectedIndex = personTable.getSelectedRow();
        return selectedIndex == -1 ? false : true;
    }

    private void clearTableModel(PersonTableModel personModel) {
        while (personModel.getRowCount() > 0) {
            personModel.removeRow(0);
        }
    }

    private ViewPersonForm createViewPersonForm(Person person) {
        ViewPersonForm viewPersonForm = new ViewPersonForm();
        viewPersonForm.setModal(true);

        viewPersonForm.setPersonName(person.getFirstName(), person.getLastName());

        String registryDate = convertDateToString(person.getRegistrationDate());
        viewPersonForm.setRegistryDate(registryDate);

        ContactInformation contactInfo = person.getContactInformation();

        viewPersonForm.setPersonCellphones(
                contactInfo.getTelephoneNumber(ContactInformation.CELLPHONE_NUMBER),
                contactInfo.getTelephoneNumber(ContactInformation.ADDITIONAL_CELLPHONE_NUMBER));

        viewPersonForm.setPersonHomephoneNumbers(
                contactInfo.getTelephoneNumber(ContactInformation.HOME_PHONE_NUMBER),
                contactInfo.getTelephoneNumber(ContactInformation.ADDITIONAL_HOME_NUMBER));

        viewPersonForm.setPersonAddresses(contactInfo.getHomeAddress(), contactInfo.getEmailAddress());
        return viewPersonForm;
    }

    private Person getSelectedPerson(JTable inPersonTable, int selectedIndex) {
        PersonTableModel tableModel = (PersonTableModel) inPersonTable.getModel();
        Person selectedPerson = tableModel.getValueAt(selectedIndex);
        return selectedPerson;
    }

    private PersonTableModel createPersonTableModel() {
        PersonTableModel personTableModel = new PersonTableModel();
        personTableModel.addColumn("ID");
        personTableModel.addColumn("Firstname");
        personTableModel.addColumn("Lastname");
        personTableModel.addColumn("Registry Date");
        return personTableModel;
    }

    private void addPersonToTable(ArrayList<Person> inPersonsList, PersonTableModel inoutTableModel) {
        for (Person person : inPersonsList) {
            inoutTableModel.addRow(person);
        }
    }

    private void showMessageDialog(String msg, JFrame inParentFrame) {
        JOptionPane.showMessageDialog(inParentFrame, msg, "!Mensaje!", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showMessageDialog(String msg, JDialog inParentFrame) {
        JOptionPane.showMessageDialog(inParentFrame, msg);
    }

    private boolean showConfirmDialog(String msg, JFrame inParentFrame) {
        int result = -1;
        result = JOptionPane.showConfirmDialog(inParentFrame, msg, "¡Mensaje!", JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.OK_OPTION ? true : false;
    }

    private boolean showConfirmDialog(String msg, JDialog inParentFrame) {
        int result = -1;
        result = JOptionPane.showConfirmDialog(inParentFrame, msg);
        return result == JOptionPane.OK_OPTION ? true : false;
    }

    private ModifyPersonForm createModifyPersonForm(Person inPerson) {
        ModifyPersonForm modifyForm = new ModifyPersonForm(inPerson);

        modifyForm.setModal(true);

        String personFirstName = inPerson.getFirstName();
        String personLastName = inPerson.getLastName();
        modifyForm.setPersonName(personFirstName, personLastName);

        Date personRegistryDate = inPerson.getRegistrationDate();
        String dateString = convertDateToString(personRegistryDate);
        modifyForm.setRegistryDate(dateString);

        ContactInformation contactInfo = inPerson.getContactInformation();

        String telephoneNumber = contactInfo.getTelephoneNumber(ContactInformation.HOME_PHONE_NUMBER);
        String extraTelephoneNumber = contactInfo.getTelephoneNumber(ContactInformation.ADDITIONAL_HOME_NUMBER);
        modifyForm.setPersonHomephoneNumbers(telephoneNumber, extraTelephoneNumber);

        String cellphoneNumber = contactInfo.getTelephoneNumber(ContactInformation.CELLPHONE_NUMBER);
        String extraCellphoneNumber = contactInfo.getTelephoneNumber(ContactInformation.ADDITIONAL_CELLPHONE_NUMBER);
        modifyForm.setPersonCellphones(cellphoneNumber, extraCellphoneNumber);

        String emailAddress = contactInfo.getEmailAddress();
        String address = contactInfo.getHomeAddress();

        modifyForm.setPersonAddresses(address, emailAddress);
        return modifyForm;
    }
}