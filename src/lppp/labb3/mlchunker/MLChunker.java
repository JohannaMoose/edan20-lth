package lppp.labb3.mlchunker;

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

    public void writeARFF(String file) throws IOException {
        ReaderWriterCoNLL2000 reader = new ReaderWriterCoNLL2000();
        reader.saveARFF(new File(file), sentenceList);
    }

    public void tag() {
        wekaGlue = new WekaGlue();
        wekaGlue.create(Constants.ARFF_MODEL, Constants.ARFF_DATA);
        for (List<WordCoNLL2000> sent : sentenceList) {
            for (WordCoNLL2000 word : sent) {
                String[] features = new String[1];
                features[0] = word.getPpos();
                word.setChunk(wekaGlue.classify(features));
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
            chunker.extractBaselineFeatures();
            chunker.writeARFF(Constants.ARFF_DATA);
        } else if (args[0].equals("-tag")) {
            chunker.load(Constants.TEST_SET_2000);
            chunker.tag();
            chunker.save(Constants.TEST_SET_PREDICTED_2000);
        } else {
            System.out.println("Usage: java mlchunker.MLChunker (-train|-tag)");
        }
    }
}
