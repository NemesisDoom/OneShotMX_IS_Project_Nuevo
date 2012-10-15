/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.person.ContactInformation;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Miguel
 */
public class ContactInformationAccessObject extends DataAccessObject<ContactInformation> {

    public final static String ID_COL = "ContactID";
    private final String ADDRESS_COL = "Address";
    private final String HOME_PHONE_NUMBER_COL = "HomePhoneNumber";
    private final String CELLPHONE_NUMBER_COL = "CellphoneNumber";
    private final String EXTRA_CELLPHONE_NUMBER_COL = "ExtraHomePhoneNumber";
    private final String EXTRA_HOME_PHONE_NUMBER_COL = "ExtraHomePhoneNumber";
    private final String EMAIL_ADDRESS_COL = "EMailAddress";
    
    private ContactInformationSQLGenerator contactInfoSQLGenerator;

    public ContactInformationAccessObject(String in_databaseTable) {
        super(in_databaseTable);
        contactInfoSQLGenerator = new ContactInformationSQLGenerator(DatabaseTables.CONTACT_INFORMATION_TABLE);
    }

    public int getMaxID() {
        DatabaseConnectionManager connManager = getConnectionManager();
        String maxQuery = contactInfoSQLGenerator.createSelectMaxIDStatement(ID_COL);
        connManager.openConnection();
        Connection dbConnection = connManager.getConnection();
        int maxID = 0;
        try {
            Statement maxStatement = dbConnection.createStatement();
            ResultSet result = maxStatement.executeQuery(maxQuery);
            result.next();
            maxID = result.getInt("MAX");
        } catch (SQLException ex) {
        } finally {
            connManager.closeConnection();
        }
        return maxID;
    }

    @Override
    public int insertObject(ContactInformation object) {
        DatabaseConnectionManager connManager = getConnectionManager();
        String insertQuery = contactInfoSQLGenerator.createInsertStatement(object);
        connManager.openConnection();
        Connection dbConection = connManager.getConnection();
        int result = ERROR_EXECUTING_UPDATE;
        try {
            Statement insertStatement = dbConection.createStatement();
            result = insertStatement.executeUpdate(insertQuery);
        } catch (SQLException e) {
        } finally {
            connManager.closeConnection();
        }
        return result;
    }

    @Override
    public int updateObject(ContactInformation prevObject, ContactInformation newObject) {
        int result = ERROR_EXECUTING_UPDATE;
        DatabaseConnectionManager connManager = getConnectionManager();
        String updateQuery = contactInfoSQLGenerator.createUpdateStatement(prevObject, newObject);
        connManager.openConnection();
        Connection dbConection = connManager.getConnection();
        try {
            Statement insertStatement = dbConection.createStatement();
            result = insertStatement.executeUpdate(updateQuery);
        } catch (SQLException e) {
        } finally {
            connManager.closeConnection();
        }
        return result;
    }

    @Override
    public int deleteObject(ContactInformation object) {
        int result = ERROR_EXECUTING_UPDATE;
        DatabaseConnectionManager connManager = getConnectionManager();
        String deleteQuery = contactInfoSQLGenerator.createDeleteStatement(object);
        connManager.openConnection();
        Connection dbConection = connManager.getConnection();
        try {
            Statement insertStatement = dbConection.createStatement();
            result = insertStatement.executeUpdate(deleteQuery);
        } catch (SQLException e) {
        } finally {
            connManager.closeConnection();
        }
        return result;
    }

    @Override
    public ArrayList<ContactInformation> selectDataFromDatabase(String[] tableValues) {
        ArrayList<ContactInformation> contactList = new ArrayList<ContactInformation>();
        DatabaseConnectionManager connManager = getConnectionManager();
        String selectQuery = contactInfoSQLGenerator.createSelectStatement(tableValues, null);
        connManager.openConnection();
        Connection dbConnection = connManager.getConnection();
        try {
            Statement selectStatement = dbConnection.createStatement();
            ResultSet selectResult = selectStatement.executeQuery(selectQuery);
            contactList = createContactInformationList(selectResult);
        } catch (SQLException e) {
        } finally {
            connManager.closeConnection();
        }
        return contactList;
    }

    private ArrayList<ContactInformation> createContactInformationList(ResultSet inResult) {
        ArrayList<ContactInformation> contactInfoList = new ArrayList<ContactInformation>();
        try {
            while (inResult.next()) {

                ContactInformation contactInfo = new ContactInformation();

                String address = inResult.getString(ADDRESS_COL);
                String emailAddress = inResult.getString(EMAIL_ADDRESS_COL);
                String homeNumber = inResult.getString(HOME_PHONE_NUMBER_COL);
                String cellphoneNumber = inResult.getString(CELLPHONE_NUMBER_COL);
                String extraHomeNumber = inResult.getString(EXTRA_HOME_PHONE_NUMBER_COL);
                String extraCellphoneNumber = inResult.getString(EXTRA_CELLPHONE_NUMBER_COL);

                contactInfo.setHomeAddress(address);
                contactInfo.setEmailAddress(emailAddress);
                contactInfo.addTelephoneNumber(ContactInformation.ADDITIONAL_CELLPHONE_NUMBER, extraCellphoneNumber);
                contactInfo.addTelephoneNumber(ContactInformation.ADDITIONAL_HOME_NUMBER, extraHomeNumber);
                contactInfo.addTelephoneNumber(ContactInformation.CELLPHONE_NUMBER, cellphoneNumber);
                contactInfo.addTelephoneNumber(ContactInformation.HOME_PHONE_NUMBER, homeNumber);

                contactInfoList.add(contactInfo);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ContactInformationAccessObject.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contactInfoList;
    }
}
