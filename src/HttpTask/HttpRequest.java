package HttpTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    /**
     * Request http method
     */
    private String method; 
    
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
    private HashMap<String, String> headers;

    /**
     * request message
     */
    private String message;

    public HttpRequest() {
        headers = new HashMap<>();
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
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

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    private void decodeRequestLine(BufferedReader reader) throws IOException {
        String[] strs = reader.readLine().split(" ");
        assert strs.length == 3;
        setMethod(strs[0]);
        setUri(strs[1]);
        setVersion(strs[2]);
    }


    private void decodeRequestHeader(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        while(!"".equals(line)) {
            String[] strs = line.split(":");
            assert strs.length == 2;
            headers.put(strs[0].toLowerCase().trim(), strs[0].trim());
            line = reader.readLine();
        }
    }

    public void parseToRequest(InputStream requestStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(requestStream, "UTF-8"));
        decodeRequestLine(reader);
        decodeRequestHeader(reader);
    }

    public boolean isKeepAlive() {
        return  Boolean.parseBoolean(headers.getOrDefault("keep-Alive", "false"));
    }

}
