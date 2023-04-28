package ml.heartfulcpvp.dataapi;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Logger;

public class Plugin extends JavaPlugin {
    private static Logger logger;

    @Override
    public void onEnable() {
        logger = getLogger();
        setupConfig();
    }

    private void setupConfig() {
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
}