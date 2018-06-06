package group.ldgame.login;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.TreeMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
/*
 * 玩家登录信息工具类
 */
public class LoginInfoUtil {
	/*
	 * 获取玩家登录信息
	 * 读取yml中玩家的加密密码信息
	 */
	public static TreeMap<String, String> getPlayerLoginInfo(String playerName) {
		TreeMap<String, String> playerLoginInfo = new TreeMap<>();

		File pwFile = new File("plugins/GUIlogin/login.yml");

		FileConfiguration fc = YamlConfiguration.loadConfiguration(pwFile);
		
		//玩家没有注册，设置加密密码值为空
		//该空值以便登录监听器判断后执行注册工具类方法
		if (Objects.equals(fc.getString(playerName + ".pwEncrypt"), null)) {
			fc.set(playerName+".pwEncrypt", "");
		}
		playerLoginInfo.put("pwEncrypt", fc.getString(playerName + ".pwEncrypt"));
		
		return playerLoginInfo;
	}
	
	public static void registerPlayerInfo(String playerName,String password) {
		//加密玩家密码
		String pwEncrypt = PassWordUtil.pwEncrypt(password);
		
		File pwFile = new File("plugins/GUIlogin/login.yml");
		FileConfiguration fc = YamlConfiguration.loadConfiguration(pwFile);
		
		//存取玩家登录信息
		fc.set(playerName+".pwEncrypt", pwEncrypt);
		
		try {
			fc.save(pwFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
