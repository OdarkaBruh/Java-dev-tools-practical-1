
package nokhrina.daria;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;

public class App {
    /** Main logger for the App.class */
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        logger.debug("Program is started!");
        try {
            new Task().runTask();
            logger.debug("Program is finished successfully!");
        } catch (FileNotFoundException e) {
            logger.error("File wasn't found: ", e);
        } catch (InvalidPropertiesFormatException e) {
            logger.error("Unknown mode was specified: ", e);
        } catch (NullPointerException e) {
            if (e.getMessage().equals("username==null")) {
                logger.error("Username is null or isn't specified.", e);
            } else if (e.getMessage().equals("mode==null")) {
                logger.error("Mode is null or isn't specified.", e);
            } else {
                logger.error("Unknown NullPointerException ", e);
            }
        } catch (IOException e) {
            logger.error("Program wasn't finished due to error: \n", e);
        }
    }

    /**
     * Depending on the mode (xml/json) specified in the properties, creates a mapper and writes value in the file
     * <p>
     * //* @param properties the properties from the outside file
     *
     * @param classMessage    the value to be written
     * @param fileName the name of the file to which write
     * @throws IOException if properties doesn't contain mode
     */
}
