package com.yadev.internal.Source;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.yadev.api.DebeziumEventAttributes;
import com.yadev.api.MuleDebeziumAttributeBuilder;
import com.yadev.api.MuleDebeziumEventBuilder;
import com.yadev.internal.Connection.MuleDebeziumConnection;
import io.debezium.connector.mysql.MySqlConnectorConfig;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.api.util.MultiMap;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.*;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.extension.api.runtime.source.Source;
import org.mule.runtime.extension.api.runtime.source.SourceCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


import static java.util.Collections.copy;
import static java.util.Collections.emptyMap;
import static org.mule.runtime.api.util.MultiMap.emptyMultiMap;
import static org.mule.runtime.extension.api.annotation.param.MediaType.APPLICATION_JSON;

@Alias("Change-Data-Capture-Listener")
@MediaType(APPLICATION_JSON)
public class MuleDebeziumChangeDataCaptureListener extends Source<String, DebeziumEventAttributes> {

    private final Logger LOGGER = LoggerFactory.getLogger(MuleDebeziumChangeDataCaptureListener.class);


  @Parameter
  @DisplayName("Schema")
  @Placement(order = 1)
  private String schema;

  @Parameter
  @DisplayName("Table Name")
  @Placement(order = 2)
  private String tableName;

  private Boolean sentEvent = false;

  @Connection
  private ConnectionProvider<MuleDebeziumConnection> MuledebeziumConnectionConnectionProvider;


  private DebeziumEngine<ChangeEvent<String, String>> engine = null;

  @Override
  public void onStart(SourceCallback<String, DebeziumEventAttributes> sourceCallback) throws MuleException {
        LOGGER.info("Starting Debezium server");
        MuleDebeziumConnection connection = MuledebeziumConnectionConnectionProvider.connect();
        Properties config =  connection.getConfiguration();
        config.setProperty(String.valueOf(MySqlConnectorConfig.DATABASE_INCLUDE_LIST), schema);
        config.setProperty(String.valueOf(MySqlConnectorConfig.TABLE_INCLUDE_LIST), (schema + "."+ tableName));
        engine = DebeziumEngine.create(Json.class)
            .using(config)
            .notifying((record  )-> {
                try {
                    String payload  = MuleDebeziumEventBuilder.buildDebeziumEvent(record.value());
                    DebeziumEventAttributes debeziumAttributes = MuleDebeziumAttributeBuilder.BuildDebeziumAttributes(record.value());
                    if (!payload.equals("")) {
                        sourceCallback.handle(Result.<String, DebeziumEventAttributes>builder()
                                .output(payload)
                                .attributes(debeziumAttributes)
                                .build());
                    }else {
                        LOGGER.debug("Received Non-CDC Event Skipping");
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
          LOGGER.error("Closing Debezium server");
          LOGGER.error(e.getMessage());

      }
  }
}