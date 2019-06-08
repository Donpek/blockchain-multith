package lt.viko.eif.blockChain;

import lt.viko.eif.blockChain.BlockChain.BlochChain;

import static org.junit.Assert.assertEquals;

/*
public class Wallet {
    private static int getNumber(Scanner scan){
        String input = scan.nextLine();
        int result = 0;
        if(input == ""){

        }else{
            try{
                result = Integer.parseInt(input.substring(0,1));
            } catch (Exception e) {
            }
        }
        return result;
    }

    private static void choiceStart(Scanner scan, int choice, String input){
        System.out.println(SCREEN_START);
        choice = getNumber(scan);

        if(choice == CHOICE_CONNECT_TO_EXISTING_WALLET){
            System.out.println(SCREEN_WALLET_CONNECT_1);
            input = scan.nextLine(); // wallet name

            // TODO check if given input matches any existing wallet name
            boolean walletExists = true; // STUB
            // TODO check if matched wallet is not currently in use
            boolean walletInUse = false; // STUB
            if(walletExists){
                if(walletInUse){
                    choice = 0;
                    while(choice != CHOICE_OK){
                        System.out.println(SCREEN_WALLET_CONNECT_1_ERROR_2);
                        choice = getNumber(scan);              }
                    choiceStart(scan, choice, input); return;
                }else{
                    choiceWallet(scan, choice, input); return;
                }
            }else{
                choice = 0;
                while(choice != CHOICE_OK){
                    System.out.println(SCREEN_WALLET_CONNECT_1_ERROR_1);
                    choice = getNumber(scan);        }
                choiceStart(scan, choice, input); return;
            }
        }else if(choice == CHOICE_CREATE_NEW_WALLET){
            System.out.println(SCREEN_CREATE_WALLET_1);
            input = scan.nextLine(); // wallet name

            // TODO check if given input matches any existing wallet name
            boolean walletExists = true; // STUB
            if(walletExists){
                // TODO create wallet and add it to the database

                choice = 0;
                while(choice != CHOICE_OK){
                    System.out.println(SCREEN_CREATE_WALLET_2);
                    choice = getNumber(scan);
                }
                choiceStart(scan, choice, input); return;
            }else{
                choice = 0;
                while(choice != CHOICE_OK){
                    System.out.println(SCREEN_CREATE_WALLET_1_ERROR);
                    choice = getNumber(scan);
                }
                choiceStart(scan, choice, input); return;
            }
        }else{
            choiceStart(scan, choice, input); return;
        }
    }

    private static void choiceWallet(Scanner scan, int choice, String input){
        System.out.println(SCREEN_WALLET);
        choice = getNumber(scan);

        // TODO retrieve candidate list from genesis block
        List<String> candidateList = new ArrayList<>(); // STUB
        candidateList.add("Nauseda");
        candidateList.add("Simonyte");
        if(choice == CHOICE_VOTE){
            String personalNo; String privateKey;

            System.out.println(SCREEN_VOTING_1);
            input = scan.nextLine(); // voter's personal no.
            personalNo = input;

            System.out.println(SCREEN_VOTING_2);
            input = scan.nextLine(); // voter's private key
            privateKey = input;

            // TODO voter authentification
            boolean credentialsValid = true; // STUB
            // TODO check if voter has already voted
            boolean hasVoted = false; // STUB
            if(credentialsValid){
                if(hasVoted){
                    choice = 0;
                    while(choice != CHOICE_OK){
                        System.out.println(SCREEN_VOTING_2_ERROR_1);
                        choice = getNumber(scan);            }
                    choiceWallet(scan, choice, input); return;
                }else {
                    System.out.println(SCREEN_VOTING_3(candidateList));
                    choice = getNumber(scan); // candidate no.

                    if (choice <= 0 || choice > candidateList.size()) {
                        choiceWallet(scan, choice, input);
                        return;
                    }
                    // TODO create new block and fill it with the voter's choice as well as other required data
                    Block vote = new Block(candidateList.get(choice - 1), "HASH"); // STUB
                    // TODO send block to other wallets to verify it and get their results
                    // boolean voteIsValid = broadcastVoteForVerification(vote);
                    boolean voteIsValid = true; // STUB
                    // TODO if verification successful, add block to synchronization queue
                    if (voteIsValid) {
                        // addVoteToSynchQueue(vote);
                        */
/* TODO verify that the first block in the synch queue is the same block for all nodes
                            then add it to all of their blockchains *//*

                        // TODO (in voter database) mark that the voter has voted
                        choice = 0;
                        while (choice != CHOICE_OK) {
                            System.out.println(SCREEN_VOTING_4);
                            choice = getNumber(scan);
                        }
                        choiceWallet(scan, choice, input);
                        return;
                    } else {
                        choice = 0;
                        while (choice != CHOICE_OK) {
                            System.out.println(SCREEN_VOTING_3_ERROR);
                            choice = getNumber(scan);
                        }
                        choiceWallet(scan, choice, input);
                        return;
                    }
                }
            }else{
                choice = 0;
                while(choice != CHOICE_OK){
                    System.out.println(SCREEN_VOTING_2_ERROR_2);
                    choice = getNumber(scan);
                }
                choiceWallet(scan, choice, input); return;
            }
        }else if(choice == CHOICE_SEE_RESULTS){
            // TODO calculate vote results by going through the blockchain
            List<Integer> voteCountList = new ArrayList<>(); // STUB
            voteCountList.add(5);
            voteCountList.add(7);

            int totalVoteCount = 0;
            for(int voteCount : voteCountList){
                totalVoteCount += voteCount;
            }

            choice = 0;
            while(choice != CHOICE_OK){
                System.out.println(SCREEN_SEE_RESULTS(candidateList, voteCountList, totalVoteCount));
                choice = getNumber(scan);
            }
            choiceWallet(scan, choice, input); return;
        }else if(choice == CHOICE_LOG_OUT) {
            choiceStart(scan, choice, input); return;
        } else{
            choiceWallet(scan, choice, input); return;
        }
    }

    public static void main(String[] args){
        // TODO generate file structure if not present
        Scanner scan = new Scanner(System.in);
        int choice = 0; String input = "b";

        choiceStart(scan, choice, input);
    }
}
*/

public class Wallet {

    public static void main(String[] args){
        Counter counter = new Counter();
        BlochChain blockchain = new BlochChain();
        blockchain.addBlock(blockchain.newBlock("Ingrida Šimonytė"));
        blockchain.addBlock(blockchain.newBlock("Vytenis Povilas Andriukaitis"));
        blockchain.addBlock(blockchain.newBlock("Vytenis Povilas Andriukaitis"));
        System.out.println(blockchain);
        int a = counter.getVotes(blockchain, "Vytenis Povilas Andriukaitis");
        System.out.println(a);

    }
}