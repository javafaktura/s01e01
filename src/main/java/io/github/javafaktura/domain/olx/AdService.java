package io.github.javafaktura.domain.olx;

import java.util.List;

public interface AdService {
    List<? extends DogAd> dogAds(int maxDogs);

    List<? extends Ad> animalAds(int maxAnimals);
}