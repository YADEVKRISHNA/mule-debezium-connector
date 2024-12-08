package com.yadev.internal.Connection;

import static org.mule.runtime.api.meta.ExternalLibraryType.DEPENDENCY;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;

import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.api.store.ObjectStore;
import org.mule.runtime.api.store.ObjectStoreManager;
import org.mule.runtime.extension.api.annotation.ExternalLib;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Password;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.param.reference.ObjectStoreReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MuleDebeziumMySQLConnectionProvider implements ConnectionProvider<MuleDebeziumConnection> {

	private final Logger LOGGER = LoggerFactory.getLogger(MuleDebeziumMySQLConnectionProvider.class);

	@Inject
	private ObjectStoreManager runtimeObjectStoreManager;

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
	@Placement(order = 1, tab = "Advanced")
	@Optional(defaultValue = "org.apache.kafka.connect.storage.FileOffsetBackingStore")
	private String offsetStorageType;

	@Parameter
	@DisplayName("Offset Storage file name")
	@Placement(order = 2, tab = "Advanced")
	@Optional(defaultValue = "${mule.home}/${app.name}/debezium/offset.dat")
	private String offsetFileName;

	@Parameter
	@DisplayName("Database History")
	@Placement(order = 3, tab = "Advanced")
	@Optional(defaultValue = "io.debezium.relational.history.FileDatabaseHistory")
	private String databaseHistory;

	@Parameter
	@DisplayName("Database History file name")
	@Placement(order = 4, tab = "Advanced")
	@Optional(defaultValue = "${mule.home}/${app.name}/debezium/history.dat")
	private String databaseHistoryFileName;

	@Parameter
	@DisplayName("Offset Flush Interval(ms)")
	@Placement(order = 5, tab = "Advanced")
	@Optional(defaultValue = "60000")
	private String flushInterval;

	@Parameter
	@DisplayName("Offset Flush Timeout(ms)")
	@Placement(order = 6, tab = "Advanced")
	@Optional(defaultValue = "5000")
	private String offsetFlushTimeoutMs;

	@Parameter
	@DisplayName("Errors Retry Delay Initial(ms)")
	@Placement(order = 7, tab = "Advanced")
	@Optional(defaultValue = "300")
	private String errorsRetryDelayInitial;

	@Parameter
	@DisplayName("Errors Max Retries")
	@Placement(order = 8, tab = "Advanced")
	@Optional(defaultValue = "-1")
	private String errorsMaxRetries;

	@Parameter
	@DisplayName("Errors Retry Delay Max(ms)")
	@Placement(order = 9, tab = "Advanced")
	@Optional(defaultValue = "10000")
	private String errorsRetryDelayMax;

	@Parameter
	@DisplayName("Additonal PropertiesS")
	@Placement(order = 11, tab = "Advanced")
	@Optional()
	private Map<String, String> addiionalProperties = new HashMap<String,String>();
	//private Properties addiionalProperties =  ;
	

	@Parameter
	@DisplayName("Connector Class")
	@Placement(order = 12, tab = "Advanced")
	@Optional(defaultValue = "io.debezium.connector.mysql.MySqlConnector")
	private String connectorClass;
	
	@Parameter
	@Optional
	@DisplayName("Offset Backing Store")
	@ObjectStoreReference
	@Placement(order = 13, tab = "Advanced")
	private ObjectStore objectStore;

	@Override
	public MuleDebeziumConnection connect() throws ConnectionException {

		Properties props = new Properties();
	    props.setProperty("connector.class", connectorClass);
		props.setProperty("name", "Mule");
		props.setProperty("database.server.name", "Mule");
		props.setProperty("database.server.id", "8192");
		props.setProperty("offset.flush.interval.ms", flushInterval);
		props.setProperty("offset.storage", offsetStorageType);
		props.setProperty("offset.storage.file.filename", offsetFileName);
		props.setProperty("database.history", databaseHistory);
		props.setProperty("database.history.file.filename", databaseHistoryFileName);
		props.setProperty("database.hostname", databaseHost);
		props.setProperty("database.port", String.valueOf(databasePort.intValue()));
		props.setProperty("database.user", username);
		props.setProperty("database.password", password);
		props.setProperty("errors.retry.delay.max.ms", errorsRetryDelayInitial);
		props.setProperty("errors.retry.delay.initial.ms", errorsRetryDelayMax);
		props.setProperty("errors.max.retries", errorsMaxRetries);
		props.setProperty("offset.flush.timeout.ms", offsetFlushTimeoutMs);
		
		if (!addiionalProperties.isEmpty()) {
			addiionalProperties.forEach((k, v) -> {
				LOGGER.info("Setting Debezium property ", k.toString(), v.toString());
				props.setProperty(k.toString(), v.toString());
			});
		}
		return new MuleDebeziumConnection(props);
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
