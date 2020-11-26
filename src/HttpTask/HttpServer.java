package HttpTask;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HttpServer {
    private static ExecutorService executor = Executors.newSingleThreadExecutor();
    private static ExecutorService taskExecutor;
    private static int PORT = 9999;

    static void startHttpServer() {
        int NumThread = Runtime.getRuntime().availableProcessors();
        taskExecutor = new ThreadPoolExecutor(NumThread, NumThread, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new ThreadPoolExecutor.DiscardPolicy());
        System.out.println("Service Start!");

        while(true) {
            try{
                ServerSocket serverSocket  = new ServerSocket(PORT);
                executor.submit(new ServerThread(serverSocket));
                break;
            } catch(Exception e) {
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch(InterruptedException ie){
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private static class ServerThread implements Runnable {
        private ServerSocket serverSocket;
        public ServerThread(ServerSocket serverSocket){
            this.serverSocket = serverSocket;
        }

        @Override
        public void run(){
            while(true) {
                try {
                    Socket socket = this.serverSocket.accept();
                    System.out.println("access successful");
                    HttpTask httpTask = new HttpTask(socket);
                    taskExecutor.submit(httpTask);
                } catch(Exception e) {
                    e.printStackTrace();
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException ie){
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        startHttpServer();
    }

}
