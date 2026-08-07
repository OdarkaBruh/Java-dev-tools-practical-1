package nokhrina.daria;

import com.fasterxml.jackson.annotation.JsonProperty;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

/**
 * The class which instance will be written using Jackson
 */
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
class Message {
    /** Message to be written */
    @JsonProperty
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

}
