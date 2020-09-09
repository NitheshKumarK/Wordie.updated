package com.nithesh.wordie;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class QueryUtils {
    private static String BASE_QUERY_URL = "https://www.dictionaryapi.com/api/v3/references/sd4/json/";
    private static String BASE_QUERY_KEY = "?key=0937209e-ae98-4f4f-81ac-c2c9b9316c01";
    private static ArrayList<Word> wordArrayList = new ArrayList<>();


    public static ArrayList<Word> getWord(String searchWord) {
        String jsonResponse = "";

        try {
            jsonResponse = makeHttpRequest(searchWord);

        } catch (Exception e) {
            e.printStackTrace();
        }
        wordArrayList = extractFeaturesFromJson(jsonResponse);
        return wordArrayList;
    }


    private static String makeHttpRequest(String searchWord) throws IOException {
        URL url = getUrl(searchWord);
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        String jsonResponse = "";
        if (url != null) {
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(15000);
                connection.connect();
                if (connection.getResponseCode() == 200) {
                    inputStream = connection.getInputStream();
                    jsonResponse = fetchJsonResponse(inputStream);
                }
                return jsonResponse;

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }
        return null;
    }


    private static URL getUrl(String word) {
        URL url = null;
        if (!TextUtils.isEmpty(word)) {
            try {
                String urlString = BASE_QUERY_URL + word + BASE_QUERY_KEY;
                url = new URL(urlString);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return url;
    }


    private static String fetchJsonResponse(InputStream inputStream) throws IOException {
        String jsonResponse = "";
        StringBuilder builder = new StringBuilder();
        BufferedReader bufferedReader = null;
        if (inputStream != null) {
            String line = "";
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            line = bufferedReader.readLine();
            while (line != null) {
                builder.append(line);
                line = bufferedReader.readLine();
            }
        }
        jsonResponse = builder.toString();
        return jsonResponse;
    }

    private static ArrayList<Word> extractFeaturesFromJson(String jsonResponse) {
        try {
            if (!TextUtils.isEmpty(jsonResponse)) {

                JSONArray wordsArray = new JSONArray(jsonResponse);
                for (int i = 0; i < wordsArray.length(); i++) {
                    String soundId = "";
                    JSONObject insideWord = wordsArray.optJSONObject(i);
                    String pos = insideWord.getString("fl");
                    JSONObject hwi = insideWord.optJSONObject("hwi");
                    JSONArray prs = hwi.optJSONArray("prs");
                    if (prs != null) {
                        JSONObject prsObject = prs.optJSONObject(0);
                        JSONObject sound = prsObject.optJSONObject("sound");
                        soundId = sound.getString("audio");
                    }
                    String hw = hwi.getString("hw");
                    JSONArray definitionArray = insideWord.optJSONArray("shortdef");
                    String firstDefinition = definitionArray.getString(0);
                    wordArrayList.add(new Word(hw, pos, firstDefinition,soundId));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wordArrayList;
    }


}
