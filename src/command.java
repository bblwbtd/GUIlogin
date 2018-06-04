import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("open")) {
            if (!(sender instanceof Player)) return true;
            Player p = (Player) sender;

            p.sendMessage(ChatColor.GREEN + "Opening vote menu!");

            try {
                Inventory inv = Bukkit.createInventory(null, 54, "Inventory!");


                p.openInventory(inv);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }
        return false;
    }
}
