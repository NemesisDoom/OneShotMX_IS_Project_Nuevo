/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sql_generator;

import com.person.ContactInformation;

/**
 *
 * @author Huallo
 */
public class ContactInformationSQLGenerator extends SQLStatementGenerator<ContactInformation> {

    public final static String ID_COL = "ContactID";
    private final String ADDRESS_COL = "Address";
    private final String HOME_PHONE_NUMBER_COL = "HomePhoneNumber";
    private final String CELLPHONE_NUMBER_COL = "CellphoneNumber";
    private final String EXTRA_CELLPHONE_NUMBER_COL = "ExtraCellPhoneNumber";
    private final String EXTRA_HOME_PHONE_NUMBER_COL = "ExtraHomePhoneNumber";
    private final String EMAIL_ADDRESS_COL = "EMailAddress";

    public ContactInformationSQLGenerator(String inDBTable) {
        super(inDBTable);
    }

    @Override
    public String createSelectStatement(String[] tableValues, String condition) {
        String selectQuery = "SELECT ";
        selectQuery += tableValues[0];
        for (int i = 1; i < tableValues.length; i++) {
            selectQuery += "," + tableValues[i];
        }
        selectQuery += " FROM " + getDatabaseTable();
        selectQuery += condition == null ? ";" : " WHERE " + condition + ";";
        return selectQuery;
    }

    @Override
    public String createUpdateStatement(ContactInformation prevContactInformation,
            ContactInformation newContactInformation) {
        String updateQuery = "";
        updateQuery += "UPDATE FROM " + getDatabaseTable();
        updateQuery += " SET ";
        updateQuery += "address ='" + newContactInformation.getHomeAddress() + "',";
        updateQuery += "homephoneNumber ='" + 
                newContactInformation.getTelephoneNumber(ContactInformation.HOME_PHONE_NUMBER) + "',";
        updateQuery += "cellphoneNumber ='" + 
                newContactInformation.getTelephoneNumber(ContactInformation.CELLPHONE_NUMBER) + "',";
        updateQuery += "extraCellphoneNumber ='" + 
                newContactInformation.getTelephoneNumber(ContactInformation.ADDITIONAL_CELLPHONE_NUMBER) +"',";
        updateQuery += "extraPhoneNumber ='" + 
                newContactInformation.getTelephoneNumber(ContactInformation.ADDITIONAL_HOME_NUMBER) +"'";
        updateQuery += " WHERE contactID = " + 
                prevContactInformation.getContactInformationID();
        return updateQuery;
    }

    @Override
    public String createDeleteStatement(ContactInformation deletingContactInformation) {
        String deleteQuery = "DELETE FROM " + getDatabaseTable();
        deleteQuery += " WHERE ContactID = " + deletingContactInformation.getContactInformationID();
        return deleteQuery;
    }

    @Override
    public String createInsertStatement(ContactInformation insertingContactInformation) {
        String address = insertingContactInformation.getHomeAddress();
        String homephoneNumber = insertingContactInformation.getTelephoneNumber(ContactInformation.HOME_PHONE_NUMBER);
        String cellphoneNumber = insertingContactInformation.getTelephoneNumber(ContactInformation.CELLPHONE_NUMBER);
        String extraCellphoneNumber = insertingContactInformation.getTelephoneNumber(ContactInformation.ADDITIONAL_CELLPHONE_NUMBER);
        String extraPhoneNumber = insertingContactInformation.getTelephoneNumber(ContactInformation.ADDITIONAL_HOME_NUMBER);
        String emailAddress = insertingContactInformation.getEmailAddress();
        
        String insertQuery = "INSERT INTO " + getDatabaseTable();
        insertQuery += " (" + ID_COL + ","+ ADDRESS_COL + "," + HOME_PHONE_NUMBER_COL + ",";
        insertQuery += CELLPHONE_NUMBER_COL + "," + EXTRA_HOME_PHONE_NUMBER_COL + ",";
        insertQuery += EXTRA_CELLPHONE_NUMBER_COL + "," + EMAIL_ADDRESS_COL + ")";
        insertQuery += " VALUES(nextval('Contact_Person_Increment'),'" + address + "','";
        insertQuery += homephoneNumber + "','"+cellphoneNumber + "','" + extraCellphoneNumber+"','";
        insertQuery += extraPhoneNumber + "','" + emailAddress + "');";

        return  insertQuery;
    }


}
