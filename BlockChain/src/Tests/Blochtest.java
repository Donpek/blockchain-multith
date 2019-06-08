package Tests;

import static org.junit.Assert.*;

import org.junit.Test;

import lt.viko.eif.blockChain.BlockChain.BlochChain;
import lt.viko.eif.blockChain.BlockChain.Block;

public class Blochtest {

	BlochChain blockchain = new BlochChain();
	
	@Test
	public void testIsValid() {
		BlochChain blockchain = new BlochChain();
    	blockchain.addBlock(blockchain.newBlock("Paulauskas"));
    	blockchain.addBlock(blockchain.newBlock("Nauseda"));
    	//blockchain.addBlock(new Block(15, System.currentTimeMillis(),"aaaa", "bbbbbb"));
    	blockchain.addBlock(blockchain.newBlock("Simonyte"));
    	
    	//blockchain.addBlock(new Block(15, System.currentTimeMillis(),"aaaa", "bbbbbb"));
    	Boolean expected = true;
    	assertEquals(expected, blockchain.isBlockChainValid());
    	System.out.println(blockchain);
	}
	
	@Test
	public void testIsValidFalse() {
		BlochChain blockchain = new BlochChain();
    	blockchain.addBlock(new Block(15, System.currentTimeMillis(),"aaaa", "bbbbbb"));
    	Boolean expected = false;
    	assertEquals(expected, blockchain.isBlockChainValid());
    	System.out.println(blockchain);
	}
	
	@Test
	public void testIsValidFalse2() {
		BlochChain blockchain = new BlochChain();
		blockchain.addBlock(blockchain.newBlock("Paulauskas"));
    	blockchain.addBlock(new Block(15, System.currentTimeMillis(),"aaaa", "bbbbbb"));
    	
    	Boolean expected = false;
    	assertEquals(expected, blockchain.isBlockChainValid());
    	System.out.println(blockchain);
	}
	
	@Test
	public void testIsValidFalse3() {
		BlochChain blockchain = new BlochChain();
		blockchain.addBlock(blockchain.newBlock("Paulauskas"));
		blockchain.addBlock(blockchain.newBlock("Paulauskas"));
    	blockchain.addBlock(new Block(15, System.currentTimeMillis(),"aaaa", "bbbbbb"));
    	blockchain.addBlock(blockchain.newBlock("Paulauskas"));
    	
    	Boolean expected = false;
    	assertEquals(expected, blockchain.isBlockChainValid());
    	System.out.println(blockchain);
	}

}
