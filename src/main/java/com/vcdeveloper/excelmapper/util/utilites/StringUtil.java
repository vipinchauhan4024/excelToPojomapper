package com.vcdeveloper.excelmapper.util.utilites;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class StringUtil {

    /**
     * Implement to show a string representation of an object.
     * @param <T>
     */
    public interface Stringifier<T extends Object> {
        String stringify(T object);
    }

    public static Long stringToLong(String s) {
        if (ExcelMapperUtilities.nullAwareEquals(s, "null")) {
            return null;
        }
        return Long.valueOf(s);
    }

  
    public static String join(Iterable<? extends Object> list, String delimiter) {
        return join(null, list, delimiter);
    }

    /**
     * Joins the objects as a string, using toString() of each.
     * @param list the list may contain null objects and these will be omitted.
     * @param delimiter
     * @return
     */
    public static String join(String delimiter, Object...list) {
        return join(null, Arrays.asList(list), delimiter);
    }



    public static List<String> splitTrim(String s, String delimiter) {
        List<String> list = new ArrayList<String>();
        for (String word : s.split(delimiter)) {
            word = word.trim();
            if (!word.isEmpty()) {
                list.add(word.trim());
            }
        }
        return list;
    }

  

    public static void listToUpperCase(List<String> list) {
        for (int x = 0; x < list.size(); ++x) {
            String name = list.get(x);
            list.remove(x);
            list.add(x, name.toUpperCase());
        }
    }


    /**
     * Interface for converting between a list of objects to a string list.
     */
    public interface StringExtractor<T> {
        String getString(T object);
    }
    public static <T> List<String> toStringListWithExtractor(StringExtractor<T> extractor, List<T> objects) {
        List<String> strings = new ArrayList<String>();
        for (T object : objects) {
            strings.add(extractor.getString(object));
        }
        return strings;
    }


 

    public static String trim(String s) {
        if (s == null) {
            return null;
        }
        return s.trim();
    }

    public static String trimClean(String s) {
        s = trim(s);
        if (s != null && s.isEmpty()) {
            return null;
        }
        return s;
    }

    public static boolean isEmpty(String s) {
        return (s == null || s.isEmpty() || s.trim().isEmpty());
    }

 
    public static boolean emptyAwareStringEquals(String s1, String s2) {
        return StringUtil.isEmpty(s1) ? StringUtil.isEmpty(s2) : s1.equals(s2);
    }

    private StringUtil() {
    }
}
