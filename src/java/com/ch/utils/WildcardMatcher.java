/** 
 * Project Name:advertise-common <br>
 * File Name:WildcardMatcher.java <br>
 * Copyright (c) 2017, babytree-inc.com All Rights Reserved. 
 */
package com.ch.utils;

import java.util.ArrayList;
import java.util.Stack;

/**
 * @date 2017年7月11日 上午11:49:40
 */
public class WildcardMatcher {

	/**
     * *表示0/多次出现
     * ?表示出现1次
     * 
     * @param filename 
     * @param wildcardMatcher 
     * @param caseSensitivity 
     * @return true if the filename matches the wilcard string
     */
    public static boolean wildcardMatch(String filename, String wildcardMatcher, IOCase caseSensitivity) {
        if (filename == null && wildcardMatcher == null) {
            return true;
        }
        if (filename == null || wildcardMatcher == null) {
            return false;
        }
        if (caseSensitivity == null) {
            caseSensitivity = IOCase.SENSITIVE;
        }
        String[] wcs = splitOnTokens(wildcardMatcher);
        boolean anyChars = false;
        int textIdx = 0;
        int wcsIdx = 0;
        Stack<int[]> backtrack = new Stack<int[]>();
        
        do {
            if (backtrack.size() > 0) {
                int[] array = backtrack.pop();
                wcsIdx = array[0];
                textIdx = array[1];
                anyChars = true;
            }
            
            while (wcsIdx < wcs.length) {
      
                if (wcs[wcsIdx].equals("?")) {
                    // ? so move to next text char
                    textIdx++;
                    if (textIdx > filename.length()) {
                        break;
                    }
                    anyChars = false;
                    
                } else if (wcs[wcsIdx].equals("*")) {
                    // set any chars status
                    anyChars = true;
                    if (wcsIdx == wcs.length - 1) {
                        textIdx = filename.length();
                    }
                    
                } else {
                    if (anyChars) {
                        textIdx = caseSensitivity.checkIndexOf(filename, textIdx, wcs[wcsIdx]);
                        if (textIdx == -1) {
                            break;
                        }
                        int repeat = caseSensitivity.checkIndexOf(filename, textIdx + 1, wcs[wcsIdx]);
                        if (repeat >= 0) {
                            backtrack.push(new int[] {wcsIdx, repeat});
                        }
                    } else {
                        if (!caseSensitivity.checkRegionMatches(filename, textIdx, wcs[wcsIdx])) {
                            break;
                        }
                    }
      
                    textIdx += wcs[wcsIdx].length();
                    anyChars = false;
                }
      
                wcsIdx++;
            }
            
            if (wcsIdx == wcs.length && textIdx == filename.length()) {
                return true;
            }
            
        } while (backtrack.size() > 0);
  
        return false;
    }
	
    static String[] splitOnTokens(String text) {
        if (text.indexOf('?') == -1 && text.indexOf('*') == -1) {
            return new String[] { text };
        }

        char[] array = text.toCharArray();
        ArrayList<String> list = new ArrayList<String>();
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (array[i] == '?' || array[i] == '*') {
                if (buffer.length() != 0) {
                    list.add(buffer.toString());
                    buffer.setLength(0);
                }
                if (array[i] == '?') {
                    list.add("?");
                } else if (list.isEmpty() ||
                        i > 0 && list.get(list.size() - 1).equals("*") == false) {
                    list.add("*");
                }
            } else {
                buffer.append(array[i]);
            }
        }
        if (buffer.length() != 0) {
            list.add(buffer.toString());
        }

        return list.toArray( new String[ list.size() ] );
    }
    
    public static void main(String[] args){
    	System.out.println(wildcardMatch("/ROOT/statics/plugins/jquery-3.2.1.min.js", "*/statics/*", IOCase.INSENSITIVE));
    }
}
