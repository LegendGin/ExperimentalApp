package com.onemt.testparameter;

public class MyClass {

    private static StringBuilder sb = new StringBuilder("old string");
    private static AClass aClass = new AClass("i am a");

    private static void method(StringBuilder sb1, StringBuilder sb2) {
        sb1.append(".method.first");
        sb2.append(".method.second");

        sb1 = new StringBuilder("new string");
        sb1.append("new method's append");
    }


    private static void method(AClass a) {
        a = new AClass("i am new a");
    }

    public static void main(String[] args) {
        method(sb, sb);
        method(aClass);
        System.out.println(sb.toString());
        System.out.println(aClass.name);
    }
}
