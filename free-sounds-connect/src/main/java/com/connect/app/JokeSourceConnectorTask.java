package com.connect.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.connect.app.JokesUtils.JokeRandomResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;

import static com.connect.app.JokeSourceConnectorConfig.*;

public class JokeSourceConnectorTask extends SourceTask {

    private static final String TYPE_COLUMN = "type";
    private static final String SETUP_COLUMN = "setup";
    private static final String PUNCHLINE_COLUMN = "punchline";
    private static final String ID_COLUMN = "id";

    private final Logger log = LoggerFactory.getLogger(JokeSourceConnectorTask.class);

    private JokeSourceConnectorConfig config;
    private int taskSleepTimeout;
    private String topic;
    private Schema recordSchema;

    @Override
    public String version() {
        return PropertiesUtil.getConnectorVersion();
    }

    @Override
    public void start(Map<String, String> properties) {
        config = new JokeSourceConnectorConfig(properties);
        taskSleepTimeout = config.getInt(INTERVAL_CONFIG);
        topic = config.getString(TOPIC_CONFIG);
        recordSchema = SchemaBuilder.struct()
            .field(ID_COLUMN, Schema.INT32_SCHEMA).required()
            .field(TYPE_COLUMN, Schema.STRING_SCHEMA).required()
            .field(SETUP_COLUMN, Schema.STRING_SCHEMA).required()
            .field(PUNCHLINE_COLUMN, Schema.STRING_SCHEMA).required()
            .build();
    }

    @Override
    public List<SourceRecord> poll() throws InterruptedException {
        Thread.sleep(taskSleepTimeout);
        List<SourceRecord> records = new ArrayList<>();
        log.info("Polling some jokes data...");
        try {
            JokeRandomResponse joke = JokesUtils.fetch();
            records.add(new SourceRecord(
                null,
                Collections.singletonMap("offset", 0),
                topic,
                null, 
                null,
                joke.getId(),
                recordSchema,
                createStruct(joke)
            ));
        } catch (IOException e) {
            e.printStackTrace();
            }
        
        return records;
    }

    private Struct createStruct(JokeRandomResponse item) {
        Struct struct = new Struct(recordSchema);
        struct.put(ID_COLUMN, item.getId());
        struct.put(TYPE_COLUMN, item.getSetup());
        struct.put(SETUP_COLUMN, item.getPunchline());
        struct.put(PUNCHLINE_COLUMN, item.getType());
        return struct;
    }

    @Override
    public void stop() {
    }

}
