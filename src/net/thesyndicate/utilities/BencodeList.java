package net.thesyndicate.utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dboolbe on 5/19/14.
 */
public class BencodeList extends BencodeObject {

    private List<BencodeObject> value;

    public BencodeList() {
        value = new ArrayList<BencodeObject>();
    }

    public void add(BencodeObject object) {
        value.add(object);
    }

    @Override
    public boolean isBencodeList() {
        return true;
    }

    @Override
    public String encode() {
        StringBuilder stringBuilder = new StringBuilder("l");
        for(BencodeObject object : value) {
            stringBuilder.append(object.encode());
        }
        stringBuilder.append("e");

        return stringBuilder.toString();
    }

    @Override
    public List<BencodeObject> getAsList() {
        return value;
    }
}
