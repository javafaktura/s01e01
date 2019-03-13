package io.github.javafaktura.s01.e01;

import io.github.javafaktura.domain.olx.*;

import java.util.ArrayList;
import java.util.List;

import static io.github.javafaktura.util.Pretty.____________;

public class Demo_2_2_Olx {
    private static AdService adService = new Gratka();
  // if no internet connection: // private static AdService adService = new GratkaMock();

    private static AdFilter<Ad> lessThan1000Pln = (Ad ad) ->
            ad.getPrice() < 1000;
    private static AdFilter<DogAd> withLineage = (DogAd dogAd) ->
            dogAd.getLineage() != null && !"brak".equals(dogAd.getLineage());

    public interface AdFilter<AD> {
        boolean allows(AD ad);
    }

    public static void main(String[] args) {
        ____________("Dog Ads");
        List<? extends DogAd> dogAds = adService.dogAds(10);
        // try also: List<DogAd> dogAds = adService.dogAds(10);

        List<AdFilter<? super DogAd>> filterPipeline = new ArrayList<>();
        filterPipeline.add(lessThan1000Pln);
        filterPipeline.add(withLineage);

        for (AdFilter<? super DogAd> adFilter : filterPipeline) {
            dogAds.removeIf(next -> !adFilter.allows(next));
        }
        System.out.println(dogAds);
    }
}
