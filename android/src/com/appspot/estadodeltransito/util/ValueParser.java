package com.appspot.estadodeltransito.util;

import java.util.HashSet;
import java.util.Iterator;

public class ValueParser {

    private HashSet<String> values;
    private int maxSize;
    private static final String TOKEN = "#";

    public ValueParser(String text, int maxSize, String defaultStr) {
        this.maxSize = maxSize;
        this.values = new HashSet<String>(2);
        parseValue(text);
    }

    public ValueParser(String text, String defaultStr) {
        this(text, Integer.MAX_VALUE, defaultStr);
    }

    public ValueParser(String text) {
        this(text, Integer.MAX_VALUE, "");
    }

    public void addValue(String val) {
        if (values.size() < this.maxSize) {
            values.add(val);
        }
    }

    public void removeValue(String val) {
        if (values.contains(val)) {
            values.remove(val);
        }
    }

    private void parseValue(String text) {
        for (String string : text.split(TOKEN)) {
            if ( string.length() >= 1 )
                values.add(string);
        }
    }

    public boolean contains(String text) {
        return values.contains(text);
    }

    public Iterator<String> iterator() {
        return values.iterator();
    }

    public int size() {
        return values.size();
    }

    @Override
    public String toString() {

        StringBuilder ret = new StringBuilder();
        for (String value : values) {
            ret.append(value + TOKEN);
        }

        return ret.toString();
    }
}
