package serviceDB;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256Hasher {
	public static String hash(String rawPassword) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hashBytes = digest.digest(rawPassword.getBytes());
			StringBuilder hexString = new StringBuilder();
			for(byte b : hashBytes) {
				String hex = Integer.toHexString(0xff & b);
				if(hex.length() == 1)	hexString.append('0');
				hexString.append(hex);
			}
			
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
}
