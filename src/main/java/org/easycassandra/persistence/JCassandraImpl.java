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

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.*;

import org.easycassandra.EasyCassandraException;
import org.easycassandra.annotations.read.EnumRead;
import org.easycassandra.persistence.DescribeField.TypeField;
import org.easycassandra.util.EncodingUtil;
import org.easycassandra.util.ReflectionUtil;

/**
 * Class intepreter for CQL Cassandra
 *
 * @author otavio
 *
 */
class JCassandraImpl implements JCassandra {

	private static final String NOT_SUPPORTED_YET = "Not supported yet.";
	private  static  List<String> cqlWords;
    /**
     * Class for run the queries
     */
    private Persistence persistence;
    /**
     *
     */
    private StringBuilder cql;
    /**
     * information about the query
     */
    private InformationQuery informationCQL;
    /**
     * to start the position
     */
    private int startPosition = 0;
    /**
     * maxResult in query
     */
    private int maxResult = -1;

    /**
     * set Persistence in Jcassandra Class
     *
     * @param persistence
     */
    public void setPersistence(Persistence persistence) {
        this.persistence = persistence;
    }

    /**
     * Constructor method
     *
     * @param expression
     */
    JCassandraImpl(String expression) {  
   initArray();
        informationCQL = new InformationQuery();
        StringTokenizer expressionTokens = new StringTokenizer(
                cqlQuery(expression));
        //in the future inner class
        Period period = Period.BEFORE_SELECT;
        //end of inner class
        while (expressionTokens.hasMoreTokens()) {
            String actualExpression = expressionTokens.nextToken();
            if (Period.FROM.equals(period)) {
                informationCQL.columnFamily = actualExpression;
                period = Period.AFTER_FROM;
                continue;
            }
         
                if ("update".equals(actualExpression)){
                    period = Period.SELECT;
                    informationCQL.isUpdate = true;
                }
                
                else if("select".equals(actualExpression)||"delete".equals(actualExpression)){
              
                    period = Period.SELECT;
                }
                else if( "*".equals(actualExpression)){
                    informationCQL.allObject = true;
                }
                else if( "from".equals(actualExpression)){
                    period = Period.FROM;

                }
                else if( "where".equals(actualExpression)){
                    period = Period.WHERE;
                
                }
                else if( ",".equals(actualExpression)){ 
                    isNeedComma();
                }
                else if( "count(*)".equals(actualExpression)){
                
                    informationCQL.countMode = true;
                }
                else if(cqlWords.contains(actualExpression)){
              
                
                    informationCQL.needsCondition = false;
                }
                else{
                    defaultCondition(period, actualExpression);
                }
            
            isStartCorrect(period);
            if (Period.SELECT.equals(period) && informationCQL.isUpdate) {
                period = Period.FROM;
            }
        }  
        verifySyntax(informationCQL);
       
        cql = new StringBuilder(expression);
        informationCQL.toVariableNameKey();
    }

    private void initArray() {
    	 cqlWords=new ArrayList<String>();
    	    cqlWords.add("set");
    	    cqlWords.add("<");
    	    cqlWords.add( ">");
    	    cqlWords.add("=");
    	    cqlWords.add( "<=");
    	    cqlWords.add(">=");
    	    cqlWords.add("and");
    	    cqlWords.add("or");
		
	}

	private void isNeedComma() {
        if (!informationCQL.needsComma && !informationCQL.isUpdate) {
            throw new EasyCassandraException(" Syntax error: unnecessary comma");
        }
        informationCQL.needsComma = false;
    }

    private void isStartCorrect(Period period) {
        if (Period.BEFORE_SELECT.equals(period)) {
            throw new EasyCassandraException(" Syntax error: the CQL must "
                    + "begin with the select command ");
        }
    }

    /**
     * Execute when enter in default option
     *
     * @param period
     * @param actualExpression
     */
    private void defaultCondition(Period period, String actualExpression) {
        if (Period.SELECT.equals(period)) {
            if (informationCQL.needsComma) {
                throw new EasyCassandraException(" Syntax error: need separate"
                        + " the attributes with ',' ");
            }
            informationCQL.variable.add(actualExpression);
            informationCQL.needsComma = true;
        } else if (Period.AFTER_FROM.equals(period) && informationCQL.isUpdate) {
            informationCQL.addSet(actualExpression);
        } else if (Period.WHERE.equals(period)) {
            if (informationCQL.needsCondition) {
                throw new EasyCassandraException(" Syntax error: need"
                        + " condiction in query ");
            }
            informationCQL.addVariable(actualExpression);
            informationCQL.needsCondition = true;
        }
    }

    /**
     *
     * @param informationCQL
     */
    private void verifySyntax(final InformationQuery informationCQL) {
        if (!informationCQL.iscondition) {
            throw new EasyCassandraException(" Syntax error:  ");
        }
        DescribeFamilyObject describeFamilyObject =
                EasyCassandraManager.getFamily(informationCQL.columnFamily);
        if (describeFamilyObject == null) {
            throw new EasyCassandraException(" Syntax error: unknown Column"
                    + " Family " + informationCQL.columnFamily);
        }
        for (String column : informationCQL.variable) {
            if (describeFamilyObject.getField(column) == null) {
                throw new EasyCassandraException(" Syntax error: unknown"
                        + " column " + column + " in  Column Family "
                        + informationCQL.columnFamily);
            }
        }
        for (String key : informationCQL.variabledMap.keySet()) {
            DescribeField describeField;
            if (informationCQL.isUpdate) {
                describeField = describeFamilyObject.getField(
                        informationCQL.variabledMap.get(key).getName().replace(
                        InformationQuery.UPDATE_STRING, ""));
            } else {
                describeField = describeFamilyObject.getField(
                        informationCQL.variabledMap.get(key).getName());
            }
            if (describeField == null) {
                throw new EasyCassandraException(" Syntax error: unknown column"
                        + informationCQL.variabledMap.get(key).getName()
                        + " in Column Family " + informationCQL.columnFamily);
            }
            if ((!TypeField.KEY.equals(describeField.getTypeField())
                    && !TypeField.INDEX.equals(describeField.getTypeField()))
                    && !key.contains(InformationQuery.UPDATE_STRING)) {
                throw new EasyCassandraException("The field "
                        + describeField.getName()
                        + " must be a key or index for be in condition");
            }
        }
    }

    private String cqlQuery(String cql) {
        StringBuilder newCql = new StringBuilder();
        int firstTime = 0;
        for (String part : cql.split(",")) {
            if (firstTime > 0) {
                newCql.append(" , ");
            }
            if (part.contains("=")) {
                part = part.replace("=", "= ");
            }
            newCql.append(part);
            firstTime++;
        }
        return newCql.toString().trim();
    }

    /**
     * The class has information about the query
     *
     * @author otavio
     *
     */
    class InformationQuery {

        private static final String UPDATE_STRING = "UPDATE";
        /**
         * Name of the column Family
         */
        private String columnFamily;
        /**
         * all object checked
         */
        private boolean allObject;
        /**
         * using count in select
         */
        private boolean countMode;
        /**
         * is update
         */
        private boolean isUpdate;
        /**
         * name of the variables
         */
        private List<String> variable;
        /**
         * verify if is condition or variable
         */
        private boolean iscondition = true;
        /**
         * need the character ','
         */
        private boolean needsComma = false;
        /**
         * need the condition
         */
        private boolean needsCondition = false;
        /**
         *
         */
        private VariableConditions variableConditions;
        /**
         * map of the variable conditions The key is name of the fields
         */
        private Map<String, VariableConditions> variabledMap;
        /**
         * map of the where conditions
         */
        private Map<String, VariableConditions> whereMap;

        void addSet(String value) {
            if (iscondition) {
                variableConditions.setName(value + UPDATE_STRING);
            } else {
                variableConditions.setCondition(value);
                variabledMap.put(variableConditions.getName(),
                        variableConditions);
                variableConditions = new VariableConditions();
            }
            iscondition = !iscondition;
        }

        void addVariable(String value) {
            if (iscondition) {
                variableConditions.setName(value);
            } else {
                variableConditions.setCondition(value);
                variabledMap.put(variableConditions.getName(),
                        variableConditions);
                variableConditions = new VariableConditions();
            }
            iscondition = !iscondition;
        }

        /**
         * modify the map to be the variable name a key if it contains the first
         * character equals
         */
        public void toVariableNameKey() {
            DescribeFamilyObject describeFamilyObject =
                    EasyCassandraManager.getFamily(informationCQL.columnFamily);
            int position = 0;
            whereMap = new HashMap<String, VariableConditions>();
            for (String key : variabledMap.keySet()) {
                String aux = key;
                VariableConditions value = variabledMap.get(key);
                if (value.getCondition() != null
                        && value.getCondition().length() > 0
                        && value.getCondition().charAt(0) == ':') {
                    if (isUpdate) {
                        value.setName(value.getName().replace(UPDATE_STRING,
                                ""));
                        aux = value.getName();
                    }
                    DescribeField describeField =
                            describeFamilyObject.getField(aux);
                    value.setParameterType(describeField.getClassField());
                    value.setPosition(position++);
                    value.setCondition(value.getCondition().trim().substring(1));
                    whereMap.put(value.getCondition(), value);
                }
            }
        }

        {
            variable = new ArrayList<String>();
            variabledMap = new HashMap<String, VariableConditions>();
            variableConditions = new VariableConditions();
            whereMap = new HashMap<String, VariableConditions>();
        }

        /**
         * Run the map and return the key
         *
         * @param variable2
         */
        public String getKey(VariableConditions variable) {
            for (String key : variabledMap.keySet()) {
                if (variabledMap.get(key).equals(variable)) {
                    return key;
                }
            }
            return "";
        }
    }

    /**
     * This enum inform period that is a query
     *
     * @author otavio
     *
     */
    enum Period {

        BEFORE_SELECT, SELECT, FROM, AFTER_FROM, WHERE
    }

    @Override
    public List<?> getResultList() {
        DescribeFamilyObject describeFamilyObject =
                EasyCassandraManager.getFamily(informationCQL.columnFamily);
        String cqlNew = replaceToCQLName(describeFamilyObject);
        List<?> list = null;
        if (informationCQL.countMode) {
            List<Long> longList = new ArrayList<Long>();
            longList.add(persistence.executeCommandCQL(cqlNew).rows.get(
                    0).getColumns().get(0).value.asLongBuffer().get());
            list = longList;
        } else if (informationCQL.allObject) {
            list = executeAll(describeFamilyObject, cqlNew);
        } else {
            list = executeSomeFields(describeFamilyObject, cqlNew);
        }
        runStartPosition(list);
        runMaxResult(list);
        return list;
    }

    /**
     * run the max resulst
     *
     * @param list
     */
    private void runMaxResult(List<?> list) {
        if (maxResult == -1) {
            return;
        }
        while (list.size() > maxResult) {
            list.remove(list.size() - 1);
        }
    }

    /**
     * run the start position
     *
     * @param list
     */
    private void runStartPosition(List<?> list) {
        for (int index = 0; index < startPosition; index++) {
            if (index > list.size()) {
                break;
            }
            list.remove(0);
        }
    }

    /**
     * When the query has only fields in the objects
     *
     * @param describeFamilyObject
     * @param cql
     * @return
     */
    private List<?> executeSomeFields(DescribeFamilyObject describeFamilyObject,
            String cql) {
        List<Map<String, Object>> listCql = new ArrayList<Map<String, Object>>();
        for (Map<String, String> resultSet : persistence.executeCql(cql)) {
            Map<String, Object> resulMap = new HashMap<String, Object>();
            List<String> used = new ArrayList<String>();
            for (String key : resultSet.keySet()) {
                if (used.contains(key)) {
                    continue;
                }
                runSubObject(describeFamilyObject, resultSet, resulMap, used);
                if (used.contains(key)) {
                    continue;
                }
                DescribeField describeField = describeFamilyObject.getField(
                        describeFamilyObject.getFieldsName(key));
                if (describeField.getClassField().isEnum()) {
                    resulMap.put(describeField.getName(), new EnumRead(
                            describeField.getClassField()).getObjectByByte(
                            EncodingUtil.stringToByte(resultSet.get(key))));
                } else {
                    if (resultSet.get(key) == null) {
                        continue;
                    }
                    resulMap.put(describeField.getName(),
                            Persistence.getReadManager().convert(
                            EncodingUtil.stringToByte(resultSet.get(key)),
                            describeField.getClassField()));
                }
            }
            listCql.add(resulMap);
        }
        return listCql;
    }

    private void runSubObject(DescribeFamilyObject describeFamilyObject,
            Map<String, String> resultSet, Map<String, Object> resulMap,
            List<String> used) {
        for (String teste : informationCQL.variable) {
            DescribeField descField = describeFamilyObject.getField(teste);
            if (descField.hasChildren()) {
                Object bean = ReflectionUtil.newInstance(
                        descField.getClassField());
                if (bean == null) {
                    continue;
                }
                for (DescribeField subDescField : descField.getChildren()) {
                    if (resultSet.get(subDescField.getRealCqlName()) == null) {
                        used.add(subDescField.getRealCqlName());
                        continue;
                    }
                    ReflectionUtil.setMethod(bean,
                            ReflectionUtil.getField(
                            subDescField.getName().substring(
                            subDescField.getName().lastIndexOf(".") + 1),
                            descField.getClassField()),
                            Persistence.getReadManager().convert(
                            EncodingUtil.stringToByte(resultSet.get(
                            subDescField.getRealCqlName())),
                            subDescField.getClassField()));
                    used.add(subDescField.getRealCqlName());
                }
                resulMap.put(teste, bean);
            }
        }
    }

    /**
     * when the query is for all fields in object
     *
     * @param describeFamilyObject
     * @param cql
     * @return
     */
    @SuppressWarnings("rawtypes")
	private List<?> executeAll(DescribeFamilyObject describeFamilyObject,
            String cql) {
        try {
            return persistence.listbyQuery(persistence.executeCommandCQL(cql),
                    describeFamilyObject.getClassFamily());
        } catch (Exception exception) {
            Logger.getLogger(Persistence.class.getName()).log(Level.SEVERE,
                    null, exception);
        }
        return new ArrayList();
    }

    @Override
    public Object getSingleResult() {
        List<?> list = getResultList();
        if (list.size() > 0) {
            if (list.get(0) instanceof HashMap) {
                return hashMapContition(list);
            }
            return list.get(0);
        }
        return null;
    }

    /**
     * When the single result is a hasmap. This method tranform either array of
     * the objects or a object
     *
     * @param list
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private Object hashMapContition(List<?> list) {
        Map map = (Map) list.get(0);
        List objects = new ArrayList();
        for (Object object : map.keySet()) {
            if (map.get(object) != null) {
                objects.add(map.get(object));
            }
        }
        if (objects.size() == 1) {
            return objects.get(0);
        }
        return objects.toArray();
    }

    /**
     * replace the objects name to column's name
     *
     * @param describeFamilyObject
     * @return cql with column's name
     */
    private String replaceToCQLName(DescribeFamilyObject describeFamilyObject) {
        String newCQL = cql.toString().replace(
                describeFamilyObject.getClassFamily().getSimpleName(),
                describeFamilyObject.getColumnFamilyName());
        for (String column : informationCQL.variable) {
            if (newCQL.contains(column)) {
                newCQL = newCQL.replace(column,
                        describeFamilyObject.getField(column).getRealCqlName());
            }
        }
        for (String key : informationCQL.variabledMap.keySet()) {
            DescribeField describeField = describeFamilyObject.getField(
                    informationCQL.variabledMap.get(key).getName());
            if (informationCQL.isUpdate) {
                String valueAux = key.replace(InformationQuery.UPDATE_STRING,
                        "");
                if (newCQL.contains(valueAux)) {
                    newCQL = newCQL.replace(valueAux + " ",
                            describeField.getRealCqlName());
                }
            } else {
                if (newCQL.contains(key)) {
                    newCQL = newCQL.replace(key, describeField.getRealCqlName());
                }
            }
        }
        return newCQL;
    }

    @Override
    public Query setFirstResult(int startPosition) {
        isNegativeValue(startPosition);
        this.startPosition = startPosition;
        return this;
    }

    /**
     * Verify is the value is negative
     *
     * @param startPosition
     */
    private void isNegativeValue(int startPosition) {
        if (startPosition < 0) {
            throw new IllegalArgumentException("Illegal Argument: The argument"
                    + " must be not a negative value");
        }
    }

    @Override
    public Query setMaxResults(int maxResult) {
        isNegativeValue(maxResult);
        this.maxResult = maxResult;
        return this;
    }

    @Override
    public Query setParameter(String name, Object value) {
        String cqlAux = cql.toString();
        VariableConditions variable = informationCQL.whereMap.get(name);
        verifyErroParameter(value, variable);
        cqlAux = cqlAux.replace(":" + name, "'"
                + EncodingUtil.byteToString(
                Persistence.getWriteManager().convert(value)) + "'");
        cql = new StringBuilder(cqlAux);
        return this;
    }

    /**
     * Verify if exist error in parameter
     *
     * @param value
     * @param variable
     */
    private void verifyErroParameter(Object value, VariableConditions variable) {
        if (variable == null) {
            throw new IllegalArgumentException(" unknown parameter in query "
                    + cql.toString());
        }
        if (!value.getClass().equals(EasyCassandraManager.getFamily(
                informationCQL.columnFamily).getField(
                variable.getName()).getClassField())) {
            throw new IllegalArgumentException("You have attempted to set a "
                    + "value of type class " + value.getClass().getName()
                    + "  with expected type of class "
                    + EasyCassandraManager.getFamily(
                    informationCQL.columnFamily).getField(
                    variable.getName()).getClassField().getName());
        }
    }

    @Override
    public int getMaxResults() {
        return maxResult;
    }

    @Override
    public int getFirstResult() {
        return startPosition;
    }

    @Override
    public Set<Parameter<?>> getParameters() {
        Set<Parameter<?>> set = new HashSet<Parameter<?>>();
        for (String key : informationCQL.whereMap.keySet()) {
            set.add(informationCQL.whereMap.get(key));
        }
        return set;
    }

    @Override
    public Parameter<?> getParameter(int position) {
        isNegativeValue(position);
        for (Parameter parameter : getParameters()) {
            if (parameter.getPosition().equals(Integer.valueOf(position))) {
                return parameter;
            }
        }
        throw new IllegalArgumentException("unknown parameter to position "
                + position);
    }

    @Override
    public Parameter<?> getParameter(String name) {
        for (Parameter parameter : getParameters()) {
            if (((VariableConditions) parameter).getCondition().equals(name)) {
                return parameter;
            }
        }
        throw new IllegalArgumentException("unknown parameter to name "
                + name);
    }

    @Override
    public int executeUpdate() {
        DescribeFamilyObject describeFamilyObject =
                EasyCassandraManager.getFamily(informationCQL.columnFamily);
        for (String key : informationCQL.variabledMap.keySet()) {
            if (key.contains(InformationQuery.UPDATE_STRING)) {
                continue;
            }
            if (!TypeField.KEY.equals(describeFamilyObject.getField(
                    informationCQL.variabledMap.get(
                    key).getName()).getTypeField())) {
                throw new EasyCassandraException("In update's command in where"
                        + " condition must be only key");
            }
        }
        String cqlNew = replaceToCQLName(describeFamilyObject);
        return persistence.executeUpdateCql(cqlNew) ? 1 : 0;
    }

    @Override
    public Query setHint(String hintName, Object value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public Map<String, Object> getHints() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public <T> Query setParameter(Parameter<T> param, T value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public Query setParameter(Parameter<Calendar> param, Calendar value,
            TemporalType temporalType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public Query setParameter(Parameter<Date> param, Date value,
            TemporalType temporalType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public Query setParameter(String name, Calendar value,
            TemporalType temporalType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public Query setParameter(String name, Date value,
            TemporalType temporalType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public Query setParameter(int position, Object value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public <T> Parameter<T> getParameter(String name, Class<T> type) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public <T> Parameter<T> getParameter(int position, Class<T> type) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public boolean isBound(Parameter<?> param) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public <T> T getParameterValue(Parameter<T> param) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public Object getParameterValue(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public Object getParameterValue(int position) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public Query setFlushMode(FlushModeType flushMode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public FlushModeType getFlushMode() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public Query setLockMode(LockModeType lockMode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public LockModeType getLockMode() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public <T> T unwrap(Class<T> cls) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public Query setParameter(int position, Calendar value,
            TemporalType temporalType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    @Override
    public Query setParameter(int position, Date value,
            TemporalType temporalType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }
}
