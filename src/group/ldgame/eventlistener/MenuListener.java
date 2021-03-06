package group.ldgame.eventlistener;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.destroystokyo.paper.Title;

import group.ldgame.login.LoginInfoUtil;
import group.ldgame.login.PassWordUtil;
import group.ldgame.main.Main;

/*
 * 交互窗口处理类
 */
public class MenuListener implements Listener {
	public static final int CHOSE_MENU = 0;
	public static final int LOGIN_MENU = 1;
	public static final int REGISTER_MENU = 2;
	
	private Main main;
	private boolean cmdOpenReg = false;
	
	public MenuListener(Main main) {
		this.main = main;
	}
	
	/*
	 * 菜单界面加载
	 */
	public void showMenu(Player player, int MenuType) {
		player.setInvulnerable(true);
		String playerName = player.getName();
		Inventory inv;

		if (MenuType == CHOSE_MENU) {
			inv = Bukkit.createInventory(null, 9, "欢迎新玩家" + playerName + "!");
			player.openInventory(inv);

			inv.setItem(3, setMeta(new ItemStack(Material.BOOK_AND_QUILL, 1), ChatColor.GREEN + "注册"));
			inv.setItem(5, setMeta(new ItemStack(Material.WOOD_SWORD, 1), ChatColor.YELLOW + "试玩"));
		} else {
			if (MenuType == LOGIN_MENU) {
				inv = Bukkit.createInventory(null, 6 * 9, "登录");
				player.openInventory(inv);
			} else {
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
		Player player = (Player) e.getWhoClicked();
		String playerName = player.getName();
		if (PlayerLoginListener.playerInfo.get(playerName).get(1).equals("no") || cmdOpenReg) {
			try {
				// 防止点击空白处产生空指针异常
				if (e.getClickedInventory() != null) {
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
							confirmButton(e,player);
						}

						// 新玩家界面处理代码
					} else {
						if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "试玩")) {
							JoinSuccess(player, (String) Main.yamlConfiguration.get("try","试玩愉快!别忘了设置密码哟!"));
							e.setCancelled(true);
						}
						if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "注册")) {
							player.closeInventory();
							new BukkitRunnable() {
								@Override
								public void run() {
									PlayerLoginListener.playerInfo.get(playerName).set(2, "yes");
									showMenu(player, MenuListener.REGISTER_MENU);
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
	private void confirmButton(InventoryClickEvent e,Player player) {
		
		StringBuilder password = new StringBuilder();
		for (int a = 35; a < 54; a++) {
			try {
				password.append(e.getClickedInventory().getItem(a).getAmount());
			} catch (Exception ignored) {

			}
		}
		// 登录界面的处理
		if (e.getClickedInventory().getName().equals("登录")) {
			boolean isPass = PassWordUtil.pwCheck(password.toString(), PlayerLoginListener.playerInfo.get(player.getName()).get(0));

			if (isPass) {
				// 密码输入正确的场合
				JoinSuccess(player,(String) Main.yamlConfiguration.get("welcome_message2","登录成功"));
			} else {
				// 密码输入错误的场合
				e.setCurrentItem(
				setMeta(e.getCurrentItem(), ChatColor.GREEN + "登录", Main.toList(ChatColor.RED + "登录失败")));
			}
			// 注册界面的处理
		} else if (e.getClickedInventory().getName().equals("注册")) {
			LoginInfoUtil.registerPlayerInfo(player.getName(), password.toString());
			JoinSuccess(player,(String) Main.yamlConfiguration.get("signup_success","注册成功!"));
		}
	}
	
	/*
	 * 玩家加入成功时的操作
	 */
	private void JoinSuccess(Player player,String extraMessage) {
		if(!cmdOpenReg) {
			PlayerLoginListener.playerInfo.get(player.getName()).set(1, "yes");			
			player.closeInventory();
			player.sendTitle(new Title(ChatColor.RED + (String) Main.yamlConfiguration.get("welcome_message","欢迎来到灵动MC服务器!"), ChatColor.GREEN + extraMessage,10,20,10));
			player.setInvulnerable(false);
		} else {
			player.closeInventory();
			player.sendTitle(new Title(ChatColor.RED + (String) Main.yamlConfiguration.get("set_message","密码设置完成！"),ChatColor.GREEN + "您已成为正式玩家/已重新设置密码",10,20,10));
			player.setInvulnerable(false);
			cmdOpenReg = false;
		}
	}
	
	public void setCmdOpenReg(boolean cmdOpenReg) {
		this.cmdOpenReg = cmdOpenReg;
	}
}
