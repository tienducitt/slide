/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rey.collectionexp.Map;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author duc.nguyentien
 */
public class ExHashMap {
    public static void main(String[] args) {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "Hello1");
        map.put(3, "Hello3");
        map.put(5, "Hello5");
        map.put(11, "Hello11");
        map.put(2, "Hello2");
        map.put(57, "Hello57");
        map.put(19, "Hello19");
        map.put(21, "Hello21");
                
        // get
        System.out.println(map.get(1));
        // return null if key is not exist
        System.out.println(map.get(3));
        
        // map does not guarantee any order, ether insert order or natural order
        
        // loop through key
        for(int k : map.keySet()) {
            System.out.println(k);
        }
        
        // loop throufh value
        for (String k : map.values()) {
            System.out.println(k);
        }
        
        
    }
}
