package dev.boiarshinov.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.boiarshinov.model.GeoObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class GeoObjectParser {
    private Logger logger = LoggerFactory.getLogger(GeoObjectParser.class);

    GeoObject parseJson(String jsonString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        GeoObject[] array = mapper.readValue(jsonString, GeoObject[].class);
        logger.debug("Parsing complete successfully!");
        if (array.length == 0) throw new IllegalArgumentException("JSON have no items");
        return array[0];
    }
}
