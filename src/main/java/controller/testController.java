package controller;

import container.annotation.Controller;
import container.annotation.RequestMapping;
import container.annotation.RequestParam;
import http.request.HttpRequest;

import java.time.format.DateTimeFormatter;

@Controller
public class testController {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @RequestMapping("/test")
    public String test(HttpRequest request, @RequestParam(value = "echo", defaultValue = "null value"), String echo) {
        request.setMessage("msg");
        return "test.html";
    }
}
