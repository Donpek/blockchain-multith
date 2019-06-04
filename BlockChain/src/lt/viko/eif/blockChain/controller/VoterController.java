package lt.viko.eif.blockChain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import lt.viko.eif.blockChain.domain.Voter;

public class VoterController {

  public static Voter GetVoterFromDatabase(String personalNo) throws IOException {
    URL urlForGetRequest = new URL(
        "http://54.242.234.218:8080/dbchecker/voters/getVoterRights/" + personalNo);
    String readLine = null;
    HttpURLConnection connection = (HttpURLConnection) urlForGetRequest.openConnection();
    connection.setRequestMethod("GET");
    int responseCode = connection.getResponseCode();
    if (responseCode == HttpURLConnection.HTTP_OK) //noinspection Duplicates
    {
      BufferedReader in = new BufferedReader(
          new InputStreamReader(connection.getInputStream()));
      StringBuilder response = new StringBuilder();
      while ((readLine = in.readLine()) != null) {
        response.append(readLine);
      }
      in.close();
      ObjectMapper mapper = new ObjectMapper();
      if (response.toString().equals("")) {
        return new Voter("Nothing");
      } else {
        return mapper.readValue(response.toString(), Voter.class);
      }
    } else {
      System.out.println("GET NOT WORKED");
    }
    return null;
  }


  public static void AlreadyVotedSoRemoveRight(String personalNo)
      throws IOException {
    URL urlForPostRequest = new URL(
        "http://54.242.234.218:8080/dbchecker/voters/removeVotersRights/" + personalNo);
    System.out.println(urlForPostRequest);
    String readLine = null;
    HttpURLConnection connection = (HttpURLConnection) urlForPostRequest.openConnection();
    connection.setRequestMethod("POST");
    int responseCode = connection.getResponseCode();
    if (responseCode == HttpURLConnection.HTTP_CREATED) {
      BufferedReader in = new BufferedReader(
          new InputStreamReader(connection.getInputStream()));
      StringBuilder response = new StringBuilder();
      while ((readLine = in.readLine()) != null) {
        response.append(readLine);
      }
      in.close();
    } else {
      System.out.println("POST NOT WORKED");
    }
  }
}