package dev.boiarshinov.controllers;

import dev.boiarshinov.dto.CenterDTO;
import dev.boiarshinov.dto.CoordinatesDTO;
import dev.boiarshinov.dto.GeoObject;
import dev.boiarshinov.geojson.Coordinate;
import dev.boiarshinov.util.GeoObjectService;
import dev.boiarshinov.service.OsmSearchHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GeoObjectController {
    private OsmSearchHandler osmSearchHandler;
    private Logger logger = LoggerFactory.getLogger(GeoObjectController.class);

    @RequestMapping("/coordinates")
    public CoordinatesDTO getCoordinates(
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "type", required = false) String type) {
        GeoObject geoObject = getGeoObject(name, type);
        CoordinatesDTO response = GeoObjectService.getCoordinates(geoObject);
        return response;
    }

    @RequestMapping("/center")
    public CenterDTO getCenter(
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "type", required = false) String type) {
        GeoObject geoObject = getGeoObject(name, type);
        CenterDTO centerDTO = GeoObjectService.getCenter(geoObject);
        return centerDTO;
    }

    private GeoObject getGeoObject(String name, String type) {
        try {
            if (type == null) {
                return osmSearchHandler.search(name);
            } else {
                if (!getTypeRestrictions().contains(type.toLowerCase())) {
                    throw new IllegalArgumentException(type + "is not defiened");
                }
                return osmSearchHandler.search(name, type);
            }
        } catch (IOException e) {
            logger.error("Search crashes", e);
            e.printStackTrace();
        }
        return null;
    }

    @Autowired
    public void setOsmSearchHandler(OsmSearchHandler osmSearchHandler) {
        this.osmSearchHandler = osmSearchHandler;
    }

    private List<String> getTypeRestrictions() {
        List<String> typeRestrictions = new ArrayList<>();
        typeRestrictions.add("street");
        typeRestrictions.add("city");
        typeRestrictions.add("county");
        typeRestrictions.add("country");
        typeRestrictions.add("postalcode");
        return typeRestrictions;
    }
}
