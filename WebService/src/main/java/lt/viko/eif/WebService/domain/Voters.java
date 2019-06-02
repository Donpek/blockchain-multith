package lt.viko.eif.WebService.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.ResourceSupport;

@Document
public class Voters extends ResourceSupport {

  private String firstName;
  private String lastName;
  private String personalNo;
  private String publicKey;
  private Boolean rightToVote;


  public Voters() {
  }

  public Voters(String firstName, String lastName, String personalNo, String publicKey,
      Boolean rightToVote) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.personalNo = personalNo;
    this.publicKey = publicKey;
    this.rightToVote = rightToVote;
  }

  @Override
  public String toString() {
    return "Voters{" +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName+ '\'' +
        ", personalNo='" + personalNo + '\'' +
        ", publicKey='" + publicKey + '\'' +
        ", rightToVote='" + rightToVote +
        '}';
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
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

  public Boolean getRightToVote() {
    return rightToVote;
  }

  public void setRightToVote(Boolean rightToVote) {
    this.rightToVote = rightToVote;
  }
}
