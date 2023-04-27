package ml.heartfulcpvp.dataapi;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jdk.jshell.spi.ExecutionControl;

public class Config {
    private final String CONFIG_DIRECTORY_PATH = "./HeartfulCPvP/DataApi";
    private final String CONFIG_FILE_PATH = "./HeartfulCPvP/DataApi/config.json";

    @SerializedName("port")
    @Expose
    private int port;

    @SerializedName("contentPath")
    @Expose
    private String contentPath;

    @SerializedName("playerDeathVar")
    @Expose
    private String playerDeathsVar;

    @SerializedName("playerKillsVar")
    @Expose
    private String playerKillsVar;

    @SerializedName("playerKitNameVar")
    @Expose
    private String playerKitNameVar;

    public void saveConfig() {
        var gson = new Gson();
        var jsonStr = gson.toJson(this);
        // TODO:
    }

    public static Config loadConfigFile() {
        return null; // TODO:
    }

    public static boolean isConfigSet() {
        return false; // TODO:
    }

    public static Config getDefaultConfig() {
        return null; // TODO:
    }
}
