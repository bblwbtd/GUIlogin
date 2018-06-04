import com.destroystokyo.paper.Title;
import com.destroystokyo.paper.event.player.PlayerInitialSpawnEvent;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.PlayerChunkMap;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import javax.jws.HandlerChain;
import java.util.ArrayList;

public class player_login implements Listener {
    private Main m;
    private ArrayList<Player> onlinelist = new ArrayList<>();
    player_login(Main m){
        this.m = m;
    }

    @EventHandler
    public void force_to_login(PlayerMoveEvent e){
        if (!onlinelist.contains(e.getPlayer())){
            if(e.getPlayer().isOnGround()){
                show_login_gui(e.getPlayer());
            }
            e.setCancelled(true);
        }

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
    public void protect_login_player(PlayerLoginEvent e){
        Player player = e.getPlayer();
        e.getPlayer().setInvulnerable(true);
        e.allow();



    }

    @EventHandler
    public void inventory_click_event(InventoryClickEvent e){
        try{
            if (e.getClickedInventory().getName().equals("登录")){
                Player player = (Player)e.getWhoClicked();
                int cursor = e.getClickedInventory().getItem(14).getAmount();
                if (e.getCurrentItem().getType().equals(Material.STONE_BUTTON)&&cursor<53){
                    ItemStack itemStack = e.getCurrentItem();
                    Inventory inv = e.getClickedInventory();
                    inv.getItem(14).setAmount(inv.getItem(14).getAmount()+1);
                    inv.setItem(inv.getItem(14).getAmount(),new ItemStack(Material.STONE,itemStack.getAmount()));
                }

                if (e.getCurrentItem().getType().equals(Material.EMERALD_BLOCK)&&cursor > 35){
                    StringBuilder password = new StringBuilder();
                    for(int a = 35;a<54;a++){
                        try{
                            password.append(e.getClickedInventory().getItem(a).getAmount());
                        }catch (Exception ignored){
                            //idk how to deal with the exception.
                        }

                    }
                    onlinelist.add (player);
                    player.getOpenInventory().close();
                }

                if (e.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)&&cursor > 34){
                    Inventory inv = e.getClickedInventory();
                    inv.setItem(cursor, new ItemStack(Material.AIR,1));
                    if ( e.getClickedInventory().getItem(14).getAmount()>35){
                        e.getClickedInventory().getItem(14).setAmount(cursor-1);
                    }


                }

                if(e.getCurrentItem().getType().equals(Material.GLASS)){
                    for(int a = 35;a<54;a++){
                        try{
                            e.getClickedInventory().setItem(a,new ItemStack(Material.AIR,1));
                            e.getClickedInventory().setItem(14, new ItemStack(Material.SNOW_BLOCK, 35));
                        }catch (Exception ignored){

                        }

                    }
                }
                e.setCancelled(true);

            }
        }catch (Exception ignored){

        }

    }
    private void show_login_gui(Player player){
        Inventory inv = Bukkit.createInventory(null, 6 * 9,"登录");
        player.openInventory(inv);

        inv.setItem(0, new ItemStack(Material.STONE_BUTTON, 1));
        inv.setItem(1, new ItemStack(Material.STONE_BUTTON, 2));
        inv.setItem(2, new ItemStack(Material.STONE_BUTTON, 3));
        inv.setItem(9, new ItemStack(Material.STONE_BUTTON, 4));
        inv.setItem(10, new ItemStack(Material.STONE_BUTTON, 5));
        inv.setItem(11, new ItemStack(Material.STONE_BUTTON, 6));
        inv.setItem(18, new ItemStack(Material.STONE_BUTTON, 7));
        inv.setItem(19, new ItemStack(Material.STONE_BUTTON, 8));
        inv.setItem(20, new ItemStack(Material.STONE_BUTTON, 9));
        inv.setItem(27, new ItemStack(Material.EMERALD_BLOCK, 1));
        inv.setItem(28, new ItemStack(Material.REDSTONE_BLOCK, 1));
        inv.setItem(29, new ItemStack(Material.GLASS,1));
        inv.setItem(14, new ItemStack(Material.SNOW_BLOCK, 35));
    }
}


