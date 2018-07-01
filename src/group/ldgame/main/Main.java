package group.ldgame.main;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import group.ldgame.command.PluginCommand;
import group.ldgame.eventlistener.MenuListener;
import group.ldgame.eventlistener.PlayerLoginListener;

public class Main extends JavaPlugin {
    public static YamlConfiguration yamlConfiguration;
    @Override
    public void onEnable() {
        MenuListener ml = new MenuListener(this);
        PlayerLoginListener pll = new PlayerLoginListener(this, ml);
    	getServer().getPluginManager().registerEvents(ml,this);
    	getServer().getPluginManager().registerEvents(pll,this);

    	this.getCommand("guilogin").setExecutor(new PluginCommand(ml));

		try {
            File file = new File("plugins/GUIlogin/");
			if(!file.exists()) {
                file.mkdir();
                file = new File("plugins/GUIlogin/login.yml");
                file.createNewFile();

			}

        } catch (IOException e) {
            e.printStackTrace();
		}

        try{
		    yamlConfiguration = new YamlConfiguration();
		    yamlConfiguration.load(new File("plugins/GUIlogin/config.yml"));
        } catch (FileNotFoundException e){
            try {
                (new File("plugins/GUIlogin/config.yml")).createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
        System.out.println("Ready to work");
    }

    @Override
    public void onDisable() {
        System.out.println("disable");
    }

    public static <T> List<T> toList(T ...ts){
        return new ArrayList<>(Arrays.asList(ts));
    }
}
