package io.github.javafaktura.s01.e01;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.javafaktura.util.Pretty.____________;
import static io.github.javafaktura.util.Pretty.printArray;
import static java.util.Arrays.sort;

/**
 * Chapter 1: Arrays
 *
 * As all the other chapters, the proper use of this class is to read it line by line, top-down, run it, break it, have fun!
 */
class ArrayIntro {

    /* Declaring arrays in Java */
    public static void main(String[] args) {
        // Declaration without initialization
        Integer[] ary0;
        // Empty array initialization
        Integer[] ary1 = new Integer[2];
        // Don't do this one
        Integer ary2[] = new Integer[2]; // 🤮
        // Long and short syntax array initialization
        Integer[] ary3 = new Integer[]{1, 2};
        Integer[] ary4 = {1, 2};
        // Multi-dimensional array
        Integer[][][] ary5 = {{{1, 2}, {3, 4}}};
        // 0-length array initialization
        Integer[] ary6 = {};

        //// nothing surprising below:

        // setting array value at given index
        ary4[0] += 1;
        ary5[0][0][1] = 5;

        // retrieving array value
        System.out.println(ary4[0]);
        System.out.println(ary5[0][0][1]);
    }
}


class ArrayCovariance {
    public static void main(String[] args) {
        // An array of Numbers (abstract class)...
        Number[] numbers = new Number[3];
        // ...accepts elements of its type subclasses
        numbers[0] = 10;                // Integer
        numbers[1] = 3.14;              // Double
        numbers[2] = (byte) 0b00001000; // Byte
        System.out.println(
                Arrays.toString(
                        numbers
                )
        );
    }
}


class ArrayCovariance_2 {
    public static void main(String[] args) {
        // same example as before, just with custom classes
        class Animals {}
        class Pet extends Animals {}
        class Dog extends Pet {}
        class Cat extends Pet {}

        Pet[] pets = new Pet[3];
        pets[0] = new Pet();
        pets[1] = new Cat();
        pets[0] = new Dog();
    }
}

class ArrayCovarianceProblem {
    public static void main(String[] args) {
        class Animal {}
        class Pet extends Animal {}
        class Dog extends Pet {}
        class Cat extends Pet {}

        Cat[] cats = {new Cat(), new Cat()};
        // "pets" is still the same thing in memory as "cats":
        Pet[] pets = cats;
        // pets[0] is still the same thing as cats[0]
        pets[0] = new Dog();
        Cat firstCat = cats[0]; // ☠️
    }
}

class ArrayUtils {
    public static void main(String[] args) {
        _1_copyingArrays();
        _2_theArraysClass();
    }

    private static void _1_copyingArrays() {
        int[] a = {1, 2, 3};
        int[] b = new int[3];
        // IDEA: press alt+enter for magic
        for (int i = 0; i < b.length; i++) {
            b[i] = a[i];
        }
        // System.arraycopy(a, 0, b, 0, b.length);
        // HINT: if unsure about the System.arraycopy parameters, don't overthink it – try it with JShell or scratchfile

        ____________("copying arrays:");
        printArray(a);
        printArray(b);

        ____________("array.clone():");
        int[] clone = a.clone();
        clone[0] = 42;
        printArray(a);
        printArray(clone);

        int[][] multi = {{1}};
        int[][] multiClone = multi.clone();
        multiClone[0][0] = 42;
        printArray(a);
        printArray(clone);
        // what happened? only the first "layer" gets cloned
        // this behavior among arrays, collections, libraries etc. is known as SHALLOW cloning

        // BTW
        // clone() on array is the only clone() you should ever use:
        // Object.clone() and the Cloneable interface are heavily flawed
        // 🌍 https://medium.com/@biratkirat/learning-effective-java-item-11-390fbf94e41c
    }

    private static void _2_theArraysClass() {
        // Arrays.toString() -> this one you've already seen

        ____________("sort & fill");
        Integer[] a = new Integer[]{1, 5, 3};
        sort(a);
        printArray(a);

        Arrays.fill(a, 42);
        printArray(a);

        List<Integer> integers = Arrays.asList(a);
        List<Integer> integers2 = Arrays.asList(1, 2, 3);

        class Pet {}
        class Dog extends Pet {}
        class Cat extends Pet {}

        ____________("Arrays.asList(...)");
        List<Pet> pets = Arrays.asList(new Cat(), new Dog());
        System.out.println(pets);
        System.out.println(pets.getClass().getName());
        // uncomment the next line and see what happens:
        // pets.add(new Cat());
        System.out.println("vs:");
        System.out.println(new ArrayList<>().getClass().getName());

        ////// "see the 'toArray()' signature"
        Object[] objects = pets.toArray();

        ____________("toArray(array) with array fitting the list:");
        Pet[] petsArray = new Pet[3];
        Pet[] petsToArrayResult1 = pets.toArray(petsArray);
        System.out.println(petsArray == petsToArrayResult1);
        printArray(petsToArrayResult1);

        ____________("toArray(array) with an array smaller than the list:");
        Pet[] zeroPetsArray = new Pet[0]; // <-- !
        Pet[] petsToArrayResult2 = pets.toArray(zeroPetsArray);
        System.out.println(zeroPetsArray == petsToArrayResult2);
        printArray(petsToArrayResult2);
    }

    // SUMMARY:
    // 1. arrays behave in a very special way when subtyped, but instead of trying to learn it, it's best to get a feel of it
    // 2. some utility methods you may want to remember:
    //      # someArray::clone() – but don't use it outside of arrays
    //      # System::arraycopy – when cloning only a part of an array
    //      # Arrays::toString – for printing arrays
    //      # Arrays::everything else – many methods useful for algorithms, like fill, sort, binarySearch
    //      # Arrays.toList() + someList.toArray(new DesiredType[0]) – for going between arrays and lists

}