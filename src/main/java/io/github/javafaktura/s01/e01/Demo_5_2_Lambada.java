package io.github.javafaktura.s01.e01;

import io.github.javafaktura.domain.olx.Ad;
import io.github.javafaktura.domain.olx.GratkaMock;

// javap -c
public class Demo_5_2_Lambada {
    public static void main(String[] args) {
        new GratkaMock().animalAds(8)
                .sort(Ad::compareTo);
    }
}
