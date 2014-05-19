package net.thesyndicate.utilities;

import java.util.List;
import java.util.Map;

/**
 * Created by dboolbe on 5/12/14.
 */
public class BencodeObject implements BencodeObjectInterface {

    @Override
    public boolean isBencodeInteger() {
        return false;
    }

    @Override
    public boolean isBencodeString() {
        return false;
    }

    @Override
    public boolean isBencodeList() {
        return false;
    }

    @Override
    public boolean isBencodeDictionary() {
        return false;
    }

    @Override
    public String encode() {
        throw new RuntimeException("BencodeObject cannot be encoded.");
    }

    @Override
    public int getAsInteger() {
        throw new ClassCastException("BencodeObject cannot be cast to String.");
    }

    @Override
    public String getAsString() {
        throw new ClassCastException("BencodeObject cannot be cast to Integer.");
    }

    @Override
    public List getAsList() {
        throw new ClassCastException("BencodeObject cannot be cast to List.");
    }

    @Override
    public Map getAsMap() {
        throw new ClassCastException("BencodeObject cannot be cast to Map.");
    }

    @Override
    public String toString() {
        return encode();
    }
}
