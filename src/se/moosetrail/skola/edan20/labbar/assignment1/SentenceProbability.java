package se.moosetrail.skola.edan20.labbar.assignment1;

import lppp.chapter5.FileReader;
import lppp.chapter5.Tokenizer;
import lppp.chapter5.WordCounter;

import java.io.IOException;
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
    Map<String, Integer> unigramCounts;
    Map<String, Integer> bigramCounts;

    void run(String file) throws IOException {
        FileReader reader = new FileReader();
        String text = reader.readFile(file);
        Normalize normalize = new Normalize();
        words = normalize.run(text);
        Tokenizer tokenizer = new Tokenizer();
        words = tokenizer.tokenize(words);
        bigramCounts = wc.countBigrams(words);
        unigramCounts = wc.count(words);

        Scanner scanner = new Scanner(System.in).useDelimiter("\\n");
        System.out.print("Skriv in den mening som ska analyseras: ");
        String sentence = scanner.nextLine();
        String[] sentenceWords = sentence.split(" ");
        System.out.print("Vill du beräkna sannolikhet på unigram (u) eller biagram (b)?");
        String type = scanner.nextLine();

        if(Objects.equals(type, "u"))
            unigram(sentenceWords);
        else if(Objects.equals(type, "b"))
            bigram(sentenceWords);
        else
            System.out.println("Kännde inte igen det val som gjordes");
    }

    private void unigram(String[] sentenceWords) {
        System.out.println("w_i \t C(w_i) \t #words \t P(w_i)");
        float probForSentence = 0;
        for (int i = 0; i < sentenceWords.length; i++) {
            int occurrencesOfWord = countOfWord(sentenceWords[i]);
            float prob = probabilityOfUnigram(occurrencesOfWord);
            probForSentence = probForSentence + prob;
            String output = String.format("%s \t %d \t %d \t %.6f", sentenceWords[i], occurrencesOfWord, words.length, prob);
            System.out.println(output);
        }

        System.out.println("Sannolikhet för meningen: " + probForSentence);
    }

    private void bigram(String[] sentenceWords) {
        System.out.println("w_i \t w:(i+1) \t C(w_i,w_i+1) \t C(w_i) \t P(w_i+1|w_i)");
        float probForSentence = 0;
        for (int i = 0; i < sentenceWords.length; i++) {
            int occurencesOfWord = countOfWord(sentenceWords[i]);
            float prob = probabilityOfUnigram(occurencesOfWord);
            probForSentence = probForSentence + prob;
            String nextWord = "";
            if(i +1 < sentenceWords.length)
                nextWord = sentenceWords[i+1];

            String output = String.format("%s \t %s \t %d \t %d \t %d \t %.6f", sentenceWords[i], nextWord, countBigram(sentenceWords[1], nextWord), occurencesOfWord, words.length, prob);
            System.out.println(output);
        }

        System.out.println("Sannolikhet för meningen: " + probForSentence);
    }

    int countOfWord(String word){
        try {
            return unigramCounts.get(word);
        }catch (NullPointerException ex){
            return 0;
        }
    }

    int countBigram(String w1, String w2){
        try {
            return bigramCounts.get(w1 + "\t" + w2);
        }catch (NullPointerException ex){
            return 0;
        }
    }

    float probabilityOfUnigram(int nbrOfOccurencesOfWord){
        float nbr = nbrOfOccurencesOfWord;
        float total = words.length;
        return nbr/ total;
    }

    float probabilityOfBigram(){


        return -1;
    }
}
