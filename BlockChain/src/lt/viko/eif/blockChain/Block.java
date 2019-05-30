package lt.viko.eif.blockChain;

import java.util.Arrays;
import java.util.Date;

public class Block {
  public int hash;
  public int previousHash;
  private String vote; //Something data...
  private long timeStamp; //as number of milliseconds since 1/1/1970.
  
  

  public int getHash() {
	return hash;
}



public void setHash(int hash) {
	this.hash = hash;
}



public int getPreviousHash() {
	return previousHash;
}



public void setPreviousHash(int previousHash) {
	this.previousHash = previousHash;
}



public String getData() {
	return vote;
}



public void setData(String data) {
	this.vote = data;
}



public long getTimeStamp() {
	return timeStamp;
}



public void setTimeStamp(long timeStamp) {
	this.timeStamp = timeStamp;
}



//Block Constructor.
  public Block(String data, int previousHash ) {
    this.vote = data;
    this.previousHash = previousHash;
    this.timeStamp = new Date().getTime();
    this.hash = Arrays.hashCode(new int[]{data.hashCode(), previousHash});
  }

 





@Override
public String toString() {
	return "Block [hash=" + hash + ", previousHash=" + previousHash + ", data=" + vote + ", timeStamp=" + timeStamp
			+ "]";
}
   
}
