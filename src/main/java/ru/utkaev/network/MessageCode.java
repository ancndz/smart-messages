package ru.utkaev.network;

public enum MessageCode {
	MESSAGE("MESSAGE"),

	NEW_USER("NEWUSER"),

	DENIED_NEW_USER("DENIEDNEWUSER"),

	PUBLIC_KEYS("PUBLICKEYS"),

	BLOCKCHAIN("BLOCKCHAIN");

	private final String message;

	MessageCode(final String message) {
		this.message = message;
	}

	public static MessageCode resolve(String value) {
		switch (value) {
			case "MESSAGE" :
				return MESSAGE;
			case "NEWUSER" :
				return NEW_USER;
			case "DENIEDNEWUSER" :
				return DENIED_NEW_USER;
			case "PUBLICKEYS" :
				return PUBLIC_KEYS;
			case "BLOCKCHAIN" :
				return BLOCKCHAIN;
			default :
				throw new EnumConstantNotPresentException(MessageCode.class, value);
		}
	}

	@Override
	public String toString() {
		return message;
	}
}
