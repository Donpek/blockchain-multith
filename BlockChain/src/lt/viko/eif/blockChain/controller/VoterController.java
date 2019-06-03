package lt.viko.eif.blockChain.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import lt.viko.eif.blockChain.domain.Voter;

public class VoterController {

  public static void GetVoterFromDatabase(String personalNo) throws IOException {
    URL urlForGetRequest = new URL(
        "http://54.242.234.218:8080/dbchecker/voters/getVoterRights/" + personalNo);
    String readLine = null;
    HttpURLConnection connection = (HttpURLConnection) urlForGetRequest.openConnection();
    connection.setRequestMethod("GET");
    int responseCode = connection.getResponseCode();
    if (responseCode == HttpURLConnection.HTTP_OK) {
      BufferedReader in = new BufferedReader(
          new InputStreamReader(connection.getInputStream()));
      StringBuilder response = new StringBuilder();
      while ((readLine = in.readLine()) != null) {
        response.append(readLine);
      }
      in.close();
      // print result
      System.out.println("JSON String Result " + response.toString());
      //GetAndPost.POSTRequest(response.toString());
    } else {
      System.out.println("GET NOT WORKED");
    }





  }
}
