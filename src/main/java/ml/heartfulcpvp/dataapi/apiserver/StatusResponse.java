package ml.heartfulcpvp.dataapi.apiserver;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import ml.heartfulcpvp.dataapi.SrvStatus;

public class StatusResponse {
    @SerializedName("tps")
    @Expose
    private int tps;

    @SerializedName("dueling")
    @Expose
    private boolean dueling;

    @SerializedName("playerCount")
    @Expose
    private int playerCount;

    @SerializedName("onlinePlayers")
    @Expose
    private String[] onlinePlayers;

    public StatusResponse(SrvStatus status) {
        this.tps = status.getTps();
        this.dueling = status.isDueling();
        this.playerCount = status.getPlayerCount();
        this.onlinePlayers = status.getOnlinePlayers();
    }

    public String export() {
        var gson = new Gson();
        return gson.toJson(this);
    }
}
