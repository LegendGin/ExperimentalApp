package com.onemt.testgeneric;

import java.util.ArrayList;
import java.util.List;

public class MyClass {

    public static void main(String[] args) {
        // compile error
//        List<Object> a = new ArrayList<Integer>();
        List a1 = new ArrayList();
        a1.add(new Animal());
        a1.add(123);
        a1.add("asdf");

        List<Object> a2 = new ArrayList<>();
        a2.add(111);

        List<Integer> a3 = a1;
        a3.add(65);

        List<?> any = a1;

        List<? super Animal> animalList = new ArrayList<>();
        List<? super Cat> catList = new ArrayList<Animal>();
//        catList.add(new Animal());
        catList.add(new TomCat());
        catList.add(new Cat());

        // 可以编译
        Object[] obj = new Integer[10];

        List<? extends Animal> animals = new ArrayList<Cat>();
        Animal c = animals.get(0);
    }
}
