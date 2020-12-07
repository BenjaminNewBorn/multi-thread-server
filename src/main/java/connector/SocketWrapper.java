package connector;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class SocketWrapper {
    private SocketChannel client;
    private Poller poller;
    private long lastConnectionTime;

    public long getLastConnectionTime() {
        return lastConnectionTime;
    }

    SocketWrapper(SocketChannel client, Poller poller) {
        this.client = client;
        this.poller = poller;
        this.lastConnectionTime = System.currentTimeMillis();
    }

    public SocketChannel getClient(){
        return client;
    }

    public Poller getPoller(){
        return poller;
    }

    @Override
    public String toString() {
        return "SocketWrapper[" + client + "]"; 
    }


    void close() throws IOException {
        poller.cancelReadListening(this);
        poller.getClients().remove(this.getClient());
        client.close();
    }

    public void setLastConnectionTime(long lastConnectionTime) {
        this.lastConnectionTime = lastConnectionTime;
    }




}
