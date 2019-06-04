package lt.viko.eif.blockChain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
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


  public static void AlreadyVotedSoRemoveRight(String personalNo) throws IOException {
    /*final String POST_PARAMS = "{\n" + "\"userId\": 101,\r\n" +
        "    \"id\": 101,\r\n" +
        "    \"title\": \"Test Title\",\r\n" +
        "    \"body\": \"Test Body\"" + "\n}";
    */
    //System.out.println(POST_PARAMS);
    URL obj = new URL(
        "http://54.242.234.218:8080/dbchecker/voters/removeVoterRights/" + personalNo);
    HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
    postConnection.setRequestMethod("POST");
    //postConnection.setRequestProperty("userId", "a1bcdefgh");
    //postConnection.setRequestProperty("Content-Type", "application/json");
    postConnection.setDoOutput(true);
    OutputStream os = postConnection.getOutputStream();
    //os.write(POST_PARAMS.getBytes());
    os.flush();
    os.close();
    int responseCode = postConnection.getResponseCode();
    //System.out.println("POST Response Code :  " + responseCode);
    //System.out.println("POST Response Message : " + postConnection.getResponseMessage());
    if (responseCode == HttpURLConnection.HTTP_CREATED) { //success
      BufferedReader in = new BufferedReader(new InputStreamReader(
          postConnection.getInputStream()));
      String inputLine;
      StringBuffer response = new StringBuffer();
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();
      // print result
      System.out.println(response.toString());
    } else {
      System.out.println("POST NOT WORKED");
    }
  }
}
