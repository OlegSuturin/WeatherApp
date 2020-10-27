package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private String address1;
    private String address2;
    private String city;
    private String urlStr;

    EditText editTextCity;
    TextView textViewInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCity = findViewById(R.id.editTextCity);
        textViewInfo = findViewById(R.id.textViewInfo);

        address1 = "http://api.openweathermap.org/data/2.5/weather?q=";
        address2 = ",ru&APPID=1d2fcf0922433a497b5fb9fe3e2c3742";

    }

    public void onClickWeather(View view) {
        urlStr = address1 + editTextCity.getText() + address2;

        Log.i("!@#",urlStr);

        DoownloadTask task = new DoownloadTask();
        task.execute(urlStr);

    }

    static class DoownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            HttpURLConnection urlConnection =null;
            StringBuilder strBuffer;

            try {
                strBuffer = new StringBuilder();
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line = bufferedReader.readLine();
                strBuffer.append(line);
                 while (line != null){
                     line = bufferedReader.readLine();
                     strBuffer.append(line);
                 }

                 return strBuffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally { if (urlConnection!=null) urlConnection.disconnect();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.i("!@#", s);
        }
    }//end of downloadTask
}