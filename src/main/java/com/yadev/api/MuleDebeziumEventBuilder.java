package com.yadev.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MuleDebeziumEventBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(MuleDebeziumEventBuilder.class);
    public static String buildDebeziumEvent(String event) throws JsonProcessingException {
        LOGGER.info("Preparing CDC Event");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(event);
        if (rootNode.path("payload").path("ddl").isMissingNode()){
            ObjectNode response = (ObjectNode) rootNode.path("payload");
            response.remove("source");
            LOGGER.info("Removed source");
            response.remove("op");
            LOGGER.info("Removed op");
            response.remove("ts_ms");
            LOGGER.info("Removed ts_ms");
            response.remove("transaction");
            LOGGER.info("Removed transaction");
            return objectMapper.writeValueAsString(response);
        }else {
                return "";
        }
    }

}
