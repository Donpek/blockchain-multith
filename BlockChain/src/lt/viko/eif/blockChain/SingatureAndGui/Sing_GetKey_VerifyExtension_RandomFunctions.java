package lt.viko.eif.blockChain.SingatureAndGui;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import org.apache.commons.io.FilenameUtils;

public class Sing_GetKey_VerifyExtension_RandomFunctions {

  public Boolean checkFileType(String keyName) {
    String extension = FilenameUtils.getExtension(keyName);
    return extension.equals("key");
  }


  public static PrivateKey getPrivateKey(String privateKeyPath)
      throws Exception {
    String userHome = System.getProperty("user.home");
    String newKey = userHome + "/private.der";
    int exitC = 0;

    //Need to find Windows alternative for this
    String[] cmd = {"/bin/sh", "-c",
        "openssl pkcs8 -topk8 -inform PEM -outform DER -in " + privateKeyPath + " -nocrypt > "
            + newKey};

    BufferedReader bri = null, bre = null;
    try {
      Process p = Runtime.getRuntime().exec(cmd);
      exitC = p.waitFor();
      bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
      bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
      String line = "";
      while ((line = bri.readLine()) != null) {
        System.out.println(line);
      }
      while ((line = bre.readLine()) != null) {
        System.out.println(line);
      }
      bri.close();
      bre.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("Exit Code: " + exitC);
    byte[] keyBytes = Files.readAllBytes(Paths.get(newKey));
    PKCS8EncodedKeySpec spec =
        new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    File keyFile = new File(newKey);
    keyFile.delete();
    return kf.generatePrivate(spec);
  }


  public String sign(String personalNoAndCandidate, PrivateKey votersPrivateKey)
      throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
    Signature privateSignature = Signature.getInstance("SHA256withRSA");
    privateSignature.initSign(votersPrivateKey);
    privateSignature.update(personalNoAndCandidate.getBytes(StandardCharsets.UTF_8));
    byte[] signature = privateSignature.sign();
    return Base64.getEncoder().encodeToString(signature);
  }


}
