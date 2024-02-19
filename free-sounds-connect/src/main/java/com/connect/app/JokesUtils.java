package com.connect.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;


public class JokesUtils {

    public static JokeRandomResponse fetch() throws IOException {
        String FREE_JOKE_URL = "https://official-joke-api.appspot.com/random_joke";
        // Configure the HTTP connection
        System.out.print(FREE_JOKE_URL);
        URI uri = URI.create(FREE_JOKE_URL);
        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET"); // Replace with appropriate method (GET, POST, PUT, etc.)
        connection.setDoInput(true);

        // Send the request and handle potential errors
        try {
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("Error: HTTP response code " + responseCode);
            }

            // Read the response body
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder responseContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
            reader.close();

            // Parse the JSON response
            ObjectMapper mapper = new ObjectMapper();
            JokeRandomResponse responseObject = mapper.readValue(responseContent.toString(), JokeRandomResponse.class);
            return responseObject;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return null;
    }

    public static class JokeRandomResponse implements JokeResponse {

        private String type;
        private String setup;
        private String punchline;
        private int id;
    
        // Setters and getters for the fields (omitted for brevity)
    
        @Override
        public String getType() {
            return type;
        }

        @Override
        public String getSetup() {
            return setup;
        }

        @Override
        public String getPunchline() {
            return punchline;
        }

        @Override
        public int getId(){
            return id;
        }
    }

    public interface JokeResponse {
        String getType();
        String getSetup();
        String getPunchline();
        int getId();
    }
}
