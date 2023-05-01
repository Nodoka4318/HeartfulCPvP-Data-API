package ml.heartfulcpvp.dataapi;

import ch.njol.skript.Skript;
import ml.heartfulcpvp.dataapi.apiserver.DataApiHttpServer;
import ml.heartfulcpvp.dataapi.apiserver.PlayerStatsResponse;
import ml.heartfulcpvp.dataapi.exceptions.InvalidConfigException;
import ml.heartfulcpvp.dataapi.exceptions.MinecraftPlayerNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.logging.Logger;

public class Plugin extends JavaPlugin {
    private static Logger logger;
    private DataApiHttpServer server;

    private static SimpleCommandMap commandMap;
    private static Plugin instance;

    @Override
    public void onEnable() {
        logger = getLogger();
        instance = this;

        setupSimpleCommandMap();
        LoggingUtils.setLogger(logger);

        initConfig();
        initHttpServer();

        Skript.registerAddon(this);

        /*
        registerCommand(new Command("dataapi") {
            @Override
            public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                String response;
                try {
                    var playerStats = new PlayerStats(args[0]);
                    var statsm = new PlayerStatsResponse(playerStats);
                    response = statsm.export();

                } catch (MinecraftPlayerNotFoundException e) {
                    response = "{\"error\":\"player not found\"}";
                    e.printStackTrace();
                } catch (IOException e) {
                    response = "{\"error\":\"io\"}";
                    e.printStackTrace();
                }
                logger.info(response);
                return true;
            }
        });
         */
    }

    private void initConfig() {
        logger.info("コンフィグの設定をしています。");

        if (Config.isConfigExists()) {
            try {
                var conf = Config.loadConfigFile();
                Config.setConfig(conf);
                logger.info("コンフィグファイルが読み込まれました。");
            } catch (IOException ex) {
                logger.warning("コンフィグファイルの読み込みに失敗しました。\nプラグインを停止します。");
                Bukkit.getPluginManager().disablePlugin(this);

                ex.printStackTrace();
            }
        } else {
            var newConf = Config.getDefaultConfig();

            try {
                newConf.saveConfig();
                Config.setConfig(newConf);
                logger.info("コンフィグファイルが新しく作成されました。再起動しろや。");
            } catch (IOException ex) {
                logger.warning("コンフィグファイルの作成に失敗しました。\nプラグインを停止します。");
                Bukkit.getPluginManager().disablePlugin(this);

                ex.printStackTrace();
            }
        }
    }

    private void initHttpServer() {
        logger.info("Httpサーバを初期化します。");
        server = new DataApiHttpServer();
        logger.info("Httpサーバを開始します。");
        int port;

        try {
            port = Config.getConfig().getPort();
        } catch (InvalidConfigException  ex) {
            logger.warning("コンフィグを読み込めなかったため、デフォルトのポートを使用します。");
            port = Config.DEFAULT_PORT;
        }

        logger.info("Httpサーバを開始します。");
        try {
            server.start(port);
        } catch (IOException ex) {
            logger.warning("Httpサーバを開始できませんでした。\nプラグインを停止します。");
            Bukkit.getPluginManager().disablePlugin(this);

            ex.printStackTrace();
        }
    }

    private void setupSimpleCommandMap() {
        var spm = (SimplePluginManager) this.getServer().getPluginManager();
        Field f = null;
        try {
            f = SimplePluginManager.class.getDeclaredField("commandMap");
        } catch (Exception e) {
            e.printStackTrace();
        }
        f.setAccessible(true);
        try {
            commandMap = (SimpleCommandMap) f.get(spm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SimpleCommandMap getCommandMap() {
        return commandMap;
    }

    public void registerCommand(Command command) {
        Plugin.getCommandMap().register(command.getName(), command);
    }

    public static JavaPlugin getInstatnce() {
        return instance;
    }
}