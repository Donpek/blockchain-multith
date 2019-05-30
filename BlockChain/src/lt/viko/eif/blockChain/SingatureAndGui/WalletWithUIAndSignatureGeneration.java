package lt.viko.eif.blockChain.SingatureAndGui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.security.PrivateKey;
import java.text.NumberFormat;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.NumberFormatter;

public class WalletWithUIAndSignatureGeneration {

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

  private WalletWithUIAndSignatureGeneration() {

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
        fileType = new RandomFunctions().checkFileType(keyFileName);
        while (!fileType) {
          JOptionPane.showMessageDialog(null,
              "Select valid key file",
              "NO KEY FILE",
              JOptionPane.WARNING_MESSAGE);
          fileChooser.showOpenDialog(selectButton);
          pathToKeyTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
          String newKeyFileName = pathToKeyTextField.getText();
          fileType = new RandomFunctions().checkFileType(newKeyFileName);
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
        //String privateKeyFromFile = new RandomFunctions().readKeyFromFile(pathToKeyTextField.getText());
        try {
          String personalNo = personalNoFormattedTextField.getText();
          String chosenCandidate = Objects.requireNonNull(candidateListComboBox.getSelectedItem()).toString();
          PrivateKey votersPrivateKey = RandomFunctions.getPrivateKey(pathToKeyTextField.getText());
          String signature = new RandomFunctions().sign(personalNo + chosenCandidate, votersPrivateKey);

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
      frame.setContentPane(new WalletWithUIAndSignatureGeneration().mainPanel);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.pack();
      frame.setVisible(true);
    });
  }

}