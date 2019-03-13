package io.github.javafaktura.s01.e01;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Chapter 2: Collections
 */
class RawCollectionsOfJava1_2 {
    public static void main(String[] args) {
        List pets = new ArrayList();
        pets.add(new Dog());

        // then, somewhere else in the code:
        Object maybeCat = pets.get(0);
        if (maybeCat instanceof Cat) {
            ((Cat) maybeCat).meow();
        } else {
            throw new IllegalStateException("Expected cat, was " + maybeCat);
        }

        // raw list operations
        pets.add(new Object());
        pets.add(new Cat());
        Object lastItem = pets.get(pets.size() - 1);
        // we can put any object to a raw lists
        // and what we ever get is always but an Object we then have to type-check and cast
    }
}

class InvariantList {
    public static void main(String[] args) {
        // 💡 look at the type declaration of List
        // Invariant list is a known PRODUCER AND CONSUMER
        List<Pet> pets = new ArrayList<>();

        // I. we can add any sub-type of pet to a list of pets:
        pets.add(new Dog());
        // II. what we get from a list of pets is some type of pet (without further casting)
        Pet pet = pets.get(0);

        // III. we cannot assign a list of anything more or less specific to an invariant lists of pets
        // ❌ pets = new ArrayList<Dog>();
        // ❌ pets = new ArrayList<Object>();

        // X.contains(Object) is not generic in collections and maps
        System.out.println(pets.contains("not a pet"));
        // we get an IDE warning but not a compiler error
    }
}

class CovariantList {
    public static void main(String[] args) {
        // Covariant list is a known PRODUCER
        // The *only* thing we know about the specific type of a covariant list, is that it EXTENDS some type
        List<? extends Pet> a_list_of_specific_but_unknown_subtype_of_pets = Arrays.asList(new Cat());

        // I. no way to add (CONSUME) anything:
        // ❌ a_list_of_specific_but_unknown_subtype_of_pets.add(new Cat());
        // ❌ a_list_of_specific_but_unknown_subtype_of_pets.add(new Pet());
        // ❌ a_list_of_specific_but_unknown_subtype_of_pets.add(new Object());

        // II. but we CAN retrieve (PRODUCE) – and we know that what we get is SOME KIND of pet (inherits from Pet)
        Pet pet = a_list_of_specific_but_unknown_subtype_of_pets.get(0);

        // III. any invariant list of a type that extends the covariant list variable'ss type can be assigned to it
        a_list_of_specific_but_unknown_subtype_of_pets = Arrays.asList(new Cat());
        a_list_of_specific_but_unknown_subtype_of_pets = Arrays.asList(new Dog());
        // ❌ a_list_of_specific_but_unknown_subtype_of_pets = Arrays.asList(new Animal());
    }
}


class ContravariantList {
    public static void main(String[] args) {
        // Contravariant list is a known CONSUMER
        // The *only* thing we know about the specific type of a contravariant list, is that it is a SUPER type of some type
        List<Pet> pets = Arrays.asList(new Cat());
        List<? super Pet> a_list_of_things_whose_type_is_a_subtype_of_pet = pets;

        // remember – this type is specific, but unknown; we can guess that the actual type is Pet or Animal or Object in this specific case
        // but cannot express it in the code

        // I. contravariant list can safely CONSUME objects of its contravariant type:
        a_list_of_things_whose_type_is_a_subtype_of_pet.add(new Pet());
        // or its subclass:
        a_list_of_things_whose_type_is_a_subtype_of_pet.add(new Cat());
        a_list_of_things_whose_type_is_a_subtype_of_pet.add(new Dog());
        // but obviously not superclass:
        // ❌ a_list_of_things_whose_type_is_a_subtype_of_pet.add(new Animal());
        // ❌ a_list_of_things_whose_type_is_a_subtype_of_pet.add(new Object());

        // II. we cannot retrieve (PRODUCE) from a covariant list anything more specific than an object
        Object object = a_list_of_things_whose_type_is_a_subtype_of_pet.get(0);
        // ❌ Animal animal = a_list_of_things_whose_type_is_a_subtype_of_pet.get(0);
        // ❌ Pet pet = a_list_of_things_whose_type_is_a_subtype_of_pet.get(0);
        /* 💡💡💡💡💡💡💡💡💡💡💡💡💡💡💡💡💡💡💡💡💡💡💡💡💡💡💡💡💡💡💡💡💡💡💡💡💡💡💡💡💡💡💡💡💡
         * Still confused by the two lines above not compiling?
         *
         * Remember: The fact that we only put subtypes of Pet doesn't matter. The list could've
         * previously contained only Object, and <? super Pet> would be OK for such list,
         * but would not convey this information any longer!
         *
         * One more approach: when having List<? extends Animal> you actually have to imagine the
         * `? extends Animal` in every method signature of the List class. So what's the signature
         * of the get() method? (control+right-click on the get method on the line 98 to find out):
         * it looks like this:
         *
         *     E get(int index);
         *
         * and what happens when we replace the generic type placeholder with the actual instance
         * type parameter (or if that's too complicated: the E thing with the thing between < >)?
         *
         *
         *    (? super Animal) get(int index);
         *
         * so the return type of the `get` method of a `List<? super Animal>` is "some super-class of
         * Animal, but you won't know which one, it may as well be object".
         * The fact that we already put only animals in there doesn't really matter now, right? :)
         */

        // III. any invariant list of a super-type to the covariant list variable's type can be assigned to it
        a_list_of_things_whose_type_is_a_subtype_of_pet = Collections.singletonList(new Pet());
        a_list_of_things_whose_type_is_a_subtype_of_pet = Collections.singletonList(new Animal());
        // but not a list sub-types
        // ❌ a_list_of_things_whose_type_is_a_subtype_of_pet = new ArrayList<Cat>();
        // so what happens here?
        a_list_of_things_whose_type_is_a_subtype_of_pet = Collections.singletonList(new Cat());
        a_list_of_things_whose_type_is_a_subtype_of_pet = Collections.singletonList(new Dog());
    }
}

class PetProcessor {
    Pet process(Pet input) { return null; }
}

class CatProcessor extends PetProcessor {
    @Override
    public Cat process(Pet input) { return null; }
}

// collection's type parameter tells us both what does the collection PRODUCE and what does it CONSUME

// thus collection's variance is tricky

// function has two type parameters, one is always produced, one is always consumed



class _Function {
    public static void main(String[] args) {
        // Function<PARAMETER TYPE, RETURN TYPE>

        // invariant example
        Function<Animal, Integer> fn = animal -> animal.toString().length();

        // bounded example
        Function<? super Animal, ? extends Number> fn2 = animalAtMost -> animalAtMost.toString().length();
        Function<? super Pet, ? extends Number> fn3 = petAtMost -> petAtMost.toString().length();
        Function<? super Cat, ? extends Number> fn4 = (Cat catAtMost) -> catAtMost.toString().length();

        // ❓ which of the 3 declarations above is the most restrictive?

        Function<? extends Animal, ? super Number> uselessFunction = (Animal animal) -> animal.toString().length();
        // such a function declaration is rather useless, although we managed to declare it:
        // 1. we can only "get" an object from it
        // 2. we have no idea what we can "put" through it
        Object apply = uselessFunction.apply(null);

        // one last example
        Consumer<? super Cat> consumer_that_at_least_can_consume_cats = (Cat cat) -> {
            // https://1.fwcdn.pl/an/np/867323/2018/15835_1.7.jpg
            System.out.println("om nom nom");
            System.out.println("I only consume cats");
        };
        consumer_that_at_least_can_consume_cats = (Animal animal) -> {
            System.out.println("I consume any animal");
            System.out.println("So a cat is fine too");
            System.out.println("Which puts me within the bound of Consumer<? super Cat>");
        };
    }
}

// parameter types may also be covariant (or: bound down from Object)
class ListOfSomeKindOfAnimals<A extends Animal> extends ArrayList<A> {
    public void feedingTime() {
        forEach(Animal::eat);
    }
}

// contravariant parameter types are not allowed – because inheritance is covariant in itself
// ❌ class PetHouse<P super Pet> extends ArrayList<P> { }

// SUMMARY:
// this is all very abstract now – but mind this
//   1. invariant generic type means full knowledge of what you get and what you put "inside"
//   2. covariannce and contravariance are type BOUNDS – not specific types
//   3. covariiance and contravariance are DIRECT OPPOSITES

// _________          _______    _______  _______  _______  _______ _________   ______  _________         _________ ______   _______
// \__   __/|\     /|(  ____ \  (  ____ \(  ____ )(  ____ \(  ___  )\__   __/  (  __  \ \__   __/|\     /|\__   __/(  __  \ (  ____ \
//    ) (   | )   ( || (    \/  | (    \/| (    )|| (    \/| (   ) |   ) (     | (  \  )   ) (   | )   ( |   ) (   | (  \  )| (    \/
//    | |   | (___) || (__      | |      | (____)|| (__    | (___) |   | |     | |   ) |   | |   | |   | |   | |   | |   ) || (__
//    | |   |  ___  ||  __)     | | ____ |     __)|  __)   |  ___  |   | |     | |   | |   | |   ( (   ) )   | |   | |   | ||  __)
//    | |   | (   ) || (        | | \_  )| (\ (   | (      | (   ) |   | |     | |   ) |   | |    \ \_/ /    | |   | |   ) || (
//    | |   | )   ( || (____/\  | (___) || ) \ \__| (____/\| )   ( |   | |     | (__/  )___) (___  \   /  ___) (___| (__/  )| (____/\
//    )_(   |/     \|(_______/  (_______)|/   \__/(_______/|/     \|   )_(     (______/ \_______/   \_/   \_______/(______/ (_______/
//
//
//        COVARIANT             |      CONTRAVARIANT
//  - get()                     | - put(), set()
//  - safe to GET bound type    | - safe to PUT bound type
//  - extends                   | - super
//  - an unknown subtype of ... | - an unknown supertype of...
//  - T <: Bound                | - T >: Bound
//  - producer                  | - consumer
//  - return type               | - method parameter
//  - C# `out` keywowrd         | – C# `in` keyword
//                              |
// 💡💡💡💡💡💡💡💡💡
// one potentially useful mnemonic: PECS
// Producer Extends, Consumer Super
// – because extends is useful for the TYPES WE PRODUCE, super is useful for the type WE CONSUME

////////////////////////////////////////////////////////////////////////////////////
// domain§

class Animal {
    public void eat() {}

    public String toString() { return "animal"; }
}

class Pet extends Animal {
    public String toString() { return "pet"; }
}

class Dog extends Pet {
    public String toString() { return "dog"; }
}

class Cat extends Pet {
    public void meow() {}

    public String toString() { return "cat"; }
}