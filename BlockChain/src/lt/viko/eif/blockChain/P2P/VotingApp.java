package lt.viko.eif.blockChain.P2P;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Objects;
import javax.swing.*;

import lt.viko.eif.blockChain.KeyOperations.ExtensionChecker;
import lt.viko.eif.blockChain.KeyOperations.JsonToKeyFileGenerator;
import lt.viko.eif.blockChain.KeyOperations.MessageSigner;
import lt.viko.eif.blockChain.KeyOperations.PemToDerKeyConverter;
import lt.viko.eif.blockChain.KeyOperations.SignatureVerification;
import lt.viko.eif.blockChain.controller.VoterController;
import lt.viko.eif.blockChain.domain.Voter;
import peerbase.*;
import peerbase.util.SimplePingStabilizer;

@SuppressWarnings("serial")
public class VotingApp extends JFrame {

	public static final int GENESIS_PORT = 4269;
	private static final int FRAME_WIDTH = 530, FRAME_HEIGHT = 400;

	private JPanel votingPanel, peersPanel;
	private JPanel lowervotingPanel, lowerPeersPanel;
	private DefaultListModel voteResultsModel, peersModel;
	private JList votingResultsList, peersList;


	private JButton fetchVotingResultsButton, voteButton, voteButtonComboBox;
	private JButton refreshPeersButton, rebuildPeersButton, selectKeyButton;

	private JComboBox candidateListComboBox;
	private JTextField addTextField, pathToKeyTextField;
	private JTextField rebuildTextField;
	private JTextField personalNoFormattedTextField;
	private JRadioButton optionComboBox, optionTextBox;

	private JLabel personalNoLabel, privateKeyLabel;

	private VotingNode peer;

	private String[] candidate = {"Ingrida Šimonytė",
			"Gitanas Nausėda",
			"Saulius Skvernelis",
			"Vytenis Povilas Andriukaitis",
			"Arvydas Juozaitis",
			"Valdemaras Tomaševskis",
			"Mindaugas Puidokas",
			"Naglis Puteikis",
			"Valentinas Mazuronis"};


	private VotingApp(String initialhost, int initialport, int maxpeers, PeerInfo mypd) {

		peer = new VotingNode(maxpeers, mypd);
		peer.buildPeers(initialhost, initialport, 2);
		peer.updateBlockchain();

		fetchVotingResultsButton = new JButton("Fetch");
		fetchVotingResultsButton.addActionListener(new FetchListener());
		voteButton = new JButton("Vote");
		voteButtonComboBox = new JButton("Vote");
		voteButton.addActionListener(new AddListener());
		voteButtonComboBox.addActionListener(new AddListenerComboBox());
		refreshPeersButton = new JButton("Refresh");
		refreshPeersButton.addActionListener(new RefreshListener());
		rebuildPeersButton = new JButton("Connect");
		rebuildPeersButton.addActionListener(new RebuildListener());

///////////////////////////
		selectKeyButton = new JButton("Select");
		selectKeyButton.addActionListener(new uploadKeyListener());
/////////////////////////////////

		candidateListComboBox = new JComboBox(candidate);
		addTextField = new JTextField(16);
		pathToKeyTextField = new JTextField(15);
		rebuildTextField = new JTextField(15);

		//////////////////////////////////////////////////////
		personalNoLabel = new JLabel("Enter personal No.:");
		personalNoFormattedTextField = new JTextField(21);
		privateKeyLabel = new JLabel("Select your private key:");
		//////////////////////////////////////////////////////
		//personalNoLabel.setLabelFor(personalNoFormattedTextField);
		//personalNoFormattedTextField.setText();
		//pathToKeyTextField.setText();
		//addTextField.setText("Candidate name surrname");
		rebuildTextField.setText("Host:Port");
		personalNoFormattedTextField.setHorizontalAlignment(JTextField.LEFT);

		optionComboBox = new JRadioButton("Select candidate from list");
		optionTextBox = new JRadioButton("Write it yourself");
		ButtonGroup group = new ButtonGroup();
		group.add(optionComboBox);
		group.add(optionTextBox);
		RadioButtonActionListener actionListener = new RadioButtonActionListener();
		optionComboBox.addActionListener(actionListener);
		optionTextBox.addActionListener(actionListener);

		setupFrame(this);

		(new Thread() {
			public void run() {
				peer.mainLoop();
			}
		}).start();
		new javax.swing.Timer(3000, new RefreshListener()).start();

		peer.startStabilizer(new SimplePingStabilizer(peer), 3000);
	}


	private void setupFrame(JFrame frame) {



	/* fixes the overlapping problem by using
	   a BorderLayout on the whole frame
	   and GridLayouts on the upper/lower panels*/

		frame = new JFrame("Voting App 2020 | ID: <" + peer.getId() + ">");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setLayout(new BorderLayout());

		JPanel upperPanel = new JPanel();
		upperPanel.setLayout(new GridLayout(2, 2));
		upperPanel.setPreferredSize(new Dimension(500, 500));

		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);

		voteResultsModel = new DefaultListModel();
		votingResultsList = new JList(voteResultsModel);
		peersModel = new DefaultListModel();
		peersList = new JList(peersModel);
		votingPanel = initPanel(new JLabel("Current Results"), votingResultsList);
		peersPanel = initPanel(new JLabel("Peer List"), peersList);

		lowervotingPanel = new JPanel();
		lowerPeersPanel = new JPanel();

		votingPanel.add(fetchVotingResultsButton);
		peersPanel.add(refreshPeersButton);

		lowervotingPanel.add(personalNoLabel);
		lowervotingPanel.add(personalNoFormattedTextField);

		lowervotingPanel.add(privateKeyLabel);
		lowervotingPanel.add(pathToKeyTextField);
		lowervotingPanel.add(selectKeyButton);

		lowervotingPanel.add(optionComboBox);
		lowervotingPanel.add(optionTextBox);
		lowervotingPanel.add(voteButton);
		voteButton.setEnabled(false);
		lowervotingPanel.add(addTextField);
		addTextField.setEnabled(false);

		lowervotingPanel.add(voteButtonComboBox);
		voteButtonComboBox.setEnabled(false);
		lowervotingPanel.add(candidateListComboBox);
		candidateListComboBox.setEnabled(false);

		lowerPeersPanel.add(rebuildTextField);
		lowerPeersPanel.add(rebuildPeersButton);

///////
		optionComboBox.setEnabled(false);
		optionTextBox.setEnabled(false);
		pathToKeyTextField.setEditable(false);
		/////////

		upperPanel.add(votingPanel);
		upperPanel.add(peersPanel);
		upperPanel.add(lowervotingPanel);
		upperPanel.add(lowerPeersPanel);

	/* by using a CENTER BorderLayout, the 
	   overlapping problem is fixed:
	   http://forum.java.sun.com/thread.jspa?threadID=551544&messageID=2698227 */

		frame.add(upperPanel, BorderLayout.NORTH);

		frame.setVisible(true);

	}


	private JPanel initPanel(JLabel textField,
			JList list) {
		JPanel panel = new JPanel();
		panel.add(textField);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setPreferredSize(new Dimension(200, 105));
		panel.add(scrollPane);
		return panel;
	}


	private void updateVotingResults() {
		voteResultsModel.removeAllElements();
		for (String candidate : peer.getCandidates()) {
			int voteCount = peer.getVoteCount(candidate);
			voteResultsModel.addElement(candidate + ":" + voteCount);
		}
	}


	private void updatePeerList() {
		peersModel.removeAllElements();
		for (String pid : peer.getPeerKeys()) {
			peersModel.addElement(pid);
		}
	}


	class uploadKeyListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			///////////////////////////////////////////////////////////
			String userHome = System.getProperty("user.home");
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.setCurrentDirectory(new File(userHome));
			fileChooser.setDialogTitle("Import private key");
			///////////////////////////////////////////////////////////

			optionComboBox.setEnabled(false);
			optionTextBox.setEnabled(false);

			Boolean fileType;
			//disableCandidateSelection();
			//disableVoting();
			//ConfirmationLabel.setText("");
			//RadioButtonActionListener

			fileChooser.showOpenDialog(selectKeyButton);
			pathToKeyTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
			String keyFileName = pathToKeyTextField.getText();
			fileType = new ExtensionChecker().checkFileType(keyFileName);
			while (!fileType) {
				JOptionPane.showMessageDialog(null,
						"Select valid key file",
						"NO KEY FILE",
						JOptionPane.WARNING_MESSAGE);
				fileChooser.showOpenDialog(selectKeyButton);
				pathToKeyTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
				String newKeyFileName = pathToKeyTextField.getText();
				fileType = new ExtensionChecker()
						.checkFileType(newKeyFileName);
			}

			optionComboBox.setEnabled(true);
			optionTextBox.setEnabled(true);
			//enableCandidateSelection();
		}
	}


	class FetchListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			updateVotingResults();
		}
	}

	class AddListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			//String candidate = addTextField.getText().trim();
			String afterText = "";

			/////
			try {
				Voter currentVoter = VoterController
						.GetVoterFromDatabase(personalNoFormattedTextField.getText());
				assert currentVoter != null;
				if (!currentVoter.getPersonalNo().equals(personalNoFormattedTextField.getText())) {
					JOptionPane.showMessageDialog(null,
							"There is no voter in registry with such personal no.",
							"You can't vote",
							JOptionPane.WARNING_MESSAGE);
					optionComboBox.setEnabled(false);
					optionTextBox.setEnabled(false);
					//disableCandidateSelection();
					//disableVoting();
				} else {
					if (currentVoter.getRightToVote().equals("false")) {
						JOptionPane.showMessageDialog(null,
								"Sorry, you already voted.",
								"You can't vote",
								JOptionPane.WARNING_MESSAGE);
					} else {

						try {
							boolean success;
							String personalNo = personalNoFormattedTextField.getText();
							//String chosenCandidate = Objects.requireNonNull(candidateListComboBox.getSelectedItem()).toString();

							String chosenCandidate = addTextField.getText().trim();

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
								System.out.println("You: " + personalNo);
								System.out.println("Vote for: " + chosenCandidate);
								System.out.println("Your private key is: " + votersPrivateKey);
								System.out.println("Your public key is: " + votersPublicKey);


								if (!chosenCandidate.equals("")) {
									peer.vote(chosenCandidate);
									afterText = "VOTE CAST";
								}
								addTextField.requestFocusInWindow();
								addTextField.setText(afterText);
								updateVotingResults();
								//>>>>>>>>>End blockChain stuff<<<<<<<<//

							} else {
								JOptionPane.showMessageDialog(null,
										"Tried to vote with someones private key? Shame on you!",
										"Bad key imported",
										JOptionPane.WARNING_MESSAGE);
								optionComboBox.setEnabled(false);
								optionTextBox.setEnabled(false);
								//disableCandidateSelection();
								//disableVoting();
							}
						} catch (Exception s) {
							s.printStackTrace();
						}


					}

				}
			} catch (IOException s) {
				s.printStackTrace();
			}
			/////

		}
	}


	class RadioButtonActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			JRadioButton button = (JRadioButton) event.getSource();

			if (button == optionComboBox) {

				voteButtonComboBox.setEnabled(true);
				candidateListComboBox.setEnabled(true);
				voteButton.setEnabled(false);
				addTextField.setEnabled(false);

			} else if (button == optionTextBox) {

				voteButton.setEnabled(true);
				addTextField.setEnabled(true);
				voteButtonComboBox.setEnabled(false);
				candidateListComboBox.setEnabled(false);

			}
		}
	}


	class AddListenerComboBox implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			//String candidate = candidateListComboBox.toString();

			/////
			try {
				Voter currentVoter = VoterController
						.GetVoterFromDatabase(personalNoFormattedTextField.getText());
				assert currentVoter != null;
				if (!currentVoter.getPersonalNo().equals(personalNoFormattedTextField.getText())) {
					JOptionPane.showMessageDialog(null,
							"There is no voter in registry with such personal no.",
							"You can't vote",
							JOptionPane.WARNING_MESSAGE);
					optionComboBox.setEnabled(false);
					optionTextBox.setEnabled(false);
					//disableCandidateSelection();
					//disableVoting();
				} else {
					if (currentVoter.getRightToVote().equals("false")) {
						JOptionPane.showMessageDialog(null,
								"Sorry, you already voted.",
								"You can't vote",
								JOptionPane.WARNING_MESSAGE);
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

								System.out.println("You: " + personalNo);
								System.out.println("Vote for: " + chosenCandidate);
								System.out.println("Your private key is: " + votersPrivateKey);
								System.out.println("Your public key is: " + votersPublicKey);


									peer.vote(chosenCandidate);
									//afterText = "VOTE CAST";

								addTextField.requestFocusInWindow();
								//ddTextField.setText(afterText);
								updateVotingResults();
								//>>>>>>>>>End blockChain stuff<<<<<<<<//

							} else {
								JOptionPane.showMessageDialog(null,
										"Tried to vote with someones private key? Shame on you!",
										"Bad key imported",
										JOptionPane.WARNING_MESSAGE);
								optionComboBox.setEnabled(false);
								optionTextBox.setEnabled(false);
								//disableCandidateSelection();
								//disableVoting();
							}
						} catch (Exception j) {
							j.printStackTrace();
						}


					}

				}
			} catch (IOException j) {
				j.printStackTrace();
			}
			/////
		}
	}

		class RefreshListener implements ActionListener {

			public void actionPerformed(ActionEvent e) {
				updateVotingResults();
				updatePeerList();
			}
		}

		class RebuildListener implements ActionListener {

			public void actionPerformed(ActionEvent e) {
				String peerid = rebuildTextField.getText().trim();
				if (!peer.maxPeersReached() && !peerid.equals("")) {
					try {
						String[] data = peerid.split(":");
						String host = data[0];
						int port = Integer.parseInt(data[1]);
						peer.buildPeers(host, port, 3);
						peer.updateBlockchain();
						updateVotingResults();
						updatePeerList();
					} catch (Exception ex) {
						LoggerUtil.getLogger().warning("VotingApp: rebuild: " + ex);
					}
				}
				rebuildTextField.requestFocusInWindow();
				rebuildTextField.setText("");
			}
		}


		public static void main(String[] args) throws IOException {
			int port = 9000;
			if (args.length != 1) {
				System.out.println("Usage: java ... lt.viko.eif.blockChain.P2P.VotingApp <host-port>");
			} else {
				port = Integer.parseInt(args[0]);
			}

//	LoggerUtil.setHandlersLevel(Level.FINE);
			new VotingApp("localhost", GENESIS_PORT, 100, new PeerInfo("localhost", port));
		}

	}

