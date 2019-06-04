package lt.viko.eif.blockChain.KeyOperations;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;

public class MessageSigner {

  public String sign(String personalNoAndCandidate, PrivateKey votersPrivateKey)
      throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
    Signature privateSignature = Signature.getInstance("SHA256withRSA");
    privateSignature.initSign(votersPrivateKey);
    privateSignature.update(personalNoAndCandidate.getBytes(StandardCharsets.UTF_8));
    byte[] signature = privateSignature.sign();
    return Base64.getEncoder().encodeToString(signature);
  }


}
