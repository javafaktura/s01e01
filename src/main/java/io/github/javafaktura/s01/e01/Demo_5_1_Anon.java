package io.github.javafaktura.s01.e01;

import io.github.javafaktura.domain.olx.Ad;
import io.github.javafaktura.domain.olx.GratkaMock;

import java.util.Comparator;

// javap -c
public class Demo_5_1_Anon {
    public static void main(String[] args) {
        new GratkaMock().animalAds(8)
                .sort(new Comparator<Ad>() {
                    @Override
                    public int compare(Ad o1, Ad o2) {
                        return o1.compareTo(o2);
                    }
                });
    }
}
