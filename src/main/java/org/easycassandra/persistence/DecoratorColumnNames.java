/*
 * Copyright 2012 Otávio Gonçalves de Santana (otaviojava)
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
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
