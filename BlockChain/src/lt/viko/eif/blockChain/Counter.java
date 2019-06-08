package lt.viko.eif.blockChain;

import lt.viko.eif.blockChain.BlockChain.BlochChain;

import java.util.ArrayList;

public class Counter {

   private int IngridaŠimonytė;
   private int GitanasNausėda;
   private int SauliusSkvernelis;
   private int VytenisPovilasAndriukaitis;
   private int ArvydasJuozaitis;
   private int ValdemarasTomaševskis;
   private int MindaugasPuidokas;
   private int NaglisPuteikis;
   private int ValentinasMazuronis;

    private float IngridaPercent;
    private float GitanasPercent;
    private float SauliusPercent;
    private float VytenisPovilasPercent;
    private float ArvydasPercent;
    private float ValdemarasPercent;
    private float MindaugasPercent;
    private float NaglisPercent;
    private float ValentinasPercent;


    public float getIngridaPercent() {
        return IngridaPercent;
    }

    public void setIngridaPercent(float ingridaPercent) {
        IngridaPercent = ingridaPercent;
    }

    public float getGitanasPercent() {
        return GitanasPercent;
    }

    public void setGitanasPercent(float gitanasPercent) {
        GitanasPercent = gitanasPercent;
    }

    public float getSauliusPercent() {
        return SauliusPercent;
    }

    public void setSauliusPercent(float sauliusPercent) {
        SauliusPercent = sauliusPercent;
    }

    public float getVytenisPovilasPercent() {
        return VytenisPovilasPercent;
    }

    public void setVytenisPovilasPercent(float vytenisPovilasPercent) {
        VytenisPovilasPercent = vytenisPovilasPercent;
    }

    public float getArvydasPercent() {
        return ArvydasPercent;
    }

    public void setArvydasPercent(float arvydasPercent) {
        ArvydasPercent = arvydasPercent;
    }

    public float getValdemarasPercent() {
        return ValdemarasPercent;
    }

    public void setValdemarasPercent(float valdemarasPercent) {
        ValdemarasPercent = valdemarasPercent;
    }

    public float getMindaugasPercent() {
        return MindaugasPercent;
    }

    public void setMindaugasPercent(float mindaugasPercent) {
        MindaugasPercent = mindaugasPercent;
    }

    public float getNaglisPercent() {
        return NaglisPercent;
    }

    public void setNaglisPercent(float naglisPercent) {
        NaglisPercent = naglisPercent;
    }

    public float getValentinasPercent() {
        return ValentinasPercent;
    }

    public void setValentinasPercent(float valentinasPercent) {
        ValentinasPercent = valentinasPercent;
    }

    public int getIngridaŠimonytė() {
        return IngridaŠimonytė;
    }

    public void setIngridaŠimonytė(int ingridaŠimonytė) {
        IngridaŠimonytė = ingridaŠimonytė;
    }

    public int getGitanasNausėda() {
        return GitanasNausėda;
    }

    public void setGitanasNausėda(int gitanasNausėda) {
        GitanasNausėda = gitanasNausėda;
    }

    public int getSauliusSkvernelis() {
        return SauliusSkvernelis;
    }

    public void setSauliusSkvernelis(int sauliusSkvernelis) {
        SauliusSkvernelis = sauliusSkvernelis;
    }

    public int getVytenisPovilasAndriukaitis() {
        return VytenisPovilasAndriukaitis;
    }

    public void setVytenisPovilasAndriukaitis(int vytenisPovilasAndriukaitis) {
        VytenisPovilasAndriukaitis = vytenisPovilasAndriukaitis;
    }

    public int getArvydasJuozaitis() {
        return ArvydasJuozaitis;
    }

    public void setArvydasJuozaitis(int arvydasJuozaitis) {
        ArvydasJuozaitis = arvydasJuozaitis;
    }

    public int getValdemarasTomaševskis() {
        return ValdemarasTomaševskis;
    }

    public void setValdemarasTomaševskis(int valdemarasTomaševskis) {
        ValdemarasTomaševskis = valdemarasTomaševskis;
    }

    public int getMindaugasPuidokas() {
        return MindaugasPuidokas;
    }

    public void setMindaugasPuidokas(int mindaugasPuidokas) {
        MindaugasPuidokas = mindaugasPuidokas;
    }

    public int getNaglisPuteikis() {
        return NaglisPuteikis;
    }

    public void setNaglisPuteikis(int naglisPuteikis) {
        NaglisPuteikis = naglisPuteikis;
    }

    public int getValentinasMazuronis() {
        return ValentinasMazuronis;
    }

    public void setValentinasMazuronis(int valentinasMazuronis) {
        ValentinasMazuronis = valentinasMazuronis;
    }

    public int getVotes(BlochChain blockChain, String candidate){
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

    public int getTotalVotes(BlochChain blockChain){

        ArrayList<String> Candidates = new ArrayList<String>();
        int total = 0;

        Candidates.add("Ingrida Šimonytė");
        Candidates.add("Gitanas Nausėda");
        Candidates.add("Saulius Skvernelis");
        Candidates.add("Vytenis Povilas Andriukaitis");
        Candidates.add("Arvydas Juozaitis");
        Candidates.add("Valdemaras Tomaševskis");
        Candidates.add("Mindaugas Puidokas");
        Candidates.add("Naglis Puteikis");
        Candidates.add("Valentinas Mazuronis");


        for (String C: Candidates)
        {
           total += getVotes(blockChain, C);

        }
        return total;
    }

    public void setVotes(BlochChain blockChain){
        String IS = "Ingrida Šimonytė";
        String GN = "Gitanas Nausėda";
        String SS = "Saulius Skvernelis";
        String VPA = "Vytenis Povilas Andriukaitis";
        String AJ = "Arvydas Juozaitis";
        String VT = "Valdemaras Tomaševskis";
        String MP = "Mindaugas Puidokas";
        String NP = "Naglis Puteikis";
        String VM = "Valentinas Mazuronis";


        setIngridaŠimonytė(getVotes(blockChain, IS));
        setArvydasJuozaitis(getVotes(blockChain, AJ));
        setGitanasNausėda(getVotes(blockChain, GN));
        setMindaugasPuidokas(getVotes(blockChain, MP));
        setNaglisPuteikis(getVotes(blockChain, NP));
        setSauliusSkvernelis(getVotes(blockChain, SS));
        setValdemarasTomaševskis(getVotes(blockChain, VT));
        setValentinasMazuronis(getVotes(blockChain, VM));
        setVytenisPovilasAndriukaitis(getVotes(blockChain, VPA));

    }

    public void setPercent(BlochChain blockChain){
        String IS = "Ingrida Šimonytė";
        String GN = "Gitanas Nausėda";
        String SS = "Saulius Skvernelis";
        String VPA = "Vytenis Povilas Andriukaitis";
        String AJ = "Arvydas Juozaitis";
        String VT = "Valdemaras Tomaševskis";
        String MP = "Mindaugas Puidokas";
        String NP = "Naglis Puteikis";
        String VM = "Valentinas Mazuronis";


        setIngridaPercent(Percent(blockChain, IS));
        setGitanasPercent(Percent(blockChain, GN));
        setSauliusPercent(Percent(blockChain, SS));
        setVytenisPovilasPercent(Percent(blockChain, VPA));
        setArvydasPercent(Percent(blockChain, AJ));
        setValdemarasPercent(Percent(blockChain, VT));
        setMindaugasPercent(Percent(blockChain, MP));
        setNaglisPercent(Percent(blockChain, NP));
        setValentinasPercent(Percent(blockChain, VM));

    }

    public float Percent(BlochChain blockChain, String candidate){
        float total = getTotalVotes(blockChain);
        float score = getVotes(blockChain, candidate);
        float percentage;
        percentage = Math.round(score * 100/ total);
        return percentage;
    }

}
