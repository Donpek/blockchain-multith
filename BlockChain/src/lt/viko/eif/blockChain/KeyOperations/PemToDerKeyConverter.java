package lt.viko.eif.blockChain.KeyOperations;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class PemToDerKeyConverter {

  public static PrivateKey getPrivateKey(String privateKeyPath)
      throws Exception {
    String userHome = System.getProperty("user.home");
    String newKey = userHome + "/private.der";
    int exitC = 0;
    //Need to find Windows alternative for this
    String[] cmd = {"cmd.exe", "/c",
        "openssl pkcs8 -topk8 -inform PEM -outform DER -in " + privateKeyPath + " -nocrypt > "
            + newKey};
    ShellScripting(newKey, exitC, cmd);
    byte[] keyBytes = Files.readAllBytes(Paths.get(newKey));
    PKCS8EncodedKeySpec spec =
        new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    File keyFile = new File(newKey);
    keyFile.delete();
    return kf.generatePrivate(spec);
  }

  public static PublicKey getPublicKey(String publicKeyPath)
      throws Exception {

    String userHome = System.getProperty("user.home");
    String newKey = userHome + "/temp_public.der";
    int exitC = 0;
    //Need to find Windows alternative for this
    String[] cmd = {"cmd.exe", "/c",
        "openssl rsa -pubin -inform PEM -in " + publicKeyPath + " -outform DER -out "
            + newKey};
    ShellScripting(newKey, exitC, cmd);
    byte[] keyBytes = Files.readAllBytes(Paths.get(newKey));;
    X509EncodedKeySpec spec =
        new X509EncodedKeySpec(keyBytes);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    File derKeyFile = new File(newKey);
    derKeyFile.delete();
    File pemKeyFile = new File(publicKeyPath);
    pemKeyFile.delete();
    return kf.generatePublic(spec);
  }

  private static void ShellScripting(String newKey, int exitC, String[] cmd) throws IOException {
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
  }

}
