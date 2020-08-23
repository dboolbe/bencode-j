package net.thesyndicate.utilities;

/**
 * Created by dboolbe on 5/19/14.
 */
public class BencodeInteger extends BencodeObject {

    private Integer value;

    public BencodeInteger(Integer value) {
        this.value = value;
    }

    @Override
    public boolean isBencodeInteger() {
        return true;
    }

    @Override
    public String encode() {
        return "i" + value + "e";
    }

    @Override
    public Integer getAsInteger() {
        return value;
    }
}
