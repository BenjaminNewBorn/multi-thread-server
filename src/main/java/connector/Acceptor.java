package connector;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

public class Acceptor implements Runnable{
    private final static Logger logger = Logger.getLogger(Acceptor.class.getPackageName());
    private HttpServer server;

    public Acceptor(HttpServer server) {
        this.server = server;
    }


    @Override
    public void run() {
        while(server.isRunning()) {
            try {
                SocketChannel client = server.accept();
                client.configureBlocking(false);
                logger.info(String.format("%s get new connection request %s", Thread.currentThread().getName(), client.getRemoteAddress()));
                server.registerToPoller(client);
                
            }catch (IOException e) {
                if(server.isRunning()) {
                    e.printStackTrace();
                    logger.warning(String.format("%s have issue[%s]", Thread.currentThread().getName(), e.getMessage()));
                }
            }
        }
    }
}
