package group.ldgame.command;

import java.util.TreeMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import group.ldgame.eventlistener.PlayerLoginListener;
import group.ldgame.login.LoginInfoUtil;

public class PluginCommand implements CommandExecutor {
	private PlayerLoginListener pll;
	
	public PluginCommand(PlayerLoginListener pll) {
		this.pll = pll;
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("guilogin")) {
        	Player player = ((Player) sender).getPlayer();
            if (sender instanceof Player) {
            	//在游戏中输入指令
            	if(args.length == 0) {
            		sender.sendMessage("/guilogin reset <玩家名> - 重置玩家的密码(仅限op使用)");
            		sender.sendMessage("/guilogin reg - 试玩玩家输入此命令打开注册页面");
            		return true;
            	}
            	
            	if(args[0].equalsIgnoreCase("reset")) {
            		if(args.length == 1) {
            			//没有输入玩家名
            			sender.sendMessage("请输入你要重置密码的玩家名");            			
            		} else if(args.length == 2) {
            			TreeMap<String, String> playerInfo = LoginInfoUtil.getPlayerLoginInfo(args[1]);
            			
            			//输入了玩家名
            			if(playerInfo.get("playerexist").equals("yes")) {
            				//配置文件中有该玩家信息
            				if(!playerInfo.get("pwEncrypt").equals("")) {
            					//配置文件中玩家加密信息不为空
            					LoginInfoUtil.registerPlayerInfo(args[1], "");
            					sender.sendMessage("玩家"+args[1]+"的密码已重置");            			
            				} else {
            					//配置文件中玩家加密信息为空(密码被重置过)
            					sender.sendMessage("玩家"+args[1]+"的密码为空，无需重置");            			
            				}
            			} else {
            				//配置文件中找不到该玩家
            				sender.sendMessage("找不到该玩家");            			
            			}
            		} else {
            			sender.sendMessage("请输入正确的重置密码指令");
            		}
            	}
            	
            	if(args[0].equalsIgnoreCase("reg")) {
            		TreeMap<String, String> playerInfo = LoginInfoUtil.getPlayerLoginInfo(player.getName());
            		if(playerInfo.get("playerexist").equals("yes")) {
            			//正常玩家 或者是 被重置了密码然后点了试玩的玩家会进入
            			if(playerInfo.get("pwEncrypt").equals("")) {
        					//配置文件中玩家加密信息为空
            				pll.setCmdOpenReg(true);
                			pll.showMenu(player, PlayerLoginListener.REGISTER_MENU);
                			return true;
        				} else {
        					sender.sendMessage("您已经是正式玩家/密码未被重置");            			        					
        					return true;
        				}
            		} else {
            			pll.setCmdOpenReg(true);
            			pll.showMenu(player, PlayerLoginListener.REGISTER_MENU);
            			return true;
            		}
            	}
            }else {
            	//在游戏外输入指令
            	return true;
            }
        }
        return true;
    }
}
