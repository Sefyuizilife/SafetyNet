package com.safetynet.safetynetalerts.services.json;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class JSONDataReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(JSONDataReader.class);

    public static JSONData readJsonFile(String jsonPath) {

        JSONData jsonData = new JSONData();

        if (jsonPath != null) {

            try {

                jsonData = new ObjectMapper().readValue(new File(jsonPath), JSONData.class);
                LOGGER.info("JsonDataReader - The JSON file has loaded correctly");

            } catch (JsonParseException e) {

                LOGGER.warn("JsonDataReader - The specified JSON isn't valide");
                LOGGER.warn("JsonDataReader - The loaded JSON file is empty");

            } catch (JsonMappingException e) {

                LOGGER.warn("JsonDataReader - One of the keys is invalid");
                LOGGER.warn("JsonDataReader - The loaded JSON file is empty");

            } catch (IOException e) {

                LOGGER.warn("JsonDataReader - The specified path of the JSON file cannot be found");
                LOGGER.warn("JsonDataReader - The loaded JSON file is empty");
            }

        } else {

            LOGGER.info("JsonDataReader - No configuration found, the application will start with a blank database");
        }

        return jsonData;
    }
}
