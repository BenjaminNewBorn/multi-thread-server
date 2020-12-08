package http.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import constant.HttpConstant;
import http.Cookie;
import http.HttpMethod;
import http.session.HttpSession;


public class HttpRequest {
    /**
     * Request http method
     */
    private HttpMethod method;

    /**
     * request http uri
     */
    private String uri;

    /**
     * request http version
     */
    private String version;

    /**
     * request http header
     */
    private Map<String, String> headers;

    /**
     * request message
     */
    private String message;

    private HttpSession session;


    private Map<String, Cookie> cookies;

    public HttpSession getSession() {
        return session;
    }

    private Map<String, List<String>> params;

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getHeader(String key) {
        return this.headers.get(key);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, List<String>> getParams() {
        return params;
    }

    public void setParams(Map<String, List<String>> params) {
        this.params = params;
    }

    public void addParam(String k, String v) {
        params.putIfAbsent(k, new ArrayList<>());
        params.get(k).add(v);
    }

    public void setSession(HttpSession session) {
        this.session = session;
        this.cookies.remove(HttpConstant.JSESSIONID);
        this.cookies.put(HttpConstant.JSESSIONID, new Cookie(HttpConstant.JSESSIONID, session.getId()));
    }

    public Cookie getCookie(String key) {
        return this.cookies.get(key);
    }

    public void setCookies(Map<String, Cookie> cookies) {
        this.cookies = cookies;
    }

    public Map<String, Cookie> getCookies() {
        return cookies;
    }


}
