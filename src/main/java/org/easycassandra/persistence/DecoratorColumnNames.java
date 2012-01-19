package org.easycassandra.persistence;

import java.util.ArrayList;
import java.util.List;

/**
 * Decorator for the name of the Columns
 * @author otavio
 */
 class DecoratorColumnNames {

     /**
      * Columns's Names
      */
    private List<String> names;
    
    public List<String> getNames() {
        return names;
    }
    
    public void setNames(List<String> names) {
        this.names = names;
    }

    /**
     * add new name for the List
     * @param name - name for the List
     */
    public void add(String name) {
        names.add(name);
        
    }

    public void addAll(List<String> names) {
        this.names.addAll(names);
    }

    public DecoratorColumnNames() {
        names = new ArrayList<>();
    }

    /**
     * Names with separation for be used in CQL
     * @return 
     */
    @Override
    public String toString() {
    StringBuilder concatNames=new StringBuilder();
       for(String string: names) {
           concatNames.append(string).append(" ,");
           
       }
        return concatNames.substring(0,( concatNames.length()-1));
    }
    
}
