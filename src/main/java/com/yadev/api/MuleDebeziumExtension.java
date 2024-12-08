package com.yadev.api;

import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.Configurations;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;

import com.yadev.internal.Connection.MuleDebeziumConfiguration;

/**
 * This is the main class of an extension, is the entry point from which
 * configurations, connection providers, operations and sources are going to be
 * declared.
 */
@Xml(prefix = "mule-debezium-module")
@Extension(name = "Debezium")
@Configurations(MuleDebeziumConfiguration.class)
public class MuleDebeziumExtension {

}
