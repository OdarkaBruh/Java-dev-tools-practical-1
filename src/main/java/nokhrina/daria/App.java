
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
import java.util.logging.Level;

/**
 * Hello world!
 *
 */
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    static final String classPathPropertiesFileName = "app";
    static final String outsidePropertiesFileName = "hello";

    public static void main(String[] args) {
        logger.debug("Program is started!");

        Test test = new Test(classPathPropertiesFileName);
        logger.debug("Test is created");

        logger.trace("Preparing to read outside file.");
        Properties outsideProps = new Properties();
        try {
            outsideProps.load(new FileInputStream("../"+outsidePropertiesFileName+".properties"));
            logger.debug("Outside file found and loaded");
        } catch (FileNotFoundException e) {
            logger.error("File from outside with name \""+outsidePropertiesFileName+"\" isn't found. Mode is set to json.");
            outsideProps.setProperty("output_mode", "json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        logger.trace("Preparing to write.");
        try {
            File file = new File("./"+classPathPropertiesFileName + "_output");

            if (file.exists()) logger.trace("The file to be written to already exists.");
            else {
                logger.trace("The file to be written to don't exists. Creating a new one.");
                file.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            writeWithMapper(outsideProps, test);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("DONE");
    }

    private static void writeWithMapper(Properties properties, Object value) throws IOException {
        if (properties.getProperty("output_mode").toLowerCase().equals("json")) {
            ObjectMapper objectMapper =  new ObjectMapper();
            logger.debug("ObjectMapper is created");
            objectMapper.writeValue(new FileOutputStream("./"+classPathPropertiesFileName+"_output.json"), value);
        } else {
            XmlMapper objectMapper = new XmlMapper();
            logger.debug("XmlMapper is created");
            objectMapper.writeValue(new FileOutputStream("./"+classPathPropertiesFileName+"_output.xml"), value);
        }
        System.out.println("??");
        logger.debug("Written!");
    }

    private static void write(ObjectMapper mapper, Test valueToWrite, String fileName){
        try {
            mapper.writeValue(new FileOutputStream(fileName), valueToWrite);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
class Test {
    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    private String username;
    private String text;

    @JsonProperty
    private String message;

    Test(String fileName) {
        logger.trace("Constructor is called (filename = \""+fileName+"\")");
        Properties properties = readProperties(fileName);
        username = properties.getProperty("username");
        text = properties.getProperty("text");
        message = "Привіт! "+ username ;
        logger.trace("Constructor is finished.");
    }

    private static Properties readProperties(String fileName) {
        Properties appProps = new Properties();
        try {
            appProps.load(new InputStreamReader(App.class.getResourceAsStream("/"+fileName+".properties")));
        } catch (NullPointerException e) {
            logger.error("File with name \""+ fileName + "\" is not found. Properties WERE NOT LOADED");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return appProps;
    }
}
