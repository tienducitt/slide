/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rey.collectionexp.Set;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author duc.nguyentien
 */
public class ExHashSet {
    public static void main(String[] args) {
        Set<Integer> set = new HashSet<>();
        
        set.add(1);
        set.add(2);
        set.add(3);
        System.out.println(set.add(4));
        System.out.println(set.add(1));
        
        System.out.println(set.contains(2));
        System.out.println(set.contains(5));
    }
}
