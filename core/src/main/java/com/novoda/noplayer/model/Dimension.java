package com.novoda.noplayer.model;

public class Dimension {

    private final int width;
    private final int heigh;

    public static Dimension from(int width, int heigth) {
        return new Dimension(width, heigth);
    }

    private Dimension(int width, int heigh) {
        this.width = width;
        this.heigh = heigh;
    }


    public int width() {
        return width;
    }

    public int height() {
        return heigh;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dimension)) return false;

        Dimension dimension = (Dimension) o;

        if (width != dimension.width) return false;
        return heigh == dimension.heigh;
    }

    @Override
    public int hashCode() {
        int result = width;
        result = 31 * result + heigh;
        return result;
    }

    @Override
    public String toString() {
        return "Dimension{" +
                "width=" + width +
                ", heigh=" + heigh +
                '}';
    }
}
