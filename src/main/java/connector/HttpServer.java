package connector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import constant.ServerConfig;
import container.Container;
import exception.ServerInternalException;


public class HttpServer {
    private static final Logger logger = Logger.getLogger(HttpServer.class.getPackageName());
    private final int pollerThreadCount = Integer.parseInt(System.getProperty(ServerConfig.POLLER_THREAD_COUNT));private Container container;
    private RequestProcessor requestProcessor;
    private ServerSocketChannel server;
    private Acceptor acceptor;
    private volatile int port;
    private List<Poller> pollers;
    private final AtomicInteger pollerIndex = new AtomicInteger(0);
    private volatile boolean isRunning = true;


    public void start(int port) {
        this.port = port;
        try {
            initServerSocket(port);
            initPollers();
            initAcceptor();
            initContianer();
            initRequestProcessor();
            logger.info("Server start successfully");
        }catch (IOException | ServerInternalException e) {
            logger.severe("Server failed to start" + e.getMessage());
            this.isRunning = false;
            e.printStackTrace();
        }
    }
    

    private void initServerSocket(int port) throws IOException {
        logger.info(String.format("Listening %s port", System.getProperty(ServerConfig.PORT)));
        this.server = ServerSocketChannel.open();
        this.server.bind(new InetSocketAddress(port));
        this.server.configureBlocking(true);
    }


    private void initAcceptor() {
        logger.info("Start Acceptor");
        this.acceptor = new Acceptor(this);
        Thread thread = new Thread(this.acceptor, "Acceptor");
        thread.setDaemon(true);
        thread.start();
    }

    private void initPollers() throws IOException {
        logger.info((String.format("start poller threads, number is %s", pollerThreadCount)));
        this.pollers = new ArrayList<>();
        for(int i = 0; i < pollerThreadCount; i++) {
            String pollerName = "Poller[" + i + "]";
            Poller poller = new Poller(pollerName, this);
            Thread pollerThread = new Thread(poller, pollerName);
            pollerThread.setDaemon(true);
            pollerThread.start();
            pollers.add(poller);
        }
    }

    private void initContianer() throws ServerInternalException {
        logger.info("init continaer");
        this.container = new Container();
        this.container.init();
    }

    private void initRequestProcessor() {
        logger.info("init Request Processor");
        this.requestProcessor = new RequestProcessor(this.container);
    }

    public void close() {
        isRunning = false;
        logger.info("server closed");
        this.requestProcessor.shutdown();
            this.container.close();
        try {
            server.close();
        }catch (IOException e){

        }
    }

    /**
     * For Accepter, listening new connection
     * */
    SocketChannel accept() throws IOException {
        return server.accept();
    }

    public int getPort() {
        return port;
    }

    /**
 * deal with ready client and send it to requestProcessor
 * @param socketWrapper
 */
    public void processClient(SocketWrapper socketWrapper) {
        this.requestProcessor.process(socketWrapper);
    }

    /**
     * register client to poller
     * */
    public void registerToPoller(SocketChannel client) {
        this.pollers.get(pollerIndex.getAndIncrement() % pollerThreadCount).register(client, true);
    }

    boolean isRunning() {
        return this.isRunning;
    }



}
