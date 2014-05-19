package net.thesyndicate.utilities;

/**
 * Created by dboolbe on 5/19/14.
 */
public class BencodeString extends BencodeObject implements Comparable<BencodeString> {

    private String value;

    public BencodeString(String value) {
        this.value = value;
    }

    @Override
    public boolean isBencodeString() {
        return true;
    }

    @Override
    public String encode() {
        return value.length() + ":" + value;
    }

    @Override
    public String getAsString() {
        return value;
    }

    @Override
    /***
     * This is needed for NavigableMap to sort correctly for BEncoded Dictionaries.
     */
    public int compareTo(BencodeString bencodeString) {
        return value.compareTo(bencodeString.value);
    }
}
