package container;

import exception.TemplateResolveException;
import http.request.HttpRequest;
import http.response.HttpResponse;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateParser {
    public static final Logger logger = Logger.getLogger(TemplateParser.class.getPackageName());

    public static final String WEBAPP_PATH = TemplateParser.class.getResource("/").toString().substring(5) + "webapp/";

    private static final Pattern pattern = Pattern.compile("\\$\\{(.*)}");


    public static void parse(HttpRequest request, HttpResponse response, String path) throws TemplateResolveException {
        StringBuilder builder = new StringBuilder();
        String content;
        try {
            content = Files.readString(Path.of(WEBAPP_PATH + path));
        } catch (IOException e) {
            logger.warning("Failed to get template" + e.getMessage());
            e.printStackTrace();
            return;
        }

        parseHolder(request, content, builder);

        try {
            Writer writer =response.getWriter();
            if(builder.length() != 0) {
                writer.write(builder.toString());
            } else {
                writer.write(content);
            }
            writer.close();
        }catch (IOException e){

        }
    }

    private static void parseHolder(HttpRequest request, String content, StringBuilder builder) throws TemplateResolveException {
        Matcher matcher = pattern.matcher(content);
        while(matcher.find()){
            String holder = matcher.group(1);
            int firstDotPosition = holder.indexOf('.');
            if(firstDotPosition == -1) {
                throw new TemplateResolveException();
            }
            String scope = holder.substring(0, firstDotPosition);
            String[] key = holder.substring(firstDotPosition + 1).split("\\.");
            Object value = null;
            System.out.println(scope);
        }

    }

    private static Object parse(Object value, String[] segments, int index) throws TemplateResolveException{
        reurn;
    }


}
