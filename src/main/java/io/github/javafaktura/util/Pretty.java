package io.github.javafaktura.util;

import java.util.Arrays;
import java.util.Collection;

public class Pretty {

    /**
     * method call which looks kinda like a header in the code, then gets printed with a header-like decoration
     * - for easily associating a line-of-code with output
     */
    public static void ____________(String header){
        System.out.println("\n");
        System.out.println("##### " + header);
    }

    public static void printArray(int[] a) {
        System.out.println(
                Arrays.toString(
                        a
                )
        );
    }

    public static void printArray(Object[] a) {
        System.out.println(
                Arrays.toString(
                        a
                )
        );
    }

    public static void verboseCollectionPrint(Collection<?> c) {
        System.out.println("Collection class: " + c.getClass().getCanonicalName() + ", size:" + c.size());
        c.forEach(System.out::println);
    }
}
