package io.github.javafaktura.domain.olx;

import java.util.Comparator;
import java.util.Objects;

public class Ad implements Comparable<Ad> {
    protected final String title;
    protected final Double price;

    public Ad(String title, Double price) {
        this.title = title;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public Double getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ad ad = (Ad) o;
        return Objects.equals(title, ad.title) &&
                Objects.equals(price, ad.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, price);
    }

    @Override
    public String toString() {
        return "Ad{" +
                "title='" + title + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public int compareTo(Ad other) {
        return Comparator.comparing(Ad::getTitle)
                .thenComparing(Ad::getPrice)
                .compare(this, other);
    }
}
