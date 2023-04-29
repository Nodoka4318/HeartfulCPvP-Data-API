package ml.heartfulcpvp.dataapi.apiserver;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import ml.heartfulcpvp.dataapi.PlayerStats;

public class PlayerStatsResponse {
    @SerializedName("playerName")
    @Expose
    private String playerName;

    @SerializedName("playerUuid")
    @Expose
    private String playerUuid;

    @SerializedName("playerDeaths")
    @Expose
    private long playerDeaths;

    @SerializedName("playerKills")
    @Expose
    private long playerKills;

    @SerializedName("playerLevel")
    @Expose
    private int playerLevel;

    @SerializedName("playerKits")
    @Expose
    private int playerKits;

    @SerializedName("bePlayer")
    @Expose
    private boolean bePlayer;

    @SerializedName("online")
    @Expose
    private boolean online;

    public PlayerStatsResponse(PlayerStats stats) {
        this.playerDeaths = stats.getPlayerDeaths();
        this.playerKills = stats.getPlayerKills();
        this.playerKits = stats.getPlayerKits();
        this.playerName = stats.getPlayerName();
        this.playerUuid = stats.getPlayerUuid();
        this.bePlayer = stats.isBePlayer();
        this.playerLevel = stats.calcPlayerLevel();
        this.online = stats.isPlayerOnline();
    }

    public String export() {
        var gson = new Gson();
        return gson.toJson(this);
    }
}
