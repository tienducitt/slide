/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rey.collectionexp.Map;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author duc.nguyentien
 */
public class ExLinkedHashMap {
    public static void main(String[] args) {
        Map<Integer, String> map = new LinkedHashMap<>();
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
        System.out.println(map.get(100));
        
        // LinkedHashMap support inserted order
        
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
