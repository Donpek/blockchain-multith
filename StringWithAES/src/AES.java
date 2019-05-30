import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class AES {

  private static SecretKeySpec secretKey;
  private JTabbedPane tabbedPane1;
  private JPanel mainPanel;
  private JTextArea plainTextArea;
  private JPasswordField encryptPasswordField;
  private JButton encryptButton;
  private JButton selectFileButton;
  private JTextArea decryptedTextArea;
  private JTextField saveLocationTextField;
  private JButton decryptButton;
  private JTextField fileSelectTextField;
  private JPasswordField decryptPasswordField;
  private JTextField filenameTextField;
  private JTextArea encryptedTextArea;

  private AES() {

    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fileChooser.setCurrentDirectory(new File("/home/velfess"));
    fileChooser.setDialogTitle("Select file with encrypted text");

    encryptButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {

        if (saveLocationTextField.getText().isEmpty()) {
          JOptionPane.showMessageDialog(null, "Enter save path.",
              "No location.", JOptionPane.INFORMATION_MESSAGE);
        } else {
          File enteredDirectory = new File(saveLocationTextField.getText());
          if (enteredDirectory.exists() && enteredDirectory.isDirectory()) {
            if (plainTextArea.getText().isEmpty()) {
              JOptionPane.showMessageDialog(null, "Enter plaintext to encrypt.",
                  "No text.", JOptionPane.INFORMATION_MESSAGE);
            } else {
              if (encryptPasswordField.getPassword().length != 18) {
                JOptionPane.showMessageDialog(null, "Enter 18 char encryption key.",
                    "Bad key.", JOptionPane.INFORMATION_MESSAGE);
              } else {
                if (filenameTextField.getText().isEmpty()) {
                  JOptionPane.showMessageDialog(null, "Enter filename to save encrypted text into.",
                      "No filename.", JOptionPane.INFORMATION_MESSAGE);
                } else {
                  String saveLocation = saveLocationTextField.getText();
                  String saveFileName = filenameTextField.getText();
                  String originalString = plainTextArea.getText();
                  String secretKey = new String(encryptPasswordField.getPassword());
                  String encryptedString = AES.encrypt(originalString, secretKey);
                  File outputFile = new File(saveLocation + "/" + saveFileName);
                  try {
                    outputFile.createNewFile();
                  } catch (IOException e1) {
                    e1.printStackTrace();
                  }
                  try {
                    PrintWriter writer = new PrintWriter(outputFile, "UTF-8");
                    writer.println(encryptedString);
                    writer.close();
                    encryptedTextArea.setText(encryptedString);
                    JOptionPane.showMessageDialog(null, "File " + saveFileName + " created.",
                        "Saved.", JOptionPane.INFORMATION_MESSAGE);
                  } catch (FileNotFoundException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                  }
                }
              }
            }
          } else {
            JOptionPane.showMessageDialog(null, "Directory " + enteredDirectory +
                " doesn't exist.", "Wrong path", JOptionPane.INFORMATION_MESSAGE);
          }
        }
      }
    });

    selectFileButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        fileChooser.showOpenDialog(selectFileButton);
        fileSelectTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
      }
    });

    decryptButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        if (fileSelectTextField.getText().isEmpty()) {
          JOptionPane
              .showMessageDialog(null, "Please select file with encrypted text.", "No file",
                  JOptionPane.INFORMATION_MESSAGE);
        } else {
          if (decryptPasswordField.getPassword().length != 18) {
            JOptionPane
                .showMessageDialog(null, "Enter 18 char decryption key.", "Bad key",
                    JOptionPane.INFORMATION_MESSAGE);
          } else {
            String readFileName = fileSelectTextField.getText();
            String secretKey = new String(decryptPasswordField.getPassword());
            try {
              String badEncryptedString = new String(Files.readAllBytes(Paths.get(readFileName)));
              String encryptedString = removeLastCharOptional(badEncryptedString);
              String decryptedString = AES.decrypt(encryptedString, secretKey);
              decryptedTextArea.setText(decryptedString);
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
      }
    });
  }


  private static String removeLastCharOptional(String s) {
    return Optional.ofNullable(s)
        .filter(str -> str.length() != 0)
        .map(str -> str.substring(0, str.length() - 1))
        .orElse(s);
  }

  private static void setKey(String myKey) {
    MessageDigest sha;
    try {
      byte[] key = myKey.getBytes(StandardCharsets.UTF_8);
      sha = MessageDigest.getInstance("SHA-1");
      key = sha.digest(key);
      key = Arrays.copyOf(key, 16);
      secretKey = new SecretKeySpec(key, "AES");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }

  private static String encrypt(String strToEncrypt, String secret) {
    try {
      setKey(secret);
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);
      return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(
          StandardCharsets.UTF_8)));
    } catch (Exception e) {
      System.out.println("Error while encrypting: " + e.toString());
    }
    return null;
  }

  private static String decrypt(String strToDecrypt, String secret) {
    try {
      setKey(secret);
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
      cipher.init(Cipher.DECRYPT_MODE, secretKey);
      return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
    } catch (Exception e) {
      System.out.println("Error while decrypting: " + e.toString());
    }
    return null;
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      JFrame frame = new JFrame("MainUI");
      frame.setContentPane(new AES().mainPanel);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.pack();
      frame.setVisible(true);
    });
  }
}