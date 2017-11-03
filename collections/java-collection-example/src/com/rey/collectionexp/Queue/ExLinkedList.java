/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rey.collectionexp.Queue;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author duc.nguyentien
 */
public class ExLinkedList {
    public static void main(String[] args) {
        Queue<Integer> q = new LinkedList<>();
        
        q.add(1);
        q.add(2);
        q.add(3);
        
        System.out.println(q.peek());   // just get the top
        System.out.println(q.poll());   // get and remove
        
        q.stream().forEach(System.out::println);
    }
}
