import dev.boiarshinov.GeoObject;
import dev.boiarshinov.OsmSearchHandler;
import dev.boiarshinov.geojson.MultiPolygon;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class OsmSearchHandlerTest {
    private static OsmSearchHandler osmSearchHandler;

    @BeforeAll
    static void setUpClass() {
        osmSearchHandler = OsmSearchHandler.getInstance();
    }

    @Test
    void prepareURL() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, MalformedURLException {
        URL expected = new URL("https://nominatim.openstreetmap.org/search?q=Moscow&country=russia&format=json&polygon_geojson=1&limit=1");

        Method m = OsmSearchHandler.class.getDeclaredMethod("prepareURL", String.class);
        m.setAccessible(true);
        URL actual = (URL) m.invoke(osmSearchHandler, "Moscow");

        assertEquals(expected, actual);
    }

    @Test
    void readJsonStringFromURL() throws NoSuchMethodException, IOException, InvocationTargetException, IllegalAccessException {
        URL url = new URL("https://nominatim.openstreetmap.org/search?q=Moscow&country=russia&format=json&polygon_geojson=1&limit=1");
        String expectedJsonString = readJsonStringFromFile("./src/main/resources/test.json");

        Method m = OsmSearchHandler.class.getDeclaredMethod("readJsonStringFromURL", URL.class);
        m.setAccessible(true);
        String actualJsonString = (String) m.invoke(osmSearchHandler, url);

        //Delete all spaces and importance field, because it can change in time
        expectedJsonString = expectedJsonString.replaceAll(" ", "").
                replaceAll("\"importance\":\\d+.\\d+", "");
        actualJsonString = actualJsonString.replaceAll(" ", "").
                replaceAll("\"importance\":\\d+.\\d+", "");

        assertEquals(expectedJsonString, actualJsonString, "Check moscow map, maybe it changed, if so, update test.json");
    }

    @Test
    void parseJsonString() throws NoSuchMethodException, IOException, InvocationTargetException, IllegalAccessException {
        String expectedDisplayName = "Москва, Центральный административный округ, Москва, Центральный федеральный округ, Россия";
        String jsonString = readJsonStringFromFile("./src/main/resources/test.json");

        Method m = OsmSearchHandler.class.getDeclaredMethod("parseJsonString", String.class);
        m.setAccessible(true);
        GeoObject geoObject = (GeoObject) m.invoke(osmSearchHandler, jsonString);
        String actualDisplayName = geoObject.displayName;

        assertEquals(expectedDisplayName, actualDisplayName, "");
        assertTrue(geoObject.geoJson instanceof MultiPolygon);
    }

    private String readJsonStringFromFile(String filename) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            while (reader.ready()) {
                stringBuilder.append(reader.readLine());
            }
        }
        return stringBuilder.toString();
    }
}