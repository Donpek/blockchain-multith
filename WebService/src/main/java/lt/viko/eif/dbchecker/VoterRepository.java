package lt.viko.eif.dbchecker;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface VoterRepository extends MongoRepository<Voter, String> {

  Voter findByPersonalNo(String personalNo);

}
