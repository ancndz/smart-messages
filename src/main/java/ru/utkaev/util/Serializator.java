package ru.utkaev.util;

import java.io.*;
import java.util.Base64;

public class Serializator {

	/** Read the object from Base64 string. */
	public static Object deserializeObject(String s) throws IOException, ClassNotFoundException {
		final byte[] data = Base64.getDecoder().decode(s);
		final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
		final Object o = ois.readObject();
		ois.close();
		return o;
	}

	/** Write the object to a Base64 string. */
	public static String serializeObject(Serializable o) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		oos.close();
		return Base64.getEncoder().encodeToString(baos.toByteArray());
	}
}
