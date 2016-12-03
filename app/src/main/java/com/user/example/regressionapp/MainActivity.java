package com.user.example.regressionapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
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
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tv;
    private Button bt1;
    private Button bt2;
    private Context context;

    public static String TAG="TAAG";

    String path = "http://192.168.1.103:4444/server/getValues";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = MainActivity.this;

        tv = (TextView)findViewById(R.id.text_test);

        final MyAsyncTask t = new MyAsyncTask();


        bt1 = (Button)findViewById(R.id.button_test1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        bt2 = (Button)findViewById(R.id.button_test2);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t.cancel(false);
            }
        });

    }

    class MyAsyncTask extends AsyncTask<Object, Object, String> {

        @Override
        protected String doInBackground(Object... voids) {
            Log.d("!!!!!!!!!!!!!", "doInBackground");
            BufferedReader rd = null;
            StringBuilder sb = null;
            //String line = null;
            String s = "";
            URL url;
            path = "http://10.60.1.212:4444/server/getValues";
            Log.d(TAG, path);
            try {
                url = new URL(path);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(15000);
                connection.setDoInput(true);
                connection.connect();
                //InputStream in = connection.getInputStream();
                //in.re
                int status = connection.getResponseCode();
                InputStream in;
                if(status >= HttpURLConnection.HTTP_BAD_REQUEST)
                    in = connection.getErrorStream();
                else
                    in = connection.getInputStream();

                //rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                rd = new BufferedReader(new InputStreamReader(in));
                sb = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                Log.d(TAG, s);
                //return sb.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.d(TAG, "MalformedURLException");
            } catch (ProtocolException e) {
                e.printStackTrace();
                Log.d(TAG, "ProtocolException");
            } catch (IOException e) {
                Log.d(TAG, "IOException");
                e.printStackTrace();
            }

            String str = "{\"friends\":"+sb.toString()+"}";

            return str;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // выводим целиком полученную json-строку
            Log.d(TAG, s);

            JSONObject dataJsonObj = null;
            String secondName = "";

            try {
                dataJsonObj = new JSONObject(s);
                JSONArray friends = dataJsonObj.getJSONArray("friends");

                // 1. достаем инфо о втором друге - индекс 1
                //JSONObject secondFriend = friends.getJSONObject(1);
                //secondName = secondFriend.getString("text");
                //Log.d("TAAG", "Второе имя: " + secondName);

                // 2. перебираем и выводим контакты каждого друга
                for (int i = 0; i < friends.length(); i++) {
                    JSONObject friend = friends.getJSONObject(i);

                    JSONObject contacts = friend.getJSONObject("Test");

                    String phone = contacts.getString("id");
                    String email = contacts.getString("text");

                    Log.d(TAG, "phone: " + phone);
                    Log.d(TAG, "email: " + email);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
