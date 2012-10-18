/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.management.PersonManager;
import com.person.ContactInformation;
import com.person.Person;
import com.visual.PersonForm;
import com.visual.PersonManagementForm;
import com.visual.PersonTableModel;
import java.awt.Dialog.ModalityType;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;

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

    public void registerPerson(Person inPerson, PersonForm inPersonForm) {
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

    public void viewPerson(JTable personTable,PersonManagementForm inPersonForm) {
        if(!isPersonSelected(personTable)){
            showMessageDialog("Favor de Seleccionar una persona para Ver",inPersonForm);
        }
    }

    public void modifyPerson(JTable personTable,PersonManagementForm inPersonForm) {
        if(!isPersonSelected(personTable)){
            showMessageDialog("Favor de seleccionar una persona para modificar",inPersonForm);
        }
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

    private boolean isPersonSelected(JTable personTable){
        int selectedIndex = personTable.getSelectedRow();
        return selectedIndex == -1 ? false : true;
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
}
