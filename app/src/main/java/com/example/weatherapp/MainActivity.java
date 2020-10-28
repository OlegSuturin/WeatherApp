package com.example.weatherapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.BreakIterator;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {


    private String urlStr;
    private final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&APPID=1d2fcf0922433a497b5fb9fe3e2c3742&lang=ru&units=metric";
    private String stringJSON;
    private StringBuilder textResult;
    private String city;
    private EditText editTextCity;
    private TextView textViewInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();

        editTextCity = findViewById(R.id.editTextCity);
        textViewInfo = findViewById(R.id.textViewInfo);

        textResult = new StringBuilder();

    }

    public void onClickWeather(View view) {
        textViewInfo.setText("  ");

        //urlStr = address1 + editTextCity.getText() + address2;
        city = editTextCity.getText().toString().trim();
        if (!city.isEmpty()) {
            urlStr = String.format(WEATHER_URL, city);
            Log.i("!@#", urlStr);
            DoownloadTask task = new DoownloadTask();
            task.execute(urlStr);
        }
    }

   class DoownloadTask extends AsyncTask<String, Void, String> {



        @Override
        protected String doInBackground(String... strings) {

            URL url = null;
            HttpURLConnection urlConnection = null;
            StringBuilder strBuffer;



            try {
                strBuffer = new StringBuilder();
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line = bufferedReader.readLine();
                if (!line.equals("{\"cod\":\"404\",\"message\":\"city not found\"}")) {

                    while (line != null) {
                        strBuffer.append(line);
                        line = bufferedReader.readLine();

                    }
                    return strBuffer.toString();
                } else    //по городу нет информации
                    return null;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) urlConnection.disconnect();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String result;

            if (s == null) {
                Toast.makeText(MainActivity.this, "По данному городу информация отсутствует", Toast.LENGTH_SHORT).show();
                return;
            } else {

                Log.i("!@#", s);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    String name = jsonObject.getString("name");
                    //имя города

                    String temp = jsonObject.getJSONObject("main").getString("temp");
                    //JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                    //String temp = jsonObjectMain.getString("temp");
                    //текущая температура

                    String naUlice = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
                    //JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                    // JSONObject jsonObject0 = jsonArrayWeather.getJSONObject(0);
                    // String naUlice = jsonObject0.getString("description");

                    result = String.format(" %s\n Температура: %s\n На улице: %s", name, temp, naUlice);
                    Log.i("!@#", result);
                    textViewInfo.setText(result); //вывод данных

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }// enf of else
        }


    }//end of downloadTask
}