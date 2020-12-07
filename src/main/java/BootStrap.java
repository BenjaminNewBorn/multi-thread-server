import connector.HttpServer;
import constant.ServerConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.Scanner;

public class BootStrap {
    public static void run() {
        try {
            initConfig();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to init server log ");
            return;
        }
        HttpServer server = new HttpServer();
        server.start(Integer.parseInt(System.getProperty(ServerConfig.PORT)));
        System.out.println("input quit to close server");
        Scanner in = new Scanner(System.in);
        while (!in.nextLine().equals("quit")) {}
        server.close();
    }

    private static void initConfig() throws IOException {
        Properties configs = new Properties();
        if (Files.exists(Path.of(BootStrap.class.getResource("/").toString().substring(5) + "/server-config.properties"))) {
            configs.load(BootStrap.class.getResourceAsStream("/server-config.properties"));
        } else {
            configs.load(BootStrap.class.getResourceAsStream("/default-server-config.properties"));
        }
        configs.forEach((k, v) -> {
            System.setProperty(k.toString(), v.toString());
        });
    }


    public static void main(String[] args) {
        BootStrap.run();
    }
}
