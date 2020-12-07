package http.session;

import container.Container;

import java.util.Map;
import java.util.UUID;


public class HttpSession {
    private String sessionId;
    private Map<String, Object> attributes;
    private long createTime;
    private long lastAccessTime;
    private Container container;
    private boolean isNew;

    public HttpSession(Container container) {
        this.sessionId = UUID.randomUUID().toString();
        this.createTime = System.currentTimeMillis();
        this.lastAccessTime = this.createTime;
        this.container = container;
    }

    public String getId() {
        return this.sessionId;
    }


    public long getlastAccessTime() {
        return this.lastAccessTime;
    }

    public void setLastAccessTime(long time) {
        this.lastAccessTime = time;
    }

    public long getCreationTime() {
        return createTime;
    }

    public boolean isNew() {
        return this.isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

}
