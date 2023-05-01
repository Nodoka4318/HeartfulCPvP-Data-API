package ml.heartfulcpvp.dataapi.apiserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ml.heartfulcpvp.dataapi.LoggingUtils;
import ml.heartfulcpvp.dataapi.PlayerStats;
import ml.heartfulcpvp.dataapi.exceptions.MinecraftPlayerNotFoundException;

import java.io.IOException;

public class StatusHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");

        String response = "{\"error\":\"unknown error occurred\"}";

        exchange.sendResponseHeaders(400, response.getBytes().length);

        var os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
