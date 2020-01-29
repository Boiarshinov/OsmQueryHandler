package dev.boiarshinov.dto;

import java.io.Serializable;

public class CoordinatesDTO implements Serializable {
    private double[][] coordinates;

    public double[][] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[][] coordinates) {
        this.coordinates = coordinates;
    }
}
