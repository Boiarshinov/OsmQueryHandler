package dev.boiarshinov.util;

import dev.boiarshinov.dto.CenterDTO;
import dev.boiarshinov.dto.CoordinatesDTO;
import dev.boiarshinov.model.GeoObject;
import dev.boiarshinov.model.geojson.Coordinate;

public class ResponsePreparer {

    private ResponsePreparer(){}

    public static CoordinatesDTO getCoordinates(GeoObject geoObject){
        Coordinate[] coordinates = geoObject.geoJson.getCoordinateArray();
        double[][] array = new double[coordinates.length][2];
        for (int i = 0; i < coordinates.length; i++) {
            array[i] = coordinates[i].convertToDoubleArray();
        }
        CoordinatesDTO coordinatesDTO = new CoordinatesDTO();
        coordinatesDTO.setCoordinates(array);
        return coordinatesDTO;
    }

    public static CenterDTO getCenter(GeoObject geoObject){
        Coordinate center = geoObject.geoJson.getCenter();
        CenterDTO centerDTO = new CenterDTO();
        centerDTO.setCenter(new double[]{center.latitude, center.longitude});
        return centerDTO;
    }
}
