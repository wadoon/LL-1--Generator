package weigl.std;

public class StringUtils {

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isUpper(String s) {
		for (char c : s.toCharArray())
			if (!Character.isUpperCase(c))
				return false;
		return true;
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isLower(String s) {
		for (char c : s.toCharArray())
			if (!Character.isLowerCase(c))
				return false;
		return true;
	}

	public static String capatalize(String s) {
		s = s.toLowerCase();
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}
}
