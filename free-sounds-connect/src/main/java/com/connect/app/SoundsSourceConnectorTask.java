package com.connect.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.connect.app.SourceSoundsFreeUtils.ResultItem;

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
    private String topic;
    private List<String> sources;
    private Schema recordSchema;

    @Override
    public String version() {
        return PropertiesUtil.getConnectorVersion();
    }

    @Override
    public void start(Map<String, String> properties) {
        config = new SoundsSourceConnectorConfig(properties);
        taskSleepTimeout = config.getInt(INTERVAL_CONFIG);
        apiKey = config.getString(API_KEY_CONFIG);
        String sourcesStr = config.getString(TERMS_LIST_CONFIG);
        sources = Arrays.asList(sourcesStr.split(","));
        topic = config.getString(TOPIC_CONFIG);
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
            try {
                List<ResultItem> results = SourceSoundsFreeUtils.fetch(source, apiKey);
                for (ResultItem item : results) {
                    records.add(new SourceRecord(
                        Collections.singletonMap("source", source),
                        Collections.singletonMap("offset", 0),
                        topic, 
                        null, 
                        null,
                        item.getId(),
                        recordSchema,
                        createStruct(item)
                    ));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return records;
    }

    private Struct createStruct(ResultItem item) {
        Struct struct = new Struct(recordSchema);
        struct.put(ID_COLUMN, item.getId());
        struct.put(NAME_COLUMN, item.getName());
        struct.put(TAGS_COLUMN, item.getTags());
        struct.put(LICENSE_COLUMN, item.getLicense());
        struct.put(USERNAME_COLUMN, item.getUsername());
        return struct;
    }

    @Override
    public void stop() {
    }

}
