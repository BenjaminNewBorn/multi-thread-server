package HttpTask;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    /**
     * response http version
     */
    private String version;

    /**
     * response http code
     */
    private int code;

    /**
     * response http status
     */
    private String status;

    /**
     * response http headers
     */
    private Map<String, String> headers;

    /**
     * response http message
     */
    private String message;

    public HttpResponse() {
        headers = new HashMap<>();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String GenerateResponse(HttpRequest request, String response) {
        setCode(200);
        setStatus("ok");
        setVersion(request.getVersion());

        headers.put("content-Length", String.valueOf(response.getBytes().length));
        headers.put("content-type", "application/json");
        
        setMessage(response);

        StringBuilder builder = new StringBuilder();
        buildResponseLine(builder);
        buildResponseHeaders(builder);
        buildResponseMessage(builder);
        return builder.toString();
    }

    private void buildResponseLine(StringBuilder builder) {
        builder.append(getVersion()).append(" ");
        builder.append(getCode()).append(" ");
        builder.append(getStatus()).append("\n");
    }

    private void buildResponseHeaders(StringBuilder builder) {
        for(String key: headers.keySet()){
            builder.append(key).append(":").append(headers.get(key)).append("\n");
        }
        builder.append("\n");
    }

    private void buildResponseMessage(StringBuilder builder) {
        builder.append(getMessage());
    }
}