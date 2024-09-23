package com.yadev.api;

import com.yadev.internal.Connection.MuleDebeziumMySQLConnectionProvider;
import com.yadev.internal.Source.MuleDebeziumChangeDataCaptureListener;
import org.mule.runtime.extension.api.annotation.Sources;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;

/**
 * This class represents an extension configuration, values set in this class are commonly used across multiple
 * operations since they represent something core from the extension.
 */
@Sources(MuleDebeziumChangeDataCaptureListener.class)
@ConnectionProviders(MuleDebeziumMySQLConnectionProvider.class)
public class MuleDebeziumConfiguration {



}
