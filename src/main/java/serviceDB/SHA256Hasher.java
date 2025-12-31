package serviceDB;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256Hasher {
	public static String hash(String rawPassword) {
		if (rawPassword == null) {
			return null;
		}

		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hashBytes = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));

			StringBuilder hexString = new StringBuilder();
			for (byte b : hashBytes) {
				String hex = String.format("%02x", b);
				hexString.append(hex);
			}

			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			System.err.println("Lỗi: Thuật toán SHA-256 không tồn tại!");
			e.printStackTrace();
			return null;
		}
	}
}