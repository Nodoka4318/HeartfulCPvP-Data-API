package ml.heartfulcpvp.dataapi.apiserver;

import com.sun.net.httpserver.HttpServer;
import ml.heartfulcpvp.dataapi.Config;
import ml.heartfulcpvp.dataapi.LoggingUtils;
import ml.heartfulcpvp.dataapi.exceptions.InvalidConfigException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

public class DataApiHttpServer {
    private final String PLAYER_DATA_CONTEXT_PATH;
    private final String STATUS_CONTEXT_PATH;
    private static Logger logger;
    private JavaPlugin plugin;

    public DataApiHttpServer(JavaPlugin plugin) {
        logger = LoggingUtils.getLogger();
        this.plugin = plugin;

        String pdpath;
        String spath;
        try {
            pdpath = Config.getConfig().getPlayerDataContextPath();
            spath = Config.getConfig().getStatusContextPath();
        } catch (InvalidConfigException ex) {
            logger.warning(ex.getMessage());
            pdpath = Config.DEFAULT_PLAYER_DATA_CONTEXT_PATH;
            spath = Config.DEFAULT_STATUS_CONTEXT_PATH;
        }

        PLAYER_DATA_CONTEXT_PATH = pdpath;
        STATUS_CONTEXT_PATH = spath;
    }

    public void start(int port) throws IOException {
        var server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext(PLAYER_DATA_CONTEXT_PATH, new PlayerStatsHandler());
        server.createContext(STATUS_CONTEXT_PATH, new StatusHandler());

        server.setExecutor(null);
        server.start();
        logger.info("Listening at " + PLAYER_DATA_CONTEXT_PATH + " on port " + port + ".");
        logger.info("Listening at " + STATUS_CONTEXT_PATH + " on port " + port + ".");
    }
}
