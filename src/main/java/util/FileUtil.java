package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.function.Consumer;

public class FileUtil {
    public static void findClassFiles(Path dir, Consumer<Path> consumer) throws IOException {
        Iterator<Path> paths = Files.list(dir).iterator();
        while(paths.hasNext()) {
            Path path = paths.next();
            if(Files.isRegularFile(path) && path.toString().endsWith(".class")) {
                consumer.accept(path);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        findClassFiles(Path.of("/Users/apple/webserver/target/classes/"), System.out::println);
    }
}
