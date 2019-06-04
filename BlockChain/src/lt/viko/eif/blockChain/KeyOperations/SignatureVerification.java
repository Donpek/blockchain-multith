package lt.viko.eif.blockChain.KeyOperations;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;
import lt.viko.eif.blockChain.BlochChain;

import static org.apache.commons.lang3.StringUtils.remove;
import static org.apache.commons.lang3.StringUtils.substringAfter;
import static org.apache.commons.lang3.StringUtils.substringBefore;


public class SignatureVerification {

  public static boolean verify(String personalNoWithChoice, String signature, PublicKey votersKey) throws Exception {
    String personalNo = substringBefore(personalNoWithChoice, ";");
    String chosenCandidate = substringAfter(personalNoWithChoice, ";");
    Signature publicSignature = Signature.getInstance("SHA256withRSA");
    publicSignature.initVerify(votersKey);
    String message = remove(personalNoWithChoice, ";");
    publicSignature.update(message.getBytes(StandardCharsets.UTF_8));
    byte[] signatureBytes = Base64.getDecoder().decode(signature);

 /*   //These things can be returned and passed to blockChain if true
    System.out.println("personalNo" + System.lineSeparator() + personalNo);
    System.out.println("chosenCandidate" + System.lineSeparator() + chosenCandidate);

    //Test chain
    BlochChain blockChain = new BlochChain();
    if (blockChain.isBlockChainValid()) {
      blockChain.addBlock(blockChain.newBlock(chosenCandidate));
    } else {
      System.out.println("Ou ou");
    }
    System.out.println(blockChain);
*/
    return publicSignature.verify(signatureBytes);
  }


}
