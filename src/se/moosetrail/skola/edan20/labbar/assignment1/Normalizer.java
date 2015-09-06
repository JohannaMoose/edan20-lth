package se.moosetrail.skola.edan20.labbar.assignment1;

import lppp.chapter5.FileReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Johanna on 2015-09-06.
 */
public class Normalizer {
    public static void main(String[] args) throws IOException {
        FileReader reader = new FileReader();
        String text = reader.readFile(args[0]);
        Normalizer normalizer = new Normalizer();
        String[] words = normalizer.normalize(text);
        for (int i = 0; i < words.length; i++) {
            if(words[i] != null)
                System.out.println(words[i]);
        }
    }


    String[] normalize(String text) {
        String[] words = getSentences(text);
        words = normalizeSentnece(words);
        return words;
    }

    String[] tokenize(String text){
        String[] sentences = getSentences(text);

        ArrayList<String> tokens = new ArrayList<String>();
        int counter = 0;
        for (int i = 0; i < sentences.length; i++) {
            String sentence = sentences[i].toLowerCase();
            sentence = sentence.replaceAll("\n", "");

            if(sentence != null && !sentence.isEmpty() && sentence.trim().length() > 0) {
                String[] parts = sentence.split("\\P{L}+");
                tokens.add("<s>");
                for (int j = 0; j < parts.length; j++) {
                    tokens.add(parts[j]);
                }
                tokens.add("</s>");
            }
        }

        return tokens.toArray(new String[tokens.size()]);
    }

    private String[] getSentences(String text) {
        Pattern pattern = Pattern.compile("(?<=[.?!;])\\s+(?=\\p{Lu})");
        String[] sentences =  pattern.split(text);
        return sentences;
    }

    private String[] normalizeSentnece(String[] sentneces) {
        String[] handledSentences = new String[sentneces.length];
        int counter = 0;
        for (int i = 0; i < sentneces.length; i++) {
            String sentence = sentneces[i];
            sentence = sentence.replaceAll("\n", "");
            if(sentence != null && !sentence.isEmpty() && sentence.trim().length() > 0) {
                handledSentences[counter] = "<s>" + sentence.toLowerCase() + "</s>";
                counter++;
            }
        }

        return handledSentences;
    }
}
