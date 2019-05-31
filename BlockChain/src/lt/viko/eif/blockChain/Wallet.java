package lt.viko.eif.blockChain;

public class Wallet {
    public static void main(String[] args){
        
    	BlochChain blockchain = new BlochChain();
    	blockchain.addBlock(blockchain.newBlock("Paulauskas"));
    	blockchain.addBlock(blockchain.newBlock("Nauseda"));
    	//blockchain.addBlock(new Block(15, System.currentTimeMillis(),"aaaa", "bbbbbb"));
    	blockchain.addBlock(blockchain.newBlock("Simonyte"));
    	
    	//blockchain.addBlock(new Block(15, System.currentTimeMillis(),"aaaa", "bbbbbb"));

    	System.out.println(blockchain.isBlockChainValid());
    	
    	System.out.println(blockchain);
    }
}
