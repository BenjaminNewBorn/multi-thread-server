package http.request;

import connector.SocketWrapper;
import constant.HttpConstant;
import container.Container;
import http.HttpMethod;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HttpRequestParse {
    public static HttpRequest parseRequest(SocketWrapper socketWrapper, Container container) {
        HttpRequest request = new HttpRequest();
        Scanner in = new Scanner(socketWrapper.getClient());
        parseReqline(in, request);
        parseParams(in, request);
        parseHeaders(in, request);
        parseBody(in, request);
        return request;
    }


    private static void parseReqline(Scanner in, HttpRequest request) {
        String[] strs = in.nextLine().split(" ");
        request.setMethod(HttpMethod.get(strs[0]));
        request.setUri(URLDecoder.decode(strs[1]));
        request.setVersion(strs[2]);
    }

    private static void parseParams(Scanner in, HttpRequest request) {
        Map<String, List<String>> params = new HashMap<>();
        if(request.getUri().indexOf('?') == -1){
            request.setParams(params);
            return;
        }
        String[] kv;
        String[] parameters = request.getUri().substring(request.getUri().indexOf('?') + 1).split("&");
        for(String param: parameters) {
            kv = param.split("=");
            params.putIfAbsent(kv[0], new ArrayList<>());
            params.get(kv[0]).add(kv[1]);
        }
        request.setParams(params);
    }

    private static void parseHeaders(Scanner in, HttpRequest request) {
        Map<String, String> headers = new HashMap<>();
        String line = in.nextLine();
        while("".equals(line)) {
            String[] strs = line.split(":");
            headers.put(strs[0], strs[1].trim());
        }
        request.setHeaders(headers);
    }

    private static void parseBody(Scanner in, HttpRequest request) {
        if(!request.getMethod().equals(HttpMethod.POST)){
            return;
        }
        if(request.getHeader(HttpConstant.CONTENT_TYPE).equals(HttpConstant.POST_COMMIT_FORM)){
            String[] strs, kv;
            strs = in.nextLine().split("&");
            for(String param: strs){
                kv = param.split("=");
                request.addParam(kv[0], kv[1]);
            }
        }
    }
}