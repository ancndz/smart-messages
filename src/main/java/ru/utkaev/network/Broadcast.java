package ru.utkaev.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.stream.IntStream;

public class Broadcast {

	public static final String DELIMITER = ",";

	public static void broadcast(final String... broadcastMessage) {
		IntStream.range(1630, 1661).forEach(value -> {
			try (final DatagramSocket socket = new DatagramSocket()) {
				socket.setBroadcast(true);
				final byte[] buffer = String.join(DELIMITER, broadcastMessage).getBytes();
				final DatagramPacket packet = new DatagramPacket(buffer, buffer.length,
						Network.availableInterfaces().get(0), value);
				socket.send(packet);
			} catch (Exception e) {
				// ignored
			}
		});
	}
}
