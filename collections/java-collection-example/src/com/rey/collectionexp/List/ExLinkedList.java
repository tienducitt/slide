/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rey.collectionexp.List;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author duc.nguyentien
 */
public class ExLinkedList {
    public static void main(String[] args) {
        // new array list
        List<Integer> list = new LinkedList<>(); // before java 8, you have to defind the type: new ArrayList<Integer>()
        // add to array list
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(3, 4); // set at index 3 (start from 0) value 4

        // loop through
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i) + ", ");
        }
        System.out.println("");

        // foreach
        for (int v : list) {
        }

        // stream foreach java 8
        list.stream().forEach(System.out::print);
        System.out.println("");

        list.remove(1);
        list.remove((Integer) 1);
        list.stream().forEach(System.out::print);
        System.out.println("");

        // clear
        list.clear();
    }
}
