package se.moosetrail.skola.edan20.labbar.assignment1;

import lppp.chapter5.FileReader;
import lppp.chapter5.Tokenizer;
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

    private void bigram(String[] sentenceWords) {
        System.out.println("w_i \t w:(i+1) \t C(w_i,w_i+1) \t C(w_i) \t P(w_i+1|w_i)");

        for (int i = 0; i < sentenceWords.length; i++) {



            String nextWord = "";
            if(i +1 < sentenceWords.length)
                nextWord = sentenceWords[i+1];

            int occurencesOfWord = countBigram(sentenceWords[i], nextWord);
            Double prob = probabilityOfUnigram(occurencesOfWord);
            sentenceWordProb.add(prob);

            String output = String.format("%s \t %s \t\t %d \t %d \t %d \t %.6f", sentenceWords[i], nextWord, countBigram(sentenceWords[1], nextWord), occurencesOfWord, nbrOfWords, prob);


            System.out.println(output);
        }
    }

    int countBigram(String w1, String w2){
        try {
            return bigramCounts.get(w1 + "\t" + w2);
        }catch (NullPointerException ex){
            return 0;
        }
    }

    float probabilityOfBigram(){


        return -1;
    }
}
