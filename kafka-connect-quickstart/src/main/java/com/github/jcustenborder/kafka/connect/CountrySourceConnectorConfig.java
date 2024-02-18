package com.github.jcustenborder.kafka.connect;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.config.ConfigDef.Importance;
import com.github.jcustenborder.kafka.connect.utils.config.ConfigKeyBuilder;
import java.util.Map;

public class CountrySourceConnectorConfig extends AbstractConfig {

  public static final String TOPIC_CONFIG = "topic";
  public static final String TOPIC_DOC="Target topic.";
  public static final String COUNTRY_CONFIG = "country";
  public static final String COUNTRY_DOC="Choose country name like: deutschland.";
  public static final String BATCH_SIZE_CONFIG = "batch.size";
  public static final String BATCH_SIZE_DOC="Choose producer batch.size .";
  public static final String FETCH_INTERVAL_CONFIG = "fetch.interval";
  public static final String FETCH_INTERVAL_DOC="Interval for api fetch in millis . Default for 600000.";

  public final String topicSettings;
  public final String countrySettings;
  public final String batchSizeSettings;
  public final String fetchIntervalSettings;


  public CountrySourceConnectorConfig(Map<?, ?> originals) {
    super(config(), originals);
    this.topicSettings = this.getString(TOPIC_CONFIG);
    this.countrySettings = this.getString(COUNTRY_CONFIG);
    this.batchSizeSettings = this.getString(BATCH_SIZE_CONFIG);
    this.fetchIntervalSettings = this.getString(FETCH_INTERVAL_CONFIG);
  }

  public static ConfigDef config() {
    return new ConfigDef()
        .define(
            ConfigKeyBuilder.of(TOPIC_CONFIG, Type.STRING)
                .documentation(TOPIC_DOC)
                .importance(Importance.HIGH)
                .build()
        ).define(
            ConfigKeyBuilder.of(COUNTRY_CONFIG, Type.STRING)
            .documentation(COUNTRY_DOC)
            .importance(Importance.HIGH)
            .build()
        ).define(
            ConfigKeyBuilder.of(BATCH_SIZE_CONFIG, Type.INT)
            .defaultValue(100)
            .documentation(BATCH_SIZE_DOC)
            .importance(Importance.MEDIUM)
            .build()
        ).define(
            ConfigKeyBuilder.of(FETCH_INTERVAL_CONFIG, Type.INT)
            .defaultValue(600000)
            .documentation(FETCH_INTERVAL_DOC)
            .importance(Importance.MEDIUM)
            .build()
        );
  }
}
