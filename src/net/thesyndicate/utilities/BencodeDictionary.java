package net.thesyndicate.utilities;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Created by dboolbe on 5/19/14.
 */
public class BencodeDictionary extends BencodeObject {

    private NavigableMap<BencodeString,BencodeObjectInterface> value;

    public BencodeDictionary() {
        value = new TreeMap<BencodeString, BencodeObjectInterface>();
    }

    public void put(BencodeString key, BencodeObject object) {
        value.put(key, object);
    }

    @Override
    public boolean isBencodeDictionary() {
        return true;
    }

    @Override
    public String encode() {
        StringBuilder stringBuilder = new StringBuilder("d");
        for(Map.Entry<BencodeString,BencodeObjectInterface> entry : value.entrySet()) {
            stringBuilder.append(entry.getKey().encode());
            stringBuilder.append(entry.getValue().encode());
        }
        stringBuilder.append("e");

        return stringBuilder.toString();
    }

    @Override
    public Map<BencodeString,BencodeObjectInterface> getAsMap() {
        return value;
    }
}
