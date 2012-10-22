/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sql_generator;

import com.person.ContactInformation;
import com.table_projection.DatabaseTableProjectionGenerator;

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
    public String createSelectStatement(DatabaseTableProjectionGenerator tableProjection, String condition) {
        String selectQuery = "SELECT ";
        selectQuery += tableProjection.getTableProjection();
        selectQuery += " FROM " + getDatabaseTable();
        selectQuery += condition == null ? ";" : " WHERE " + condition + ";";
        return selectQuery;
    }

    @Override
    public String createUpdateStatement(ContactInformation prevContactInformation,
            ContactInformation newContactInformation) {
        String updateQuery = "UPDATE " + getDatabaseTable() + " ";
        String address = newContactInformation.getHomeAddress();
        updateQuery += "SET " + ADDRESS_COL + " = '" + address + "' ,";
        
        String emailAddress = newContactInformation.getEmailAddress();
        updateQuery += EMAIL_ADDRESS_COL + " = '" + emailAddress + "' , ";
        
        String telephoneNumber = newContactInformation.getTelephoneNumber(ContactInformation.HOME_PHONE_NUMBER);
        updateQuery += HOME_PHONE_NUMBER_COL + " = '" + telephoneNumber + "' , ";
        String extraTelephoneNumber = newContactInformation.getTelephoneNumber(ContactInformation.ADDITIONAL_HOME_NUMBER);
        updateQuery += EXTRA_HOME_PHONE_NUMBER_COL + " = '" + extraTelephoneNumber + "' , ";
        
        String cellphoneNumber = newContactInformation.getTelephoneNumber(ContactInformation.CELLPHONE_NUMBER);
        updateQuery += CELLPHONE_NUMBER_COL + " = '" + cellphoneNumber + "', ";
        String extraCellphoneNumber = newContactInformation.getTelephoneNumber(ContactInformation.ADDITIONAL_CELLPHONE_NUMBER);
        updateQuery += EXTRA_CELLPHONE_NUMBER_COL + " = '" + extraCellphoneNumber + "' ";
        
        updateQuery += "WHERE " + ID_COL + " = " + prevContactInformation.getContactInformationID() + ";--";
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
