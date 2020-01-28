import dev.boiarshinov.util.OsmSearchHandler;

import java.io.IOException;

public class TestCaching {
    public static void main(String[] args) {
        OsmSearchHandler osmSearchHandler = OsmSearchHandler.getInstance();
        try {
            osmSearchHandler.search("Москва");
            osmSearchHandler.search("Москва");
        } catch (IOException e){
            System.out.println("Something went wrong");
        }
    }
}
