package lt.viko.eif.blockChain.KeyOperations;

import org.apache.commons.io.FilenameUtils;

public class ExtensionChecker {

  public Boolean checkFileType(String keyName) {
    String extension = FilenameUtils.getExtension(keyName);
    return extension.equals("key");
  }

}
