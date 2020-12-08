package http.session;

import constant.ServerConfig;
import container.Container;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ExpiredSessionCleaner {
    private final static Logger logger = Logger.getLogger(ExpiredSessionCleaner.class.getPackageName());
    private final static long SESSION_CLEANING_CYCLE = Long.parseLong(System.getProperty(ServerConfig.SESSION_CLEANING_CYCLE));
    private final static ScheduledExecutorService schedule = Executors.newSingleThreadScheduledExecutor();

    private Container container;


    public ExpiredSessionCleaner(Container container) {
        this.container = container;
    }

    public void start() {
        logger.info("Start Session Cleaner");
        schedule.scheduleAtFixedRate(container::clearExpiredSession, 0, SESSION_CLEANING_CYCLE, TimeUnit.MILLISECONDS);

    }

    public void shutdown() {
        logger.info("shutdown session cleaner");
        schedule.shutdown();
    }
}
