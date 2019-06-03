package lt.viko.eif.dbchecker;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface VoterRepository extends MongoRepository<Voter, String> {

  public Voter findByFirstName(String firstName);

  public List<Voter> findByLastName(String lastName);

  public Voter findByPersonalNo(String personalNo);

}
