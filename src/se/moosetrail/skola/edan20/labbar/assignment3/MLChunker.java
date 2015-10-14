package se.moosetrail.skola.edan20.labbar.assignment3;

import lppp.labb3.format.Constants;
import lppp.labb3.format.Corpus;
import lppp.labb3.format.ReaderWriterCoNLL2000;
import lppp.labb3.format.WordCoNLL2000;
import lppp.labb3.wekaglue.WekaGlue;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Johanna on 2015-09-23.
 */
public class MLChunker extends Corpus {

    WekaGlue wekaGlue;

    public MLChunker() {
    }

    public void writeMultiARFF(String file) throws IOException {
        ReaderWriterCoNLL2000 reader = new ReaderWriterCoNLL2000();
        reader.saveARFFMulti(new File(file), featureList);
    }

    public void tag() {
        wekaGlue = new WekaGlue();
        wekaGlue.create(Constants.ARFF_MODEL, Constants.ARFF_DATA);
        String previousWord = null, currWord = null, nextWord = null;
        String[] features = new String[3];
        WordCoNLL2000 currWordStruct = null;
        for (List<WordCoNLL2000> sent : sentenceList) {
            for (WordCoNLL2000 word : sent) {
                nextWord = word.getPpos();
                if (currWord != null && previousWord != null && nextWord != null) {
                    features[0] = previousWord;
                    features[1] = currWord;
                    features[2] = nextWord;
                    currWordStruct.setChunk(wekaGlue.classify(features));
                }
                previousWord = currWord;
                currWord = nextWord;
                currWordStruct = word;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        MLChunker chunker = new MLChunker();
        if (args.length < 1) {
            System.out.println("Usage: java mlchunker.MLChunker (-train|-tag)");
            System.exit(0);
        }
        if (args[0].equals("-train")) {
            chunker.load(Constants.TRAINING_SET_2000);
            chunker.extractFeatures();
            chunker.writeMultiARFF(Constants.ARFF_DATA);
        } else if (args[0].equals("-tag")) {
            chunker.load(Constants.TEST_SET_2000);
            chunker.tag();
            chunker.save(Constants.TEST_SET_PREDICTED_2000);
        } else {
            System.out.println("Usage: java mlchunker.MLChunker (-train|-tag)");
        }
    }
}
