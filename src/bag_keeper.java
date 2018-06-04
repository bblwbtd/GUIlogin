import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class bag_keeper {

    private ArrayList<item_container> storehouse = new ArrayList<>();

    public void keep_items(Player player){
        storehouse.add(new item_container(player));

    }
    public ItemStack[] get_back(Player player){
        for (item_container container:storehouse) {
            if (container.getName().equals(player.getName())){
                return container.getItems();
            }
        }
        return null;
    }

}

class item_container{
    String name ;
    ItemStack[] items;
    public item_container(Player player){
        name = player.getName();
        items = player.getInventory().getContents();
    }

    public ItemStack[] getItems() {
        return items;
    }

    public String getName() {
        return name;
    }
}
