package search;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import play.Logger;
import play.Logger.ALogger;


public class ClientProvider {
	private static ALogger logger = Logger.of(ClientProvider.class);
	private static ClientProvider instance = null;
    private static Object lock      = new Object();
    
    private TransportClient transportClient;

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
    	transportClient = new TransportClient();
    	transportClient.addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
        logger.info("Client is prepared");
    }
  
public void closeClient(){
		transportClient.close();
        logger.info("Client is closed");
    }
    
    public TransportClient getClient(){
        return transportClient;
    }
    
}
