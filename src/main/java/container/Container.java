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
import http.session.ExpiredSessionCleaner;
import http.session.HttpSession;
import util.ErrorResponseUtil;

public class Container {
    private final long SESSION_EXPIRY_TIME = Long.parseLong(System.getProperty(ServerConfig.SESSION_EXPIRY_TIME));
    private final static Logger logger = Logger.getLogger(Container.class.getPackageName());
    private final Map<String, HttpSession> sessions = new ConcurrentHashMap<>(); //For Thread Safety
    private ExpiredSessionCleaner sessionCleaner;
    // private Expire
//    private  Map<String, TargetMethod> methodMap;


    public void init() throws ServerInternalException {
        logger.info("Init container");
        initExpiredSessionCleaner();
    }

    public HttpResponse handle(HttpRequest request) {
        int idx = request.getUri().indexOf('?');
        int end = idx == -1? request.getUri().length(): idx;
        String target = request.getUri().substring(0, end);

        logger.info(String.format("Target Name: %s", target));
        HttpResponse response = new HttpResponse();
        return null;
    }

    private void initExpiredSessionCleaner() {
        this.sessionCleaner = new ExpiredSessionCleaner(this);
        this.sessionCleaner.start();
    }

    public HttpSession createNewSession() {
        HttpSession session = new HttpSession(this);
        sessions.put(session.getId(), session);
        session.setIsNew(true);
        return session;
    }

    public HttpSession getSession(String id) {
        HttpSession session = sessions.get(id);
        if(session != null && session.isNew()) {
            session.setIsNew(false);
        }
        return session;
    }

    public void clearExpiredSession() {
        logger.info("start cleaning expired session");
        for(String key: sessions.keySet()) {
            if(System.currentTimeMillis() - sessions.get(key).getlastAccessTime() > SESSION_EXPIRY_TIME) {
                logger.info(String.format("Session[%s] is expired", sessions.get(key).getId()));
                sessions.remove(key);
            }
        }
    }

    public void invalidateSession(HttpSession session) {
        logger.info(String.format("Session[%s] is removed", session.getId()));
        this.sessions.remove(session.getId());
    }

    public void close() {
        this.sessionCleaner.shutdown();
    }
    
}
