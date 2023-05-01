package ml.heartfulcpvp.dataapi;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Array;
import java.util.ArrayList;

public class SrvStatus {
    private double tps;
    private boolean dueling;
    private int playerCount;
    private String[] onlinePlayers;

    public SrvStatus(JavaPlugin plugin) {
        setTps(plugin);
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

    private void setTps(JavaPlugin plugin) {
        tps = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
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
}
