package se.moosetrail.skola.edan20.labbar.assignment1;

import lppp.chapter5.FileReader;
import lppp.chapter5.WordCounter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

/**
 * Created by Johanna on 2015-09-06.
 */
public class SentenceProbability {
    public static void main(String[] args) throws IOException {


       SentenceProbability prob = new SentenceProbability();
        prob.run(args[0]);

        System.out.println("Programmet avslutat");
    }


    WordCounter wc = new WordCounter();
    String[] words;
    int nbrOfWords;
    Map<String, Integer> unigramCounts;
    Map<String, Integer> bigramCounts;
    ArrayList<Double> sentenceWordProb = new ArrayList<Double>();

    void run(String file) throws IOException {
        FileReader reader = new FileReader();
        String text = reader.readFile(file);
        Normalizer normalizer = new Normalizer();
        words = normalizer.tokenize(text);
        bigramCounts = wc.countBigrams(words);
        unigramCounts = wc.count(words);
        nbrOfWords =  words.length;

        Scanner scanner = new Scanner(System.in).useDelimiter("\\n");
        System.out.print("Skriv in den mening som ska analyseras: ");
        String sentence = scanner.nextLine().toLowerCase();
        String[] sentenceWords = normalizer.tokenize(sentence);
        System.out.print("Vill du beräkna sannolikhet på unigram (u) eller biagram (b)?");
        String type = scanner.nextLine();

        if(Objects.equals(type, "u"))
            unigram(sentenceWords);
        else if(Objects.equals(type, "b"))
            bigram(sentenceWords);

        System.out.println("-----------------------------------------");
        System.out.println("Sannolikhet för meningen: " + calculateSentenceProbability());
        System.out.println("Entropy för meningen: " + calculateEntropy(sentenceWords));
        System.out.println("Perplexity för meningen: " + calulatePerplexity(sentenceWords));
    }

    private void unigram(String[] sentenceWords) {
        System.out.println("w_i \t C(w_i)\t #words \t P(w_i)");
        System.out.println("-----------------------------------------");

        for (int i = 0; i < sentenceWords.length; i++) {
            int occurrencesOfWord = countOfWord(sentenceWords[i]);
            Double prob = probabilityOfUnigram(occurrencesOfWord);
            sentenceWordProb.add(prob);

            printInformationAboutWord(sentenceWords[i], occurrencesOfWord, prob);
        }
    }

    int countOfWord(String word){
        try {
            return unigramCounts.get(word);
        }catch (NullPointerException ex){
            return 0;
        }
    }

    Double probabilityOfUnigram(int nbrOfOccurencesOfWord){
        Double nbr = new Double(nbrOfOccurencesOfWord);
        Double total = new Double(nbrOfWords);
        return nbr/ total;
    }

    private void printInformationAboutWord(String sentenceWord, int occurrencesOfWord, Double prob) {
        String output;
        if(Objects.equals(sentenceWord, "<s>"))
            output = "<s> \t - \t\t - \t\t\t -";
        else if(sentenceWord.length() > 2)
            output= String.format("%s \t %d \t %d \t %.6f", sentenceWord, occurrencesOfWord, nbrOfWords, prob);
        else
            output= String.format("%s \t\t %d \t %d \t %.6f", sentenceWord, occurrencesOfWord, nbrOfWords, prob);

        System.out.println(output);
    }

    private Double calculateSentenceProbability() {
        Double prob = sentenceWordProb.get(1);
        for (int i = 2; i < sentenceWordProb.size(); i++) {
            prob = prob * sentenceWordProb.get(i);
        }
        return prob;
    }

    private Double calculateEntropy(String[] sentenceWords){
        Double log = Math.log(calculateSentenceProbability()) / Math.log(2);
        Double mod = -1.0/sentenceWords.length;
        return log*mod;
    }

    private Double calulatePerplexity   (String[] sentenceWords){


        Double entr = calculateEntropy(sentenceWords);
        Double per = Math.pow(2.0,entr);
        return per;
    }

    private void bigram(String[] sentenceWords) {
        System.out.println("w_i \tw:(i+1) \tC(w_i,w_i+1) \tC(w_i) \t\tP(w_i+1|w_i)");

        for (int i = 0; i < sentenceWords.length-1; i++) {
            String nextWord = "";
            if(i +1 < sentenceWords.length)
                nextWord = sentenceWords[i+1];

            int occurencesOfWord = countBigram(sentenceWords[i], nextWord);
            Double prob = probabilityOfBigram(sentenceWords[i], nextWord);
            boolean hasBackof = false;
            if(occurencesOfWord == 0){
                hasBackof = true;
                prob = probabilityOfUnigram(countOfWord(nextWord));
            }
            sentenceWordProb.add(prob);

            StringBuilder sb = new StringBuilder();
            if(sentenceWords[i].length() > 2)
                sb.append(sentenceWords[i]+ " \t");
            else
                sb.append(sentenceWords[i]+ " \t\t");
            if(nextWord.length() > 2)
                sb.append(nextWord+ " \t\t");
            else
                sb.append(nextWord+ " \t\t\t");
            sb.append(occurencesOfWord + " \t\t\t");
            if(occurencesOfWord < 100)
                sb.append("\t");
            sb.append(countOfWord(sentenceWords[i]) + " \t\t");
            if(countOfWord(sentenceWords[i]) < 100)
                sb.append("\t");
            if(hasBackof)
                sb.append("backoff: ");
            sb.append(prob);

            String output = String.format("%s \t %s \t\t %d \t %d \t %d \t %.6f", sentenceWords[i], nextWord, countBigram(sentenceWords[1], nextWord), occurencesOfWord, nbrOfWords, prob);

            System.out.println(sb.toString());
        }
    }

    int countBigram(String w1, String w2){
        try {
            return bigramCounts.get(w1 + "\t" + w2);
        }catch (NullPointerException ex){
            return 0;
        }
    }

    Double probabilityOfBigram(String w1, String w2) {
        Double nbr = new Double(countBigram(w1, w2));
        Double total = new Double(nbrOfWords);
        Double probw1 = probabilityOfUnigram(countOfWord(w1));
        return (nbr / total) / probw1;
    }
}
