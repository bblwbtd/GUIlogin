package group.ldgame.main;
import java.io.File;
import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

import group.ldgame.eventlistener.PlayerLoginListener;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerLoginListener(this),this);
        
		File pwFile = new File("plugins/GUIlogin/login.yml");
		try {
			if(!pwFile.exists()) {
				pwFile.createNewFile();
			}	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        System.out.println("Ready to work");
    }

    @Override
    public void onDisable() {
        System.out.println("disable");
    }
}
