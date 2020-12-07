package connector;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import constant.ServerConfig;

public class Poller implements Runnable{
    private final long expiryTime = Long.parseLong(System.getProperty(ServerConfig.CONNECTION_EXPIRY_TIME));
    private final static Logger logger = Logger.getLogger(Poller.class.getPackageName());
    private Selector selector;
    private String pollerName;
    private HttpServer server;
    private Map<SocketChannel, SocketWrapper> clients;


    private Queue<PollerEvent> pollerEventQueue;

    Poller(String pollerName, HttpServer server)throws IOException {
        this.selector = Selector.open();
        this.pollerName = pollerName;
        this.server = server;
        this.pollerEventQueue = new ConcurrentLinkedQueue<>();
        this.clients = new ConcurrentHashMap<>();
    }

    @Override
    public void run(){
        logger.info(this.pollerName + " start asking client connection");
        while(server.isRunning()) {
            try {
                //listeng the connections in poller event queue
                handleEvents();

                if(selector.select() <= 0) {
                    continue;
                }


                logger.info(String.format("%s starting read event", this.pollerName));
                //Get ready event
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();
                while(iter.hasNext()) {
                    SelectionKey key = iter.next();
                    if(key.isReadable()) {
                        logger.info(String.format("%s is ready, starting reading", ((SocketChannel)key.channel()).getRemoteAddress()));
                        SocketWrapper socketWrapper = (SocketWrapper) key.attachment();
                        socketWrapper.setLastConnectionTime(System.currentTimeMillis());

                        cancelReadListening(socketWrapper);
                        server.processClient(socketWrapper);
                    }
                }
            }catch (IOException e) {
                logger.warning(String.format("selector[%s] issue: %s", this.pollerName, e.getMessage()));
                e.printStackTrace();
            }
        }
    }


    void register(SocketChannel client, boolean isNew) {
        SocketWrapper socketWrapper;
        if(isNew) {
            logger.info(String.format("new client is registered to PollerEvent %s queue", this.pollerName));
            socketWrapper = new SocketWrapper(client, this);
            clients.put(client, socketWrapper);
        } else {
            socketWrapper = clients.get(client);
        }
        pollerEventQueue.offer(new PollerEvent(socketWrapper));
        selector.wakeup();
    }

    public Selector getSelector() {
        return this.selector;
    }

    private void handleEvents() {
        int size = pollerEventQueue.size();
        for(int i = 0; i < size; i++) {
            if(pollerEventQueue.peek() != null) {
                pollerEventQueue.poll().start();
            }
        }
    }

    public void cancelReadListening(SocketWrapper socketWrapper) {
        socketWrapper.getClient().keyFor(selector).cancel();
    }

    void close() throws IOException {
        for(SocketWrapper client: clients.values()) {
            client.close();
        }
        selector.close();
    }
    





    private class PollerEvent {
        private SocketWrapper socketWrapper;

        PollerEvent(SocketWrapper socketWrapper) {
            this.socketWrapper = socketWrapper;
        }

        void start() {
            try{
                if(socketWrapper.getClient().isOpen()) {
                    socketWrapper.getClient().register(Poller.this.getSelector(), SelectionKey.OP_READ, socketWrapper);
                    logger.info(String.format("%s start listening %s", Poller.this.pollerName, socketWrapper.getClient().getRemoteAddress()));
                }
            }catch (ClosedChannelException e) {
                e.printStackTrace();
                logger.info(String.format("%s failed in listering: [%s] ", Poller.this.pollerName, e.getMessage()));
            } catch (IOException ie){

            }
        }
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    public String getPollerName() {
        return pollerName;
    }

    public void setPollerName(String pollerName) {
        this.pollerName = pollerName;
    }

    public Map<SocketChannel, SocketWrapper> getClients() {
        return clients;
    }

    public void setClients(Map<SocketChannel, SocketWrapper> clients) {
        this.clients = clients;
    }

    
}
