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
    public static final String DEFAULT_PLAYER_DATA_CONTEXT_PATH = "/playerdata/";
    public static final String DEFAULT_STATUS_CONTEXT_PATH = "/status";
    public static final String DEFAULT_PLAYER_DEATHS_VAR = "${player}::hoge"; // dummy
    public static final String DEFAULT_PLAYER_KILLS_VAR = "${player}::fuga";
    public static final String DEFAULT_PLAYER_KIT_NAME_VAR = "${player}::${index}::piyo";
    public static final String DEFAULT_IS_DUELING_VAR = "hogehoge::piyopiyo";
    public static final String DEFAULT_BE_PREFIX = "*BE_";

    private static Config config;

    @SerializedName("port")
    @Expose
    private int port;

    @SerializedName("playerDataContextPath")
    @Expose
    private String playerDataContextPath;

    @SerializedName("statusContextPath")
    @Expose
    private String statusContextPath;

    @SerializedName("playerDeathVar")
    @Expose
    private String playerDeathsVar;

    @SerializedName("playerKillsVar")
    @Expose
    private String playerKillsVar;

    @SerializedName("playerKitNameVar")
    @Expose
    private String playerKitNameVar;

    @SerializedName("isDuelingVar")
    @Expose
    private String isDuelingVar;

    @SerializedName("bePrefix")
    @Expose
    private String bePrefix;

    public void setPort(int port) {
        this.port = port;
    }

    public void setPlayerDataContextPath(String playerDataContextPath) {
        this.playerDataContextPath = playerDataContextPath;
    }

    public void setStatusContextPath(String statusContextPath) {
        this.statusContextPath = statusContextPath;
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

    public void setIsDuelingVar(String isDuelingVar) {
        this.isDuelingVar = isDuelingVar;
    }

    public void setBePrefix(String bePrefix) {
        this.bePrefix = bePrefix;
    }

    public int getPort() {
        return port;
    }

    public String getPlayerDataContextPath() {
        return playerDataContextPath;
    }

    public String getStatusContextPath() {
        return statusContextPath;
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

    public String getIsDuelingVar() {
        return isDuelingVar;
    }

    public String getBePrefix() {
        return bePrefix;
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
        conf.setPlayerDataContextPath(DEFAULT_PLAYER_DATA_CONTEXT_PATH);
        conf.setStatusContextPath(DEFAULT_STATUS_CONTEXT_PATH);
        conf.setPlayerDeathsVar(DEFAULT_PLAYER_DEATHS_VAR);
        conf.setPlayerKillsVar(DEFAULT_PLAYER_KILLS_VAR);
        conf.setPlayerKitNameVar(DEFAULT_PLAYER_KIT_NAME_VAR);
        conf.setIsDuelingVar(DEFAULT_IS_DUELING_VAR);
        conf.setBePrefix(DEFAULT_BE_PREFIX);

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
