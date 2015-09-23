package se.moosetrail.skola.edan20.labbar.assignment3;

import lppp.labb3.format.Constants;
import lppp.labb3.format.Corpus;
import lppp.labb3.format.WordCoNLL2000;
import lppp.labb3.mlchunker.Features;

import java.io.IOException;
import java.util.*;

/**
 * Created by Johanna on 2015-09-23.
 */
public class Chunker extends Corpus {

    Map<String, Integer> posCnt;
    Map<String, Integer> chunkCnt;
    Map<Features, Integer> associationFreq;
    Map<String, String> bestAssociations;
    Set<String> uniquePosTags;
    Set<String> uniqueChunkTags;

    // This method counts the associations (POS, Chunk). It uses a hashmap.
    public void countAssoc() {
        associationFreq = new HashMap<Features, Integer>();
        for (Features feat : featureList){
            if(associationFreq.get(feat) == null)
                associationFreq.put(feat, 1);
            else
                associationFreq.put(feat, associationFreq.get(feat) + 1);
        }
    }

    public void printAssocCounts() {
        Set<Features> feats = new HashSet<Features>(associationFreq.keySet());
        for (Features feat : feats) {
            System.out.println(feat.getPpos() + "\t" + feat.getChunk() + "\t" + associationFreq.get(feat));
        }
    }

    public void printBestAssoc() {
        Set<String> pposs = new HashSet<String>(bestAssociations.keySet());
        int counte = 1;
        for (String ppos : pposs) {
            System.out.println(counte + ". " + ppos + "\t\t" + bestAssociations.get(ppos));
            counte++;
        }
    }

    public void getUniqueTags() {
        uniquePosTags = new HashSet<String>();
        uniqueChunkTags = new HashSet<String>();
        for (Features feat : featureList) {
            uniquePosTags.add(feat.getPpos());
            uniqueChunkTags.add(feat.getChunk());
        }
    }

    // This method selects the best (most frequent) association: POS--Chunk
    public void selectBestAssoc() {
        bestAssociations = new HashMap<String, String>();
        System.out.println(uniquePosTags.size() + " POS tags\t" + uniqueChunkTags.size() + " chunk tags");

        for (String pos : uniquePosTags){
            int highestCount = 0;
            String chunk = null;
            Iterator it = associationFreq.entrySet().iterator();
            while (it.hasNext()){
                Map.Entry pair = (Map.Entry)it.next();
                Features fet = (Features)pair.getKey();
                int count = (int) pair.getValue();
                if(fet.getPpos() == pos && count > highestCount){
                    highestCount = count;
                    chunk = fet.getChunk();
                }
            }
            bestAssociations.put(pos, chunk);
        }
    }

    // This method tags the words with their chunk.
    public void tag() {
        for (List<WordCoNLL2000> sent : sentenceList) {
            for (WordCoNLL2000 word : sent) {
                String chunk = bestAssociations.get(word.getPpos());
                word.setChunk(chunk);
                String str = "";
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Chunker chunker = new Chunker();
        chunker.load(Constants.TRAINING_SET_2000);
        chunker.extractBaselineFeatures();
        chunker.getUniqueTags();
        chunker.countAssoc(); // This method counts the associations (POS, Chunk). It uses a hashmap.
        //chunker.printAssocCounts();
        chunker.selectBestAssoc(); // This method selects the best (most frequent) association: POS--Chunk
        chunker.printBestAssoc();
        chunker.load(Constants.TEST_SET_2000);
        chunker.tag(); // This method tags the words with their chunk.
        chunker.save(Constants.TEST_SET_PREDICTED_2000);
        //chunker.printFeatures();
    }
}
