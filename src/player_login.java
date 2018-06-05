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
import java.util.ArrayList;
import java.util.List;

public class player_login implements Listener {
    private Main main;
    private ArrayList<Player> onlinelist = new ArrayList<>();
    player_login(Main m){
        this.main = m;
    }
    private ArrayList<not_login_in_lpayer> waitlist = new ArrayList<>();
    @EventHandler
    public void avoid_move(PlayerMoveEvent e){
        if(!onlinelist.contains(e.getPlayer())){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void avoid_close_gui(InventoryCloseEvent e){
        new BukkitRunnable() {
            @Override
            public void run() {
                if(e.getInventory().getName().equals("登录")&&!onlinelist.contains((Player) e.getPlayer())){
                    e.getPlayer().openInventory(e.getInventory());
                    this.cancel();
                }
            }
        }.runTaskTimer(main,1,0);

    }
    @EventHandler
    public void avoid_break(PlayerInteractEvent e){
        if(!onlinelist.contains(e.getPlayer())){
            e.setCancelled(true);
        }
    }


    @EventHandler
    public void avoid_drop(PlayerDropItemEvent e){
        if(!onlinelist.contains(e.getPlayer())){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void delete_leave_player(PlayerQuitEvent e){
        onlinelist.remove(e.getPlayer());
    }
    @EventHandler
    public void onjoin(PlayerLoginEvent e){
        Player player = e.getPlayer();
        e.getPlayer().setInvulnerable(true);
        if (e.getPlayer().hasPlayedBefore()){
            waitlist.add(new not_login_in_lpayer(player,main));
            new BukkitRunnable() {
                @Override
                public void run() {
                    show_login_gui(player);
                    this.cancel();
                }
            }.runTaskTimer(main,1,-1);
        }else {

        }


    }

    @EventHandler
    public void inventory_click_event(InventoryClickEvent e){
        try{
            Player player = (Player)e.getWhoClicked();

            if (e.getClickedInventory().getName().equals("登录")){
                e.setCancelled(true);
                not_login_in_lpayer player1 = null;
                for (not_login_in_lpayer n:waitlist) {
                    if (n.getPlayer().equals(player)){
                        player1 = n;
                        break;
                    }
                }


                //number button
                assert player1 != null;
                if (e.getCurrentItem().getType().equals(Material.STONE_BUTTON)&&player1.getCusor()<54){
                    ItemStack itemStack = e.getCurrentItem();
                    Inventory inv = e.getClickedInventory();
                    player1.movecusor(1);
                    inv.setItem(player1.getCusor(),new ItemStack(Material.STONE,itemStack.getAmount()));

                }

                //delete button
                if (e.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)&&player1.getCusor() > 34){
                    Inventory inv = e.getClickedInventory();
                    inv.setItem(player1.getCusor(), new ItemStack(Material.AIR,1));
                    if ( player1.getCusor()>35){
                        player1.movecusor(-1);
                    }


                }
                //clear button
                if(e.getCurrentItem().getType().equals(Material.GLASS)){
                    for(int a = 35;a<54;a++){
                        try{
                            e.getClickedInventory().setItem(a,new ItemStack(Material.AIR,1));
                            player1.setCursor(35);
                        }catch (Exception ignored){

                        }

                    }
                }
                //confirm button
                if (e.getCurrentItem().getType().equals(Material.EMERALD_BLOCK)&&player1.getCusor() > 35){
                    StringBuilder password = new StringBuilder();
                    for(int a = 35;a<54;a++){
                        try{
                            password.append(e.getClickedInventory().getItem(a).getAmount());
                        }catch (Exception ignored){
                            //idk how to deal with the exception.
                        }

                    }
                    login_success(player,player1);
                }


            }else if(e.getClickedInventory().getName().equals("欢迎新玩家"+e.getWhoClicked().getName()+"!")){
                if(e.getCurrentItem().getItemMeta().getDisplayName().equals("试玩")){
                    onlinelist.add((Player)e.getWhoClicked());
                    login_success((Player) e.getWhoClicked());
                    e.setCancelled(true);
                }
            }
        }catch (Exception ignored){

        }

    }
    private void login_success(Player player){
        onlinelist.add (player);
        player.getOpenInventory().close();
        player.sendTitle(ChatColor.RED+"欢迎来到灵动MC服务器!",ChatColor.GREEN+"登录成功!");
    }
    private void login_success(Player player,not_login_in_lpayer player1){
        onlinelist.add (player);
        player.getOpenInventory().close();
        player1.setLogin_statue(true);
        waitlist.remove(player1);
        player.sendTitle(ChatColor.RED+"欢迎来到灵动MC服务器!",ChatColor.GREEN+"登录成功!");

    }
    private void show_chose_gui(Player player){
        Inventory inv = Bukkit.createInventory(null, 3 * 9,"欢迎新玩家"+player.getName()+"!");
        player.openInventory(inv);
        inv.setItem(12, setMeta(new ItemStack(Material.PAPER, 1),"注册"));
        inv.setItem(14, setMeta(new ItemStack(Material.WOOD_SWORD, 1),"试玩"));
    }
    private void show_login_gui(Player player){
        Inventory inv = Bukkit.createInventory(null, 6 * 9,"登录");
        player.openInventory(inv);

        inv.setItem(0, setMeta(new ItemStack(Material.STONE_BUTTON, 1),"1"));
        inv.setItem(1, setMeta(new ItemStack(Material.STONE_BUTTON, 2),"2"));
        inv.setItem(2, setMeta(new ItemStack(Material.STONE_BUTTON, 3),"3"));
        inv.setItem(9, setMeta(new ItemStack(Material.STONE_BUTTON, 4),"4"));
        inv.setItem(10, setMeta(new ItemStack(Material.STONE_BUTTON, 5),"5"));
        inv.setItem(11, setMeta(new ItemStack(Material.STONE_BUTTON, 6),"6"));
        inv.setItem(18, setMeta(new ItemStack(Material.STONE_BUTTON, 7),"7"));
        inv.setItem(19, setMeta(new ItemStack(Material.STONE_BUTTON, 8),"8"));
        inv.setItem(20, setMeta(new ItemStack(Material.STONE_BUTTON, 9),"9"));
        inv.setItem(27, setMeta(new ItemStack(Material.EMERALD_BLOCK, 1),ChatColor.GREEN+"确认"));
        inv.setItem(28, setMeta(new ItemStack(Material.REDSTONE_BLOCK, 1),ChatColor.RED+"撤销"));
        inv.setItem(29, setMeta(new ItemStack(Material.GLASS, 1),"清空"));

    }
    private void show_register_gui(Player player){
        Inventory inv = Bukkit.createInventory(null, 6 * 9,"注册");
        player.openInventory(inv);
        inv.setItem(0, setMeta(new ItemStack(Material.STONE_BUTTON, 1),"1"));
        inv.setItem(1, setMeta(new ItemStack(Material.STONE_BUTTON, 2),"2"));
        inv.setItem(2, setMeta(new ItemStack(Material.STONE_BUTTON, 3),"3"));
        inv.setItem(9, setMeta(new ItemStack(Material.STONE_BUTTON, 4),"4"));
        inv.setItem(10, setMeta(new ItemStack(Material.STONE_BUTTON, 5),"5"));
        inv.setItem(11, setMeta(new ItemStack(Material.STONE_BUTTON, 6),"6"));
        inv.setItem(18, setMeta(new ItemStack(Material.STONE_BUTTON, 7),"7"));
        inv.setItem(19, setMeta(new ItemStack(Material.STONE_BUTTON, 8),"8"));
        inv.setItem(20, setMeta(new ItemStack(Material.STONE_BUTTON, 9),"9"));
        inv.setItem(27, setMeta(new ItemStack(Material.EMERALD_BLOCK, 1),ChatColor.GREEN+"确认"));
        inv.setItem(28, setMeta(new ItemStack(Material.REDSTONE_BLOCK, 1),ChatColor.RED+"撤销"));
        inv.setItem(29, setMeta(new ItemStack(Material.GLASS, 1),"清空"));
    }
    private ItemStack setMeta(ItemStack itemStack,String name,List<String> lore){
        if (((itemStack == null)||itemStack.getType() == Material.AIR )){
            return null;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    private ItemStack setMeta(ItemStack itemStack,String name){
        if (((itemStack == null)||itemStack.getType() == Material.AIR )){
            return null;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}


