package com.connect.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.kafka.common.config.Config;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigValue;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceConnector;
import org.apache.kafka.connect.util.ConnectorUtils;

import static com.connect.app.SoundsSourceConnectorConfig.*;

public class SourceSourceConnector extends SourceConnector{

    private final Logger log = LoggerFactory.getLogger(SourceSourceConnector.class);
    private Map<String, String> originalProps;
    private SoundsSourceConnectorConfig config;
    private NewPartitionsCheckerThread checker;

    @Override
    public ConfigDef config() {
        return CONFIG_DEF;
    }

    @Override
    public Class<? extends Task> taskClass() {
        return MyFirstKafkaConnectorTask.class;
    }
}