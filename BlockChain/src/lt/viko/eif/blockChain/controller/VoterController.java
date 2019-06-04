package lt.viko.eif.blockChain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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
      System.out.println("Still alive");
      System.out.println("My response is: " + response);
      if (response.toString().equals("")){
        //System.out.println("response is null");
        return new Voter("Nothing");
      } else{
        return mapper.readValue(response.toString(), Voter.class);
      }
    } else {
      System.out.println("GET NOT WORKED");
    }
    return null;
  }

/*  public static String GetVoterPublicKeyFromDatabase(String personalNo) throws IOException {
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
      Voter voterFromDatabase = mapper.readValue(response.toString(), Voter.class);
      return voterFromDatabase.getPublicKey();
    } else {
      System.out.println("GET NOT WORKED");
    }
    return null;
  }*/


}
