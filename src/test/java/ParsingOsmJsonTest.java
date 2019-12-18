import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ParsingOsmJsonTest {
    public static void main(String[] args) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("./src/main/resources/test.json"))){
            while (reader.ready()){
                stringBuilder.append(reader.readLine());
            }
        }
        String jsonString = stringBuilder.toString();

        ObjectMapper mapper = new ObjectMapper();
        RFSubject[] array = mapper.readValue(jsonString, RFSubject[].class);

        System.out.println("Parsing complete successfully!\n");

        System.out.println(array[0]);
    }
}
