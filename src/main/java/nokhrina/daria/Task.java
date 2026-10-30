package nokhrina.daria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.dataformat.xml.XmlMapper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;

public class Task {
    /** Main logger for the App.class */
    private static final Logger logger = LoggerFactory.getLogger(Task.class);

    private static final String PROPERTY_USERNAME_FIELD_IS_CALLED = "username";
    private static final String PROPERTY_FILE_NAME = "outside";

    private final PropertyReader propertyReader = new PropertyReader();

    private static final String SYSTEM_PROPERTY_NAME = "output_mode";
    private static final String OUTPUT_FILE_NAME = "output";


    void runTask() throws IOException {
        logger.debug("Task is started!");
        Message message = createMessageInstance(PROPERTY_FILE_NAME, PROPERTY_USERNAME_FIELD_IS_CALLED);
        writeMessage(message);
        logger.debug("Task is finished!");
    }


    private Message createMessageInstance(String propertiesFileName, String propertiesFieldName) throws IOException {
        Message test = new Message();
        String message = propertyReader.getFromFile(propertiesFileName, propertiesFieldName);
        if (message == null)  throw new NullPointerException("username==null");
        test.setMessage("Привіт " + message);
        logger.debug("Test class is ready to be written: \"{}\"", test.getMessage());
        return test;
    }

    private ObjectMapper getMapperByMode(String propertyName) throws IOException{
        String mode = propertyReader.getSystem(propertyName);

        if (mode == null) throw new NullPointerException("mode==null");
        logger.debug("Mode is loaded. Mode = {}", mode);

        if (mode.equalsIgnoreCase("json")) return new ObjectMapper();
        else if (mode.equalsIgnoreCase("xml")) return new XmlMapper();
        else throw new InvalidPropertiesFormatException("Unknown mode: " + mode);
    }

    private void writeMessage(Message classMessage) throws IOException {
        ObjectMapper objectMapper = getMapperByMode(SYSTEM_PROPERTY_NAME);
        System.out.println(objectMapper.writeValueAsString(classMessage));
    }
}
