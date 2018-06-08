package group.ldgame.eventlistener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.destroystokyo.paper.Title;

import group.ldgame.login.LoginInfoUtil;
import group.ldgame.login.PassWordUtil;
import group.ldgame.main.Main;

import java.util.List;
import java.util.TreeMap;

public class PlayerLoginListener implements Listener {
	public final int CHOSE_MENU = 0;
	public final int LOGIN_MENU = 1;
	public final int REGISTER_MENU = 2;

	private Main main;
	private Player player;
	private String playerName;
	private Inventory inv;
	private TreeMap<String, String> playerLoginInfo;
	private boolean isRegister = false;
	private boolean isJoined = false;
	private boolean isClickReg = false;
	public PlayerLoginListener(Main m) {
		this.main = m;
	}

	/*
	 * 阻止玩家在未登录时移动
	 */
	@EventHandler
	public void avoidMove(PlayerMoveEvent e) {
		this.player = e.getPlayer();
		if (!isJoined) {
			e.setCancelled(true);
		}
	}

	/*
	 * 阻止玩家在未登录时关闭菜单
	 */
	@EventHandler
	public void avoidCloseGUI(InventoryCloseEvent e) {
		this.player = (Player) e.getPlayer();
		new BukkitRunnable() {
			@Override
			public void run() {

				if (e.getInventory().getName().equals("登录") && !isJoined && player.isOnline()) {
					e.getPlayer().openInventory(e.getInventory());
				} else if (e.getInventory().getName().equals("注册") && !isJoined && player.isOnline()) {
                    e.getPlayer().openInventory(e.getInventory());
				} else if (e.getInventory().getName().equals("欢迎新玩家" + playerName + "!") 
						   && !isJoined && player.isOnline() && !isClickReg) {
                    e.getPlayer().openInventory(e.getInventory());
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
		if (!isJoined) {
			e.setCancelled(true);
		}
	}

	/*
	 * 阻止玩家在未登录时掉落
	 */
	@EventHandler
	public void avoidDrop(PlayerDropItemEvent e) {
		if (!isJoined) {
			e.setCancelled(true);
		}
	}

	/*
	 * 在玩家退出时，将玩家的isJoined设为false
	 */
	@EventHandler
	public void deleteLeavePlayer(PlayerQuitEvent e) {
		isJoined = false;
	}

	/*
	 * 判断玩家之前是否已注册 未注册玩家跳转至注册、试玩选择界面 注册玩家跳转至登录界面
	 */
	@EventHandler
	public void onJoin(PlayerLoginEvent e) {
		player = e.getPlayer();
		player.setInvulnerable(true);

		playerName = player.getPlayer().getName();
		playerLoginInfo = LoginInfoUtil.getPlayerLoginInfo(playerName);

		// 判断登录信息中的pwEncrypt是否不为空，若不为空，说明玩家已注册
		if (!playerLoginInfo.get("pwEncrypt").equals("")) {
			isRegister = true;
		}
		countDown c = new countDown(player);
		// 跳转界面判定
		if (isRegister) {
			c.runTaskTimer(main, 0, 20);
			new BukkitRunnable() {
				@Override
				public void run() {
					showMenu(player, LOGIN_MENU);
					this.cancel();
				}
			}.runTaskTimer(main, 1, -1);
		} else {
			c.setTimeLeft(120);
			c.runTaskTimer(main, 0, 20);
			new BukkitRunnable() {
				@Override
				public void run() {
					showMenu(player, CHOSE_MENU);
					this.cancel();
				}
			}.runTaskTimer(main, 1, -1);
		}
	}

	private ItemStack setMeta(ItemStack itemStack, String name, List<String> lore) {
		if (((itemStack == null) || itemStack.getType() == Material.AIR)) {
			return null;
		}
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(name);
		itemMeta.setLore(lore);
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	private static ItemStack setMeta(ItemStack itemStack, String name) {
		if (((itemStack == null) || itemStack.getType() == Material.AIR)) {
			return null;
		}
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(name);
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	/*
	 * 菜单界面加载
	 */
	public void showMenu(Player player, int MenuType) {
		if (MenuType == CHOSE_MENU) {
			inv = Bukkit.createInventory(null, 9, "欢迎新玩家" + playerName + "!");
			player.openInventory(inv);

			inv.setItem(3, setMeta(new ItemStack(Material.BOOK_AND_QUILL, 1), ChatColor.GREEN + "注册"));
			inv.setItem(5, setMeta(new ItemStack(Material.WOOD_SWORD, 1), ChatColor.YELLOW + "试玩"));
		} else {
			if (MenuType == LOGIN_MENU) {
				inv = Bukkit.createInventory(null, 6 * 9, "登录");
				player.openInventory(inv);
			} else if (MenuType == REGISTER_MENU) {
				inv = Bukkit.createInventory(null, 6 * 9, "注册");
				player.openInventory(inv);
			}
			inv.setItem(0, setMeta(new ItemStack(Material.STONE_BUTTON, 1), "1"));
			inv.setItem(1, setMeta(new ItemStack(Material.STONE_BUTTON, 2), "2"));
			inv.setItem(2, setMeta(new ItemStack(Material.STONE_BUTTON, 3), "3"));
			inv.setItem(9, setMeta(new ItemStack(Material.STONE_BUTTON, 4), "4"));
			inv.setItem(10, setMeta(new ItemStack(Material.STONE_BUTTON, 5), "5"));
			inv.setItem(11, setMeta(new ItemStack(Material.STONE_BUTTON, 6), "6"));
			inv.setItem(18, setMeta(new ItemStack(Material.STONE_BUTTON, 7), "7"));
			inv.setItem(19, setMeta(new ItemStack(Material.STONE_BUTTON, 8), "8"));
			inv.setItem(20, setMeta(new ItemStack(Material.STONE_BUTTON, 9), "9"));
			inv.setItem(27, setMeta(new ItemStack(Material.EMERALD_BLOCK, 1), ChatColor.GREEN + "确认"));
			inv.setItem(28, setMeta(new ItemStack(Material.REDSTONE_BLOCK, 1), ChatColor.RED + "撤销"));
			inv.setItem(29, setMeta(new ItemStack(Material.GLASS, 1), "清空"));
		}
	}

	/*
	 * 菜单点击操作
	 */
	@EventHandler
	public void inventoryClickEvent(InventoryClickEvent e) {
		if (!isJoined) {
			try {
				this.player = (Player) e.getWhoClicked();
				playerLoginInfo = LoginInfoUtil.getPlayerLoginInfo(playerName);
				
				//防止点击空白处产生空指针异常
				if(e.getClickedInventory() != null) {
					if (!(e.getClickedInventory().getName().equals("欢迎新玩家" + e.getWhoClicked().getName() + "!"))) {
						int Cursor = 36;
						e.setCancelled(true);

						while (Cursor < 54) {
							if (e.getClickedInventory().getItem(Cursor) == null) {
								break;
							}
							Cursor++;
						}
						if (e.getCurrentItem().getType().equals(Material.STONE_BUTTON) && Cursor < 54) {
							ItemStack itemStack = e.getCurrentItem();
							Inventory inv = e.getClickedInventory();
							inv.setItem(Cursor, new ItemStack(Material.STONE, itemStack.getAmount()));
						}

						// delete button
						if (e.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)) {
							deleteButton(e, Cursor);
						}
						// clear button
						if (e.getCurrentItem().getType().equals(Material.GLASS)) {
							clearButton(e);
						}

						// confirm button
						if (e.getCurrentItem().getType().equals(Material.EMERALD_BLOCK) && Cursor > 35) {
							confirmButton(e);
						}

						// 新玩家界面处理代码
					} else {
						if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "试玩")) {
							player = (Player) e.getWhoClicked();
							JoinSuccess(player, "游玩快乐！");
							e.setCancelled(true);
						}
						if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "注册")) {
							player.closeInventory();
							new BukkitRunnable() {
								@Override
								public void run() {
									showMenu(player, REGISTER_MENU);
									isClickReg = true;
									this.cancel();
								}
							}.runTaskTimer(main, 1, -1);

						}
					}
				}
			} catch (Exception a) {
				a.printStackTrace();
			}
		}
	}

	/*
	 * 清除密码按钮
	 */
	private void clearButton(InventoryClickEvent e) {
		for (int a = 35; a < 54; a++) {
			try {
				e.getClickedInventory().setItem(a, new ItemStack(Material.AIR, 1));

			} catch (Exception ignored) {

			}
		}
	}

	/*
	 * 删除密码按钮
	 */
	private void deleteButton(InventoryClickEvent e, int cursorPosition) {
		Inventory inv = e.getClickedInventory();
		inv.setItem(cursorPosition - 1, new ItemStack(Material.AIR, 1));
	}

	/*
	 * 确认按钮
	 */
	private void confirmButton(InventoryClickEvent e) {
		StringBuilder password = new StringBuilder();
		for (int a = 35; a < 54; a++) {
			try {
				password.append(e.getClickedInventory().getItem(a).getAmount());
			} catch (Exception ignored) {

			}
		}
		// 登录界面的处理
		if (e.getClickedInventory().getName().equals("登录")) {
			boolean isPass = PassWordUtil.pwCheck(password.toString(), playerLoginInfo.get("pwEncrypt"));

			if (isPass) {
				// 密码输入正确的场合
				JoinSuccess(player, "登录成功!");
			} else {
				// 密码输入错误的场合
				e.setCurrentItem(setMeta(e.getCurrentItem(), ChatColor.GREEN + "登录", Main.toList(ChatColor.RED + "登录失败")));
			}
			// 注册界面的处理
		} else if (e.getClickedInventory().getName().equals("注册")) {
			LoginInfoUtil.registerPlayerInfo(playerName, password.toString());
			JoinSuccess(player, "注册成功!");
		}
	}

	/*
	 * 玩家加入成功时的操作
	 */
	private void JoinSuccess(Player player, String extraMessage) {
		isJoined = true;
		player.closeInventory();
		player.sendTitle(new Title(ChatColor.RED + "欢迎来到灵动MC服务器!", ChatColor.GREEN + extraMessage));
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
			if (isJoined) {
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
		}
	}
}
