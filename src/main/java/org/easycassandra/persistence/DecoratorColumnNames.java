/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.persistence;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author otavio
 */
 class DecoratorColumnNames {

    private List<String> names;
    
    public List<String> getNames() {
        return names;
    }
    
    public void setNames(List<String> names) {
        this.names = names;
    }

    public void add(String name) {
        names.add(name);
        
    }

    public void addAll(List<String> names) {
        this.names.addAll(names);
    }

    public DecoratorColumnNames() {
        names = new ArrayList<>();
    }

    @Override
    public String toString() {
    StringBuilder concatNames=new StringBuilder();
       for(String string: names) {
           concatNames.append(string).append(" ,");
           
       }
        return concatNames.substring(0,( concatNames.length()-1));
    }
    
}
