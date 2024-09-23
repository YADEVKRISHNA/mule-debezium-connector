package com.yadev.internal.Connection;


import io.debezium.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * This class represents an extension connection just as example (there is no real connection with anything here c:).
 */
public final class MuleDebeziumConnection {
  private final Logger LOGGER = LoggerFactory.getLogger(MuleDebeziumConnection.class);
  Configuration config = null;
  Properties props = null;
  public MuleDebeziumConnection(
          String databaseHost,
          Integer databasePort,
          String username,
          String password ,
          String flushInterval,
          String offsetStorageType,
          String offsetFileName,
          String databaseHistory,
          String databaseHistoryFileName) {


//    config = Configuration.empty().withSystemProperties(Function.identity()).edit()
//            .with(EmbeddedEngine.CONNECTOR_CLASS, "io.debezium.connector.mysql.MySqlConnector")
//            .with(EmbeddedEngine.ENGINE_NAME, "Mule")
//            .with(MySqlConnectorConfig.SERVER_NAME,"Mule")
//            .with(MySqlConnectorConfig.SERVER_ID, 8192)
//            .with(EmbeddedEngine.OFFSET_FLUSH_INTERVAL_MS,60000 )
//            .with(EmbeddedEngine.OFFSET_STORAGE, "org.apache.kafka.connect.storage.MemoryOffsetBackingStore")
//            .with(EmbeddedEngine.OFFSET_STORAGE_FILE_FILENAME, "org.apache.kafka.connect.storage.MemoryOffsetBackingStore")
//            .with(MySqlConnectorConfig.DATABASE_HISTORY, MemoryDatabaseHistory.class.getName())
//            .with(MySqlConnectorConfig.HOSTNAME, databaseHost)
//            .with(MySqlConnectorConfig.PORT, databasePort.intValue())
//            .with(MySqlConnectorConfig.USER, username)
//            .with(MySqlConnectorConfig.PASSWORD, password)
//            .with("schemas.enable", false)
//            .build();



     props = new Properties();
    props.setProperty("connector.class","io.debezium.connector.mysql.MySqlConnector");
    props.setProperty("name","Mule");
    props.setProperty("database.server.name","Mule");
    props.setProperty("database.server.id", "8192");
    props.setProperty("offset.flush.interval.ms",flushInterval);
    //props.setProperty("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore");
    props.setProperty("offset.storage", offsetStorageType);
    props.setProperty("offset.storage.file.filename",offsetFileName );
   // props.setProperty(String.valueOf(MySqlConnectorConfig.DATABASE_HISTORY), MemoryDatabaseHistory.class.getName());
    //props.setProperty("database.history", "io.debezium.relational.history.FileDatabaseHistory");
    props.setProperty("database.history", databaseHistory);
    props.setProperty("database.history.file.filename",databaseHistoryFileName);
    props.setProperty("database.hostname", databaseHost);
    props.setProperty("database.port", String.valueOf(databasePort.intValue()));
    props.setProperty("database.user", username);
    props.setProperty("database.password", password);
    props.setProperty("schemas.enable", "false");
  }

  public Properties getConfiguration() {
    return props;
  }

  public void invalidate() {
    // do something to invalidate this connection!
  }
}
