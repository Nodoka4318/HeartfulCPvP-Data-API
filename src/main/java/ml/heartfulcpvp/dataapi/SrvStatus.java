package ml.heartfulcpvp.dataapi;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.ExpressionInfo;
import ml.heartfulcpvp.dataapi.exceptions.InvalidConfigException;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Logger;

public class SrvStatus {
    private double tps;
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

    public double getTps() {
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
        try {
            var method = Bukkit.getServer().getClass().getMethod("getTPS");
            var tpss = (double[]) method.invoke(Bukkit.getServer());
            tps = tpss[0];
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
            tps = -1;
            ex.printStackTrace();
        }
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

        dueling = queuings >= 2l;
    }
}
