package container;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import constant.ServerConfig;
import exception.HttpMethodNotSupportException;
import exception.ServerInternalException;
import http.HttpMethod;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.HttpStatus;
import util.ErrorResponseUtil;

public class Container {
//    private final long SESSION_EXPIRY_TIME = Long.parseLong(ServerConfig.SESSION_EXPIRY_TIME);
    private final static Logger logger = Logger.getLogger(Container.class.getPackageName());
    // private final Map<String, HttpSession> sessions = new ConcurrentHashMap<>(); //For Thread Safety
    // private Expire
//    private  Map<String, TargetMethod> methodMap;


    public void init() throws ServerInternalException {
        logger.info("Init container");
//        initExpiredSessionCleaner();
    }

    public HttpResponse handle(HttpRequest request) {
        int idx = request.getUri().indexOf('?');
        int end = idx == 1? request.getUri().length(): idx;
        String target = request.getUri().substring(0, end);

        logger.info(String.format("Target Name: %s", target));
        HttpResponse response = new HttpResponse();
        return null;
    }
    
}
