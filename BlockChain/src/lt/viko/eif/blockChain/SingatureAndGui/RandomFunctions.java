package lt.viko.eif.blockChain.SingatureAndGui;

import org.apache.commons.io.FilenameUtils;

class RandomFunctions {

  Boolean checkFileType(String keyName){
    String extension = FilenameUtils.getExtension(keyName);
    return extension.equals("key");
  }

}
