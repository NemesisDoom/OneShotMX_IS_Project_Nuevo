/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.table_projection;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Miguel
 */
public class DatabaseTableProjectionGenerator {
    public final static String ALL_ATTRIBUTES = "*";
    
    private ArrayList<String> attributeList;
    
    public DatabaseTableProjectionGenerator(){
        attributeList = new ArrayList<String>();
    }
    
    public void addAttribute(String tableAttribute){
        attributeList.add(tableAttribute);
    }
    
    public String getTableProjection(){
        String tableProjection = attributeList.get(0);
        for(int i=1;i<attributeList.size();i++){
            tableProjection += "," + attributeList.get(i);
        }
        return tableProjection;
    }
}
