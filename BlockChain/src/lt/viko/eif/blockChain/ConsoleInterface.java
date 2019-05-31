package lt.viko.eif.blockChain;

import java.util.List;

public class ConsoleInterface {
    public static final String SCREEN_START = "BLOCKCHAIN VOTING by Ivan, Eima, and Dohn\n" +
            "1. Connect to an existing wallet\n" +
            "2. Create new wallet \n";
    public static final int CHOICE_CONNECT_TO_EXISTING_WALLET = 1;
    public static final int CHOICE_CREATE_NEW_WALLET = 2;

    public static final String SCREEN_CREATE_WALLET_1 = "BLOCKCHAIN VOTING by Ivan, Eima, and Dohn\n" +
            "Input wallet name (Ctrl-B to cancel):\n";
    public static final String SCREEN_CREATE_WALLET_1_ERROR = "BLOCKCHAIN VOTING by Ivan, Eima, and Dohn\n" +
            "A wallet with that name already exists.\n" +
            "1. OK.\n";
    public static final int CHOICE_OK = 1;
    public static final String SCREEN_CREATE_WALLET_2 = "BLOCKCHAIN VOTING by Ivan, Eima, and Dohn\n" +
            "Your wallet has been successfully created.\n" +
            "1. OK!\n";

    public static final String SCREEN_WALLET_CONNECT_1 = "BLOCKCHAIN VOTING by Ivan, Eima, and Dohn\n" +
            "Input wallet name (Ctrl-B to cancel):\n";
    public static final String SCREEN_WALLET_CONNECT_1_ERROR_1= "BLOCKCHAIN VOTING by Ivan, Eima, and Dohn\n" +
            "There is no wallet with such a name.\n" +
            "1. OK. :/\n";
    public static final String SCREEN_WALLET_CONNECT_1_ERROR_2 = "BLOCKCHAIN VOTING by Ivan, Eima, and Dohn\n" +
            "That wallet is currently in use.\n" +
            "1. OK. :/\n";

    public static final String SCREEN_WALLET = "BLOCKCHAIN VOTING by Ivan, Eima, and Dohn\n" +
            "What do you want to do?\n" +
            "1. Vote\n" +
            "2. See results\n" +
            "3. Log out of wallet\n";
    public static final int CHOICE_VOTE = 1;
    public static final int CHOICE_SEE_RESULTS = 2;
    public static final int CHOICE_LOG_OUT = 3;

    public static final String SCREEN_VOTING_1 = "BLOCKCHAIN VOTING by Ivan, Eima, and Dohn\n" +
            "Please state your personal no. (Ctrl-B to cancel):";
    public static final String SCREEN_VOTING_2 = "BLOCKCHAIN VOTING by Ivan, Eima, and Dohn\n" +
            "Please state your private key (Ctrl-B to cancel):";
    public static final String SCREEN_VOTING_2_ERROR_1 = "BLOCKCHAIN VOTING by Ivan, Eima, and Dohn\n" +
            "Sorry, you have already cast your vote.\n" +
            "1. OK... :(\n";
    public static final String SCREEN_VOTING_2_ERROR_2 = "BLOCKCHAIN VOTING by Ivan, Eima, and Dohn\n" +
            "Sorry, your provided credentials are incorrect.\n" +
            "1. OK... :(\n";
    public static String SCREEN_VOTING_3(List<String> candidateList){
        StringBuilder sb = new StringBuilder();
        sb.append("BLOCKCHAIN VOTING by Ivan, Eima, and Dohn\n" +
                "Pick your candidate (Ctrl-B to cancel):\n");
        int i = 1;
        for(String candidate : candidateList){
            sb.append(i++ + ". " + candidate + "\n");
        }
        return sb.toString();
    }
    public static final String SCREEN_VOTING_3_ERROR = "BLOCKCHAIN VOTING by Ivan, Eima, and Dohn\n" +
            "Sorry, the vote didn't go through. Please try again at a later time.\n" +
            "1. OK... :(\n";
    public static final String SCREEN_VOTING_4 = "BLOCKCHAIN VOTING by Ivan, Eima, and Dohn\n" +
            "Your vote has been cast.\n" +
            "1. OK. :)\n";

    public static final String SCREEN_SEE_RESULTS(List<String> candidateList, List<Integer> voteCountList, int totalVoteCount){
        StringBuilder sb = new StringBuilder();
        sb.append("BLOCKCHAIN VOTING by Ivan, Eima, and Dohn\n" +
                "Total vote count: ...\n" +
                "Candidate Vote # Vote %\n");
        for(int i = 0; i<candidateList.size(); ++i){
            sb.append(candidateList.get(i) + " " + voteCountList.get(i) + " " + String.format("%.2f%%\n",
                    (float)voteCountList.get(i)/totalVoteCount*100));
        }
        sb.append("1. OK!\n");
        return sb.toString();
    }
}
