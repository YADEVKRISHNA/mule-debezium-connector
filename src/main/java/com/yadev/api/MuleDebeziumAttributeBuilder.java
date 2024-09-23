package com.yadev.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MuleDebeziumAttributeBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(MuleDebeziumAttributeBuilder.class);
    public static DebeziumEventAttributes BuildDebeziumAttributes(String event) throws JsonProcessingException {
        DebeziumEventAttributes debeziumEventAttributes = new DebeziumEventAttributes();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(event);
        String version = rootNode.path("payload").path("source").path("version").toString();
        String connector = rootNode.path("payload").path("source").path("connector").toString();
        String name = rootNode.path("payload").path("source").path("name").toString();
        String ts_ms = rootNode.path("payload").path("source").path("ts_ms").toString();
        String snapshot = rootNode.path("payload").path("source").path("snapshot").toString();
        String db = rootNode.path("payload").path("source").path("db").toString();
        String sequence = rootNode.path("payload").path("source").path("sequence").toString();
        String table = rootNode.path("payload").path("source").path("table").toString();
        String server_id = rootNode.path("payload").path("source").path("server_id").toString();
        String file = rootNode.path("payload").path("source").path("file").toString();
        String pos = rootNode.path("payload").path("source").path("pos").toString();
        String row = rootNode.path("payload").path("source").path("row").toString();
        String thread = rootNode.path("payload").path("source").path("thread").toString();
        String op = rootNode.path("payload").path("op").toString();
        String transaction = rootNode.path("payload").path("transaction").toString();
        debeziumEventAttributes.setVersion(version);

        debeziumEventAttributes.setConnector(connector);
        LOGGER.info("Setting connector " + connector);

        debeziumEventAttributes.setName(name);
        LOGGER.info("Setting name " + name);

        debeziumEventAttributes.setTs_ms(ts_ms);
        LOGGER.info("Setting ts_ms " + ts_ms);

        debeziumEventAttributes.setSnapshot(snapshot);
        LOGGER.info("Setting snapshot " + snapshot);

        debeziumEventAttributes.setDb(db);
        LOGGER.info("Setting db " + db);

        debeziumEventAttributes.setSequence(sequence);
        LOGGER.info("Setting sequence " + sequence);

        debeziumEventAttributes.setTable(table);
        LOGGER.info("Setting table " + table);

        debeziumEventAttributes.setServer_id(server_id);
        LOGGER.info("Setting server_id " + server_id);

        debeziumEventAttributes.setFile(file);
        LOGGER.info("Setting file " + file);

        debeziumEventAttributes.setPos(pos);
        LOGGER.info("Setting pos " + pos);

        debeziumEventAttributes.setRow(row);
        LOGGER.info("Setting row " + row);

        debeziumEventAttributes.setThread(thread);
        LOGGER.info("Setting thread " + thread);

        debeziumEventAttributes.setOp(op);
        LOGGER.info("Setting op " + op);

        debeziumEventAttributes.setTransaction(transaction);
        LOGGER.info("Setting transaction " + transaction);
        return debeziumEventAttributes;
    }
}
