package se.moosetrail.skola.edan20.labbar.assignment6.guide;


import se.moosetrail.skola.edan20.labbar.assignment6.parser.ParserState;
import se.moosetrail.skola.edan20.labbar.assignment6.wekaglue.WekaGlue;

/**
 *
 * @author Pierre Nugues
 */
public class Guide4 extends Guide {

    public Guide4(WekaGlue wekaModel, ParserState parserState) {
        super(wekaModel, parserState);
    }
    // This is a simple oracle that uses the top and second in the stack and first and second in the queue + the Booleans

    public String predict() {
        Features feats = extractFeatures();
        String[] features = new String[6];
        features[0] = feats.getTopPostagStack();
        features[1] = feats.getSecondPostagStack();
        features[2] = feats.getFirstPostagQueue();
        features[3] = feats.getSecondPostagQueue();
        features[4] = String.valueOf(feats.getCanLA());
        features[5] = String.valueOf(feats.getCanRE());
        /* for (int i=0; i<6; i++) { */
        /*     System.out.println(features[i]); */
        /* } */
        return wekaModel.classify(features);
    }
}
