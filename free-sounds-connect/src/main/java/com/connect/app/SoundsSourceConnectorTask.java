package com.connect.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;

import static com.connect.app.SoundsSourceConnectorConfig.*;

public class SoundsSourceConnectorTask extends SourceTask {

    private static final String ID_COLUMN = "id";
    private static final String NAME_COLUMN = "name";
    private static final String TAGS_COLUMN = "tags";
    private static final String LICENSE_COLUMN = "license";
    private static final String USERNAME_COLUMN = "username";

    private static final Schema ARRAY_SCHEMA = SchemaBuilder.array(Schema.STRING_SCHEMA).build();
    private final Logger log = LoggerFactory.getLogger(SoundsSourceConnectorTask.class);

    private SoundsSourceConnectorConfig config;
    private int taskSleepTimeout;
    private String apiKey;
    private List<String> sources;
    private Schema recordSchema;

    @Override
    public void start(Map<String, String> properties) {
        config = new SoundsSourceConnectorConfig(properties);
        taskSleepTimeout = config.getInt(INTERVAL_CONFIG);
        apiKey = config.getString(API_KEY_CONFIG);
        String sourcesStr = config.getString(TERMS_LIST_CONFIG);
        sources = Arrays.asList(sourcesStr.split(","));
        recordSchema = SchemaBuilder.struct()
            .field(ID_COLUMN, Schema.INT32_SCHEMA).required()
            .field(NAME_COLUMN, Schema.STRING_SCHEMA).required()
            .field(TAGS_COLUMN, ARRAY_SCHEMA).required()
            .field(LICENSE_COLUMN, Schema.STRING_SCHEMA).required()
            .field(USERNAME_COLUMN, Schema.STRING_SCHEMA).required()
            .build();
    }

    @Override
    public List<SourceRecord> poll() throws InterruptedException {
        Thread.sleep(taskSleepTimeout);
        List<SourceRecord> records = new ArrayList<>();

        for (String source : sources) {
            log.info("Polling data from the source '" + source + "'");
            records.add(new SourceRecord(
                Collections.singletonMap("source", source),
                Collections.singletonMap("offset", 0),
                source, null, null, null,
                recordSchema, createStruct(recordSchema)
            ));
        }
        return records;
    }

}
