package lt.viko.eif.blockChain.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.security.PrivateKey;
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
import lt.viko.eif.blockChain.SingatureAndGui.SignatureVerification;
import lt.viko.eif.blockChain.SingatureAndGui.Sing_GetKey_VerifyExtension_RandomFunctions;
import lt.viko.eif.blockChain.controller.VoterController;
import lt.viko.eif.blockChain.domain.Voter;

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
        fileType = new Sing_GetKey_VerifyExtension_RandomFunctions().checkFileType(keyFileName);
        while (!fileType) {
          JOptionPane.showMessageDialog(null,
              "Select valid key file",
              "NO KEY FILE",
              JOptionPane.WARNING_MESSAGE);
          fileChooser.showOpenDialog(selectButton);
          pathToKeyTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
          String newKeyFileName = pathToKeyTextField.getText();
          fileType = new Sing_GetKey_VerifyExtension_RandomFunctions()
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
        //String privateKeyFromFile = new Sing_GetKey_VerifyExtension_RandomFunctions().readKeyFromFile(pathToKeyTextField.getText());
        try {
          Voter currentVoter = VoterController.GetVoterFromDatabase(personalNoFormattedTextField.getText());
          assert currentVoter != null;
          if (!currentVoter.getPersonalNo().equals(personalNoFormattedTextField.getText())){
            JOptionPane.showMessageDialog(null,
                "There is no voter in registry with such personal no.",
                "You can't vote",
                JOptionPane.WARNING_MESSAGE);
          } else {
            System.out.println("Personal No: "+ currentVoter.getPersonalNo());
            System.out.println("Public key: " + currentVoter.getPublicKey());
            System.out.println("Right to vote: " + currentVoter.getRightToVote());








          }


          //String haveIAnyRights = VoterController.GetVoterRightsFromDatabase(personalNoFormattedTextField.getText());
/*
          assert haveIAnyRights != null;
          if (haveIAnyRights.equals("true")){
            System.out.println("do more stuff here");

          } else {
            JOptionPane.showMessageDialog(null,
                "Sorry, you cannot vote (anymore)",
                "No rights",
                JOptionPane.WARNING_MESSAGE);
          }
*/


        } catch (IOException e) {
          e.printStackTrace();
        }



        try {
          boolean success;
          String personalNo = personalNoFormattedTextField.getText();
          String chosenCandidate = Objects.requireNonNull(candidateListComboBox.getSelectedItem())
              .toString();
          PrivateKey votersPrivateKey = Sing_GetKey_VerifyExtension_RandomFunctions
              .getPrivateKey(pathToKeyTextField.getText());
          String signature = new Sing_GetKey_VerifyExtension_RandomFunctions()
              .sign(personalNo + chosenCandidate, votersPrivateKey);

          //Sends his choice, ID and signature for verification
          //need so integrate from this point on
          success = SignatureVerification.verify(personalNo + ";" + chosenCandidate, signature);
          if (success) {
            System.out.println("Voting was fine");
          } else {
            System.out.println("You did't vote");
          }
        } catch (Exception e) {
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