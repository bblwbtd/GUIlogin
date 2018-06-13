package group.ldgame.eventlistener;

import org.bukkit.Bukkit;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

import group.ldgame.login.LoginInfoUtil;
import group.ldgame.main.Main;

import java.util.ArrayList;
import java.util.TreeMap;

/*
 * 未打开交互窗口的前的处理类
 */
public class PlayerLoginListener implements Listener {

	private Main main;
	private MenuListener menuListener;
	public static TreeMap<String, ArrayList<String>> playerInfo = new TreeMap<>();

	public PlayerLoginListener(Main m,MenuListener menuListener) {
		this.main = m;
		this.menuListener = menuListener;
	}

	
	/*
	 * 判断玩家之前是否已注册
	 * 未注册玩家跳转至注册、试玩选择界面
	 * 注册玩家跳转至登录界面
	 */
	@EventHandler
	public void onJoin(PlayerLoginEvent e) {
		Player player = e.getPlayer();
		
		String playerName = player.getName();

		initializePlayerInfo(playerName);
		countDown c = new countDown(player);
		// 跳转界面判定
		if (!playerInfo.get(playerName).get(0).equals("no")) {
			// 已注册的场合
			c.runTaskTimer(main, 0, 20);
			new BukkitRunnable() {
				@Override
				public void run() {
					if(player.isDead()){
						player.spigot().respawn();
					}
					menuListener.showMenu(player, MenuListener.LOGIN_MENU);
					this.cancel();
				}
			}.runTaskTimer(main, 1, -1);
		} else {
			// 未注册的场合
			c.setTimeLeft(120);
			c.runTaskTimer(main, 0, 20);
			new BukkitRunnable() {
				@Override
				public void run() {
					if(player.isDead()){
						player.spigot().respawn();
					}
					menuListener.showMenu(player, MenuListener.CHOSE_MENU);
					this.cancel();
				}
			}.runTaskTimer(main, 1, -1);
		}
	}

	/*
	 * 阻止玩家在未登录时关闭菜单
	 */
	@EventHandler
	public void avoidCloseGUI(InventoryCloseEvent e) {
		Player player = (Player) e.getPlayer();
		String playerName = player.getName();
		new BukkitRunnable() {
			@Override
			public void run() {
				if(player.isOnline() && playerInfo.get(playerName).get(1).equals("no")) {
					if (!e.getInventory().getName().equals("欢迎新玩家" + playerName + "!")) {
						player.openInventory(e.getInventory());
					}else {
						if(playerInfo.get(playerName).get(2).equals("no")) {
							menuListener.showMenu(player, MenuListener.CHOSE_MENU);												
						}
					}
				}
				this.cancel();
			}
		}.runTaskTimer(main, 1, 0);
	}

	/*
	 * 阻止玩家在未登录时
	 */
	@EventHandler
	public void avoidBreak(PlayerInteractEvent e) {
		if (playerInfo.get(e.getPlayer().getName()).get(1).equals("no")) {
			e.setCancelled(true);
		}
	}
	/*
	* 阻止玩家移动
	* */
	@EventHandler
	public void avoidMove(PlayerMoveEvent e){
		if (playerInfo.get(e.getPlayer().getName()).get(1).equals("no")) {
			e.setCancelled(true);
		}
	}
	/*
	 * 阻止玩家在未登录时掉落
	 */
	@EventHandler
	public void avoidDrop(PlayerDropItemEvent e) {
		if (playerInfo.get(e.getPlayer().getName()).get(1).equals("no")) {
			e.setCancelled(true);
		}
	}
	
	private void initializePlayerInfo(String playerName) {
		ArrayList<String> infos = new ArrayList<>();
		String pwEncrypt = LoginInfoUtil.getPlayerLoginInfo(playerName).get("pwEncrypt");
		String playerExist = LoginInfoUtil.getPlayerLoginInfo(playerName).get("playerexist");
		/*
		 * 元素1 密码加密信息，未注册或者密码被重置的直接填no
		 * 元素2 是否已成功进入游戏
		 * 元素3 是否点击注册菜单
		 */

		// 判断登录信息中的playerExist是否不为空，若不为空，说明玩家已注册
		// 判断登录信息中的pwEncrypt是否不为空，若为空，说明玩家密码被重置
		if (!playerExist.equals("no")) {
			if(!pwEncrypt.equals("")) {
				infos.add(pwEncrypt);				
			} else {
				infos.add("no");				
			}
		} else {
			infos.add("no");
		}
		infos.add("no");
		infos.add("no");
		playerInfo.put(playerName, infos);
	}
	
	private class countDown extends BukkitRunnable {
		private int timeLeft = 60;
		private Player player;

		countDown(Player player) {
			this.player = player;
		}

		public void setTimeLeft(int timeLeft) {
			this.timeLeft = timeLeft;
		}

		@Override
		public void run() {
		    try{
                if (playerInfo.get(player.getName()).get(1).equals("yes")) {
                    this.cancel();
                } else if (timeLeft < 0) {
                    Bukkit.getScheduler().runTask(main, new Runnable() {
                        public void run() {
                            player.kickPlayer(ChatColor.RED + "" + ChatColor.BOLD + "timemout!");
                        }
                    });
                    this.cancel();
                } else {
                    timeLeft--;
                }
            }catch (Exception e){
		        this.cancel();
		    }

		}
	}
}
