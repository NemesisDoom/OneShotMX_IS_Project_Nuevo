/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.management;

import java.util.ArrayList;

/**
 *
 * @author Miguel
 */
public interface ResourceManager<T> {
    public void registerResource(T inRegisteringObject);
    public void modifyResource(T inModifyingObject);
    public void unRegisterResource(T inUnregisteringObject);
    public ArrayList<T> obtainResourceList(String inCondition);
}
