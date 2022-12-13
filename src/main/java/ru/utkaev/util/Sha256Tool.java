package ru.utkaev.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Sha256Tool {
	private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);
	public static String getSHA256Hash(final String data) {
		try {
			final byte[] hash = MessageDigest.getInstance("SHA-256").digest(data.getBytes(StandardCharsets.UTF_8));
			return bytesToHex(hash);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String bytesToHex(final byte[] bytes) {
		final byte[] hexChars = new byte[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}
		return new String(hexChars, StandardCharsets.UTF_8);
	}
}