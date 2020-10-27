package com.example.weatherapp;

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
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private String address1;
    private String address2;
    private String city;
    private String urlStr;
    private String stringJSON;
    private StringBuilder textResult;

    private EditText editTextCity;
    private TextView textViewInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCity = findViewById(R.id.editTextCity);
        textViewInfo = findViewById(R.id.textViewInfo);
        textResult = new StringBuilder();

        address1 = "http://api.openweathermap.org/data/2.5/weather?q=";
        address2 = ",ru&APPID=1d2fcf0922433a497b5fb9fe3e2c3742";

    }

    public void onClickWeather(View view) {
        textViewInfo.setText("  ");

        urlStr = address1 + editTextCity.getText() + address2;

        Log.i("!@#", urlStr);

        DoownloadTask task = new DoownloadTask();
        try {
            stringJSON = task.execute(urlStr).get();

           if (stringJSON != null ) {

                JSONObject jsonObject = new JSONObject(stringJSON);
                String name = jsonObject.getString("name");
                textResult.append(name + "\n");                   //имя города

                JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                String temp = jsonObjectMain.getString("temp");
                Float tempC = Float.parseFloat(temp) - 273;
                textResult.append("Температура " + tempC + "\n");          //текущая температура

                JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                JSONObject jsonObject0 = jsonArrayWeather.getJSONObject(0);
                String naUlice = jsonObject0.getString("main");
                textResult.append("На улице " + naUlice);

                textViewInfo.setText(textResult.toString());
           } else Toast.makeText(this, "Неверный город", Toast.LENGTH_SHORT).show();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    static class DoownloadTask extends AsyncTask<String, Void, String> {

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
                if (!line.equals("{\"cod\":\"404\",\"message\":\"city not found\"}") ){

                    while (line != null) {
                        strBuffer.append(line);
                        line = bufferedReader.readLine();

                    }
                    return strBuffer.toString();
                } else
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
            Log.i("!@#", " " + s.equals("city not found"));
            Log.i("!@#", s);
        }
    }//end of downloadTask
}