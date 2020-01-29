package dev.boiarshinov.controllers;

import dev.boiarshinov.dto.CenterDTO;
import dev.boiarshinov.dto.CoordinatesDTO;
import dev.boiarshinov.model.GeoObject;
import dev.boiarshinov.service.MainService;
import dev.boiarshinov.util.ResponsePreparer;
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
    private MainService mainService;
    private Logger logger = LoggerFactory.getLogger(GeoObjectController.class);

    @RequestMapping("/coordinates")
    public CoordinatesDTO getCoordinates(
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "type", required = false) String type) {
        GeoObject geoObject = getGeoObject(name, type);
        CoordinatesDTO response = ResponsePreparer.getCoordinates(geoObject);
        return response;
    }

    @RequestMapping("/center")
    public CenterDTO getCenter(
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "type", required = false) String type) {
        GeoObject geoObject = getGeoObject(name, type);
        CenterDTO centerDTO = ResponsePreparer.getCenter(geoObject);
        return centerDTO;
    }

    private GeoObject getGeoObject(String name, String type) {
        try {
            if (type == null) {
                return mainService.service(name);
            } else {
                if (!getTypeRestrictions().contains(type.toLowerCase())) {
                    throw new IllegalArgumentException(type + "is not defiened");
                }
                return mainService.service(name, type);
            }
        } catch (IOException e) {
            logger.error("Search crashes", e);
            e.printStackTrace();
        }
        return null;
    }

    @Autowired
    public void setMainService(MainService mainService) {
        this.mainService = mainService;
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
