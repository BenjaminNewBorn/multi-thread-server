package connector;

import constant.HttpConstant;
import constant.ServerConfig;
import container.Container;
import exception.HttpMethodNotSupportException;
import http.request.HttpRequest;
import http.request.HttpRequestParse;
import http.response.HttpResponse;
import http.response.HttpStatus;
import util.ErrorResponseUtil;


import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

class RequestProcessor {
    private final static Logger logger = Logger.getLogger(RequestProcessor.class.getPackageName());

    private final static int REQUEST_THREAD_COUNT = Integer.parseInt(System.getProperty(ServerConfig.REQUEST_THREAD_COUNT));

    private final String WEBAPP_PATH = RequestProcessor.class.getResource("/").toString().substring(5) + "webapp/";

    private final Properties mime;

    private final ExecutorService threadPool = Executors.newFixedThreadPool(REQUEST_THREAD_COUNT);

    private final Container container;

    public RequestProcessor(Container container) {
        this.container = container;
        this.mime = new Properties();
        try{
            this.mime.load(ClassLoader.getSystemClassLoader().getResourceAsStream("mime.properties"));
        } catch (IOException e) {
            logger.warning("Load mime properties fail");
            e.printStackTrace();
        }
    }

    public void process(SocketWrapper socketWrapper) {
        threadPool.submit(new RequestProcessorTask(socketWrapper));
    }

    public void shutdown() {
        this.threadPool.shutdown();
    }

    private class RequestProcessorTask implements Runnable {
        private SocketWrapper socketWrapper;

        RequestProcessorTask(SocketWrapper socketWrapper){
            this.socketWrapper = socketWrapper;
        }

        @Override
        public void run() {
            //decode request
            HttpRequest request = HttpRequestParse.parseRequest(socketWrapper, RequestProcessor.this.container);

            //build response
            HttpResponse response = buildResponse(request);

            //write back response and deal with connection
            writeResponse(request, response);
            handleConnection(request);
            try{
                logger.info(String.format("%s| %s| %d | %d", socketWrapper.getClient().getRemoteAddress(), request.getUri(),response.getStatus().getCode(), response.getContentLentgh()));
            }catch (Exception e){}
        }


        private HttpResponse buildResponse(HttpRequest request){
            HttpResponse response;
            response = RequestProcessor.this.container.handle(request);

            //static response
            if(response == null) {
                response = processStaticResource(request);
            }
            return response;
        }

        /**
         * process static resource
         * */
        private HttpResponse processStaticResource(HttpRequest request) {
            HttpResponse response = new HttpResponse();
            String uri = request.getUri();
            int idx = uri.indexOf('?');
            String filename = uri.substring(1, idx == -1? uri.length(): idx);
            filename = RequestProcessor.this.WEBAPP_PATH + filename;
            System.out.println(filename);

            String extName = filename.substring(filename.lastIndexOf('.') + 1);
            response.setContentType(mime.getProperty(extName));
            try(FileInputStream fin = new FileInputStream(filename)) {
                response.setContentType(mime.getProperty(extName));
                byte[] buffer = new byte[512];
                int len;
                while ((len = fin.read(buffer)) > 0) {
                    response.getOutputSteam().write(buffer, 0, len);
                }
            } catch (FileNotFoundException e) {
                ErrorResponseUtil.sendErrorResponse(response, HttpStatus.SC_400);
                logger.warning(String.format("File not Found: %s" , filename));
            } catch (IOException e) {
                ErrorResponseUtil.sendErrorResponse(response, HttpStatus.SC_500);
                logger.warning(String.format("Failed to read static file: %s", filename));
            }

            return response;
        }


        private void writeResponse(HttpRequest request, HttpResponse response) {
            try{
                socketWrapper.getClient().write(response.getResponseData());

            }catch (IOException e) {
                logger.warning(String.format("Failed to write data to client[%s]: %s ", socketWrapper.getClient(), e.getMessage()));
                e.printStackTrace();
            }
        }


        private void handleConnection(HttpRequest request) {
            String conn = request.getHeader(HttpConstant.CONNECTION);
            if(conn != null && conn.contains("close")) {
                try{
                    logger.info(String.format("not persistant connection, close %s", socketWrapper.getClient()));
                    socketWrapper.close();
                } catch (IOException e){
                    logger.warning("Failed to close connection: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                socketWrapper.getPoller().register(socketWrapper.getClient(), false);
                logger.info(String.format("persistant connection: %s has registered again", socketWrapper.getClient()));
            }
        }
    }



}