package com.example.taras.FTA;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by User on 08.03.2015.
 */
public class URLLoader extends AsyncTask<String, Void, String> {
    private MainActivity MA;
    private JSONArray js;

    public URLLoader(MainActivity ma){
        this.MA = ma;
    }
    @Override
    protected String doInBackground(String... urls) {
        try {
            return downloadUrl(urls[0]);
        } catch (IOException e) {
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }

    public String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            //conn.connect();
            is = conn.getInputStream();
            return readIt(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        BufferedReader reader = null;
        reader = new BufferedReader( new InputStreamReader(stream, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line = "";
        while((line = reader.readLine()) != null)
        {
            sb.append(line);

        }
        return sb.toString();
    }

    protected void onPostExecute(String result) {
        JSONObject jsObj = null;
        try {
            js = new JSONArray(result);
            jsObj = js.getJSONObject(0);
            //final TextView artistTxt = (TextView) MA.findViewById(R.id.trackArtist);
            final TextView trackTitleTxt = (TextView) MA.findViewById(R.id.trackTitle);
            //artistTxt
            trackTitleTxt.setText(jsObj.getString("artist") + "\n"+jsObj.getString("title"));
            //trackTitleTxt.setText();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}