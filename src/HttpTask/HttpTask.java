package HttpTask;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class HttpTask implements Runnable{
    private Socket socket;
    private HttpRequest httpRequest;
    private HttpResponse httpResponse;

    public HttpTask(Socket socket) {
        this.socket = socket;
        httpRequest = new HttpRequest();
        httpResponse = new HttpResponse();
    }

    @Override
    public void run() {
        if(socket == null) {
            throw new IllegalArgumentException("socket is null");
        }

        try{
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            httpRequest.parseToRequest(socket.getInputStream());
            try{
                String responseMessage = "Server get message successful";
                String resString = httpResponse.GenerateResponse(httpRequest, responseMessage);
                System.out.println(resString);
                out.println(resString);
            } catch (Exception e) {
                String resString = httpResponse.GenerateResponse(httpRequest, e.toString());
                out.println(resString);
            }
            out.flush();
        } catch(IOException e){
            e.printStackTrace();
        }finally {
            try{
                socket.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
