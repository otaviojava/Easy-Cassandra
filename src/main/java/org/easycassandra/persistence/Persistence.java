/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.persistence;

import org.easycassandra.annotations.ColumnValue;


import org.easycassandra.annotations.read.UTF8Read;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.easycassandra.util.EncodingUtil;


import org.easycassandra.util.ReflectionUtil;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.cassandra.thrift.Cassandra.Client;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.Compression;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.apache.cassandra.thrift.CqlResult;
import org.apache.cassandra.thrift.CqlRow;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.SchemaDisagreementException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;

import org.apache.thrift.TException;
import org.easycassandra.ConsistencyLevelCQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *main Class for persistence The Objects
 * @author otavio
 */
public class Persistence extends BasePersistence {

    private static Logger LOOGER = LoggerFactory.getLogger(Persistence.class);
    protected Client client = null;

    Persistence(Client client, AtomicReference<ColumnFamilyIds> referenciaSuperColunas) {
        this.client = client;
        this.referenciaSuperColunas = referenciaSuperColunas;
    }

    protected List createRunCql(String condiction, String keyString, List objects, Class persistenceClass, ConsistencyLevelCQL consistencyLevel, int limit) throws NumberFormatException, InstantiationException, IllegalAccessException {
        try {
            StringBuilder cql = new StringBuilder();

            cql.append("select KEY, ");

            cql.append(columnNames(persistenceClass));
            cql.append(" from ");
            cql.append(getColumnFamilyName(persistenceClass));
            cql.append(" USING ").append(consistencyLevel.getValue()).append(" ");   //padra One
            cql.append(" where ");
            cql.append(" ").append(condiction).append(" =");
            cql.append("'");
            cql.append(keyString);
            cql.append("' ");
            cql.append("LIMIT ").append(limit);//default 10000


            CqlResult execute_cql_query = executeCQL(cql.toString());
            objects = listbyQuery(execute_cql_query, persistenceClass);
        } catch (InvalidRequestException | UnavailableException | TimedOutException | SchemaDisagreementException | TException ex) {
            LOOGER.error("Error during execute CQL", ex);

        }

        return objects;
    }

    //insert commands
    public void insert(Object object) throws IOException {
        insert(object, ConsistencyLevel.ONE);
    }

    public void insert(Object object, ConsistencyLevel consistencyLevel) throws IOException {

        ColumnParent parent = new ColumnParent(getColumnFamilyName(object.getClass()));

        ByteBuffer rowid = getKey(object);


        List<Column> columns = getColumns(object);


        try {
            for (Column column : columns) {

                client.insert(rowid, parent, column, consistencyLevel);
            }

        } catch (InvalidRequestException | UnavailableException | TimedOutException | TException ex) {
            LOOGER.error("Error insert Objects", ex);
        }

    }

    public CqlResult executeCQL(String cql) throws NumberFormatException, InstantiationException, IllegalAccessException, InvalidRequestException, UnavailableException, TimedOutException, SchemaDisagreementException, TException {
        return client.execute_cql_query(ByteBuffer.wrap(cql.toString().getBytes()), Compression.NONE);
    }

    public List findAll(Class persistenceClass) throws NumberFormatException, InstantiationException, IllegalAccessException {
        return findAll(persistenceClass, ConsistencyLevelCQL.ONE, 10000);
    }

    public List findAll(Class persistenceClass, int limit) throws NumberFormatException, InstantiationException, IllegalAccessException {
        return findAll(persistenceClass, ConsistencyLevelCQL.ONE, limit);
    }

    public List findAll(Class persistenceClass, ConsistencyLevelCQL consistencyLevel) throws NumberFormatException, InstantiationException, IllegalAccessException {
        return findAll(persistenceClass, consistencyLevel, 10000);
    }

    public List findAll(Class persistenceClass, ConsistencyLevelCQL consistencyLevel, int limit) throws NumberFormatException, InstantiationException, IllegalAccessException {
        List list = new ArrayList<>();

        try {

            StringBuilder cql = new StringBuilder();
            cql.append(" select  KEY, ");
            cql.append(columnNames(persistenceClass));
            cql.append(" from ");
            cql.append(getColumnFamilyName(persistenceClass));
            cql.append(" USING ").append(consistencyLevel.getValue()).append(" ");   //padra One
            cql.append("LIMIT ").append(limit);//padrao 10000
            CqlResult execute_cql_query = executeCQL(cql.toString());
            list = listbyQuery(execute_cql_query, persistenceClass);
        } catch (InvalidRequestException | UnavailableException | TimedOutException | SchemaDisagreementException | TException ex) {
             LOOGER.error("Error during execute CQL", ex);

        }

        return list;
    }

    protected List listbyQuery(CqlResult execute_cql_query, Class persistenceClass) throws NumberFormatException, InstantiationException, IllegalAccessException {
        List<Map<String, ByteBuffer>> listMap = new ArrayList<>();

        for (CqlRow row : execute_cql_query.rows) {
            Map<String, ByteBuffer> mapColumn = new HashMap<>();

            for (Column cl : row.getColumns()) {

                mapColumn.put(EncodingUtil.byteToString(cl.name), cl.value);

            }
            listMap.add(mapColumn);
        }

        return getList(listMap, persistenceClass);

    }

    public Object findByKey(Object key, Class persistenceClass) throws NotFoundException, NumberFormatException, InstantiationException, IllegalAccessException {
        return findByKey(key, persistenceClass, 1, ConsistencyLevelCQL.ONE);
    }

    public Object findByKey(Object key, Class persistenceClass, ConsistencyLevelCQL consistencyLevel) throws NotFoundException, NumberFormatException, InstantiationException, IllegalAccessException {
        return findByKey(key, persistenceClass, 1, consistencyLevel);
    }

    public Object findByKey(Object key, Class persistenceClass, int limit, ConsistencyLevelCQL consistencyLevel) throws NotFoundException, NumberFormatException, InstantiationException, IllegalAccessException {
        List objects = new ArrayList<>();

        Field keyField = getKeyField(persistenceClass);
        ByteBuffer keyBuffer = writeMap.get(keyField.getType().getName()).getBytebyObject(key);
        String keyString = new UTF8Read().getObjectByByte(keyBuffer).toString();
        String condicao = "KEY";

        objects = createRunCql(condicao, keyString, objects, persistenceClass, consistencyLevel, limit);
        if (objects.size() > 0) {
            return objects.get(0);
        }
        return null;


    }

    //delete comand
    public void deleteByKeyValue(Object keyValue, Class obClass) {
        ByteBuffer keyBuffer = writeMap.get(getKeyField(obClass).getType().getName()).getBytebyObject(keyValue);
        String keyString = new UTF8Read().getObjectByByte(keyBuffer).toString();


        runDeleteCqlCommand(keyString, obClass);
    }

    public void delete(Object keyObject) {
        Field keyField = getKeyField(keyObject.getClass());
        ByteBuffer keyBuffer = writeMap.get(keyField.getType().getName()).getBytebyObject(ReflectionUtil.getMethod(keyObject, keyField.getName()));
        String keyString = new UTF8Read().getObjectByByte(keyBuffer).toString();

        runDeleteCqlCommand(keyString, keyObject.getClass());


    }

    protected void runDeleteCqlCommand(String keyString, Class persistenceClass) {
        try {
            StringBuilder cql = new StringBuilder();
            cql.append("delete ");
            cql.append(columnNames(persistenceClass));
            cql.append(" from ");
            cql.append(getColumnFamilyName(persistenceClass));
            cql.append(" where KEY = '");
            cql.append(keyString);
            cql.append("'");
            CqlResult cqlResult = executeCQL(cql.toString());
        } catch (NumberFormatException | InstantiationException | IllegalAccessException | InvalidRequestException | UnavailableException | TimedOutException | SchemaDisagreementException | TException ex) {

            LOOGER.error("Error during execute CQL", ex);
        }
    }

    //find index
    public List findByIndex(Object index, Class obClass) throws NotFoundException, NumberFormatException, InstantiationException, IllegalAccessException {
        return findByIndex(index, obClass, ConsistencyLevelCQL.ONE);
    }

    public List findByIndex(Object index, Class obClass, ConsistencyLevelCQL consistencyLevel) throws NotFoundException, NumberFormatException, InstantiationException, IllegalAccessException {
        return findByIndex(index, obClass, consistencyLevel, 10000);
    }

    public List findByIndex(Object index, Class obClass, ConsistencyLevelCQL consistencyLevelCQL, int limit) throws NotFoundException, NumberFormatException, InstantiationException, IllegalAccessException {
        List objects = new ArrayList<>();

        String indexString = index.toString();
        ColumnValue coluna = getIndexField(obClass).getAnnotation(ColumnValue.class);
        String condicao = coluna.nome();


        return createRunCql(condicao, indexString, objects, obClass, consistencyLevelCQL, limit);


    }

    public void update(Object object) throws Exception {
        update(object, ConsistencyLevelCQL.ONE);
    }

    public void update(Object object, ConsistencyLevelCQL consistencyLevel) throws Exception {
        StringBuilder cql = new StringBuilder();
        cql.append("UPDATE ");
        cql.append(getColumnFamilyName(object.getClass()));
        cql.append(" USING ").append(consistencyLevel.getValue()).append(" ");
        cql.append(" SET");
        List<String> strings = prepareCQLtoUpdate(object);
        int cont = 1;
        for (String string : strings) {
            cql.append(string);
            if (cont < strings.size()) {
                cql.append(" ,");
            }
            cont++;
        }

        cql.append(" where Key ='");
        cql.append("31");
        cql.append("'");
        executeCQL(cql.toString());
    }
}
