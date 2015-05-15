package search;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;

import play.Logger;
import play.Logger.ALogger;


public class ClientProvider {
	private static ALogger logger = Logger.of(ClientProvider.class);
	private static ClientProvider instance = null;
    private static Object lock      = new Object();
    
    private Client client;
    private Node node;

    public static ClientProvider instance(){
        
        if(instance == null) { 
            synchronized (lock) {
                if(null == instance){
                    instance = new ClientProvider();
                }
            }
        }
        return instance;
    }

    public void prepareClient(){
        node   = nodeBuilder().node();
        client = node.client();
        logger.info("Client is prepared");
    }

    public void closeNode(){
        
        if(!node.isClosed())
            node.close();
        logger.info("Node is closed");
    }
    
    public Client getClient(){
        return client;
    }
    
}
