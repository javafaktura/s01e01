package io.github.javafaktura.s01.e01;

import io.github.javafaktura.domain.olx.DogAd;

import java.util.*;

import static io.github.javafaktura.util.Pretty.____________;
import static io.github.javafaktura.util.Pretty.verboseCollectionPrint;

class MostIntereestingCollections {
    // note the awesome usage of a generic return type 😎
    public static <T extends Collection<DogAd>> T fillWithDogAds(T collection) {
        collection.add(new DogAd("Smieszny piesel", 420.0, "brak"));
        collection.add(new DogAd("Akita Inu -ZKwP (FCI) po Championach", 4500.0, "ZKwP"));
        collection.add(new DogAd("Smieszny piesel", 420.0, "brak"));
        collection.add(new DogAd("York Yorkshire Terrier Rodowodowe FCI", 1700.0, "FCI"));
        collection.add(new DogAd("Hawańczyki, piękna, rodowodowasuczka ZKwP/FCI", 4000.0, "ZKwP"));
        return collection;
    }

    public static void main(String[] args) {
        ____________("ArrayList");
        verboseCollectionPrint(
                fillWithDogAds(
                        new ArrayList<>()
                ));

        ____________("LinkedList");
        verboseCollectionPrint(
                fillWithDogAds(
                        new LinkedList<>()
                ));

        ____________("LinkedList + Collections.sort");
        LinkedList<DogAd> listForSorting = fillWithDogAds(new LinkedList<>());
        Collections.sort(listForSorting);
        verboseCollectionPrint(
                listForSorting);

        ____________("HashSet");
        verboseCollectionPrint(
                fillWithDogAds(
                        new HashSet<>()
                ));

        ____________("LinkedHashSet");
        verboseCollectionPrint(
                fillWithDogAds(
                        new LinkedHashSet<>()
                ));

        ____________("TreeSet");
        verboseCollectionPrint(
                fillWithDogAds(
                        new TreeSet<>()
                ));

        ____________("HashSet(List)");
        verboseCollectionPrint(
                new HashSet<>(
                        fillWithDogAds(
                                new ArrayList<>()
                        )));
    }
}

// multimap is a data structure that is basically a Map<T, ? extends List<R>>
// i.e. a map whose values are lists, providing a special type of put: put(key, valueElement)
// that:
// - if the map doesn't contain the key, the `valueElement` is first put within a new list container
//   (like Arrays.asList(valueElement)) and that list is the actual map value associated with that list
// - if the map already contains the `key`, then the associated value is not overridden with a new List
//   but rather the `valueElement` is added to this existing list
//
// this kind of lists is used, e.g., for storing HTTP header values – e.g. a multi map of headers
// may associate the key Accept-Encoding with a list of values of length 2: "gzip" and "deflate"
//
// Multimap implementations are provided by popular utility libraries, like commons-collections and guava
//
// but Java 8 lets us easily create a multimap put method (which we could then have, e.g. in a Multimap wrapper class)
class HomebrewMultimap {
    public static void main(String[] args) {
        Map<String, List<DogAd>> dogAdsByFederation = new HashMap<>();

        multiPut(dogAdsByFederation, new DogAd("Akita Inu -ZKwP (FCI) po Championach", 4500.0, "ZKwP"));
        multiPut(dogAdsByFederation, new DogAd("Złote Shih-tzu z metryką ZKwP FCI Łódź", 3000.0, "ZKwP"));
        multiPut(dogAdsByFederation, new DogAd("Yorki", 2000.0, "inny"));
        multiPut(dogAdsByFederation, new DogAd("maltańczyk miniaturka , pieski z włosem - rodowód FCI -ZKwP", 4500.0, "ZKwP"));

        System.out.println(dogAdsByFederation.get("ZKwP"));
        System.out.println(dogAdsByFederation.get("inny"));
    }

    private static void multiPut(Map<String, List<DogAd>> dogAdsByFederation, DogAd dogAd) {
        // this is how it was done before Java 8
        // do the `computeIfAbsent` Intellij IDEA refactoring to make it look so much better :)
        // (hit alt-enter over the `prevList == null` part)
        // in the end you can put it all in a single line of code (not even a too long one)
        List<DogAd> prevList = dogAdsByFederation.get(dogAd.getLineage());
        if (prevList == null) {
            prevList = new ArrayList<>();
            dogAdsByFederation.put(dogAd.getLineage(), prevList);
        }
        prevList.add(dogAd);
    }
}

// homebrew bag?
//
// bag is a type of collection used to count the number of occurrences of a value
// bags are too provided by popular utility libraries
// but perhaps now it is also easy enough to have bag-like methods over a good old hash-map?
//
// hint: just reuse most of the code above, but instead of a Map<T, List<R>>, our underlying map
//       would be defined as Map<T, Integer>