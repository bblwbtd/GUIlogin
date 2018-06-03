import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new player_login(this),this);
//        this.getCommand("open").setExecutor(new command());
        this.getCommand("open").setExecutor(new command());
        System.out.println("Ready to work");
    }

    @Override
    public void onDisable() {
        System.out.println("disable");
    }
}
