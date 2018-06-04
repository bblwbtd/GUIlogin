import org.bukkit.plugin.java.JavaPlugin;



public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new player_login(this),this);
    }

    @Override
    public void onDisable() {
        System.out.println("disable");
    }
}
