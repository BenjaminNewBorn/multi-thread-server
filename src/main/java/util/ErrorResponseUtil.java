package util;


import http.response.HttpResponse;
import http.response.HttpStatus;

import java.io.IOException;

public class ErrorResponseUtil {
    public static void sendErrorResponse(HttpResponse response, HttpStatus status) {
        response.setStatus(status);
        response.setContentType("text/html; charset=utf-8");
        String msg="<h4>" + status.getReason() + "</h4?>";

        try{
            response.getWriter().write(msg);
        }catch (IOException e){

        }
    }
}
