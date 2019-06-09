package lt.viko.eif.blockChain.P2P;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.util.logging.Level;

import javax.swing.*;

import peerbase.*;
import peerbase.util.SimplePingStabilizer;

@SuppressWarnings("serial")
public class VotingApp extends JFrame
{

public static final int GENESIS_PORT = 4269;
private static final int FRAME_WIDTH = 565, FRAME_HEIGHT = 265;

private JPanel votingPanel, peersPanel;
private JPanel lowervotingPanel, lowerPeersPanel;
private DefaultListModel voteResultsModel, peersModel;
private JList votingResultsList, peersList;


private JButton fetchVotingResultsButton, voteButton;
private JButton removePeersButton, refreshPeersButton, rebuildPeersButton;

private JTextField addTextField;
private JTextField rebuildTextField;

private VotingNode peer;


private VotingApp(String initialhost, int initialport, int maxpeers, PeerInfo mypd)
{
	peer = new VotingNode(maxpeers, mypd);
	peer.buildPeers(initialhost, initialport, 2);
	peer.updateBlockchain();

	fetchVotingResultsButton = new JButton("Fetch");
	fetchVotingResultsButton.addActionListener(new FetchListener());
	voteButton = new JButton("Vote");
	voteButton.addActionListener(new AddListener());
	refreshPeersButton = new JButton("Refresh");
	refreshPeersButton.addActionListener(new RefreshListener());
	rebuildPeersButton = new JButton("Rebuild");
	rebuildPeersButton.addActionListener(new RebuildListener());

	addTextField = new JTextField(15);
	rebuildTextField = new JTextField(15);

	setupFrame(this);

	(new Thread() { public void run() { peer.mainLoop(); }}).start();

	/*
	  Swing is not threadsafe, so can't update GUI component
	  from a thread other than the event thread
	 */
	/*
	(new Thread() { public void run() { 
		while (true) {

			new RefreshListener().actionPerformed(null);
			try { Thread.sleep(1000); } catch (InterruptedException e) { }
		}
	}}).start();
	 */
	new javax.swing.Timer(3000, new RefreshListener()).start();

	peer.startStabilizer(new SimplePingStabilizer(peer), 3000);
}


private void setupFrame(JFrame frame)
{
	/* fixes the overlapping problem by using
	   a BorderLayout on the whole frame
	   and GridLayouts on the upper/lower panels*/

	frame = new JFrame("Voting App 2020 | ID: <" + peer.getId() + ">");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	frame.setLayout(new BorderLayout());


	JPanel upperPanel = new JPanel();
	JPanel lowerPanel = new JPanel();
	upperPanel.setLayout(new GridLayout(1, 2));
	// allots the upper panel 2/3 of the frame height
	upperPanel.setPreferredSize(new Dimension(FRAME_WIDTH, (FRAME_HEIGHT * 2 / 3)));
	lowerPanel.setLayout(new GridLayout(1, 2));


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

	lowervotingPanel.add(addTextField);
	lowervotingPanel.add(voteButton);

	lowerPeersPanel.add(rebuildTextField);
	lowerPeersPanel.add(rebuildPeersButton);

	upperPanel.add(votingPanel);
	upperPanel.add(peersPanel);
	lowerPanel.add(lowervotingPanel);
	lowerPanel.add(lowerPeersPanel);

	/* by using a CENTER BorderLayout, the 
	   overlapping problem is fixed:
	   http://forum.java.sun.com/thread.jspa?threadID=551544&messageID=2698227 */

	frame.add(upperPanel, BorderLayout.NORTH);
	frame.add(lowerPanel, BorderLayout.CENTER);

	frame.setVisible(true);

}


private JPanel initPanel(JLabel textField,
		JList list)
{
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


private void updatePeerList(){
	peersModel.removeAllElements();
	for (String pid : peer.getPeerKeys()) {
		peersModel.addElement(pid);
	}
}


class FetchListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		updateVotingResults();
	}
}

class AddListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		String candidate = addTextField.getText().trim();
		String afterText = "";
		if (!candidate.equals("")) {
			peer.vote(candidate);
			afterText = "VOTE CAST";
		}
		addTextField.requestFocusInWindow();
		addTextField.setText(afterText);
		updateVotingResults();
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
			}
			catch (Exception ex) {
				LoggerUtil.getLogger().warning("VotingApp: rebuild: " + ex);
			}
		}
		rebuildTextField.requestFocusInWindow();
		rebuildTextField.setText("");
	}
}


public static void main(String[] args) throws IOException
{
	int port = 9000;
	if (args.length != 1) {
		System.out.println("Usage: java ... lt.viko.eif.blockChain.P2P.VotingApp <host-port>");
	}
	else {
		port = Integer.parseInt(args[0]);
	}

//	LoggerUtil.setHandlersLevel(Level.FINE);
	new VotingApp("localhost", GENESIS_PORT, 5, new PeerInfo("localhost", port));
}

}
