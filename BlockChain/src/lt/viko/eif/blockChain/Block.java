package lt.viko.eif.blockChain;

import java.util.Date;

public class Block {
  public String hash;
  public String previousHash;
  private String data; //Something data...
  private long timeStamp; //as number of milliseconds since 1/1/1970.

  //Block Constructor.
  public Block(String data,String previousHash ) {
    this.data = data;
    this.previousHash = previousHash;
    this.timeStamp = new Date().getTime();
  }
}
