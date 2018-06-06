package group.ldgame.login;

public class PassWordUtil {
	
	/*
	 * 密码验证方法
	 */
	public static boolean pwCheck(String inputPw,String hashedPw) {
		System.out.println(inputPw);
		System.out.println(hashedPw);
		if (BCrypt.checkpw(inputPw, hashedPw)) {
			return true;
		}else {
			return false;
		}
	}
	
	/*
	 * 加密方法，用户第一次设定密码的时候会调用
	 * 返回加密后的密码信息
	 */
	public static String pwEncrypt(String pw) {
		String hashed = BCrypt.hashpw(pw,BCrypt.gensalt());
		return hashed;
	}
}
