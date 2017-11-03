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
public class ExHashMap_EqualsAndHashCode {
    public static void main(String[] args) {
        Map<Article, Integer> map = new HashMap<>();
        map.put(new Article(1, 1), 1);

        System.out.println(map.get(new Article(1, 2)));
    }
}
