package io.github.javafaktura.domain.olx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class GratkaMock implements AdService {
    List<Ad> ads = Arrays.asList(
            new DogAd("Berneński pies pasterski ZKwP FCI- wspaniałe pieski do odbioru. Śląsk Katowice", 3000.0, "ZKwP"),
            new Ad("papugi", 10.0),
            new DogAd("SPRZEDAM AMERICAN BULLY SZCZENIĘTA ABKC REZERWACJA", 3200.0, "ZKwP"),
            new DogAd("Akita Inu -ZKwP (FCI) po Championach", 500.0, "ZKwP"),
            new Ad("Stanówka Ogierem Oldenburskim Dream Rubin", -1.0),
            new Ad("Stanówka ogierem śląskim Rodan 81pkt", 400.0),
            new DogAd("Złote Shih-tzu z metryką ZKwP FCI Łódź", 3000.0, "null"),
            new DogAd("YORK,YORKI ,BIEWER - MAMY SZCZENIAKI !!!", 2500.0, "inny"),
            new DogAd("Szczeniaki Akita Inu z rodowodem", 1200.0, "inny"),
            new DogAd("Hawańczyki, piękna, rodowodowasuczka ZKwP/FCI", 4000.0, "ZKwP"),
            new Ad("amazonka żółtogardła oratrix para", 10.0),
            new DogAd("Szczeniaki Entlebucher z rodowodem, po championach", 3000.0, "FCI"),
            new DogAd("American Staffordshire Terrier -Amstaff", -1.0, "brak"),
            new DogAd("York Yorkshire Terrier Rodowodowe FCI", 1700.0, "null")
    );

    @Override
    public List<DogAd> dogAds(int maxDogs) {
        return new ArrayList<>(ads)
                .stream()
                .filter(DogAd.class::isInstance)
                .map(DogAd.class::cast)
                .collect(toList())
                .subList(0, Math.min(ads.size(), maxDogs));
    }

    @Override
    public List<Ad> animalAds(int maxAnimals) {
        return new ArrayList<>(ads)
                .subList(0, Math.min(ads.size(), maxAnimals));
    }
}
