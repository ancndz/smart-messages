package ru.utkaev.entity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.utkaev.network.Broadcast;
import ru.utkaev.network.Message;
import ru.utkaev.network.MessageCode;
import ru.utkaev.util.Serializator;

public class Miner extends User {
	private static final Logger LOG = LoggerFactory.getLogger(Miner.class);
	private static final long serialVersionUID = 1L;

	public Miner(final String userName) throws NoSuchAlgorithmException {
		super(userName);
	}

	@Override
	public void receive() {
		try {
			byte[] receiveData = new byte[65507];
			LOG.info("Listening on udp:{}:{}\n", InetAddress.getLocalHost().getHostAddress(), getPort());
			final DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			while (true) {
				getServerSocket().receive(receivePacket);
				final String sentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
				final String[] data = sentence.split(Broadcast.DELIMITER);
				final MessageCode resolvedCode = MessageCode.resolve(data[0]);
				if (MessageCode.MESSAGE.equals(resolvedCode)) {
					final Message message = (Message) Serializator.deserializeObject(data[1]);
					LOG.info("Received message to {}.", message.getReceiver());
					getBlockChain().addMessage(message);
					broadcastChain();
				} else if (MessageCode.NEW_USER.equals(resolvedCode)) {
					final String newUserName = data[1];
					final PublicKey newPublicKey = (PublicKey) Serializator.deserializeObject(data[2]);
					if (getPublicKeys().containsKey(newUserName)) {
						LOG.warn("Received new user registration: {} tries to join. Failed to join!", newUserName);
						Broadcast.broadcast(MessageCode.DENIED_NEW_USER.toString(), newUserName);
					} else {
						LOG.info("Received new user registration: {} tries to join. Success!", newUserName);
						getPublicKeys().put(newUserName, newPublicKey);
						broadcastPublicKeys();
					}
				}
			}
		} catch (final Exception e) {
			LOG.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	private void broadcastPublicKeys() throws IOException {
		final String serializedPublicKeys = Serializator.serializeObject(new HashMap<>(getPublicKeys()));
		Broadcast.broadcast(MessageCode.PUBLIC_KEYS.toString(), serializedPublicKeys);
	}

	public void broadcastChain() throws Exception {
		final String blockChainData = Serializator.serializeObject(getBlockChain());
		Broadcast.broadcast(MessageCode.BLOCKCHAIN.toString(), blockChainData);
	}
}
