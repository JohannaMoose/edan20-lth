package se.moosetrail.skola.edan20.labbar.assignment4;

/**
 * Created by Johanna on 2015-10-06.
 */
public class TripleStruct implements Comparable<TripleStruct> {
    Triple t;
    int freq;

    TripleStruct(Triple t, int freq) {
        this.t = t;
        this.freq = freq;
    }

    public int getFrequency() {
        return this.freq;
    }

    public Triple getTriple() {
        return this.t;
    }

    @Override
    public int compareTo(TripleStruct t) {
        return (t.getFrequency() - freq);
    }
}