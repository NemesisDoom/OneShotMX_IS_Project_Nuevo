/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visual;

import com.person.Person;
import java.text.DateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Miguel
 */
public class PersonTableModel extends DefaultTableModel {
    private final static int MAX_COLS = 4;
    
    @Override
    public boolean isCellEditable(int row, int mColumnIndex) {
        return false;
    }

    public void addRow(Person person) {
        Vector vector = new Vector();
        int personID = person.getPersonID();
        vector.add(personID);
        String firstName = person.getFirstName();
        vector.add(firstName);
        String lastName = person.getLastName();
        vector.add(lastName);
        Date registryDate = person.getRegistrationDate();
        String registryDateString = getRegisterDateString(registryDate);
        vector.add(registryDateString);
        addRow(vector);
    }

    public Person getValueAt(int index){
        Person newPerson = new Person();
        int personID = (Integer)getValueAt(index, 0);
        newPerson.setPersonID(personID);
        String firstName = (String)getValueAt(index,1);
        newPerson.setFirstName(firstName);
        String lastName = (String)getValueAt(index,2);
        newPerson.setLastName(lastName);
        String registryDate = (String)getValueAt(index,3);
        DateFormat dateFormmater = DateFormat.getDateInstance();
        try{
            Date rgstrDate = dateFormmater.parse(registryDate);
            newPerson.setRegistrationDate(rgstrDate);
        }catch(Exception ex){
            
        }
        return newPerson;
    }
    
    private String getRegisterDateString(Date inRegisterDate) {
        DateFormat dateFormatter = DateFormat.getDateInstance();
        String registerDate = dateFormatter.format(inRegisterDate);
        return registerDate;
    }
}
