package io.github.javafaktura.domain.olx;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Gratka implements AdService {

    @Override
    public List<DogAd> dogAds(int maxDogs) {
        return ads(maxDogs, yoloConnect("https://gratka.pl/zwierzeta/psy"), this::mapDog);
    }

    @Override
    public List<Ad> animalAds(int maxAnimals) {
        return ads(maxAnimals, yoloConnect("https://gratka.pl/zwierzeta"), this::mapAnimal);
    }

    private <T extends Ad> List<T> ads(int maxAnimals, Document doc, Function<Document, T> mapper) {
        return doc.getElementsByTag("article")
                .subList(0, maxAnimals) // limit so that we won't get blocked too fast
                .parallelStream()
                .map(a -> a.attr("data-href"))
                .peek(href -> System.out.println("retrieving " + href))
                .map(Gratka::yoloConnect)
                .map(mapper)
                .collect(Collectors.toList());
    }

    private Ad mapAnimal(Document adPage) {
        Elements breadcrumbs = adPage.select("a.breadcrumb__link");
        Element lastCrumb = breadcrumbs.get(breadcrumbs.size() - 1);
        String href = lastCrumb.attr("href");
        if (href.contains("/psy/")) {
            return mapDog(adPage);
        } else if (href.contains("/koty/")) {
            return mapCat(adPage);
        } else {
            return new Ad(
                    adPage.select("h1").first().text(),
                    price(adPage)
            );
        }
    }

    private CatAd mapCat(Document adPage) {
        return new CatAd(
                adPage.select("h1").first().text(),
                price(adPage),
                adPage.select(".parameters__rolled li")
                        .stream()
                        .filter(p -> p.text().contains("Rodowód"))
                        .findAny()
                        .map(e -> e.select("b").text())
                        .orElse(null)
        );
    }

    private DogAd mapDog(Document adPage) {
        return new DogAd(
                adPage.select("h1").first().text(),
                price(adPage),
                adPage.select(".parameters__rolled li")
                        .stream()
                        .filter(p -> p.text().contains("Rodowód"))
                        .findAny()
                        .map(e -> e.select("b").text())
                        .orElse(null)
        );
    }

    private static Document yoloConnect(String href) {
        try {
            return Jsoup.connect(href).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Double price(Document adPage) {
        return Optional.ofNullable(adPage.select(".creditCalc__price"))
                .map(Elements::first)
                .map(Element::text)
                .map(s -> s.replaceAll("[^\\d]", ""))
                .map(Double::valueOf)
                .orElse(-1.0);
    }

    public interface HasLineage {
        String getLineage();
    }

}
