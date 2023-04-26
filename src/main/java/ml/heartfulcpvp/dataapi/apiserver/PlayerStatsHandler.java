package ml.heartfulcpvp.dataapi.apiserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ml.heartfulcpvp.dataapi.PlayerStats;
import ml.heartfulcpvp.dataapi.exceptions.MinecraftPlayerNotFoundException;

import java.io.IOException;
import java.io.OutputStream;

public class PlayerStatsHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        var requestURI = exchange.getRequestURI().toString();
        var playerName = requestURI.substring(requestURI.lastIndexOf("/") + 1);

        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");

        String response = "";

        try {
            var playerStats = new PlayerStats(playerName);
            response = playerStats.buildJson();

            exchange.sendResponseHeaders(200, response.getBytes().length);
        } catch (MinecraftPlayerNotFoundException e) {
            response = "{\"error\":\"player not found\"}";
            exchange.sendResponseHeaders(400, response.getBytes().length);
        }

        var os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
