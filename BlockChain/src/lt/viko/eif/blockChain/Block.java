package lt.viko.eif.blockChain;

import java.util.Date;

public class Block {
  public String hash;
  public String previousHash;
  private String data; //Something data...
  private long timeStamp; //as number of milliseconds since 1/1/1970.
  
  

  public String getHash() {
	return hash;
}



public void setHash(String hash) {
	this.hash = hash;
}



public String getPreviousHash() {
	return previousHash;
}



public void setPreviousHash(String previousHash) {
	this.previousHash = previousHash;
}



public String getData() {
	return data;
}



public void setData(String data) {
	this.data = data;
}



public long getTimeStamp() {
	return timeStamp;
}



public void setTimeStamp(long timeStamp) {
	this.timeStamp = timeStamp;
}



//Block Constructor.
  public Block(String data,String previousHash ) {
    this.data = data;
    this.previousHash = previousHash;
    this.timeStamp = new Date().getTime();
  }



@Override
public String toString() {
	return "Block [hash=" + hash + ", previousHash=" + previousHash + ", data=" + data + ", timeStamp=" + timeStamp
			+ "]";
}
   
}
