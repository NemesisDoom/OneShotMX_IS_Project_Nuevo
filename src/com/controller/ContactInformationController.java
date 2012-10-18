/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.person.ContactInformation;

/**
 *
 * @author Huallo
 */
public class ContactInformationController {
    
    public ContactInformationController() {
    }

    public ContactInformation createContactInformation(String homeAddress, String emailAddress,
            String homeNumber, String cellphoneNumber, String extraCellphone, String extraHomePhone) {

        ContactInformation personContactInformation = new ContactInformation();
        personContactInformation.setEmailAddress(emailAddress);
        personContactInformation.setHomeAddress(homeAddress);

        personContactInformation.addTelephoneNumber(ContactInformation.CELLPHONE_NUMBER, cellphoneNumber);
        personContactInformation.addTelephoneNumber(ContactInformation.HOME_PHONE_NUMBER, homeNumber);
        personContactInformation.addTelephoneNumber(ContactInformation.ADDITIONAL_CELLPHONE_NUMBER, extraCellphone);
        personContactInformation.addTelephoneNumber(ContactInformation.ADDITIONAL_HOME_NUMBER, extraHomePhone);
        
        return personContactInformation;
    }
}
