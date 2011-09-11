/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easycassandra.persistence;

import org.easycassandra.util.DomUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.Cassandra.Client;
import org.apache.cassandra.thrift.InvalidRequestException;


import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author otavio
 */
public class EasyCassandraManager implements AutoCloseable {

    private static Logger LOOGER = LoggerFactory.getLogger(DomUtil.class);
    private static List<Conection> conections;
    private static AtomicReference<ColumnFamilyIds> referenciaSuperColunas;
    private static Thread escreverSuperColuna;

    public static Client getClient(String keySpace, String host, int port) {
        initListConection();
        Conection conection = new Conection(host, keySpace, port);
        if (conections.contains(conection)) {
            return conections.get(conections.indexOf(conection)).getClient();

        }

        try {

            TTransport transport = new TFramedTransport(new TSocket(host, port));
            TProtocol protocol = new TBinaryProtocol(transport);
            Cassandra.Client client = new Cassandra.Client(protocol);
            transport.open();
            client.set_keyspace(keySpace);
            conection.setClient(client);
            conection.setTransport(transport);
            conections.add(conection);
            return client;
        } catch (InvalidRequestException | TException ex) {
            LOOGER.error("Error during crete Client for Cassandra", ex);
        }
        return null;
    }

    public static Persistence getPersistence(String keySpace, String host, int port) {
        Persistence persistence = new Persistence(getClient(keySpace, host, port), referenciaSuperColunas);


        return persistence;
    }

    private static void initListConection() {
        if (conections == null) {
            conections = new ArrayList<>();
            referenciaSuperColunas = new AtomicReference<>(ReadColumnReferenceID.getColunaID());

        }


        if (escreverSuperColuna == null) {
            escreverSuperColuna = new Thread(new WriteColumnReferenceID(referenciaSuperColunas));

        }

    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    @Override
    public void close() throws Exception {
        closeClients();
    }

    public static void closeClients() {
        WriteColumnReferenceID.stop();

        for (Conection conexao : conections) {
            conexao.getTransport().close();
        }
        DomUtil.getFileDom(referenciaSuperColunas.get());

    }
}
