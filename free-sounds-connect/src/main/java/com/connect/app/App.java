package com.connect.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import java.util.List;



public class App {

    private static final String SOUNDS_FREE_URL = "https://freesound.org/apiv2/search/text/?query=piano&token=RrzwXe3my2LbmMoSBfei2ZE3BJrGns5gDBT4YUxo&size=50";  // Replace with your actual API endpoint
    public static void main(String[] args) throws IOException {
        // Configure the HTTP connection
        System.out.print(
            SourceSoundsFreeUtils.fetch("strings", "RrzwXe3my2LbmMoSBfei2ZE3BJrGns5gDBT4YUxo")
        );
    }

}
