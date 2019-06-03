package lt.viko.eif.blockChain.domain;

public class Voter {

  private String personalNo;
  private String publicKey;
  private String rightToVote;

  public Voter() {
  }

  public Voter(String personalNo, String publicKey, String rightToVote) {
    this.personalNo = personalNo;
    this.publicKey = publicKey;
    this.rightToVote = rightToVote;
  }

  public String getPersonalNo() {
    return personalNo;
  }

  public void setPersonalNo(String personalNo) {
    this.personalNo = personalNo;
  }

  public String getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }

  public String getRightToVote() {
    return rightToVote;
  }

  public void setRightToVote(String rightToVote) {
    this.rightToVote = rightToVote;
  }

}
