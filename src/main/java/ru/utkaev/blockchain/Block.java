package ru.utkaev.blockchain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.utkaev.network.Message;
import ru.utkaev.util.Sha256Tool;

public class Block implements Serializable {
	private static final long serialVersionUID = 1L;

	private String blockHash = "EMPTYBLOCK";
	private final List<Message> blockMessages = new ArrayList<>();
	private final String parentHash;

	public Block(final String parentHash) {
		this.parentHash = parentHash;
	}

	Block addMessageAndUpdate(final Message m) {
		blockMessages.add(m);
		updateHash();
		return this;
	}

	private void updateHash() {
		blockMessages.stream().parallel().forEach(message -> setBlockHash(Sha256Tool
				.getSHA256Hash(getBlockHash() + Arrays.toString(message.getCipherText()) + message.getReceiver())));
	}

	void setBlockHash(String hash) {
		this.blockHash = hash;
	}

	public String getBlockHash() {
		return blockHash;
	}

	public List<Message> getBlockMessages() {
		return blockMessages;
	}

	public String getParentHash() {
		return parentHash;
	}
}
