package lt.viko.eif.WebService.service;

import lt.viko.eif.WebService.domain.Voters;
import java.util.List;

public interface VoterService {

  Voters saveUser(Voters e);

  Voters findByPersonalNo(String personalNo);

  void updateUser(Voters e);

  boolean userExists(Voters e);

  List<Voters> findAll();

}
