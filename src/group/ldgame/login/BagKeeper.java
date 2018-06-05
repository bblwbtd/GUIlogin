package group.ldgame.login;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class BagKeeper {

    private ArrayList<itemContainer> storehouse = new ArrayList<>();

    public void keepItems(Player player){
        storehouse.add(new itemContainer(player));

    }
    public ItemStack[] get_back(Player player){
        for (itemContainer container:storehouse) {
            if (container.getName().equals(player.getName())){
                return container.getItems();
            }
        }
        return null;
    }

}

class itemContainer{
    String name ;
    ItemStack[] items;
    public itemContainer(Player player){
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
