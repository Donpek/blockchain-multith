package lt.viko.eif.blockChain.SingatureAndGui;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

import static org.apache.commons.lang3.StringUtils.remove;
import static org.apache.commons.lang3.StringUtils.substringAfter;
import static org.apache.commons.lang3.StringUtils.substringBefore;


class SignatureVerification {

  static boolean verify(String personalNoWithChoice, String signature) throws Exception {
    String personalNo = substringBefore(personalNoWithChoice, ";");
    String chosenCandidate = substringAfter(personalNoWithChoice, ";");
    PublicKey votersKey = new SomethingLikeKeyChainAlternative().publicKeyReader(personalNo);
    Signature publicSignature = Signature.getInstance("SHA256withRSA");
    publicSignature.initVerify(votersKey);
    String message = remove(personalNoWithChoice, ";");
    publicSignature.update(message.getBytes(StandardCharsets.UTF_8));
    byte[] signatureBytes = Base64.getDecoder().decode(signature);

    //These things can be returned and passed to blockChain if true
    System.out.println("personalNo" + System.lineSeparator() + personalNo);
    System.out.println("chosenCandidate" + System.lineSeparator() + chosenCandidate);

    return publicSignature.verify(signatureBytes);
  }


}
