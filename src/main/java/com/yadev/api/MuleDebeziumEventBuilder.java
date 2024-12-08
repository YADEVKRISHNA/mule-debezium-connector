package com.yadev.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class MuleDebeziumEventBuilder {
	private static final Logger LOGGER = LoggerFactory.getLogger(MuleDebeziumEventBuilder.class);

	public static String buildDebeziumEvent(String event, boolean schemasEnabled) throws JsonProcessingException {
		LOGGER.debug("Preparing CDC Event");
		LOGGER.debug(event);
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode debeziumEvent = objectMapper.readTree(event);

		if ((!schemasEnabled)) {
			if (debeziumEvent.path("ddl").isMissingNode()) {
				ObjectNode response = (ObjectNode) debeziumEvent;
				response.remove("source");
				LOGGER.debug("Removed source");
				response.remove("op");
				LOGGER.debug("Removed op");
				response.remove("ts_ms");
				LOGGER.debug("Removed ts_ms");
				response.remove("transaction");
				LOGGER.debug("Removed transaction");
				return objectMapper.writeValueAsString(response);
			}
			LOGGER.debug(event);
			return "";

		} else {

			if ((debeziumEvent.path("payload").path("ddl").isMissingNode())) {
				if (debeziumEvent.path("ddl").isMissingNode()) {
					ObjectNode response = (ObjectNode) debeziumEvent.path("payload");
					response.remove("source");
					LOGGER.debug("Removed source");
					response.remove("op");
					LOGGER.debug("Removed op");
					response.remove("ts_ms");
					LOGGER.debug("Removed ts_ms");
					response.remove("transaction");
					LOGGER.debug("Removed transaction");
					return objectMapper.writeValueAsString(response);
				}
			}

			LOGGER.debug(event);
			return "";
		}

	}
}