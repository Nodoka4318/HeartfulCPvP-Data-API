package ml.heartfulcpvp.dataapi.exceptions;

import java.io.Serial;

public class MinecraftPlayerNotFoundException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    public MinecraftPlayerNotFoundException(String playerName) {
        super("The player, " + playerName + " is not a valid player.");
    }
}
