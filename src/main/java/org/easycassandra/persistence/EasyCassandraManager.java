package org.easycassandra.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.Cassandra.Client;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.easycassandra.util.DomUtil;


/**
 *Class for manage  Connections
 * @author otavio
 */
public class EasyCassandraManager {

   
	/**
	 * List with all Connections
	 */
    private static List<Connection> conections;
    
    /**
     * information about id Column Family
     */
    private static AtomicReference<ColumnFamilyIds> referenceSuperColunms;
    
    static {
    	conections=new ArrayList<>();
    	referenceSuperColunms=new AtomicReference<>();
    	ReadDocument readDocument=new ReadDocument();
    	referenceSuperColunms.set(readDocument.read());
    }

    /**
     * Methodo for create the Casansndra's Client
     * @param keySpace
     * @param host
     * @param port
     * @return
     */
    public static Client getClient(String keySpace, String host, int port) {
        Connection conection = new Connection(host, keySpace, port);
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
        } catch (InvalidRequestException | TException exception) {
             Logger.getLogger(EasyCassandraManager.class.getName()).log(Level.SEVERE, null, exception);
        }
        return null;
    }

    /**
     * Class for create the persistence from Client
     * @param keySpace
     * @param host
     * @param port
     * @see Persistence
     * @see EasyCassandraManager#getClient(String, String, int)
     * @return
     */
    public static Persistence getPersistence(String keySpace, String host, int port) {
        Persistence persistence = new Persistence(getClient(keySpace, host, port), referenceSuperColunms,keySpace);


        return persistence;
    }

    

    /**
     * When the JVM call finalize in this object
     * its close all connections
     */
    @Override
    protected void finalize() throws Throwable {
    	  closeClients();
    	  super.finalize();
    }


    /**
     * Method for close all Cleint in the list
     * @see EasyCassandraManager#conections
     */
    public static void closeClients() {

        for (Connection conexao : conections) {
            conexao.getTransport().close();
        }
        DomUtil.getFileDom(referenceSuperColunms.get());

    }
}
