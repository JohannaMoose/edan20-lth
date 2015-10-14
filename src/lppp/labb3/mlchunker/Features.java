package lppp.labb3.mlchunker;

import lppp.labb3.format.WordCoNLL2000;

/**
 * Created by Johanna on 2015-09-23.
 */
// This class extracts the baseline feature: the part of speech and the class, the chunk tag.
public class Features {

    String ppos;
    String ppos_1;
    String ppos1;
    String chunk;

    public Features(WordCoNLL2000 word) {
        ppos = word.getPpos();
        chunk = word.getChunk();
    }

    public Features(String ppos, String chunk, String ppos_1, String ppos1) {
        this.ppos = ppos;
        this.chunk = chunk;
        this.ppos_1 = ppos_1;
        this.ppos1 = ppos1;
    }

    public boolean equals(Object features) {
        String signature1 = getPpos() + getChunk();
        String signature2 = ((Features) features).getPpos() + ((Features) features).getChunk();
        return signature1.equals(signature2);
    }

    public int hashCode() {
        return (getPpos() + getChunk()).hashCode();
    }

    public String getPpos() {
        return ppos;
    }

    public String getPpos1() {
        return ppos1;
    }

    public String getPpos_1() {
        return ppos_1;
    }

    public String getChunk() {
        return chunk;
    }

    public void setChunk(String chunk) {
        this.chunk = chunk;
    }
}
