package ml.heartfulcpvp.dataapi;

import ch.njol.skript.Skript;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import ml.heartfulcpvp.dataapi.exceptions.InvalidConfigException;
import ml.heartfulcpvp.dataapi.exceptions.MinecraftPlayerNotFoundException;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Logger;

public class PlayerStats {
    private static final String BE_PREFIX = "*"; // TODO: configに書いてもいいかも

    private String playerName;
    private String playerUuid;
    private long playerDeaths;
    private long playerKills;
    private int playerKits;

    private Config config;
    private static Logger logger = LoggingUtils.getLogger();

    public PlayerStats(String playerName) throws MinecraftPlayerNotFoundException, IOException {
        try {
            config = Config.getConfig();
        } catch (InvalidConfigException ex) {
            logger.warning(ex.getMessage());
            config = Config.getDefaultConfig();
        }

        this.playerName = playerName;

        if (isBePlayer()) {
            this.playerUuid = getPlayerUuidFromName(playerName);
        } else {
            this.playerUuid = getMinecraftPlayerUuid(playerName);
        }

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

    public long getPlayerDeaths() {
        return playerDeaths;
    }

    public long getPlayerKills() {
        return playerKills;
    }

    public int getPlayerKits() {
        return playerKits;
    }

    public boolean isBePlayer() {
        return playerName.contains(BE_PREFIX);
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
        return formatUuidString(profile.getId());
    }

    public static String formatUuidString(String uuidString) {
        StringBuilder sb = new StringBuilder(uuidString);
        sb.insert(8, "-");
        sb.insert(13, "-");
        sb.insert(18, "-");
        sb.insert(23, "-");
        return sb.toString();
    }

    private static String getPlayerUuidFromName(String playerName) {
        var player = Bukkit.getOfflinePlayer(playerName);
        return player.getUniqueId().toString();
    }

    private void setPlayerDeaths() {
        var deaths = 0l;
        var skflag = config.getPlayerDeathsVar().replace("${player}", this.playerUuid.toLowerCase(Locale.ROOT));

        var skvar = SkriptUtils.getVar(skflag);

        if (skvar != null) {
            deaths = (long) skvar;
        }

        this.playerDeaths = deaths;
    }

    private void setPlayerKills() {
        var kills = 0l;
        var skflag = config.getPlayerKillsVar().replace("${player}", this.playerUuid.toLowerCase(Locale.ROOT));

        var skvar = SkriptUtils.getVar(skflag);

        if (skvar != null) {
            kills = (long) skvar;
        }

        this.playerKills = kills;
    }

    private void setPlayerKits() {
        var kits = 0;
        var skflagBase = config.getPlayerKitNameVar().replace("${player}", this.playerName.toLowerCase(Locale.ROOT));

        for (int i = 1; true; i++) {
            var skflag = skflagBase.replace("${index}", i + "");
            var skvar = SkriptUtils.getVar(skflag);

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

        var currentSc = 0d;

        for (int i = 1; true; i++) {
            if (i < 20) {
                currentSc = 5 * (Math.pow(1.2, i) - 1);
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
}
