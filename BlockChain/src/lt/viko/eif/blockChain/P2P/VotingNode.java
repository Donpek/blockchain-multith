package lt.viko.eif.blockChain.P2P;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import com.google.gson.*;

import lt.viko.eif.blockChain.BlockChain.BlochChain;
import lt.viko.eif.blockChain.BlockChain.Block;
import lt.viko.eif.blockChain.Counter;
import peerbase.*;

public class VotingNode extends Node {

/* MESSAGE TYPES */
public static final String INSERTPEER = "JOIN";
public static final String LISTPEER = "LIST";
public static final String PEERNAME = "NAME";
public static final String PEERQUIT = "QUIT";

public static final String GIVE_ME_UR_BRUCKCHEIN = "GMUB";
public static final String NEW_BLOCK = "NBLK";
public static final String ADD_BLOCK = "ABLK";
public static final String IM_WAITING_FOR_YOU_TO_ADD = "WYTA";
public static final String REROLL = "RERL";

public static final String REPLY = "REPL";
public static final String ERROR = "ERRO";

private static final ArrayList<String> candidates = new ArrayList<String>();
private static BlochChain blockchain;
private static Block myNewBlock;
private static int myRoll;
private static final HashMap<String,Integer> peers = new HashMap<>();
public static boolean needToDownloadChain;

public VotingNode(int maxPeers, PeerInfo myInfo) {
	super(maxPeers, myInfo);
	myNewBlock = null;
	myRoll = 0;
	
	candidates.add("Ingrida Šimonytė");
	candidates.add("Gitanas Nausėda");
	candidates.add("Saulius Skvernelis");
	candidates.add("Vytenis Povilas Andriukaitis");
	candidates.add("Arvydas Juozaitis");
	candidates.add("Valdemaras Tomaševskis");
	candidates.add("Mindaugas Puidokas");
	candidates.add("Naglis Puteikis");
	candidates.add("Valentinas Mazuronis");



	this.addRouter(new Router(this));
	
	this.addHandler(INSERTPEER, new JoinHandler(this));
	this.addHandler(LISTPEER, new ListHandler(this));
	this.addHandler(PEERNAME, new NameHandler(this));
	this.addHandler(PEERQUIT, new QuitHandler(this));
	this.addHandler(GIVE_ME_UR_BRUCKCHEIN, new GetBlockchainHandler(this));
	this.addHandler(IM_WAITING_FOR_YOU_TO_ADD, new WaitForAddHandler(this));
	this.addHandler(NEW_BLOCK, new NewBlockHandler(this));
	this.addHandler(ADD_BLOCK, new AddBlockHandler(this));
	this.addHandler(REROLL, new RerollBlockHandler(this));

	if(myInfo.getPort() == VotingApp.GENESIS_PORT) {
		blockchain = new BlochChain();
	}else {
		needToDownloadChain = true;
	}
}

public boolean updateBlockchain() {
	if(!needToDownloadChain) {
		return true;
	}
	Set<String> keys = this.getPeerKeys();
	System.out.println(keys);
	List<PeerMessage> chains = new ArrayList<>();
	
	for(String key : keys) {
		String[] data = key.split(":");
		String host = data[0];
		int port = Integer.parseInt(data[1]);
		List<PeerMessage> resplist = this.connectAndSend(new PeerInfo(host, port), 
				GIVE_ME_UR_BRUCKCHEIN, "", true);		
		chains.add(resplist.get(0));
	}
	
	String mostRepeatingBlockchain = "";
	int prevOccurances = 0; int currOccurances = 0;
	boolean[] ignoreList = new boolean[chains.size()];
	for(int i=0;i<chains.size();++i) {
		ignoreList[i] = false;
	}
	
	for(int i=0;i<chains.size();++i) {
		for(int j=i;j<chains.size();++j) {
			if(!ignoreList[j] && chains.get(i) == chains.get(j)) {
				++currOccurances;
				ignoreList[j] = true;
			}
		}
		if(currOccurances > prevOccurances) {
			mostRepeatingBlockchain = chains.get(i).getMsgData();
		}
		prevOccurances = currOccurances;
	}
	
	Gson gson = new Gson();
	blockchain = gson.fromJson(mostRepeatingBlockchain, BlochChain.class);
	System.out.println("hello? " + mostRepeatingBlockchain);
	
	return true;
}

public ArrayList<String> getCandidates() {
	return candidates;
}

public int getVoteCount(String candidate) {
	return Counter.getVotes(blockchain, candidate);
}

public void recursiveReroll(HashMap<Integer, ArrayList<String[]>> rollers, ArrayList<String[]> addOrder){
	int biggestIndex;
	Integer[] rolls = rollers.keySet().toArray(new Integer[rollers.size()]);
	for(int i=0;i<rolls.length-1;++i) {
		biggestIndex = i;
		for(int j=i;j<rolls.length-1;++j)
		{
			if(rolls[j] > rolls[biggestIndex]) {
				biggestIndex = j;
			}
		}
		Integer temp = rolls[i];
		rolls[i] = rolls[biggestIndex];
		rolls[biggestIndex] = temp;
	}
	
	HashMap<Integer, ArrayList<String[]>> rerolls = new HashMap<>();
	for(int i=0;i<rollers.size();++i) {
		ArrayList<String[]> peersThatRolledThat = rollers.get(rolls[i]);
		
		if(peersThatRolledThat.size() == 1) {
			addOrder.add(peersThatRolledThat.get(0));
		}else {
			myRoll = ThreadLocalRandom.current().nextInt(1, 101);
			for(String[] peerData : peersThatRolledThat) {
				List<PeerMessage> reroll = this.connectAndSend(new PeerInfo(peerData[0], 
						Integer.parseInt(peerData[1])), REROLL, "", true);
				int roll = Integer.parseInt(reroll.get(0).getMsgData());
				if(rerolls.containsKey(roll)) {
					rerolls.get(roll).add(peerData);
				}else{
					rerolls.put(roll, new ArrayList<String[]>());
				}
				this.recursiveReroll(rerolls, addOrder);
			}
		}
	}
}

public void vote(String candidate) {
	Gson gson = new Gson();
	myNewBlock = blockchain.newBlock(candidate);
	String voteJSON = gson.toJson(myNewBlock);
	
	Set<String> keys = this.getPeerKeys();
	HashMap<String[], PeerMessage> replies = new HashMap<>();
	
	for(String key : keys) {
		String[] data = key.split(":");
		String host = data[0];
		int port = Integer.parseInt(data[1]);
		List<PeerMessage> resplist = this.connectAndSend(new PeerInfo(host, port), 
				NEW_BLOCK, voteJSON, true);	
		replies.put(data, resplist.get(0));
	}
	
	HashMap<Integer, ArrayList<String[]>> rollers = new HashMap<>();
	for(Map.Entry<String[], PeerMessage> entry : replies.entrySet()) {
		PeerMessage reply = entry.getValue();
		String[] peerData = entry.getKey();
		
		String[] replyData = reply.getMsgData().split("\\s");
		String replyType = replyData[0];
		if(replyType == "nope") {
		}else if(replyType == "ts") {
			long timestamp = Long.parseLong(replyData[1]);
			if(timestamp < myNewBlock.getTimestamp()) {
				List<PeerMessage> resplist = 
						this.connectAndSend(new PeerInfo(peerData[0], 
								Integer.parseInt(peerData[1])), 
						IM_WAITING_FOR_YOU_TO_ADD, voteJSON, true);
			}
		}else if(replyType == "r#") {
			// NOTE Don #4 is reikalavimu
			int roll = Integer.parseInt(replyData[1]);
			if(rollers.containsKey(roll)) {
				rollers.get(roll).add(peerData);
			}else{
				rollers.put(roll, new ArrayList<String[]>());
			}
		}
	}
	
	ArrayList<String[]> addOrder = new ArrayList<String[]>();
	this.recursiveReroll(rollers, addOrder);
	
	for(String key : keys) {
		String[] data = key.split(":");
		String host = data[0];
		int port = Integer.parseInt(data[1]);
		this.connectAndSend(new PeerInfo(host, port), ADD_BLOCK, voteJSON, false);	
	}
	myNewBlock.setPreviousHash(blockchain.latestBlock().getHash());
	myNewBlock.setHash(myNewBlock.calculateHash(myNewBlock));
	blockchain.addBlock(myNewBlock);
	myNewBlock = null;
}

public void buildPeers(String host, int port, int hops) {
//	LoggerUtil.getLogger().fine("build peers");
	
	if (this.maxPeersReached() || hops <= 0)
		return;
	PeerInfo pd = new PeerInfo(host, port);
	List<PeerMessage> resplist = this.connectAndSend(pd, PEERNAME, "", true);
	if (resplist == null || resplist.size() == 0)
		return;
	String peerid = resplist.get(0).getMsgData();
//	LoggerUtil.getLogger().fine("contacted " + peerid);
	pd.setId(peerid);
	
	String resp = this.connectAndSend(pd, INSERTPEER,
			String.format("%s %s %d", this.getId(), this.getHost(), this.getPort()), true).get(0).getMsgType();
	if (!resp.equals(REPLY) || this.getPeerKeys().contains(peerid))
		return;
	
	this.addPeer(pd);
	
	// do recursive depth first search to add more peers
	resplist = this.connectAndSend(pd, LISTPEER, "", true);
	
	if (resplist.size() > 1) {
		resplist.remove(0);
		for (PeerMessage pm : resplist) {
			String[] data = pm.getMsgData().split("\\s");
			String nextpid = data[0];
			String nexthost = data[1];
			int nextport = Integer.parseInt(data[2]);
			if (!nextpid.equals(this.getId()))
				buildPeers(nexthost, nextport, hops - 1);
		}
	}
}

/* msg syntax: RERL */
private class RerollBlockHandler implements HandlerInterface {
	private Node peer;
	
	public RerollBlockHandler(Node peer) { this.peer = peer; }
	
	public void handleMessage(PeerConnection peerconn, PeerMessage msg) {
		myRoll = ThreadLocalRandom.current().nextInt(1, 101);
		peerconn.sendData(new PeerMessage(REPLY, Integer.toString(myRoll)));
	}
}

/* NEW_BLOCK syntax: NBLK new-block-in-json-format
 * REPLY syntax: RPLY param
 * param options:
 * 'nope' - signals that the verification failed
 * 'ts' timestamp - the timestamp value of their own new block if they have one (if not, it's '0')
 * 'r#' roll - the result of a 1d100 roll used for resolving 'stamped a new block at the same time' issues
 * */
private class NewBlockHandler implements HandlerInterface {
	private Node peer;
	
	public NewBlockHandler(Node peer) { this.peer = peer; }
	
	public void handleMessage(PeerConnection peerconn, PeerMessage msg) {
		System.out.println("KRWA");
		Gson gson = new Gson();

		String blockJSON = msg.getMsgData();
		Block receivedBlock = gson.fromJson(blockJSON, Block.class);
		
		if(!blockchain.isValidNewBlock(receivedBlock, blockchain.latestBlock())) {
			peerconn.sendData(new PeerMessage(REPLY, "nope"));
			System.out.println("Block " + blockJSON + " is funky");
			return; // block is funky, me no likey
		}
		
		if(myNewBlock != null) {
			if(myNewBlock.getTimestamp() == receivedBlock.getTimestamp())
			{
				myRoll = ThreadLocalRandom.current().nextInt(1, 101);
				peerconn.sendData(new PeerMessage(REPLY, "r# " + Integer.toString(myRoll)));
				System.out.println("Block " + blockJSON + " is r#");

			}else {
				peerconn.sendData(new PeerMessage(REPLY, "ok " + Long.toString(
						myNewBlock.getTimestamp())));
				System.out.println("Block " + blockJSON + " is ok ts");

			}
		}else {
			peerconn.sendData(new PeerMessage(REPLY, "ok 0"));
			System.out.println("Block " + blockJSON + " is ok 0");

		}
	}
}

/* msg syntax: WYTA */
private class WaitForAddHandler implements HandlerInterface {
	private Node peer;
	
	public WaitForAddHandler(Node peer) { this.peer = peer; }
	
	public void handleMessage(PeerConnection peerconn, PeerMessage msg) {
		while(myNewBlock != null) {} 
		/* A short poem, while you wait:
		 * Why is there blood,
		 * I'm in the mud,
		 * CPU melt, I know,
		 * No one's paying me, though.
		 * By Donatas Pekelis */
		peerconn.sendData(new PeerMessage(REPLY, ""));
	}
}

/* msg syntax: ABLK block-in-json-format */
private class AddBlockHandler implements HandlerInterface {
	private Node peer;
	
	public AddBlockHandler(Node peer) { this.peer = peer; }
	
	public void handleMessage(PeerConnection peerconn, PeerMessage msg) {
		Gson gson = new Gson();
		Block receivedBlock = gson.fromJson(msg.getMsgData(), Block.class);
		receivedBlock.setPreviousHash(blockchain.latestBlock().getHash());
		receivedBlock.setHash(receivedBlock.calculateHash(receivedBlock));
		blockchain.addBlock(receivedBlock);
	}
}

/* msg syntax: GMUB */
private class GetBlockchainHandler implements HandlerInterface {
	private Node peer;
	
	public GetBlockchainHandler(Node peer) { this.peer = peer; }
	
	public void handleMessage(PeerConnection peerconn, PeerMessage msg) {
		Gson gson = new Gson();
		String blockchainJSON = gson.toJson(blockchain);
		PeerMessage reply = new PeerMessage(REPLY, blockchainJSON);
		peerconn.sendData(reply);
		System.out.println("handler " + blockchainJSON);
	}
}

/* msg syntax: JOIN pid host port */
private class JoinHandler implements HandlerInterface {
	private Node peer;
	
	public JoinHandler(Node peer) { this.peer = peer; }
			
	public void handleMessage(PeerConnection peerconn, PeerMessage msg) {
		if (peer.maxPeersReached()) {
//			LoggerUtil.getLogger().fine("maxpeers reached " + 
//								peer.getMaxPeers());
			peerconn.sendData(new PeerMessage(ERROR, "Join: " +
								"too many peers"));
			return;
		}
		
		// check for correct number of arguments
		String[] data = msg.getMsgData().split("\\s");
		if (data.length != 3) {
			peerconn.sendData(new PeerMessage(ERROR, "Join: " +
								"incorrect arguments"));
			return;
		}
		
		// parse arguments into PeerInfo structure
		PeerInfo info = new PeerInfo(data[0], data[1],
									 Integer.parseInt(data[2]));
		
		if (peer.getPeer(info.getId()) != null) 
			peerconn.sendData(new PeerMessage(ERROR, "Join: " +
									"peer already inserted"));
		else if (info.getId().equals(peer.getId())) 
			peerconn.sendData(new PeerMessage(ERROR, "Join: " +
									"attempt to insert self"));
		else {
			peer.addPeer(info);
			peerconn.sendData(new PeerMessage(REPLY, "Join: " +
									"peer added: " + info.getId()));
		}
	}
}

/* msg syntax: LIST */
private class ListHandler implements HandlerInterface {
	private Node peer;
	
	public ListHandler(Node peer) { this.peer = peer; }
	
	public void handleMessage(PeerConnection peerconn, PeerMessage msg) {
		peerconn.sendData(new PeerMessage(REPLY, 
				String.format("%d", peer.getNumberOfPeers())));
		for (String pid : peer.getPeerKeys()) {
			peerconn.sendData(new PeerMessage(REPLY, 
					String.format("%s %s %d", pid, peer.getPeer(pid).getHost(),
							peer.getPeer(pid).getPort())));
		}
	}
}

/* msg syntax: NAME */
private class NameHandler implements HandlerInterface {
	private Node peer;
	
	public NameHandler(Node peer) { this.peer = peer; }
	
	public void handleMessage(PeerConnection peerconn, PeerMessage msg) {
		peerconn.sendData(new PeerMessage(REPLY, peer.getId()));
	}
}

/* msg syntax: QUER return-pid key ttl */
private class QueryHandler implements HandlerInterface {
	private VotingNode peer;
	public QueryHandler(VotingNode peer) { this.peer = peer; }
	public void handleMessage(PeerConnection peerconn, PeerMessage msg) {
		String[] data = msg.getMsgData().split("\\s");
		if (data.length != 3) {
			peerconn.sendData(new PeerMessage(ERROR, "Query: incorrect arguments"));
			return;
		}
		
		String ret_pid = data[0].trim();
		String key = data[1].trim();
		int ttl = Integer.parseInt(data[2].trim());
		peerconn.sendData(new PeerMessage(REPLY, "Query: ACK"));
		/* After acknowledging the query, this connection will be closed. A
		 * separate thread will be started to actually perform the task of the
		 * query...
		 */ 
		
		QueryProcessor qp = new QueryProcessor(peer, ret_pid, key, ttl);
		qp.start();
	}
}

private class QueryProcessor extends Thread {
	private VotingNode peer;
	private String ret_pid;
	private String key;
	private int ttl;
	
	public QueryProcessor(VotingNode peer, String ret_pid, 
							String key, int ttl) {
		this.peer = peer;
		this.ret_pid = ret_pid;
		this.key = key;
		this.ttl = ttl;
	}
	
	public void run() {
		/*
		// search through this node's list of files for a filename 
		// containing the key
		for (String filename : peer.files.keySet()) {
			if (filename.toUpperCase().indexOf(key.toUpperCase()) >= 0) {
				String fpid = peer.files.get(filename);
				String[] data = ret_pid.split(":");
				String host = data[0];
				int port = Integer.parseInt(data[1]);
				peer.connectAndSend(new PeerInfo(ret_pid, host, port), 
						QRESPONSE, filename + " " + fpid, true);
				LoggerUtil.getLogger().fine("Sent QRESP " 
						+ new PeerInfo(ret_pid, host, port) 
						+ " " + filename + " " + fpid);
				return;
			}
		}
		*/
		/*
		// will only reach here if key not found... 
		// in which case propagate query to neighbors, if there is still
		// time-to-live for the query
		if (ttl > 0) {
			String msgdata = String.format("%s %s %d", ret_pid, key, ttl - 1);
			for (String nextpid : peer.getPeerKeys())
				peer.sendToPeer(nextpid, QUERY, msgdata, true);
		}*/
	}
}

/* msg syntax: RESP file-name pid */
private class QResponseHandler implements HandlerInterface {
	@SuppressWarnings("unused")
	private Node peer;
	
	public QResponseHandler(Node peer) { this.peer = peer; }
	
	public void handleMessage(PeerConnection peerconn, PeerMessage msg) {
		/*String[] data = msg.getMsgData().split("\\s");
		if (data.length != 2) {
			peerconn.sendData(new PeerMessage(ERROR, "Resp: "
					+ "incorrect arguments"));
			return;
		}
		
		String filename = data[0];
		String pid = data[1];
		if (files.containsKey(filename)) {
			peerconn.sendData(new PeerMessage(ERROR, "Resp: " 
					+ "can't add duplicate file " + filename));
			return;
		}
		
		files.put(filename, pid);
		peerconn.sendData(new PeerMessage(REPLY, "Resp: "
				+ "file info added " + filename));*/
	}
}

/* msg syntax: FGET file-name */
private class FileGetHandler implements HandlerInterface {
	@SuppressWarnings("unused")
	private Node peer;
	
	public FileGetHandler(Node peer) { this.peer = peer; }
	
	public void handleMessage(PeerConnection peerconn, PeerMessage msg) {
		/*String filename = msg.getMsgData().trim();
		if (!files.containsKey(filename)) {
			peerconn.sendData(new PeerMessage(ERROR, "Fget: "
					+ "file not found " + filename));
			return;
		}
		
		byte[] filedata = null;
		try {
			FileInputStream infile = new FileInputStream(filename);
			int len = infile.available();
			filedata = new byte[len];
			infile.read(filedata);
			infile.close();
		}
		catch (IOException e) {
			LoggerUtil.getLogger().info("Fget: error reading file: " + e);
			peerconn.sendData(new PeerMessage(ERROR, "Fget: "
					+ "error reading file " + filename));
			return;
		}
		
		peerconn.sendData(new PeerMessage(REPLY, filedata));*/
	}
}

/* msg syntax: QUIT pid */
private class QuitHandler implements HandlerInterface {
	private Node peer;
	
	public QuitHandler(Node peer) { this.peer = peer; }
	
	public void handleMessage(PeerConnection peerconn, PeerMessage msg) {
		String pid = msg.getMsgData().trim();
		if (peer.getPeer(pid) == null) {
			peerconn.sendData(new PeerMessage(ERROR, "Quit: peer not found: " + pid));
		} 
		else {
			peer.removePeer(pid);
			peerconn.sendData(new PeerMessage(REPLY, "Quit: peer removed: " + pid));
		}
	}
}

private class Router implements RouterInterface {
	private Node peer;
	
	public Router(Node peer) {
		this.peer = peer;
	}
	
	public PeerInfo route(String peerid) {
		if (peer.getPeerKeys().contains(peerid)) return peer.getPeer(peerid);
		else return null;
	}
}


}
