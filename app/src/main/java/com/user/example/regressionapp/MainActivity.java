package com.user.example.regressionapp;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
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

    private Button bt1;
    private Button bt2;
    private Context context;
    RegressAsyncTask regressAsyncTask;
    double[] values1;
    double[] values2;
    double[] values3;
    String val1;
    String val2;
    String val3;

    private GraphicalView mChart;
    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    private XYSeries mCurrentSeries1;
    private XYSeries mCurrentSeries2;
    private XYSeries mCurrentSeries3;
    private XYSeriesRenderer mCurrentRenderer1;
    private XYSeriesRenderer mCurrentRenderer2;
    private XYSeriesRenderer mCurrentRenderer3;

    public static String TAG="TAAG";

    String path = "http://192.168.1.103:4444/server/getValues";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = MainActivity.this;


        bt1 = (Button)findViewById(R.id.button_calc);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regressAsyncTask = new RegressAsyncTask();
                regressAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        bt2 = (Button)findViewById(R.id.button_show);
        bt2.setEnabled(false);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Start convert");
                String[] str;
                int n;

                str = val1.split(",");
                n = str.length;
                values1 = new double[n];
                for(int i = 0; i< n; i++)
                    values1[i] = Double.valueOf(str[i]);

                str = val2.split(",");
                n = str.length;
                values2 = new double[n];
                for(int i = 0; i< n; i++)
                    values2[i] = Double.valueOf(str[i]);

                str = val3.split(",");
                n = str.length;
                values3 = new double[n];
                for(int i = 0; i< n; i++)
                    values3[i] = Double.valueOf(str[i]);

                Log.d(TAG, "End convert");

                ///////////////////////////////////////////////
                LinearLayout layout = (LinearLayout) findViewById(R.id.linear);
                if (mChart == null) {
                    initChart();
                    addSampleData();
                    mRenderer.setXTitle("Наблюдения");
                    mRenderer.setYTitle("Оценка");
                    mChart = ChartFactory.getCubeLineChartView(MainActivity.this, mDataset, mRenderer, 0.3f);
                    layout.addView(mChart);
                } else {
                    mChart.repaint();
                }

            }
        });

    }

    private void initChart() {
        mCurrentSeries1 = new XYSeries("10 признаков");
        mDataset.addSeries(mCurrentSeries1);
        mCurrentSeries2 = new XYSeries("7 признаков");
        mDataset.addSeries(mCurrentSeries2);
        mCurrentSeries3 = new XYSeries("5 признаков");
        mDataset.addSeries(mCurrentSeries3);

        mCurrentRenderer1 = new XYSeriesRenderer();
        mCurrentRenderer1.setColor(Color.BLUE);
        mRenderer.addSeriesRenderer(mCurrentRenderer1);
        mCurrentRenderer2 = new XYSeriesRenderer();
        mCurrentRenderer2.setColor(Color.RED);
        mRenderer.addSeriesRenderer(mCurrentRenderer2);
        mCurrentRenderer3 = new XYSeriesRenderer();
        mCurrentRenderer3.setColor(Color.MAGENTA);
        mRenderer.addSeriesRenderer(mCurrentRenderer3);
    }

    private void addSampleData() {
        for(int i = 0; i<values1.length; i++) {
            mCurrentSeries1.add(i,values1[i]);
            mCurrentSeries2.add(i,values2[i]);
            mCurrentSeries3.add(i,values3[i]);
        }
    }

    class RegressAsyncTask extends AsyncTask<Object, Object, String> {

        @Override
        protected String doInBackground(Object... voids) {
            Log.d("!!!", "Start Async");
            BufferedReader rd = null;
            StringBuilder sb = null;
            //String line = null;
            String s = "";
            URL url;
            path = "http://192.168.1.103:4444/server/getResult";
            Log.d(TAG, path);
            try {
                url = new URL(path);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(15000);
                connection.setDoInput(true);
                connection.connect();

                int status = connection.getResponseCode();
                InputStream in;
                if(status >= HttpURLConnection.HTTP_BAD_REQUEST)
                    in = connection.getErrorStream();
                else
                    in = connection.getInputStream();

                rd = new BufferedReader(new InputStreamReader(in));
                sb = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                Log.d(TAG, s);
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

            String str = "{\"result\":"+sb.toString()+"}";

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
                JSONArray friends = dataJsonObj.getJSONArray("result");

                for (int i = 0; i < friends.length(); i++) {
                    JSONObject friend = friends.getJSONObject(i);

                    JSONObject contacts = friend.getJSONObject("Result");

                    String res = contacts.getString("result");

                    Log.d(TAG, "result: " +i+" "+ res);

                    res = res.substring(0,res.length()-1);
                    res = res.substring(1);
                    if(i==0)
                        val1 = res;
                    else if(i==1) val2 = res;
                    else val3 = res;
                }
                bt2.setEnabled(true);

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
