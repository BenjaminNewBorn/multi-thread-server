package http.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import http.HttpMethod;




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

    // private HttpSession session;

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


    // private void decodeRequestLine(BufferedReader reader) throws IOException {
    //     String[] strs = reader.readLine().split(" ");
    //     assert strs.length == 3;
    //     setMethod(strs[0]);
    //     setUri(strs[1]);
    //     setVersion(strs[2]);
    // }


    // private void decodeRequestHeader(BufferedReader reader) throws IOException {
    //     String line = reader.readLine();
    //     while(!"".equals(line)) {
    //         String[] strs = line.split(":");
    //         assert strs.length == 2;
    //         headers.put(strs[0].toLowerCase().trim(), strs[0].trim());
    //         line = reader.readLine();
    //     }
    // }

    // public void parseToRequest(InputStream requestStream) throws IOException {
    //     BufferedReader reader = new BufferedReader(new InputStreamReader(requestStream, "UTF-8"));
    //     decodeRequestLine(reader);
    //     decodeRequestHeader(reader);
    // }

    // public boolean isKeepAlive() {
    //     return  Boolean.parseBoolean(headers.getOrDefault("keep-Alive", "false"));
    // }

}
