package lt.viko.eif.dbchecker.controllers;

import lt.viko.eif.dbchecker.Voter;
import lt.viko.eif.dbchecker.VoterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@RestController
@RequestMapping("/voters")
public class VoterControllers {

  @Autowired
  private VoterRepository repository;

  @RequestMapping(value = "/getVoterRights/{personalNo}", method = RequestMethod.GET)
  public HttpEntity<Voter> readVoter(@PathVariable("personalNo") String personalNo) {
    Voter voter = repository.findByPersonalNo(personalNo);
    return new ResponseEntity<>(voter, HttpStatus.OK);
  }

  @RequestMapping(value = "/removeVotersRights/{personalNo}", method = RequestMethod.POST)
  public HttpEntity<Voter> editVoter(@PathVariable("personalNo") String personalNo) {
    Voter voter = repository.findByPersonalNo(personalNo);
    voter.setRightToVote(false);
    repository.save(voter);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

}
