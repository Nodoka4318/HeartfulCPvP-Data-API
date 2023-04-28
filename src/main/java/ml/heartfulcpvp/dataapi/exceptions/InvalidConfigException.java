package ml.heartfulcpvp.dataapi.exceptions;

import java.io.Serial;

public class InvalidConfigException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidConfigException(String reason) {
        super(reason);
    }
}
