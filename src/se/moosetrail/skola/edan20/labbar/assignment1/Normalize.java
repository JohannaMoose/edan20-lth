package se.moosetrail.skola.edan20.labbar.assignment1;

import lppp.chapter5.FileReader;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Johanna on 2015-09-06.
 */
public class Normalize {
    public static void main(String[] args) throws IOException {
        FileReader reader = new FileReader();
        String text = reader.readFile(args[0]);
        Normalize normalize = new Normalize();
        String[] words = normalize.run(text);
        for (int i = 0; i < words.length; i++) {
            if(words[i] != null)
                System.out.println(words[i]);
        }
    }


    String[] run(String text) {
        String[] words = getSentences(text);
        words = handleSentences(words);
        return words;
    }

    String[] tokenize(String text){
        return null;
    }

    private String[] getSentences(String text) {
        Pattern pattern = Pattern.compile("([A-Ö][.]*)\\.");
        String[] sentences =  pattern.split(text);
        return sentences;
    }

    private String[] handleSentences(String[] sentneces) {
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
