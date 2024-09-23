package com.yadev.internal.Connection;

import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

import org.mule.runtime.extension.api.annotation.param.display.Password;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mule.runtime.api.meta.ExternalLibraryType.JAR;



//@DisplayName("Debezium MySql Connector")
//@Alias("mssql")
//@ExternalLib(name = "Debezium MySql Connector",
//        description = "A JDBC driver which supports Debezium to an MySQL Database",
//        type = JAR,
//        coordinates = "(.*)\\\\.jar",
//        nameRegexpMatcher = "io.debezium:debezium-connector-mysql:1.5.4.Final")
public class MuleDebeziumMySQLConnectionProvider implements ConnectionProvider<MuleDebeziumConnection> {

  private final Logger LOGGER = LoggerFactory.getLogger(MuleDebeziumMySQLConnectionProvider.class);

  @Parameter
  @Placement(order = 1)
  @DisplayName("Host")
  private String databaseHost;

  @Parameter
  @DisplayName("Port")
  @Placement(order = 2)
  private Integer databasePort;

  @Parameter
  @DisplayName("Username")
  @Placement(order = 3)
  private String username;


  @Parameter
  @DisplayName("Password")
  @Placement(order = 4)
  @Password
  private String password;

  @Parameter
  @DisplayName("Offset Storage")
  @Placement(order = 1, tab="Advanced")
  @Optional(defaultValue = "org.apache.kafka.connect.storage.FileOffsetBackingStore")
  private String offsetStorageType;


  @Parameter
  @DisplayName("Offset Storage file name")
  @Placement(order = 2, tab="Advanced")
  @Optional(defaultValue = "${mule.home}/${app.name}/debezium/offset.dat")
  private String offsetFileName;

  @Parameter
  @DisplayName("Database History")
  @Placement(order = 3, tab="Advanced")
  @Optional(defaultValue = "io.debezium.relational.history.FileDatabaseHistory")
  private String databaseHistory;

  @Parameter
  @DisplayName("Database History file name")
  @Placement(order = 4, tab="Advanced")
  @Optional(defaultValue = "${mule.home}/${app.name}/debezium/history.dat")
  private String databaseHistoryFileName;

  @Parameter
  @DisplayName("Offset Flush Interval(ms)")
  @Placement(order = 5, tab="Advanced")
  @Optional(defaultValue = "60000")
  private String flushInterval;



  @Override
  public MuleDebeziumConnection connect() throws ConnectionException {
    return new MuleDebeziumConnection( databaseHost,databasePort,username,password,flushInterval,offsetStorageType,offsetFileName,databaseHistory, databaseHistoryFileName );
  }

  @Override
  public void disconnect(MuleDebeziumConnection connection) {
    try {

      connection.invalidate();
    } catch (Exception e) {
      LOGGER.error("Error while disconnecting");
    }
  }

  @Override
  public ConnectionValidationResult validate(MuleDebeziumConnection connection) {
    return ConnectionValidationResult.success();
  }
}
