package ru.utkaev.entity;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.utkaev.blockchain.BlockChain;
import ru.utkaev.network.Broadcast;
import ru.utkaev.network.Message;
import ru.utkaev.network.MessageCode;
import ru.utkaev.util.RsaCrypto;
import ru.utkaev.util.Serializator;

public class User extends Thread implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(User.class);

	private Map<String, PublicKey> publicKeys = new HashMap<>();

	private final String userName;
	private int port;
	private final PrivateKey privateKey;
	private final PublicKey publicKey;
	private BlockChain blockChain = new BlockChain(3);
	protected DatagramSocket serverSocket;

	public User(final String userName) throws NoSuchAlgorithmException {
		this.userName = userName;
		final KeyPair keyPair = RsaCrypto.buildKeyPair();
		this.privateKey = keyPair.getPrivate();
		this.publicKey = keyPair.getPublic();
	}

	@Override
	public void run() {
		port = IntStream.range(1630, 1661).filter(this::tryPort).findFirst().getAsInt();
		receive();
	}

	private boolean tryPort(int value) {
		try {
			serverSocket = new DatagramSocket(value);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean createMessage(final String plainText, final String receiverName) throws Exception {
		final String plainMsg = String.format("Sender:\t%s\nBody:\t%s\nTime:\t%s\n", getUserName(), plainText,
				LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

		final PublicKey receiverKey = getPublicKeys().get(receiverName);
		if (receiverKey == null) {
			LOG.error("User {} does not exists!", receiverName);
			return false;
		}
		final byte[] cipherText = RsaCrypto.encrypt(receiverKey, plainMsg);
		final Message message = new Message(cipherText, receiverName);
		Broadcast.broadcast(MessageCode.MESSAGE.toString(), Serializator.serializeObject(message));
		return true;
	}

	public String printMyMessages() {
		final StringBuffer messages = new StringBuffer();
		blockChain.getChain().stream().flatMap(block -> block.getBlockMessages().stream())
				.filter(message -> message.getReceiver().equals(userName)).forEach(message -> messages
						.append(RsaCrypto.decrypt(getPrivateKey(), message.getCipherText())).append("-------\n"));
		return messages.toString();
	}

	public void receive() {
		try {
			final byte[] buffer = new byte[65507];
			final DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
			LOG.info("Listening on udp:{}:{}\n", InetAddress.getLocalHost().getHostAddress(), getPort());
			while (true) {
				getServerSocket().receive(receivePacket);
				final String sentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
				final String[] data = sentence.split(Broadcast.DELIMITER);
				final MessageCode resolvedCode = MessageCode.resolve(data[0]);
				if (MessageCode.BLOCKCHAIN.equals(resolvedCode)) {
					setBlockChain((BlockChain) Serializator.deserializeObject(data[1]));
				} else if (MessageCode.PUBLIC_KEYS.equals(resolvedCode)) {
					setPublicKeys((Map<String, PublicKey>) Serializator.deserializeObject(data[1]));
				}
			}
		} catch (final Exception e) {
			LOG.error(e.getLocalizedMessage());
		}
	}

	public void broadcastPublicKey() throws IOException {
		final String pubKey = Serializator.serializeObject(getPublicKey());
		Broadcast.broadcast(MessageCode.NEW_USER.toString(), userName, pubKey);
	}

	public Set<String> getAllReceivers() {
		return publicKeys.keySet();
	}

	public Map<String, PublicKey> getPublicKeys() {
		return publicKeys;
	}

	public void setPublicKeys(Map<String, PublicKey> publicKeys) {
		this.publicKeys = publicKeys;
	}

	public BlockChain getBlockChain() {
		return blockChain;
	}

	public void setBlockChain(BlockChain blockChain) {
		this.blockChain = blockChain;
	}

	public String getUserName() {
		return userName;
	}

	public int getPort() {
		return port;
	}

	private PrivateKey getPrivateKey() {
		return privateKey;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public DatagramSocket getServerSocket() {
		return serverSocket;
	}

}
