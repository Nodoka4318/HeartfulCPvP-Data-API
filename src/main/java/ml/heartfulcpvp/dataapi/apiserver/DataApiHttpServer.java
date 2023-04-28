package ml.heartfulcpvp.dataapi.apiserver;

import com.sun.net.httpserver.HttpServer;
import ml.heartfulcpvp.dataapi.Config;
import ml.heartfulcpvp.dataapi.LoggingUtils;
import ml.heartfulcpvp.dataapi.exceptions.InvalidConfigException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

public class DataApiHttpServer {
    private final String CONTEXT_PATH;
    private static final Logger logger = LoggingUtils.getLogger();

    public DataApiHttpServer() {
        String path;
        try {
            path = Config.getConfig().getContentPath();
        } catch (InvalidConfigException ex) {
            logger.warning(ex.getMessage());
            path = Config.DEFAULT_CONTENT_PATH;
        }

        CONTEXT_PATH = path;
    }

    public void start(int port) throws IOException {
        var server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext(CONTEXT_PATH, new PlayerStatsHandler());
        server.setExecutor(null);
        server.start();
        logger.info("Listening at " + CONTEXT_PATH + " on port " + port + ".");
    }
}
