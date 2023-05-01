package ml.heartfulcpvp.dataapi.apiserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ml.heartfulcpvp.dataapi.LoggingUtils;
import ml.heartfulcpvp.dataapi.PlayerStats;
import ml.heartfulcpvp.dataapi.SrvStatus;
import ml.heartfulcpvp.dataapi.exceptions.MinecraftPlayerNotFoundException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class StatusHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        var requestURI = exchange.getRequestURI().toString();
        LoggingUtils.Log("Request accepted ; " + requestURI);

        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");

        String response = "{\"error\":\"unknown error occurred\"}";

        try {
            var status = new SrvStatus();
            response = new StatusResponse(status).export();

            exchange.sendResponseHeaders(200, response.getBytes().length);
        } catch (Exception ex) {
            ex.printStackTrace();
            exchange.sendResponseHeaders(400, response.getBytes().length);
        }

        var os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
