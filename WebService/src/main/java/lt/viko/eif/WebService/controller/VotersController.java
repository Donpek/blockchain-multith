package lt.viko.eif.WebService.controller;

import lt.viko.eif.WebService.domain.Voters;
import lt.viko.eif.WebService.service.VoterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Configuration
@EnableSwagger2
@RestController
@RequestMapping("/voters")
public class VotersController {

  @Autowired
  private VoterService voterService;



  @RequestMapping(value = "/check/{personalNo}", method = RequestMethod.GET)
  public HttpEntity<Voters> getUsersById(@PathVariable("personalNo") String personalNo) {
    Voters byPersonalNo = voterService.findByPersonalNo(personalNo);
    if (byPersonalNo == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } else {
      byPersonalNo.add(
          linkTo(methodOn(VotersController.class).getUsersById(byPersonalNo.getPersonalNo()))
              .withSelfRel());
      return new ResponseEntity<>(byPersonalNo, HttpStatus.OK);
    }
  }

  @RequestMapping(value = "/updateRight/{personalNo}", method = RequestMethod.PUT)
  public HttpEntity<?> updateUser(@PathVariable("personalNo") String personalNo, @RequestBody Voters e) {
    Voters byPersonalNo = voterService.findByPersonalNo(personalNo);
    if (byPersonalNo == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } else {
      byPersonalNo.setRightToVote(e.getRightToVote());
      voterService.updateUser(byPersonalNo);
      byPersonalNo.add(
          linkTo(methodOn(VotersController.class).getUsersById(byPersonalNo.getPersonalNo()))
              .withSelfRel());
      return new ResponseEntity<>(byPersonalNo, HttpStatus.OK);
    }
  }


  @RequestMapping(value = "/list/", method = RequestMethod.GET)
  public HttpEntity<List<Voters>> getAllUsers() {
    List<Voters> voters = voterService.findAll();
    if (voters.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      voters.forEach(e -> e
          .add(linkTo(methodOn(VotersController.class).getAllUsers()).withRel("voters")));
      voters.forEach(e -> e.add(
          linkTo(methodOn(VotersController.class).getUsersById(e.getPersonalNo()))
              .withSelfRel()));
      return new ResponseEntity<>(voters, HttpStatus.OK);
    }
  }

}
