package net.thesyndicate.utilities;

import java.util.List;
import java.util.Map;

/**
 * Created by dboolbe on 5/12/14.
 */
public interface BencodeObjectInterface {

    /**
     * Tells if BencodeObject contains 'Integer' value.
     * @return 'true' if value is a Integer object else 'false'.
     */
    boolean isBencodeInteger();

    /**
     * Tells if BencodeObject contains 'String' value.
     * @return 'true' if value is a String object else 'false'.
     */
    boolean isBencodeString();

    /**
     * Tells if BencodeObject contains 'List' value.
     * @return 'true' if value is a List object else 'false'.
     */
    boolean isBencodeList();

    /**
     * Tells if BencodeObject contains 'Dictionary' value.
     * @return 'true' if value is a Map object else 'false'.
     */
    boolean isBencodeDictionary();

    /**
     * Encodes the value contained within the BencodeObject.
     * @return The value encoded String as described in the BitTorrent Bencode protocol.
     */
    String encode();

    /**
     * Gets the value contained within the BencodeObject.
     * @return The de-encoded Integer object.
     */
    Integer getAsInteger();

    /**
     * Gets the value contained within the BencodeObject.
     * @return The de-encoded String object.
     */
    String getAsString();

    /**
     * Gets the value contained within the BencodeObject.
     * @return The de-encoded List object.
     */
    List getAsList();

    /**
     * Gets the value contained within the BencodeObject.
     * @return The de-encoded Map object.
     */
    Map getAsMap();
}
