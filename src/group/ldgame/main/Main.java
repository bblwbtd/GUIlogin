package group.ldgame.main;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import group.ldgame.eventlistener.PlayerLoginListener;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerLoginListener(this),this);

		try {
            File pwFile = new File("plugins/GUIlogin/");
			if(!pwFile.exists()) {
                pwFile.mkdir();
                pwFile = new File("plugins/GUIlogin/login.yml");
                if(!pwFile.exists()){
                    pwFile.createNewFile();
                }
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

    public static <T> List<T> toList(T ...ts){
        return new ArrayList<>(Arrays.asList(ts));
    }
}
