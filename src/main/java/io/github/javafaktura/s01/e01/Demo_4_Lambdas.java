package io.github.javafaktura.s01.e01;

import io.github.javafaktura.domain.olx.Ad;
import io.github.javafaktura.domain.olx.GratkaMock;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static io.github.javafaktura.util.Pretty.____________;

class IterationToLambda {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
        }
        // alt+enter in IntelliJ IDEA puts us in the lambda world:
        IntStream.range(0, 10).forEach(x -> System.out.println(x));
    }
}

class AnonToLambda {
    public static void main(String[] args) {
        final int i = 0;
        new GratkaMock().animalAds(8)
                .sort(new Comparator<Ad>() {
                    @Override
                    public int compare(Ad o1, Ad o2) {
                        System.out.println(i);
                        return o1.compareTo(o2);
                    }
                });
    }
}

class LambdaCoercion {
    public static void main(String[] args) {
        // logically this code doesn't make much sense
        // but shows how the same lambda (s -> s.isEmpty())
        // can be coerced to different types (either a Predicate or a Function)
        // - this too is one more feature that an anonymous inner class does not have
        Arrays.asList("a", "b", "c")
                .stream()
                .filter(s -> s.isEmpty())
                .map(s -> s.isEmpty());
    }
}

class EffectivelyFinalAndAtomicInteger {
    public static void main(String[] args) {
        // I. non-final variable cannot go into the closure:
//       ❌  int i = 0;
//       ❌ IntStream.range(0, 100)
//       ❌   .forEach(j -> i += j);

        // II. we can "deceive" the compiler into "thinking" a closure parameter
        // is final by containing it within an object – but we will see some
        // interesting behavior if we do concurrent modification (the parallel() method)
        IntContainer notReallyFinal = new IntContainer();
        IntStream.range(0, 100).parallel()
          .forEach(j -> notReallyFinal.add(j));
        System.out.println(notReallyFinal.i);

        // III. ...or simply an array:
        int[] notReallyFinalObject = {0};
        IntStream.range(0, 100).parallel()
          .forEach(j -> notReallyFinalObject[0] += j);
        System.out.println(notReallyFinalObject[0]);

        // IV. if you actually have to modify a value within a closure and, more importantly, do so
        //     in a concurrent manner, use data structures (or other techniques) suitable for such
        //     usage, one example being AtomicInteger which provides atomic operations of modification
        //     and value retrieval
        AtomicInteger atomicInteger = new AtomicInteger(0);
        IntStream.range(0, 100).parallel()
                .forEach(atomicInteger::addAndGet);
        System.out.println(atomicInteger.get());
    }

    static class IntContainer {
        int i = 0;
        void add(int j) {
            i += j;
        }
    }
}

class Closures {
    public static void main(String[] args) {
        AtomicInteger initialValue = new AtomicInteger(0);
        Supplier<Integer> x = newIntegerIncrementor(initialValue);
        // interact with both the result of the newIntegerIncrementor (the Supplier instance) and both its parameter
        // to see how the parameter gets *closed over* within the lambda
        // hint: use IDE color scheme highlighting closed over variables

        ____________("calling the closure 3 times:");
        System.out.println(x.get());
        System.out.println(x.get());
        System.out.println(x.get());

        ____________("although contained within the closure, the original variable gets modified too:");
        System.out.println(initialValue.get());
    }

    private static Supplier<Integer> newIntegerIncrementor(AtomicInteger initialValue) {
        return () -> initialValue.incrementAndGet();
    }
}