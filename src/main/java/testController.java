package controller;

        import container.annotation.Controller;
        import container.annotation.RequestMapping;
        import container.annotation.RequestParam;
        import http.request.HttpRequest;

        import java.time.LocalDateTime;
        import java.time.ZoneOffset;
        import java.time.format.DateTimeFormatter;

@Controller
public class testController {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @RequestMapping("/test")
    public String test(HttpRequest request, @RequestParam(value = "msg", defaultValue = "null value") String msg) {
        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(request.getSession().getlastAccessTime() / 1000, 0, ZoneOffset.ofHours(8));
        request.setAttribute("lastConnectionTime", localDateTime.format(formatter));
        request.setAttribute("msg", msg);
        return "test.html";
    }
}
