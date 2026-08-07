package nokhrina.daria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.InvalidPropertiesFormatException;
import java.util.Objects;
import java.util.Properties;

public class PropertyReader {
    /**
     * //     * Reads properties from the file which is located INSIDE the jar
     * //     *
     * //     * @param fileName the file to read
     * //     * @return received properties
     * //
     */
    Properties getFromFile(String fileName) throws IOException {
        String fullName = fileName + ".properties";
        Properties props = new Properties();
        try (InputStreamReader inputStream = new InputStreamReader(new FileInputStream(fullName),
                StandardCharsets.UTF_8)) {
            props.load(inputStream);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File with name \""+ fileName + "\" does not exist.");
        }
        return props;
    }

    String getFromFile(String filename, String propertyName) throws IOException {
        return getFromFile(filename).getProperty(propertyName);
    }

    String getSystem(String propertyName) {
        return System.getProperty(propertyName);
    }

}
