import com.destroystokyo.paper.Title;
import com.destroystokyo.paper.event.player.PlayerInitialSpawnEvent;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import net.minecraft.server.v1_12_R1.EntityPlayer;
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

import javax.jws.HandlerChain;
import java.util.ArrayList;

public class player_login implements Listener {
    Main m;
    ArrayList<Player> onlinelist = new ArrayList<>();

    public player_login(Main m){
        this.m = m;
    }



    @EventHandler
    public void click_to_login(PlayerLoginEvent e){


    }


    @EventHandler
    public void force_to_login(PlayerMoveEvent e){
        if (!onlinelist.contains(e.getPlayer())){
            e.setCancelled(true);
            Player p = e.getPlayer();
            Inventory inv = Bukkit.createInventory(null, 6 * 9,"登录");
            p.openInventory(inv);
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
            inv.setItem(14, new ItemStack(Material.WHITE_GLAZED_TERRACOTTA, 35));
        }

    }

    @EventHandler
    public void inventory_click_event(InventoryClickEvent e){
        if (e.getClickedInventory().getName().equals("登录")){
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
                        //异常处理不会整
                    }

                }
                System.out.println(password);

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
                        e.getClickedInventory().setItem(14, new ItemStack(Material.WHITE_GLAZED_TERRACOTTA, 35));
                    }catch (Exception ignored){
                        //异常处理不会整
                    }

                }
            }
            e.setCancelled(true);

        }

    }


}
