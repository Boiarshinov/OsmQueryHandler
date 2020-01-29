package dev.boiarshinov.dto;

import java.io.Serializable;

public class CenterDTO implements Serializable {
    private double[] center;

    public double[] getCenter() {
        return center;
    }

    public void setCenter(double[] center) {
        this.center = center;
    }
}
