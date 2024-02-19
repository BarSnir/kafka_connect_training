package com.connect.app;

import java.io.IOException;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;

import com.connect.app.JokesUtils.JokeRandomResponse;

public class App {

    private static final String TYPE_COLUMN = "type";
    private static final String SETUP_COLUMN = "setup";
    private static final String PUNCHLINE_COLUMN = "punchline";
    private static final String ID_COLUMN = "id";
    private static Schema recordSchema;

    public static void main(String[] args) throws IOException {
        // Configure the HTTP connection
        recordSchema = SchemaBuilder.struct()
            .field(ID_COLUMN, Schema.INT32_SCHEMA).required()
            .field(TYPE_COLUMN, Schema.STRING_SCHEMA).required()
            .field(SETUP_COLUMN, Schema.STRING_SCHEMA).required()
            .field(PUNCHLINE_COLUMN, Schema.STRING_SCHEMA).required()
            .build();
        System.out.print(
            createStruct(JokesUtils.fetch(), recordSchema)
        );
    }

    public static Struct createStruct(JokeRandomResponse item, Schema recordSchema) {
        Struct struct = new Struct(recordSchema);
        struct.put(ID_COLUMN, item.getId());
        struct.put(TYPE_COLUMN, item.getSetup());
        struct.put(SETUP_COLUMN, item.getPunchline());
        struct.put(PUNCHLINE_COLUMN, item.getType());
        return struct;
    }

}
