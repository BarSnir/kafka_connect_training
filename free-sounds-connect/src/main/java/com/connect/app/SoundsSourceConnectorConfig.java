package com.connect.app;

import java.util.Map;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;

public class SoundsSourceConnectorConfig extends AbstractConfig {

    public SoundsSourceConnectorConfig(final Map<?, ?> originalProps) {
        super(CONFIG_DEF, originalProps);
        
    }

    public static final String TOPIC_CONFIG = "topic";
    private static final String TOPIC_DOC = "Topic to publish";
    public static final String API_KEY_CONFIG = "api.key";
    private static final String API_KEY_DOC = "Api key of free-source.org";
    public static final String TERMS_LIST_CONFIG = "terms.list";
    private static final String TERMS_LIST_DOC = "Terms string list to search in free-source.org";
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
            API_KEY_CONFIG,
            Type.STRING,
            Importance.HIGH,
            API_KEY_DOC)
        .define(
            TERMS_LIST_CONFIG,
            Type.STRING,
            Importance.HIGH,
            TERMS_LIST_DOC)
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