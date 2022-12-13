package ru.utkaev.util;

import java.security.*;

import javax.crypto.Cipher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RsaCrypto {
	private static final Logger LOG = LoggerFactory.getLogger(RsaCrypto.class);

	public static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
		final int keySize = 1024;
		final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(keySize);
		return keyPairGenerator.genKeyPair();
	}

	public static byte[] encrypt(final PublicKey publicKey, final String message) {
		try {
			final Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return cipher.doFinal(message.getBytes());
		} catch (Exception e) {
			LOG.error(e.getLocalizedMessage());
		}
		return new byte[0];
	}
	public static String decrypt(final PrivateKey privateKey, final byte[] encrypted) {
		try {
			final Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return new String(cipher.doFinal(encrypted));
		} catch (Exception e) {
			LOG.error(e.getLocalizedMessage());
		}
		return "";
	}
}
