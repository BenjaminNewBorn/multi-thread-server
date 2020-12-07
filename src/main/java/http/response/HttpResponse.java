package http.response;

import constant.HttpConstant;
import http.request.HttpRequest;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class HttpResponse {
    private HttpStatus status;
    private Map<String, String> headers;
    private ByteArrayOutputStream content;

    public HttpResponse() {
        this.headers = new HashMap<>();
        this.content = new ByteArrayOutputStream();
        this.status = HttpStatus.SC_200;
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setContentType(long length) {
        this.headers.put(HttpConstant.CONTENT_LENGTH, length + "");
    }

    public void setContentType(String contentType) {
        this.headers.put(HttpConstant.CONTENT_TYPE, contentType);
    }

    public void setCharacterEncoding(String charset) {
        this.headers.put(HttpConstant.CONTENT_ENCODING, charset);
    }

    public void sendError(int httpCode, String msg) {
        switch (httpCode) {
            case 400:
                this.status = HttpStatus.SC_400;
                break;
            case 403:
                this.status = HttpStatus.SC_403;
                break;
            case 404:
                this.status = HttpStatus.SC_404;
                break;
            case 500:
                this.status = HttpStatus.SC_500;
                break;
        }
        this.content.reset();
        try{
            this.headers.put(HttpConstant.CONTENT_ENCODING, "UTF-8");
            this.content.write(msg.getBytes(StandardCharsets.UTF_8));
        }catch (Exception e) {

        }
    }

    public ByteBuffer[] getResponseData() {
        byte[] bytes = content.toByteArray();
        this.headers.put(HttpConstant.CONTENT_LENGTH, bytes.length+"");
        return new ByteBuffer[] {buildHeader(), ByteBuffer.wrap(bytes)};
    }

    private ByteBuffer buildHeader() {
        StringBuilder builder = new StringBuilder();
        buildResponseLine(builder);
        buildResponseHeaders(builder);
        builder.append(HttpConstant.CRLF);
        return ByteBuffer.wrap(builder.toString().getBytes(StandardCharsets.US_ASCII));
    }

    private void buildResponseLine(StringBuilder builder) {
        builder.append(HttpConstant.PROTOCOL).append(" ");
        builder.append(status.getCode()).append(" ");
        builder.append(status.getReason()).append(HttpConstant.CRLF);
    }

    private void buildResponseHeaders(StringBuilder builder) {
        for(String key: headers.keySet()){
            builder.append(key).append(":").append(headers.get(key)).append(HttpConstant.CRLF);
        }
    }

    public int getContentLentgh() {
        return Integer.parseInt(this.headers.get(HttpConstant.CONTENT_LENGTH));
    }

    public Writer getWriter() {
        return new OutputStreamWriter(content);
    }

    public OutputStream getOutputSteam() {
        return content;
    }










}
