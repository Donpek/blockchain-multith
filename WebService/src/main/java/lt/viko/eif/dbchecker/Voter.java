package lt.viko.eif.dbchecker;

import org.springframework.data.annotation.Id;


public class Voter {

  @Id
  public String id;
  public String firstName;
  public String lastName;
  public String personalNo;
  public String publicKey;
  public Boolean rightToVote;


  public Voter() {
  }

  public Voter(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public Voter(String firstName, String lastName, String personalNo, String publicKey,
      Boolean rightToVote) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.personalNo = personalNo;
    this.publicKey = publicKey;
    this.rightToVote = rightToVote;
  }

  @Override
  public String toString() {
    return String.format(
        "Voter[id=%s, firstName='%s', lastName='%s']",
        id, firstName, lastName);
  }

}

