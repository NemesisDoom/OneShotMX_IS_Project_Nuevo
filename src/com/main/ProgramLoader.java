/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.main;

import com.dao.DatabaseLoader;
import com.visual.PersonManagementForm;

/**
 *
 * @author Miguel
 */
public class ProgramLoader {
    public static void main(String args[]){
        DatabaseLoader acceser = DatabaseLoader.getInstance();
        acceser.loadDatabaseConfiguration();
        acceser.testDatabaseConnection();
        new PersonManagementForm().setVisible(true);
    }
}
