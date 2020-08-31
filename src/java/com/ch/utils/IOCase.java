/** 
 * Project Name:advertise-common <br>
 * File Name:IOCase.java <br>
 * Copyright (c) 2017, babytree-inc.com All Rights Reserved. 
 */
package com.ch.utils;

import java.io.File;
import java.io.Serializable;

/**
 * @date 2017年7月11日 下午3:23:21
 */
public final class IOCase implements Serializable {

    public static final IOCase SENSITIVE = new IOCase("Sensitive", true);
    
    public static final IOCase INSENSITIVE = new IOCase("Insensitive", false);
    
    public static final IOCase SYSTEM = new IOCase("System", !isSystemWindows());

    private static final long serialVersionUID = -6343169151696340687L;

    private final String name;
    
    private final transient boolean sensitive;

    public static IOCase forName(String name) {
        if (IOCase.SENSITIVE.name.equals(name)){
            return IOCase.SENSITIVE;
        }
        if (IOCase.INSENSITIVE.name.equals(name)){
            return IOCase.INSENSITIVE;
        }
        if (IOCase.SYSTEM.name.equals(name)){
            return IOCase.SYSTEM;
        }
        throw new IllegalArgumentException("Invalid IOCase name: " + name);
    }

    private IOCase(String name, boolean sensitive) {
        this.name = name;
        this.sensitive = sensitive;
    }

    private Object readResolve() {
        return forName(name);
    }

    public String getName() {
        return name;
    }

    public boolean isCaseSensitive() {
        return sensitive;
    }

    public int checkCompareTo(String str1, String str2) {
        if (str1 == null || str2 == null) {
            throw new NullPointerException("The strings must not be null");
        }
        return sensitive ? str1.compareTo(str2) : str1.compareToIgnoreCase(str2);
    }

    public boolean checkEquals(String str1, String str2) {
        if (str1 == null || str2 == null) {
            throw new NullPointerException("The strings must not be null");
        }
        return sensitive ? str1.equals(str2) : str1.equalsIgnoreCase(str2);
    }

    public boolean checkStartsWith(String str, String start) {
        return str.regionMatches(!sensitive, 0, start, 0, start.length());
    }

    public boolean checkEndsWith(String str, String end) {
        int endLen = end.length();
        return str.regionMatches(!sensitive, str.length() - endLen, end, 0, endLen);
    }

    public int checkIndexOf(String str, int strStartIndex, String search) {
        int endIndex = str.length() - search.length();
        if (endIndex >= strStartIndex) {
            for (int i = strStartIndex; i <= endIndex; i++) {
                if (checkRegionMatches(str, i, search)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public boolean checkRegionMatches(String str, int strStartIndex, String search) {
        return str.regionMatches(!sensitive, strStartIndex, search, 0, search.length());
    }

    @Override
    public String toString() {
        return name;
    }
    
    private static final char WINDOWS_SEPARATOR = '\\';

    private static final char SYSTEM_SEPARATOR = File.separatorChar;
    
    static boolean isSystemWindows() {
        return SYSTEM_SEPARATOR == WINDOWS_SEPARATOR;
    }
}
