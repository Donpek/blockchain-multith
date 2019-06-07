package lt.viko.eif.blockChain.KeyOperations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonToKeyFileGenerator {

  public String savePublicKeyToFile(String KeyBeforeFormatting) {
    String userHome = System.getProperty("user.home");
    try {
      String slightlyFormattedKey = KeyBeforeFormatting.replace("\\n", System.lineSeparator());
      Files.write(Paths.get(userHome + "/temp_public.key"), slightlyFormattedKey.getBytes());
      return userHome + "/temp_public.key";
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

}
