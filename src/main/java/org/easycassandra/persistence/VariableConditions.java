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

//import java.util.Objects;
import javax.persistence.Parameter;

import org.apache.commons.lang.StringUtils;

/**
 * This class is for represent parameter in CQL
 *
 * @author otavio
 *
 */
@SuppressWarnings("rawtypes")
class VariableConditions implements Parameter {

    private static final int VALUE = 23;
    private static final int HASH = 5;
    /**
     * name of the variable
     */
    private String name;
    /**
     * Condition
     */
    private String condition;
    /**
     * information of class Parameter type
     */
    private Class parameterType;
    /**
     * information position
     */
    private Integer position;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Class getParameterType() {
        return parameterType;
    }

    public void setParameterType(Class parameterType) {
        this.parameterType = parameterType;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VariableConditions other = (VariableConditions) obj;
        if(StringUtils.equals(this.name, other.name)){
        	return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = HASH;
        return VALUE * hash +name.hashCode();
    }
}
