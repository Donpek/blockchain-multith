package lt.viko.eif.WebService.service.impl;

import lt.viko.eif.WebService.domain.Voters;
import lt.viko.eif.WebService.repository.VoterRepository;
import lt.viko.eif.WebService.service.VoterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VoterServiceImpl implements VoterService {

  @Autowired
  private VoterRepository voterRepository;

  @Override
  public Voters saveUser(Voters e) {
    return voterRepository.save(e);
  }

  @Override
  public Voters findByPersonalNo(String personalNo) {
    return voterRepository.findOne(personalNo);
  }

  @Override
  public void updateUser(Voters e) {
    voterRepository.save(e);
  }

  @Override
  public boolean userExists(Voters e) {
    return voterRepository.exists(Example.of(e));
  }

  @Override
  public List<Voters> findAll() {
    return voterRepository.findAll();
  }

}
