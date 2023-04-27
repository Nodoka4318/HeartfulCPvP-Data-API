package ml.heartfulcpvp.dataapi;

import java.util.logging.Logger;

public class LoggingUtils {
    private static Logger logger;

    public static void setLogger(Logger logger) {
        LoggingUtils.logger = logger;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void Log(String message) {
        logger.info(message);
    }
}
