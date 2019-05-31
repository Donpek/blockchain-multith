package lt.viko.eif.blockChain.SingatureAndGui;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

class SomethingLikeKeyChainAlternative {

  /*
  49101134312 JolitaBrans
  36702127422 JonasKlevas
  38905258472 JuozasParnauskas
  37112027462 KazimierasButkus
  49406138372 OnaGenys
  39209128472 TadasAdomaitis
  */

  private String userHome = System.getProperty("user.home");

  private static Map<String, String> personalNoToKeyMap = new HashMap<>();

  private void keyChain() {
    personalNoToKeyMap.put("49101134312",
        userHome + "/blockchain-multith/BlockChain/pseudoDatabase/JolitaBransPub.der");
    personalNoToKeyMap.put("36702127422",
        userHome + "/blockchain-multith/BlockChain/pseudoDatabase/JonasKlevasPub.der");
    personalNoToKeyMap.put("38905258472",
        userHome + "/blockchain-multith/BlockChain/pseudoDatabase/JuozasParnauskasPub.der");
    personalNoToKeyMap.put("37112027462",
        userHome + "/blockchain-multith/BlockChain/pseudoDatabase/KazimierasButkusPub.der");
    personalNoToKeyMap.put("49406138372",
        userHome + "/blockchain-multith/BlockChain/pseudoDatabase/OnaGenysPub.der");
    personalNoToKeyMap.put("39209128472",
        userHome + "/blockchain-multith/BlockChain/pseudoDatabase/TadasAdomatisPub.der");
  }


  private String returnPublicKey(String personalNo) {
    keyChain();
    String keyFileLocation = personalNoToKeyMap.get(personalNo);
    personalNoToKeyMap.clear();
    if (keyFileLocation == null) {
      keyFileLocation = "There is no registered voter with ID " + personalNo;
    }
    return keyFileLocation;
  }

  PublicKey publicKeyReader(String personalNo)
      throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
    String locationInKeyChain = returnPublicKey(personalNo);
    byte[] keyBytes = Files.readAllBytes(Paths.get(locationInKeyChain));
    X509EncodedKeySpec spec =
        new X509EncodedKeySpec(keyBytes);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    return kf.generatePublic(spec);
  }


}

