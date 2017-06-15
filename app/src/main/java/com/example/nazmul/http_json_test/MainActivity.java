package com.example.nazmul.http_json_test;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String myUrl = "https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesDemoItem.txt";

    private TextView tvData;  // this Field was made from a variable, using "CTRL+ALT+SHIFT+T"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnHit = (Button) findViewById(R.id.btnHit);
        tvData = (TextView) findViewById(R.id.tvJsonItem);

        btnHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONTask task = new JSONTask();
                task.execute();
            }
        });
    }

    public class JSONTask extends AsyncTask<URL, String, String>{

        @Override
        protected String doInBackground(URL... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(myUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";

                while ((line=reader.readLine()) != null){
                    buffer.append(line);
                }

                //return buffer.toString();
                return extractStringFromJSON(buffer.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null){
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            tvData.setText(result);

        }

        private String extractStringFromJSON(String jsonString){
            try{
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray moviesArray = jsonObject.getJSONArray("movies");
                JSONObject moviesObject = moviesArray.getJSONObject(0);

                String movieName = moviesObject.getString("movie");
                int release = moviesObject.getInt("year");
                String mainText = "The name of the movies is " + movieName+ " and it was released in " +Integer.toString(release);

                return mainText;

            }catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
