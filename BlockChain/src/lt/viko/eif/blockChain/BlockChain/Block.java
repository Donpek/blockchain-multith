package lt.viko.eif.blockChain.BlockChain;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class Block {
	
	private int index;
	private long timestamp;
	private String hash;
	private String previousHash;
	private String data;

	
	public Block(int index, long timestamp, String previousHash, String data) {
		super();
		this.index = index;
		this.timestamp = timestamp;
		this.previousHash = previousHash;
		this.data = data;
		hash = Block.calculateHash(this);
	}

	public int getIndex() {
		return index;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash ;
	}
	

	public String getPreviousHash() {
		return previousHash;
	}
	public void setPreviousHash(String hash) {
		this.previousHash = hash ;
	}

	public String getData() {
		return data;
	}
	
	public String str() {
		return index + timestamp + previousHash + data;
	}
	
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Block #").append(index).append(" [previousHash : ").append(previousHash).append(", ").
		append("timestamp :").append(new Date(timestamp)).append(", ").
		append("data: ").append(data).append(", ").
		append("hash: ").append(hash).append("]");
		
		return builder.toString();
	}
	
	public static String calculateHash(Block block) {
		
		if(block!=null) {
			MessageDigest digest = null;
			
			try {
				digest = MessageDigest.getInstance("SHA-256");
			} catch (NoSuchAlgorithmException e) {
				return null;
			}
			
			String txt = block.str();
			final byte bytes[] = digest.digest(txt.getBytes());
			final StringBuilder builder = new StringBuilder();
			
			for(final byte b : bytes) {
				String hex = Integer.toHexString(0xff & b);
				
				if(hex.length() == 1)
				{
					builder.append('0');
				}
				
				builder.append(hex);
			}
			return builder.toString();
		}
		return null;
	}

  
   
}
