public class Coordinate {
    double latitude;
    double longitude;

    public Coordinate(double longitude, double latitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Coordinate(double[] coordinate) {
        this(coordinate[0], coordinate[1]);
    }

    public double[] convertToDoubleArray() {
        return new double[]{longitude, latitude};
    }

    @Override
    public String toString() {
        return "[" + longitude + ", " + latitude + "]\n";
    }
}
