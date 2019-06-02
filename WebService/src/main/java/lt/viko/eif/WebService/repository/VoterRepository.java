package lt.viko.eif.WebService.repository;

import lt.viko.eif.WebService.domain.Voters;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VoterRepository extends MongoRepository<Voters, String> {

}
