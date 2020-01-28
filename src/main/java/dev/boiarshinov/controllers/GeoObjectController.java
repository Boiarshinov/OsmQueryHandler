package dev.boiarshinov.controllers;

import dev.boiarshinov.dto.GeoObject;
import dev.boiarshinov.util.OsmSearchHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class GeoObjectController {
    private OsmSearchHandler osmSearchHandler;

    @RequestMapping("")
    public GeoObject getGeoObject(
            @RequestParam(value="name", required = true) String name,
            @RequestParam(value = "type", required = true) String type){
        try {
            return osmSearchHandler.search(name);
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Autowired
    public void setOsmSearchHandler(OsmSearchHandler osmSearchHandler) {
        this.osmSearchHandler = osmSearchHandler;
    }
}
