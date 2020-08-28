package net.thesyndicate.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dboolbe on 5/19/14.
 */
public class Bencode {

    // Encoding functions

    /**
     * Wraps the input Integer object in a BencodeInteger object.
     * @param inputInteger input Integer object
     * @return Wrapped up BencodeInteger object.
     */
    private BencodeInteger encodeInteger(int inputInteger) {
        return new BencodeInteger(inputInteger);
    }

    /**
     * Wraps the input String object in a BencodeString object.
     * @param inputString input String object
     * @return Wrapped up BencodeString object.
     */
    private BencodeString encodeString(String inputString) {
        return new BencodeString(inputString);
    }

    /**
     * Wraps the input List object in a BencodeList object.
     * @param inputList input List object
     * @return Wrapped up BencodeList object.
     */
    private BencodeList encodeList(List inputList) {
        BencodeList bencodeList = new BencodeList();
        for(Object object : inputList)
            bencodeList.add(encode(object));
        return bencodeList;
    }

    /**
     * Wraps the input Map object in a BencodeDictionary object.
     * @param inputDictionary input Map object
     * @return Wrapped up BencodeDictionary object.
     */
    private BencodeDictionary encodeDictionary(Map inputDictionary) {
        BencodeDictionary bencodeDictionary = new BencodeDictionary();
        for(Object entry : inputDictionary.entrySet())
            bencodeDictionary.put((BencodeString) encode(((Map.Entry) entry).getKey()), encode(((Map.Entry) entry).getValue()));
        return bencodeDictionary;
    }

    /**
     * Wraps the input object in a BencodeObject object.
     * @param inputObject input object
     * @return Wrapped up BencodeObject object.
     * @throws RuntimeException if input object is unsupported.
     */
    public BencodeObject encode(Object inputObject) throws RuntimeException {
        if(inputObject instanceof String) {
            return encodeString((String) inputObject);
        } else if(inputObject instanceof Integer) {
            return encodeInteger((Integer) inputObject);
        } else if(inputObject instanceof List) {
            return encodeList((List) inputObject);
        } else if(inputObject instanceof Map) {
            return encodeDictionary((Map) inputObject);
        } else
            throw new RuntimeException("Unsupported " + inputObject.getClass());
    }

    // Decoding functions

    int currentCursor;
    int maxCursor;

    private BencodeInteger decodeInteger(String inputString) {
        StringBuilder stringBuilder = new StringBuilder("0");
        Character character;
        currentCursor++;
        while((character = inputString.charAt(currentCursor)) != 'e') {
            stringBuilder.append(character);
            currentCursor++;
        }
        currentCursor++;
        return new BencodeInteger(Integer.parseInt(stringBuilder.toString()));
    }

    private BencodeString decodeString(String inputString) {
        StringBuilder stringBuilder = new StringBuilder("");
        Character character;
        while((character = inputString.charAt(currentCursor)) != ':') {
            stringBuilder.append(character);
            currentCursor++;
        }
        currentCursor++;
        Integer limit = Integer.parseInt(stringBuilder.toString());
        stringBuilder = new StringBuilder("");
        for(int i = 0; i < limit; i++) {
            stringBuilder.append(inputString.charAt(currentCursor));
            currentCursor++;
        }
        return new BencodeString(stringBuilder.toString());
    }

    private BencodeList decodeList(String inputString) {
        BencodeList list = new BencodeList();
        Character character;
        currentCursor++;
        while((character = inputString.charAt(currentCursor)) != 'e') {
//            try {
            list.add(decodeI(inputString));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
        currentCursor++;
        return list;
    }

    private BencodeDictionary decodeDictionary(String inputString) {
        BencodeDictionary map = new BencodeDictionary();
        Character character;
        currentCursor++;
        while((character = inputString.charAt(currentCursor)) != 'e') {
//            try {
            BencodeString key = (BencodeString)decodeI(inputString);
            BencodeObject value = decodeI(inputString);
            map.put(key, value);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
        currentCursor++;
        return map;
    }

    public BencodeObject decode(String inputString) throws RuntimeException {
        return decode(inputString, 0, inputString.length() - 1);
    }

    private BencodeObject decode(String inputString, Integer currentCursor, Integer maxCursor) throws RuntimeException {
        this.currentCursor = currentCursor;
        this.maxCursor = maxCursor;
        if(validate(inputString)) {
            if(inputString.charAt(this.currentCursor) == 'i') {
                return decodeInteger(inputString);
            } else if(inputString.charAt(this.currentCursor) >= '0' && inputString.charAt(this.currentCursor) <= '9') {
                return decodeString(inputString);
            } else if(inputString.charAt(this.currentCursor)== 'l') {
                return decodeList(inputString);
            } else if(inputString.charAt(this.currentCursor)== 'd') {
                return decodeDictionary(inputString);
            } else
                throw new RuntimeException("Invalid character detected '" + inputString.charAt(this.currentCursor) + "'");
        } else
            throw new RuntimeException("Invalid ");
    }

    private BencodeObject decodeI(String inputString) throws RuntimeException {
        if(inputString.charAt(this.currentCursor) == 'i') {
            return decodeInteger(inputString);
        } else if(inputString.charAt(this.currentCursor) >= '0' && inputString.charAt(this.currentCursor) <= '9') {
            return decodeString(inputString);
        } else if(inputString.charAt(this.currentCursor)== 'l') {
            return decodeList(inputString);
        } else if(inputString.charAt(this.currentCursor)== 'd') {
            return decodeDictionary(inputString);
        } else
            throw new RuntimeException("Invalid character detected '" + inputString.charAt(this.currentCursor) + "'");
    }

    // Validation functions

    private StringBuilder validInteger(StringBuilder stringBuilder) {
        if(stringBuilder.charAt(0) == 'i')
            stringBuilder.deleteCharAt(0);
        else
            return null;
        while(stringBuilder.charAt(0) >= '0' && stringBuilder.charAt(0) <= '9')
            stringBuilder.deleteCharAt(0);
        if(stringBuilder.charAt(0) == 'e')
            stringBuilder.deleteCharAt(0);
        else
            return null;
        return stringBuilder;
    }

    private StringBuilder validString(StringBuilder stringBuilder) {
        StringBuilder stringLength = new StringBuilder();
        while(stringBuilder.charAt(0) >= '0' && stringBuilder.charAt(0) <= '9') {
            stringLength.append(stringBuilder.charAt(0));
            stringBuilder.deleteCharAt(0);
        }
        if(stringBuilder.charAt(0) == ':')
            stringBuilder.deleteCharAt(0);
        else
            return null;
        if(stringBuilder.length() >= Integer.parseInt(stringLength.toString())) {
            for(int i = 0; i < Integer.parseInt(stringLength.toString()); i++)
                stringBuilder.deleteCharAt(0);
            return stringBuilder;
        }
        return stringBuilder;
    }

    private StringBuilder validList(StringBuilder stringBuilder) {
        if(stringBuilder.charAt(0) == 'l')
            stringBuilder.deleteCharAt(0);
        else
            return null;
        try {
            while(stringBuilder != null && stringBuilder.charAt(0) != 'e') {
                StringBuilder temp = validate(stringBuilder);
                if(temp == null)
                    return temp;
                else
                    stringBuilder = temp;
            }
            if(stringBuilder != null && stringBuilder.charAt(0) == 'e')
                stringBuilder.deleteCharAt(0);
            else
                return null;
        } catch(Exception e) {
            stringBuilder = null;
        }
        return stringBuilder;
    }

    private StringBuilder validDictionary(StringBuilder stringBuilder) {
        if(stringBuilder.charAt(0) == 'd')
            stringBuilder.deleteCharAt(0);
        else
            return null;
        try {
            String state = "KEY";
            while(stringBuilder != null && stringBuilder.charAt(0) != 'e') {
                StringBuilder temp = state.equals("KEY") ? validString(stringBuilder) : validate(stringBuilder);
                if(temp == null)
                    return temp;
                else
                    stringBuilder = temp;
                if(state.equals("KEY"))
                    state = "VALUE";
                else
                    state = "KEY";
            }
            if(stringBuilder != null && stringBuilder.charAt(0) == 'e' && state.equals("KEY"))
                stringBuilder.deleteCharAt(0);
            else
                return null;
        } catch(Exception e) {
            stringBuilder = null;
        }
        return stringBuilder;
    }

    public boolean validate(String inputString) {
        StringBuilder stringBuilder = new StringBuilder(inputString);
        while(stringBuilder != null && stringBuilder.length() != 0) {
            if(stringBuilder.charAt(0) == 'i')
                stringBuilder = validInteger(stringBuilder);
            else if(stringBuilder.charAt(0) >= '0' && stringBuilder.charAt(0) <= '9')
                stringBuilder = validString(stringBuilder);
            else if(stringBuilder.charAt(0) == 'l')
                stringBuilder = validList(stringBuilder);
            else if(stringBuilder.charAt(0) == 'd')
                stringBuilder = validDictionary(stringBuilder);
            else
                stringBuilder = null;
        }
        return stringBuilder != null;
    }

    private StringBuilder validate(StringBuilder stringBuilder) {
        if(stringBuilder.charAt(0) == 'i')
            return validInteger(stringBuilder);
        else if(stringBuilder.charAt(0) >= '0' && stringBuilder.charAt(0) <= '9')
            return validString(stringBuilder);
        else if(stringBuilder.charAt(0) == 'l')
            return validList(stringBuilder);
        else if(stringBuilder.charAt(0) == 'd')
            return validDictionary(stringBuilder);
        else
            return null;
    }

    public static void main(String[] args) {
        System.out.println("Hello, World!!!");

        Bencode bencode = new Bencode();

        String testString0 = "Hello, World!!";
        System.out.println("String: '" + testString0 + "' = '" + bencode.encode(testString0) + "'");

        assert bencode.encode(testString0).getClass().equals(BencodeString.class) : "object String not encoded into object BencodeString";
        assert bencode.encode(testString0).toString().equals("14:Hello, World!!") : "object String not encoded properly";

        String testString1 = "Supercalifragilisticexpialidocious!!";
        System.out.println("String: '" + testString1 + "' = '" + bencode.encode(testString1) + "'");

        assert bencode.encode(testString1).getClass().equals(BencodeString.class) : "object String not encoded into object BencodeString";
        assert bencode.encode(testString1).toString().equals("36:Supercalifragilisticexpialidocious!!") : "object String not encoded properly";

        String testString2 = "Quick fox";
        System.out.println("String: '" + testString2 + "' = '" + bencode.encode(testString2) + "'");

        assert bencode.encode(testString2).getClass().equals(BencodeString.class) : "object String not encoded into object BencodeString";
        assert bencode.encode(testString2).toString().equals("9:Quick fox") : "object String not encoded properly";

        Integer testInteger0 = 2468;
        System.out.println("Integer: '" + testInteger0 + "' = '" + bencode.encode(testInteger0) + "'");

        assert bencode.encode(testInteger0).getClass().equals(BencodeInteger.class) : "object Integer not encoded into object BencodeInteger";
        assert bencode.encode(testInteger0).toString().equals("i2468e") : "object Integer not encoded properly";

        Integer testInteger1 = 12111986;
        System.out.println("Integer: '" + testInteger1 + "' = '" + bencode.encode(testInteger1) + "'");

        assert bencode.encode(testInteger1).getClass().equals(BencodeInteger.class) : "object Integer not encoded into object BencodeInteger";
        assert bencode.encode(testInteger1).toString().equals("i12111986e") : "object Integer not encoded properly";

        Integer testInteger2 = 36524182;
        System.out.println("Integer: '" + testInteger2 + "' = '" + bencode.encode(testInteger2) + "'");

        assert bencode.encode(testInteger2).getClass().equals(BencodeInteger.class) : "object Integer not encoded into object BencodeInteger";
        assert bencode.encode(testInteger2).toString().equals("i36524182e") : "object Integer not encoded properly";

        List testList0 = new ArrayList();
        testList0.add(testString0);
        testList0.add(testInteger0);
        testList0.add(testString1);
        testList0.add(testInteger1);
        testList0.add(testString2);
        testList0.add(testInteger2);
        System.out.println("List: '" + testList0 + "' = '" + bencode.encode(testList0) + "'");

        assert bencode.encode(testList0).getClass().equals(BencodeList.class) : "object List not encoded into object BencodeList";
        assert bencode.encode(testList0).toString().equals("l14:Hello, World!!i2468e36:Supercalifragilisticexpialidocious!!i12111986e9:Quick foxi36524182ee") : "object List not encoded properly";

        Map testMap0 = new HashMap();
        testMap0.put(testString0, testInteger0);
        testMap0.put(testString1, testInteger1);
        testMap0.put(testString2, testInteger2);
        System.out.println("Dictionary: '" + testMap0 + "' = '" + bencode.encode(testMap0) + "'");

        assert bencode.encode(testMap0).getClass().equals(BencodeDictionary.class) : "object Map not encoded into object BencodeDictionary";
        assert bencode.encode(testMap0).toString().equals("d14:Hello, World!!i2468e9:Quick foxi36524182e36:Supercalifragilisticexpialidocious!!i12111986ee") : "object Map not encoded properly";

        String decodeString = "i345e";
        System.out.println("Integer: '" + decodeString + "' = '" + bencode.decode(decodeString).getAsInteger() + "'");

        assert bencode.decode(decodeString).getClass().equals(BencodeInteger.class) : 
            "expected: " + BencodeInteger.class + " actual: " + bencode.decode(decodeString).getClass();
        assert bencode.decode(decodeString).getAsInteger().equals(345) : 
            "expected: " + 345 + " actual: " + bencode.decode(decodeString).getAsInteger();

        decodeString = "14:Hello, World!!";
        System.out.println("String: '" + decodeString + "' = '" + bencode.decode(decodeString).getAsString() + "'");

        assert bencode.decode(decodeString).getClass().equals(BencodeString.class) : 
            "expected: " + BencodeString.class + " actual: " + bencode.decode(decodeString).getClass();
        assert bencode.decode(decodeString).getAsString().equals("Hello, World!!") : 
            "expected: " + "Hello, World!!" + " actual: " + bencode.decode(decodeString).getAsString();

        decodeString = "lli345e15:Hello, World!!aei345e15:Hello, World!!ae";
        System.out.println("List: '" + decodeString + "' = '" + bencode.decode(decodeString).getAsList() + "' = " + bencode.decode(decodeString).getAsList().size());

        assert bencode.decode(decodeString).getClass().equals(BencodeList.class) : 
            "expected: " + BencodeList.class + " actual: " + bencode.decode(decodeString).getClass();
        List<BencodeObject> listObj = bencode.decode(decodeString).getAsList();
        assert listObj.get(0).getAsList().get(0).getAsInteger().equals(345) :
            "expected: " + listObj.get(0).getAsList().get(0).getAsInteger() + " actual: " + 345;
        assert listObj.get(0).getAsList().get(1).getAsString().equals("Hello, World!!a") :
            "expected: " + listObj.get(0).getAsList().get(1).getAsString() + " actual: " + "Hello, World!!a";
        assert listObj.get(1).getAsInteger().equals(345) :
            "expected: " + listObj.get(1).getAsInteger() + " actual: " + 345;
        assert listObj.get(2).getAsString().equals("Hello, World!!a") :
            "expected: " + listObj.get(2).getAsString() + " actual: " + "Hello, World!!a";

        decodeString = "d15:Hello, World!!ai345e3:bob5:ricky6:sillielli345e15:Hello, World!!aei345e15:Hello, World!!aee";
        System.out.println("Dictionary: '" + decodeString + "' = '" + bencode.decode(decodeString).getAsMap());

        assert bencode.decode(decodeString).getClass().equals(BencodeDictionary.class) : 
            "expected: " + BencodeDictionary.class + " actual: " + bencode.decode(decodeString).getClass();
        Map<BencodeString,BencodeObjectInterface> dictionaryObj = bencode.decode(decodeString).getAsMap();
        for(BencodeString key : dictionaryObj.keySet()) {
            System.out.println("key: " + key.getAsString() + " value: " + dictionaryObj.get(key));
        }
        assert dictionaryObj.get(bencode.encode("Hello, World!!a")).getAsInteger().equals(345) :
            "expected: " + 345 + " actual: " + dictionaryObj.get(bencode.encode("Hello, World!!a")).getAsInteger();
        assert dictionaryObj.get(bencode.encode("bob")).getAsString().equals("ricky") :
            "expected: " + "ricky" + " actual: " + dictionaryObj.get(bencode.encode("bob")).getAsString();
        assert dictionaryObj.get(bencode.encode("sillie")).getAsList().get(2).getAsString().equals("Hello, World!!a") :
            "expected: " + "Hello, World!!a" + " actual: " + dictionaryObj.get(bencode.encode("sillie")).getAsList().get(2).getAsString();

        decodeString = "i12ee";
        System.out.println("Valid String?: '" + decodeString + "' = '" + bencode.validate(decodeString));

        assert !bencode.validate(decodeString) : "expected: " + "false" + " actual: " + bencode.validate(decodeString);

        decodeString = "2:ee";
        System.out.println("Valid String?: '" + decodeString + "' = '" + bencode.validate(decodeString));

        assert bencode.validate(decodeString) : "expected: " + "true" + " actual: " + bencode.validate(decodeString);

        decodeString = "li3e3:bobe";
        System.out.println("Valid String?: '" + decodeString + "' = '" + bencode.validate(decodeString));

        assert bencode.validate(decodeString) : "expected: " + "true" + " actual: " + bencode.validate(decodeString);

        decodeString = "d1:b3:bob1:ei3e3:madli3eee";
        System.out.println("Valid String?: '" + decodeString + "' = '" + bencode.validate(decodeString));

        assert bencode.validate(decodeString) : "expected: " + "true" + " actual: " + bencode.validate(decodeString);
    }
}
