package com.connect.app;

import java.util.Map;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;

public class JokeSourceConnectorConfig extends AbstractConfig {

    public JokeSourceConnectorConfig(final Map<?, ?> originalProps) {
        super(CONFIG_DEF, originalProps);
        
    }

    public static final String TOPIC_CONFIG = "topic";
    private static final String TOPIC_DOC = "Topic to publish";
    public static final String INTERVAL_CONFIG = "interval";
    private static final String INTERVAL_DOC = "Time to wait between to api requests, in milliseconds..";
    public static final String MONITOR_THREAD_TIMEOUT_CONFIG = "monitor.thread.timeout";
    private static final String MONITOR_THREAD_TIMEOUT_DOC = "Timeout used by the monitoring thread";
    private static final int MONITOR_THREAD_TIMEOUT_DEFAULT = 10000;
    public static final ConfigDef CONFIG_DEF = createConfigDef();

    private static ConfigDef createConfigDef() {
        ConfigDef configDef = new ConfigDef();
        addParams(configDef);
        return configDef;
    }

    private static void addParams(final ConfigDef configDef) {
        configDef.define(
            TOPIC_CONFIG,
            Type.STRING,
            Importance.HIGH,
            TOPIC_DOC)
        .define(
            INTERVAL_CONFIG,
            Type.INT,
            5000,
            Importance.MEDIUM,
            INTERVAL_DOC
        ).define(
            MONITOR_THREAD_TIMEOUT_CONFIG,
            Type.INT,
            MONITOR_THREAD_TIMEOUT_DEFAULT,
            Importance.LOW,
            MONITOR_THREAD_TIMEOUT_DOC
        );
    }
}