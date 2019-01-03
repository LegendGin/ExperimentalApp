package com.onemt.multithread;

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/19 16:10
 * @see
 */
public class Shift {
    public static void main(String[] args) {
        byte origin = 5;
        byte a = (byte) (origin >>> 1);
        byte b = (byte) (origin >>> 65);
        System.out.println(a == b);
        System.out.println(b);
    }
}
