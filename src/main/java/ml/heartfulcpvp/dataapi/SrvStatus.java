package ml.heartfulcpvp.dataapi;

import ml.heartfulcpvp.dataapi.exceptions.InvalidConfigException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Logger;

public class SrvStatus {
    private int tps;
    private boolean dueling;
    private int playerCount;
    private String[] onlinePlayers;

    private Config config;
    private static Logger logger;

    public SrvStatus() {
        logger = LoggingUtils.getLogger();

        try {
            config = Config.getConfig();
        } catch (InvalidConfigException ex) {
            logger.warning(ex.getMessage());
            config = Config.getDefaultConfig();
        }

        setTps();
        setOnlinePlayers();
        playerCount = onlinePlayers.length;
    }

    public int getTps() {
        return tps;
    }

    public boolean isDueling() {
        return dueling;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public String[] getOnlinePlayers() {
        return onlinePlayers;
    }

    private void setTps() {
        tps = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Plugin.getInstatnce(), new Runnable() {
            public static int TICK_COUNT = 0;
            public static final long[] TICKS = new long[600];

            @Override
            public void run() {
                TICKS[(TICK_COUNT % TICKS.length)] = System.currentTimeMillis();

                TICK_COUNT += 1;
            }
        }, 100L, 1L);
    }

    private void setOnlinePlayers() {
        var list = new ArrayList<String>();

        for (var p : Bukkit.getOnlinePlayers()) {
            if (!p.getName().equals("HeartfulCPvP")) // TODO: これもconfig化するかも
                list.add(p.getName());
        }

        onlinePlayers = list.toArray(new String[list.size()]);
    }

    private void setDueling() {
        var queuings = 0l;
        var skflag = config.getIsDuelingVar();

        var skvar = SkriptUtils.getVar(skflag);

        if (skvar != null) {
            queuings = (long) skvar;
        }

        dueling = queuings != 0l;
    }
}
