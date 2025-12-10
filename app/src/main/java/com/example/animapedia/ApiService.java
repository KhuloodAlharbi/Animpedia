package com.example.animapedia;

import android.util.Log;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiService {

    // CHANGE THIS to your backend URL
    private static final String BASE_URL = "http://10.0.2.2:8000/chat";

    public interface Callback {
        void onSuccess(String response);
        void onError(String error);
    }

    public void sendMessage(String question, Callback callback) {
        new Thread(() -> {
            try {
                URL url = new URL(BASE_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                JSONObject json = new JSONObject();
                json.put("question", question);

                OutputStream os = conn.getOutputStream();
                os.write(json.toString().getBytes());
                os.flush();
                os.close();

                int status = conn.getResponseCode();
                if (status == 200) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream())
                    );
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    reader.close();

                    JSONObject responseJson = new JSONObject(result.toString());
                    callback.onSuccess(responseJson.getString("answer"));
                } else {
                    callback.onError("Server error: " + status);
                }

            } catch (Exception e) {
                callback.onError(e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }
}
