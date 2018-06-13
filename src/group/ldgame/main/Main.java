package group.ldgame.main;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import group.ldgame.command.PluginCommand;
import group.ldgame.eventlistener.MenuListener;
import group.ldgame.eventlistener.PlayerLoginListener;

public class Main extends JavaPlugin {
	private PlayerLoginListener pll;
	private MenuListener ml;
    @Override
    public void onEnable() {
    	ml = new MenuListener(this);
    	pll = new PlayerLoginListener(this,ml);
    	
    	getServer().getPluginManager().registerEvents(ml,this);
    	getServer().getPluginManager().registerEvents(pll,this);
    	
    	this.getCommand("guilogin").setExecutor(new PluginCommand(ml));

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
