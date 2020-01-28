package dev.boiarshinov.controllers;

import dev.boiarshinov.dto.GeoObject;
import dev.boiarshinov.OsmSearchHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class GeoObjectController {

    @RequestMapping("")
    public GeoObject getGeoObject(
            @RequestParam(value="name", required = true) String name,
            @RequestParam(value = "type", required = true) String type){
        try {
            GeoObject geoObject = OsmSearchHandler.getInstance().search(name);
            return geoObject;
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
