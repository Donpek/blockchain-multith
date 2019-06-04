package lt.viko.eif.dbchecker;

import org.springframework.data.annotation.Id;

public class Voter {

  @Id
  private String userId;
  private String firstName;
  private String lastName;
  public String personalNo;
  public String publicKey;
  public Boolean rightToVote;

  public Voter(String personalNo, String publicKey, Boolean rightToVote) {
    this.personalNo = personalNo;
    this.publicKey = publicKey;
    this.rightToVote = rightToVote;
  }



  public void setRightToVote(Boolean rightToVote) {
    this.rightToVote = rightToVote;
  }

}

