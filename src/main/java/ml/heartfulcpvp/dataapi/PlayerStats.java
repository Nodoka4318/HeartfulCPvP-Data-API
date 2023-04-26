package ml.heartfulcpvp.dataapi;

import ch.njol.skript.Skript;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import ml.heartfulcpvp.dataapi.exceptions.MinecraftPlayerNotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class PlayerStats {
    @SerializedName("playerName")
    @Expose
    private String playerName;

    @SerializedName("playerUuid")
    @Expose
    private String playerUuid;

    @SerializedName("playerDeaths")
    @Expose
    private int playerDeaths;

    @SerializedName("playerKills")
    @Expose
    private int playerKills;

    @SerializedName("playerKits")
    @Expose
    private int playerKits;

    public PlayerStats(String playerName) throws MinecraftPlayerNotFoundException, IOException {
        this.playerName = playerName;
        this.playerUuid = getMinecraftPlayerUuid(playerName);

        setPlayerDeaths();
        setPlayerKills();

        setPlayerKits();
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getPlayerUuid() {
        return playerUuid;
    }

    public int getPlayerDeaths() {
        return playerDeaths;
    }

    public int getPlayerKills() {
        return playerKills;
    }

    public int getPlayerKits() {
        return playerKits;
    }

    private static String getMinecraftPlayerUuid(String playerName) throws IOException, MinecraftPlayerNotFoundException {
        String remote = "https://api.mojang.com/users/profiles/minecraft/" + playerName;
        URL url = new URL(remote);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        var responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new MinecraftPlayerNotFoundException(playerName);
        }

        var in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        var response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        var gson = new Gson();
        var profile = gson.fromJson(response.toString(), MinecraftProfile.class);
        return profile.getId();
    }

    private void setPlayerDeaths() {
        var deaths = 0;
        var skflag = "{" + this.playerUuid + "::deaths}";
        var skvar = SkriptUtils.getVar(skflag);

        if (skvar != null) {
            deaths = (int) skvar;
        }

        this.playerDeaths = deaths;
    }

    private void setPlayerKills() {
        var kills = 0;
        var skflag = "{" + this.playerUuid + "::kills}";
        var skvar = SkriptUtils.getVar(skflag);

        if (skvar != null) {
            kills = (int) skvar;
        }

        this.playerKills = kills;
    }

    private void setPlayerKits() {
        var kits = 0;
        var skflagBase = "{userkit::name::" + this.playerName + "::"; // + "${index}}"

        for (int i = 1; true; i++) {
            var skvar = SkriptUtils.getVar(skflagBase + i + "}");

            if (skvar != null)
                kits++;
            else
                break;
        }

        this.playerKits = kits;
    }

    // Skriptと共通化したいところ
    // ref: function calcLevel(p: player) :: number (leveling.sk, 24)
    public int calcPlayerLevel() {
        var minus = playerDeaths * 0.2;
        var score = playerKills - minus;

        if (score <= 0) {
            return 1; // スコア0以下はレベル１
        }

        for (int i = 1; true; i++) {
            var currentSc = 0d;

            if (i < 20) {
                currentSc = (5 * Math.pow(1.2, i) - 1);
            } else if (i <= 40) {
                currentSc += 35;
            } else if (i <= 60) {
                currentSc += 70;
            } else {
                currentSc += 100;
            }

            if (currentSc > score) {
                return i;
            }
        }
    }

    public String buildJson() {
        var gson = new Gson();
        return gson.toJson(this);
    }
}
