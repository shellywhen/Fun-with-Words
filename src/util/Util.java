package util;
/**
 * @version 1
 * @author Liwenhan Xie (1600017744)
 * @since 2019-May-24
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {
	/**
	 * Encrypt the password with MD5 algorithm.
	 * @param password string
	 * @return encrypted password
	 */
	static public String md5(String password) {
		MessageDigest md;
		byte[] output;
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] input = password.getBytes();
			output = md.digest(input);
		}catch(NoSuchAlgorithmException e) {
		  e.printStackTrace();
		  return password;
		}
		return Base64.getEncoder().encodeToString(output);
	}
	/**
	 * Get the detailed time string of today.
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	static public String getTodayLongString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date today = new Date();
		return format.format(today);
	}
	/**
	 * Get the brief date string of today.
	 * @return yyyy-MM-dd
	 */
	static public String getTodayString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		return format.format(today);
	}
	/**
	 * 
	 * @param meaning long string of the meaning
	 * @return split meanings
	 */
	static public ArrayList<String>splitMeaning(String meaning) {
		ArrayList<String> meaningList;
		List<String> list = Arrays.asList(meaning.split("\\s*\n\\s*"));
		meaningList = new ArrayList<String>(list);
		return meaningList;
	}
}
