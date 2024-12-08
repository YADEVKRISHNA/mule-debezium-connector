package com.yadev.internal.Source;

import static org.mule.runtime.extension.api.annotation.param.MediaType.APPLICATION_JSON;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.extension.api.runtime.source.Source;
import org.mule.runtime.extension.api.runtime.source.SourceCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yadev.api.DebeziumEventAttributes;
import com.yadev.api.MuleDebeziumAttributeBuilder;
import com.yadev.api.MuleDebeziumEventBuilder;
import com.yadev.api.MuleDebeziumSnapShotModes;
import com.yadev.internal.Connection.MuleDebeziumConnection;

import io.debezium.connector.mysql.MySqlConnectorConfig;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;

@Alias("CDC-Listener")
@MediaType(APPLICATION_JSON)
public class MuleDebeziumChangeDataCaptureListener extends Source<String, DebeziumEventAttributes> {

	private final Logger LOGGER = LoggerFactory.getLogger(MuleDebeziumChangeDataCaptureListener.class);

	@Parameter
	@Placement(order = 1)
	private ArrayList<String> tables;

	@Parameter
	@Placement(order = 2)
	@Optional(defaultValue = "false")
	@DisplayName("Schemas enable")
	private boolean schemasEnable;

	@Parameter
	@Placement(order = 3)
	@Optional(defaultValue = "INITIAL")
	@DisplayName("Snapshot mode")
	private MuleDebeziumSnapShotModes snapShotMode;

	@Connection
	private ConnectionProvider<MuleDebeziumConnection> MuledebeziumConnectionConnectionProvider;
	private DebeziumEngine<ChangeEvent<String, String>> engine = null;

	@Override
	public void onStart(SourceCallback<String, DebeziumEventAttributes> sourceCallback) throws MuleException {
		LOGGER.info("Starting Debezium server");
		String tablesString = String.join(",", tables);
		MuleDebeziumConnection connection = MuledebeziumConnectionConnectionProvider.connect();
		Properties config = connection.getConfiguration();
		config.setProperty("converter.schemas.enable", String.valueOf(schemasEnable));
		config.setProperty("snapshot.mode", snapShotMode.toString().toLowerCase());
		config.setProperty(String.valueOf(MySqlConnectorConfig.TABLE_INCLUDE_LIST), tablesString);

		engine = DebeziumEngine.create(Json.class).using(config).notifying((record) -> {
			try {

				String payload = MuleDebeziumEventBuilder.buildDebeziumEvent(record.value(), schemasEnable);

				if (!payload.equals("")) {
					DebeziumEventAttributes debeziumAttributes = MuleDebeziumAttributeBuilder
							.BuildDebeziumAttributes(record.value());
					sourceCallback.handle(Result.<String, DebeziumEventAttributes>builder().output(payload)
							.attributes(debeziumAttributes).build());
				} else {
					LOGGER.debug("Received non cdc event,skipping");
				}
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
		}).build();
		LOGGER.info("Debezium server build successful");
		engine.run();
	}

	@Override
	public void onStop() {
		try {
			engine.close();
			LOGGER.info("Closing Debezium server");

		} catch (IOException e) {

			LOGGER.error("Error closing Debezium server");
			LOGGER.error(e.getMessage());

		}
	}
}