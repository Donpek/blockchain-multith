package lt.viko.eif.blockChain.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import lt.viko.eif.blockChain.BlochChain;
import lt.viko.eif.blockChain.KeyOperations.ExtensionChecker;
import lt.viko.eif.blockChain.KeyOperations.JsonToKeyFileGenerator;
import lt.viko.eif.blockChain.KeyOperations.MessageSigner;
import lt.viko.eif.blockChain.KeyOperations.PemToDerKeyConverter;
import lt.viko.eif.blockChain.KeyOperations.SignatureVerification;
import lt.viko.eif.blockChain.controller.VoterController;
import lt.viko.eif.blockChain.domain.Voter;

/*
        Citizens able to vote

      49101134312 JolitaBrans
      36702127422 JonasKlevas
      38905258472 JuozasParnauskas
      37112027462 KazimierasButkus
      49406138372 OnaGenys
      39209128472 TadasAdomaitis
*/

public class WalletUI {

  private JPanel mainPanel;
  private JButton selectButton;
  private JTextField pathToKeyTextField;
  private JPanel keySelectionPanel;
  private JPanel candidateSelectionPanel;
  private JComboBox candidateListComboBox;
  private JPanel confirmationPanel;
  private JButton castVoteButton;
  private JLabel candidateSelectionLabel;
  private JLabel ConfirmationLabel;
  private JPanel personalNoPanel;
  private JFormattedTextField personalNoFormattedTextField;

  private WalletUI() {

    String userHome = System.getProperty("user.home");
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fileChooser.setCurrentDirectory(new File(userHome));
    fileChooser.setDialogTitle("Import private key");

    selectButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        Boolean fileType;
        disableCandidateSelection();
        disableVoting();
        ConfirmationLabel.setText("");
        fileChooser.showOpenDialog(selectButton);
        pathToKeyTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
        String keyFileName = pathToKeyTextField.getText();
        fileType = new ExtensionChecker().checkFileType(keyFileName);
        while (!fileType) {
          JOptionPane.showMessageDialog(null,
              "Select valid key file",
              "NO KEY FILE",
              JOptionPane.WARNING_MESSAGE);
          fileChooser.showOpenDialog(selectButton);
          pathToKeyTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
          String newKeyFileName = pathToKeyTextField.getText();
          fileType = new ExtensionChecker()
              .checkFileType(newKeyFileName);
        }
        enableCandidateSelection();
      }
    });

    candidateListComboBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        String selectedCandidate = (String) candidateListComboBox.getSelectedItem();
        enableVoting();
        ConfirmationLabel.setText("Vote for " + selectedCandidate + "?");
      }
    });

    castVoteButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        try {
          Voter currentVoter = VoterController
              .GetVoterFromDatabase(personalNoFormattedTextField.getText());
          assert currentVoter != null;
          if (!currentVoter.getPersonalNo().equals(personalNoFormattedTextField.getText())) {
            JOptionPane.showMessageDialog(null,
                "There is no voter in registry with such personal no.",
                "You can't vote",
                JOptionPane.WARNING_MESSAGE);
            disableCandidateSelection();
            disableVoting();
          } else {
            try {
              boolean success;
              String personalNo = personalNoFormattedTextField.getText();
              String chosenCandidate = Objects
                  .requireNonNull(candidateListComboBox.getSelectedItem())
                  .toString();
              PrivateKey votersPrivateKey = PemToDerKeyConverter
                  .getPrivateKey(pathToKeyTextField.getText());
              String signature = new MessageSigner()
                  .sign(personalNo + chosenCandidate, votersPrivateKey);
              String uglyPublicKey = currentVoter.getPublicKey();
              String publicKeyOnPc = new JsonToKeyFileGenerator()
                  .savePublicKeyToFile(uglyPublicKey);
              PublicKey votersPublicKey = PemToDerKeyConverter.getPublicKey(publicKeyOnPc);
              success = SignatureVerification
                  .verify(personalNo + ";" + chosenCandidate, signature, votersPublicKey);
              if (success) {

                //>>>>>>>>>Integrate blockChain here<<<<<<<<//
                BlochChain blockChain = new BlochChain();
                if (blockChain.isBlockChainValid()) {
                  blockChain.addBlock(blockChain.newBlock(chosenCandidate));
                  //>>>>>>>>>If good - no more votes for him<<<<<<<<//
                  disableCandidateSelection();
                  disableVoting();
                  VoterController.AlreadyVotedSoRemoveRight(personalNo);
                } else {
                  System.out.println("BlockChain broke or something");
                }
                System.out.println(blockChain);
                //>>>>>>>>>End blockchain stuff<<<<<<<<//


              } else {
                JOptionPane.showMessageDialog(null,
                    "Tried to vote with someones private key? Shame on you!",
                    "Bad key imported",
                    JOptionPane.WARNING_MESSAGE);
                disableCandidateSelection();
                disableVoting();
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }

  private void disableCandidateSelection() {
    candidateListComboBox.setEnabled(false);
    candidateSelectionLabel.setEnabled(false);
  }

  private void disableVoting() {
    castVoteButton.setEnabled(false);
  }

  private void enableCandidateSelection() {
    candidateListComboBox.setEnabled(true);
    candidateSelectionLabel.setEnabled(true);
  }

  private void enableVoting() {
    castVoteButton.setEnabled(true);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      JFrame frame = new JFrame("WalletUI");
      frame.setContentPane(new WalletUI().mainPanel);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.pack();
      frame.setVisible(true);
    });
  }

}