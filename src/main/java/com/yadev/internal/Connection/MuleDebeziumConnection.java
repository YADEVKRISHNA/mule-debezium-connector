package com.yadev.internal.Connection;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.debezium.config.Configuration;

/**
 * This class represents an extension connection just as example (there is no
 * real connection with anything here c:).
 */
public final class MuleDebeziumConnection {
	private final Logger LOGGER = LoggerFactory.getLogger(MuleDebeziumConnection.class);
	Configuration config = null;
	Properties props = null;

	public MuleDebeziumConnection(Properties configProperties) {
        LOGGER.info("Initializing Debezium connection properties.");
		props = configProperties;

	}

	public Properties getConfiguration() {
		return props;
	}

	public void invalidate() {
		// do something to invalidate this connection!
	}
}
