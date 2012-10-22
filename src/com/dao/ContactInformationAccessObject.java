/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import database_tables.DatabaseTables;
import com.sql_generator.ContactInformationSQLGenerator;
import com.person.ContactInformation;
import com.table_projection.DatabaseTableProjectionGenerator;
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
    public static final String ADDRESS_COL = "Address";
    public static final String HOME_PHONE_NUMBER_COL = "HomePhoneNumber";
    public static final String CELLPHONE_NUMBER_COL = "CellphoneNumber";
    public static final String EXTRA_CELLPHONE_NUMBER_COL = "ExtraCellPhoneNumber";
    public static final String EXTRA_HOME_PHONE_NUMBER_COL = "ExtraHomePhoneNumber";
    public static final String EMAIL_ADDRESS_COL = "EMailAddress";

    //private ContactInformationSQLGenerator contactInfoSQLGenerator;
    public ContactInformationAccessObject(String in_databaseTable) {
        super(in_databaseTable);
        ContactInformationSQLGenerator contactInfoSQLGenerator =
                new ContactInformationSQLGenerator(DatabaseTables.CONTACT_INFORMATION_TABLE);
        setSQLGenerator(contactInfoSQLGenerator);
    }

    public int getMaxID() {
        DatabaseConnectionManager connManager = getConnectionManager();
        String maxQuery = getSQLGenerator().createSelectMaxIDStatement(ID_COL);
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
        String insertQuery = getSQLGenerator().createInsertStatement(object);
        connManager.openConnection();
        Connection dbConection = connManager.getConnection();
        int result = ERROR_EXECUTING_OPERATION;
        try {
            Statement insertStatement = dbConection.createStatement();
            result = insertStatement.executeUpdate(insertQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connManager.closeConnection();
        }
        return result;
    }

    @Override
    public int updateObject(ContactInformation prevObject, ContactInformation newObject) {
        int result = ERROR_EXECUTING_OPERATION;
        DatabaseConnectionManager connManager = getConnectionManager();
        String updateQuery = getSQLGenerator().createUpdateStatement(prevObject, newObject);
        connManager.openConnection();
        Connection dbConection = connManager.getConnection();
        try {
            Statement insertStatement = dbConection.createStatement();
            result = insertStatement.executeUpdate(updateQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connManager.closeConnection();
        }
        return result;
    }

    @Override
    public int deleteObject(ContactInformation object) {
        int result = ERROR_EXECUTING_OPERATION;
        DatabaseConnectionManager connManager = getConnectionManager();
        String deleteQuery = getSQLGenerator().createDeleteStatement(object);
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
    public ArrayList<ContactInformation> selectDataFromDatabase(DatabaseTableProjectionGenerator tableProjection, String condition) {
        ArrayList<ContactInformation> contactList = new ArrayList<ContactInformation>();
        DatabaseConnectionManager connManager = getConnectionManager();
        String selectQuery = getSQLGenerator().createSelectStatement(tableProjection, condition);
        connManager.openConnection();
        Connection dbConnection = connManager.getConnection();
        try {
            Statement selectStatement = dbConnection.createStatement();
            ResultSet selectResult = selectStatement.executeQuery(selectQuery);
            contactList = createContactInformationList(selectResult);
        } catch (SQLException e) {
            e.printStackTrace();
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

                int contactID = inResult.getInt(ID_COL);
                contactInfo.setContactInformationID(contactID);
                
                String address = inResult.getString(ADDRESS_COL);
                contactInfo.setHomeAddress(address);
                
                String emailAddress = inResult.getString(EMAIL_ADDRESS_COL);
                contactInfo.setEmailAddress(emailAddress);
                
                String extraCellphoneNumber = inResult.getString(EXTRA_CELLPHONE_NUMBER_COL);
                contactInfo.addTelephoneNumber(ContactInformation.ADDITIONAL_CELLPHONE_NUMBER, extraCellphoneNumber);
                
                String extraHomeNumber = inResult.getString(EXTRA_HOME_PHONE_NUMBER_COL);
                contactInfo.addTelephoneNumber(ContactInformation.ADDITIONAL_HOME_NUMBER, extraHomeNumber);
                
                String cellphoneNumber = inResult.getString(CELLPHONE_NUMBER_COL);
                contactInfo.addTelephoneNumber(ContactInformation.CELLPHONE_NUMBER, cellphoneNumber);
                
                String homeNumber = inResult.getString(HOME_PHONE_NUMBER_COL);
                contactInfo.addTelephoneNumber(ContactInformation.HOME_PHONE_NUMBER, homeNumber);

                contactInfoList.add(contactInfo);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ContactInformationAccessObject.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contactInfoList;
    }
}
