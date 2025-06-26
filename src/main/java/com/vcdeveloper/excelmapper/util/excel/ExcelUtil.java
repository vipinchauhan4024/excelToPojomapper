package com.vcdeveloper.excelmapper.util.excel;

/**
 * Util Class for Excel Utils.
 */
public class ExcelUtil {
    static final String SET = "set";
    static final String IS = "is";
    
 
    /**
     * Change Method name to propery 
     * ex setCheckFunction 
     * @param start : 3
     * @param methodName : setCheckFunction 
     * @return : checkFunction
     */
    public static String toProperty(int start, String methodName) {
        char[] prop = new char[methodName.length() - start];
        methodName.getChars(start, methodName.length(), prop, 0);
        int firstLetter = prop[0];
        prop[0] = (char) (firstLetter < 91 ? firstLetter + 32 : firstLetter);
        return new String(prop);
    }
    
    /**
     * Change Method name to propery for Bollean setter
     * Ex setLoaded
     * @param start: 3
     * @param methodName : setLoaded
     * @return : isLoaded
     */
    public static String toPropertyForBoolean(int start, String methodName)  {
        char[] prop = new char[methodName.length() - start];
        methodName.getChars(start, methodName.length(), prop, 0);
        String feildName = new String(prop);
        return IS + feildName;
     }
}
