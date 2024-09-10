package org.graviton.data.access;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonReader {
    public JsonNode readJsonNodeFromFile(String filePath) {
        try {
            byte[] jsonData = Files.readAllBytes(Paths.get(filePath));
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(jsonData);
        } catch (IOException e) {
            //log and throw exception
            throw new RuntimeException(e);
        }
    }
}
