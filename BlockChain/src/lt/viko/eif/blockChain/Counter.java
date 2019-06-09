package lt.viko.eif.blockChain;

import lt.viko.eif.blockChain.BlockChain.BlochChain;

public class Counter {

    public static int getVotes(BlochChain blockChain, String candidate){
        String chain = blockChain.toString();

        int index = chain.indexOf(candidate);
        int count = 0;
        while (index != -1) {
            count++;
            chain = chain.substring(index + 1);
            index = chain.indexOf(candidate);
        }
        return count;
    }
}
