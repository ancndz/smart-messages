package ru.utkaev.network;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Network {
	private static final Logger LOG = LoggerFactory.getLogger(Network.class);

	public static List<InetAddress> availableInterfaces() {
		final List<InetAddress> broadcastList = new ArrayList<>();
		Enumeration<NetworkInterface> interfaces = null;
		try {
			interfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			LOG.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
		while (interfaces.hasMoreElements()) {
			NetworkInterface networkInterface = interfaces.nextElement();

			try {
				if (networkInterface.isLoopback() || !networkInterface.isUp()) {
					continue;
				}
			} catch (SocketException e) {
				LOG.error(e.getLocalizedMessage());
				e.printStackTrace();
			}
			networkInterface.getInterfaceAddresses().stream().map(InterfaceAddress::getBroadcast)
					.filter(Objects::nonNull).forEach(broadcastList::add);
		}
		return broadcastList;
	}
}
