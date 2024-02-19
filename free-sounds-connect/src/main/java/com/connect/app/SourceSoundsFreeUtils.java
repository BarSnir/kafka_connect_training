package com.connect.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import java.util.List;



public class SourceSoundsFreeUtils {

    public static List<ResultItem> fetch(String param, String apiKey ) throws IOException {
        String SOUNDS_FREE_URL = "https://freesound.org/apiv2/search/text/?query="+param+"&token="+apiKey;
        // Configure the HTTP connection
        URI uri = URI.create(String.format(SOUNDS_FREE_URL, param));
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
            FreesoundSearchResponse responseObject = mapper.readValue(responseContent.toString(), FreesoundSearchResponse.class);
            return responseObject.getResults();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return null;
    }

    public static class FreesoundSearchResponse implements SearchResponse {

        private int count;
        private String previous;
        private String next;
        private List<ResultItem> results;
    
        // Setters and getters for the fields (omitted for brevity)
    
        @Override
        public int getCount() {
            return count;
        }

        @Override
        public String getPrevious() {
            return previous;
        }

        @Override
        public String getNext() {
            return next;
        }

        @Override
        public List<ResultItem> getResults(){
            return results;
        }
    }

    public interface SearchResponse {
        int getCount();
        String getPrevious();
        String getNext();
        List<ResultItem> getResults();
    }

    public static class ResultItem {
        private int id;
        private String name;
        private List<String> tags;
        private String license;
        private String username;


        public int getId() {
            return id;
        }
    
        public void setId(int id) {
            this.id = id;
        }
    
        public String getName() {
            return name;
        }
    
        public void setName(String name) {
            this.name = name;
        }
    
        public List<String> getTags() {
            return tags;
        }
    
        public void setTags(List<String> tags) {
            this.tags = tags;
        }
    
        public String getLicense() {
            return license;
        }
    
        public void setLicense(String license) {
            this.license = license;
        }
    
        public String getUsername() {
            return username;
        }
    
        public void setUsername(String username) {
            this.username = username;
        }
    
        // Override toString() method for printing in a clear format
        @Override
        public String toString() {
            return "ResultItem{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", tags=" + tags +
                    ", license='" + license + '\'' +
                    ", username='" + username + '\'' +
                    '}';
        }
    }
}
