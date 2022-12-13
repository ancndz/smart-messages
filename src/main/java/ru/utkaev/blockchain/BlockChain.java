package ru.utkaev.blockchain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.utkaev.network.Message;

public class BlockChain implements Serializable {

	private static final long serialVersionUID = 1L;

	private final List<Block> chain = new ArrayList<>();

	private final int maxMessagesPerBlock;

	public BlockChain(int maxMessagesPerBlock) {
		this.maxMessagesPerBlock = maxMessagesPerBlock;
	}

	public void addMessage(final Message m) {
		if (chain.isEmpty()) {
			final Block nextBlock = new Block("ROOT BLOCK");
			nextBlock.addMessageAndUpdate(m);
			chain.add(nextBlock);
			return;
		}
		final Block lastBlock = chain.get(chain.size() - 1);
		if (lastBlock.getBlockMessages().size() == getMaxMessagesPerBlock()) {
			final Block nextBlock = new Block(lastBlock.getBlockHash()).addMessageAndUpdate(m);
			chain.add(nextBlock);
		} else {
			lastBlock.addMessageAndUpdate(m);
		}
	}

	public List<Block> getChain() {
		return chain;
	}

	public int getMaxMessagesPerBlock() {
		return maxMessagesPerBlock;
	}
}
