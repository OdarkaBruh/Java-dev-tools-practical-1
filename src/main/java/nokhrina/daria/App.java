
package nokhrina.daria;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.io.*;
import java.util.Properties;

/**
 * This program:
 *  1. Creates instance of the Test class.
 *      1.1. It reads properties from the file inside (source => main => resources => *.properties)
 *      1.2. Creates String message "hello {username}", where username is received from previous properties.
 *  2. Reads properties from OUTSIDE file (file must be in the same folder as .jar)
 *  3. If there is a property "output_mode"...
 *      ...and it equals to "json", ObjectMapper will be created.
 *      ...and it equals to "xml", XmlMapper will be created.
 *  4. Created mapper saves Test instance.
 */
public class App {
    /** Main logger for the App.class */
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    /** INSIDE properties file which contains username */
    static final String classPathFileName = "inside";
    /** OUTSIDE properties file which contains mode (properties field should be named "output_mode") */
    static final String outputFileName = "result";
    /**
     * OUTSIDE properties file which contains mode
     * Properties field should be named "output_mode" and equals to "xml" or "json" (not case-sensitive)
     */
    static final String outsideFileName = "outside";

    public static void main(String[] args) {
        logger.debug("Program is started!");

        Test test = new Test();
        test.createMessage(classPathFileName);
        logger.debug("Test class is ready to be written.");

        Properties outsideProps = readPropertiesFromOutsideFile(outsideFileName);
        try {
            writeWithMapper(outsideProps, test, outputFileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        logger.debug("Program is finished!");
        System.out.println("DONE");
    }

    /**
     * Reads properties from the file which is located INSIDE the jar
     *
     * @param fileName the file to read
     * @return received properties
     */
    private static Properties readPropertiesFromOutsideFile(String fileName) {
        logger.trace("Preparing to read OUTSIDE properties file. Filename = \"{}\"", fileName);
        Properties outsideProps = new Properties();
        try {
            outsideProps.load(new FileInputStream("./" + fileName + ".properties"));
            logger.debug("Outside file found and loaded. Mode = {}", outsideProps.getProperty("output_mode"));
        } catch (FileNotFoundException e) {
            logger.error("File from outside with name {} isn't found.", fileName);
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return outsideProps;
    }

    /**
     * Depending on the mode (xml/json) specified in the properties, creates a mapper and writes value in the file
     *
     * @param properties the properties from the outside file
     * @param value      the value to be written
     * @param fileName   the name of the file to which write
     * @throws IOException if properties doesn't contain mode
     */
    private static void writeWithMapper(Properties properties, Object value, String fileName) throws IOException {
        logger.trace("Preparing to write a mapper");
        String mode = properties.getProperty("output_mode");
        if (mode == null) {
            throw new RuntimeException();
        } else if (mode.equalsIgnoreCase("json")) {
            ObjectMapper objectMapper = new ObjectMapper();
            logger.debug("ObjectMapper is created");
            objectMapper.writeValue(new FileOutputStream("./" + fileName + "_output.json"), value);
        } else if (mode.equalsIgnoreCase("xml")) {
            XmlMapper objectMapper = new XmlMapper();
            logger.debug("XmlMapper is created");
            objectMapper.writeValue(new FileOutputStream("./" + fileName + "_output.xml"), value);
        } else {
            logger.error("Unknown mode \"{}\" ", mode);
            return;
        }
        logger.debug("Value is written!");
    }
}

/**
 * The class which instance will be written using Jackson
 */
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
class Test {
    /** Sub-logger for more clear logging */
    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    /** Username inside properties file */
    private String username;
    /** Message to be written */
    @JsonProperty
    private String message;

    /** Constructor, just logs successful creation */
    Test() {
        logger.trace("Test is created!");
    }

    /**
     * Creates message to be written, which consists of property "username" which is read from inside file
     *
     * @param fileName the name of the file to read from
     */
    void createMessage(String fileName) {
        logger.trace("Preparing to read INSIDE properties file. Filename = \"{}\"", fileName);
        Properties properties = readProperties(fileName);
        username = properties.getProperty("username");
        if (username == null) logger.warn("Username property doesn't exist (is null)");
        message = "Привіт! " + username;
        logger.trace("Message has been created.");
    }

    /**
     * Reads properties from the inside file
     *
     * @param fileName the name of the file to read from
     * @return received properties
     */
    private static Properties readProperties(String fileName) {
        Properties appProps = new Properties();
        try {
            appProps.load(new InputStreamReader(App.class.getResourceAsStream("/" + fileName + ".properties")));
        } catch (NullPointerException e) {
            logger.error("File with name \"{}\" is not found.", fileName);
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return appProps;
    }
}
