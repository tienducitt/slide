/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rey.collectionexp.Queue;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 *
 * @author duc.nguyentien
 */
public class ExPriorityQueue {

    public static void main(String[] args) {
        Queue<Integer> q = new PriorityQueue<>();

        q.add(3);
        q.add(1);
        q.add(5);
        q.add(6);
        q.add(2);
        
        q.stream().forEach(System.out::print);
        
        System.out.println("Peek from Q");
        while (!q.isEmpty()) {
            System.out.println(q.poll());
        }
    }
}
