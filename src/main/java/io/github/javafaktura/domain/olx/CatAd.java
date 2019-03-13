package io.github.javafaktura.domain.olx;

import java.util.Objects;

public class CatAd extends Ad implements Gratka.HasLineage {
    private final String lineage;

    public CatAd(String title, Double price, String lineage) {
        super(title, price);
        this.lineage = lineage;
    }

    public String getLineage() {
        return lineage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CatAd dogAd = (CatAd) o;
        return Objects.equals(lineage, dogAd.lineage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lineage);
    }

    @Override
    public String toString() {
        return "CatAd{" +
                "title='" + title + '\'' +
                ", price=" + price +
                ", lineage='" + lineage + '\'' +
                '}';
    }
}
