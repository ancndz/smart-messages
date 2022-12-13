package ru.utkaev.network;

import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = 1L;

	private final byte[] cipherText;
	private final String receiver;

	public Message(byte[] cipherText, String receiver) {
		this.cipherText = cipherText;
		this.receiver = receiver;
	}

	public byte[] getCipherText() {
		return cipherText;
	}

	public String getReceiver() {
		return receiver;
	}
}
