package ml.heartfulcpvp.dataapi.apiserver;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

public class DataApiHttpServer {
    private static final String CONTEXT_PATH = "/playerstatus/";

    public void start(int port) throws IOException {
        var server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext(CONTEXT_PATH, new PlayerStatsHandler());
        server.setExecutor(null);
        server.start();
    }
}
