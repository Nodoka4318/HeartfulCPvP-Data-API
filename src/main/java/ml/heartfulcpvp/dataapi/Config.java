package ml.heartfulcpvp.dataapi;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import ml.heartfulcpvp.dataapi.exceptions.InvalidConfigException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Config {
    private static final String CONFIG_DIRECTORY_PATH = "./plugins/HeartfulCPvP/DataApi";
    private static final String CONFIG_FILE_PATH = "./plugins/HeartfulCPvP/DataApi/config.json";
    public static final int DEFAULT_PORT = 8080;
    public static final String DEFAULT_CONTEXT_PATH = "/playerdata/";
    public static final String DEFAULT_PLAYER_DEATHS_VAR = "${player}::hoge"; // dummy
    public static final String DEFAULT_PLAYER_KILLS_VAR = "${player}::hoge";
    public static final String DEFAULT_PLAYER_KIT_NAME_VAR = "${player}::${index}::hoge";

    private static Config config;

    @SerializedName("port")
    @Expose
    private int port;

    @SerializedName("contextPath")
    @Expose
    private String contextPath;

    @SerializedName("playerDeathVar")
    @Expose
    private String playerDeathsVar;

    @SerializedName("playerKillsVar")
    @Expose
    private String playerKillsVar;

    @SerializedName("playerKitNameVar")
    @Expose
    private String playerKitNameVar;

    public void setPort(int port) {
        this.port = port;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public void setPlayerDeathsVar(String playerDeathsVar) {
        this.playerDeathsVar = playerDeathsVar;
    }

    public void setPlayerKillsVar(String playerKillsVar) {
        this.playerKillsVar = playerKillsVar;
    }

    public void setPlayerKitNameVar(String playerKitNameVar) {
        this.playerKitNameVar = playerKitNameVar;
    }

    public int getPort() {
        return port;
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getPlayerDeathsVar() {
        return playerDeathsVar;
    }

    public String getPlayerKillsVar() {
        return playerKillsVar;
    }

    public String getPlayerKitNameVar() {
        return playerKitNameVar;
    }

    public void saveConfig() throws IOException {
        var gson = new Gson();
        var jsonStr = gson.toJson(this);

        var dir = new File(CONFIG_DIRECTORY_PATH);
        var file = new File(CONFIG_FILE_PATH);

        if (!dir.exists()) {
            dir.mkdir();
        }

        if (file.exists()) {
            file.delete();
        }

        Files.createFile(file.toPath());
        Files.writeString(file.toPath(), jsonStr);
    }

    public static boolean isConfigExists() {
        var file = new File(CONFIG_FILE_PATH);
        return file.exists();
    }

    public static Config loadConfigFile() throws IOException {
        var file = new File(CONFIG_FILE_PATH);
        var content = Files.readString(file.toPath());
        var gson = new Gson();
        var conf = gson.fromJson(content, Config.class);

        return conf;
    }

    public static boolean isConfigSet() {
        return false; // TODO:
    }

    public static Config getDefaultConfig() {
        var conf = new Config();

        conf.setPort(DEFAULT_PORT);
        conf.setContextPath(DEFAULT_CONTEXT_PATH);
        conf.setPlayerDeathsVar(DEFAULT_PLAYER_DEATHS_VAR);
        conf.setPlayerKillsVar(DEFAULT_PLAYER_KILLS_VAR);
        conf.setPlayerKitNameVar(DEFAULT_PLAYER_KIT_NAME_VAR);

        return conf;
    }

    public static void setConfig(Config config) {
        Config.config = config;
    }

    public static Config getConfig() throws InvalidConfigException {
        if (config == null) {
            throw new InvalidConfigException("Config has not set yet!");
        }

        return config;
    }
}
