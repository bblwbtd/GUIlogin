package group.ldgame.eventlistener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import group.ldgame.main.Main;

import java.util.Timer;

public class NotLoginInPlayer {
    private int countdown = 60;
    private int cursor = 35;
    private boolean login_statue = false;
    private Timer timer = new Timer();
    private Player player;
    public NotLoginInPlayer(Player p,Main main){
        player = p;
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if(login_statue){
                    this.cancel();
                }else if(countdown < 1){
                    this.cancel();
                    Bukkit.getScheduler().runTask(main, new Runnable() {
                        public void run() {
                            player.kickPlayer(ChatColor.RED + "" + ChatColor.BOLD + "timemout!" );
                        }
                    });
                }
                player.sendMessage("你还剩"+countdown+"秒登录!");
                countdown = countdown - 1;
            }
        };
        runnable.runTaskTimer(main,20,  20);
    }

    public void setLogin_statue(boolean login_statue) {
        this.login_statue = login_statue;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    public void movecusor(int i){
        cursor = cursor + i;
    }

    public int getCusor() {
        return cursor;
    }

    public Player getPlayer() {
        return player;
    }
}
