package lppp.chapter5;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Pierre Nugues on 25/07/15.
 */
public class Tokenizer {

    public static void main(String[] args) throws IOException {
        FileReader reader = new FileReader();
        String text = reader.readFile(args[0]);
        Tokenizer tokenizer = new Tokenizer();
        String[] words = tokenizer.tokenize(text);
        for (int i = 0; i < words.length; i++) {
            System.out.println(words[i]);
        }
    }

    public String[] tokenize(String text) {
        //String [] words = text.split("[\\s\\-,;:!?.’\'«»()–...&‘’“”*—]+");
        //String [] words = text.split("[^a-zåàâäæçéèêëîïôöœßùûüÿA-ZÅÀÂÄÆÇÉÈÊËÎÏÔÖŒÙÛÜŸ’\\-]+");
        //String [] words = text.split("\\W+"); // Not unicode friendly
        String[] words = text.split("\\P{L}+");
        return words;
    }

    String tokenize2(String text) {
        String words = text.replaceAll("\\P{L}+", " ");
        return words;
    }

    public String[] tokenize(String[] text){
        ArrayList<String> toknized = new ArrayList<String>();
        for (int i = 0; i < text.length; i++) {
            String[] part = text[i].split("\\P{L}+");
            for (int j = 0; j < part.length; j++) {
                toknized.add(part[j]);
            }
        }

        return toknized.toArray(new String[toknized.size()]);
    }
}
