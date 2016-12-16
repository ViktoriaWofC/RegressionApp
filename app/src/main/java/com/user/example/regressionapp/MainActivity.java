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
import android.widget.EditText;
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
    private Button bt3;
    private EditText editCount;
    String count = "";
    private Context context;
    RegressAsyncTask regressAsyncTask;
    double[] values = new double[7];
    double[] values10;
    double[] values9;
    double[] values8;
    double[] values7;
    double[] values6;
    double[] values5;
    double[] values4;
    String val10;
    String val9;
    String val8;
    String val7;
    String val6;
    String val5;
    String val4;


    private GraphicalView mChart;
    private GraphicalView mChart2;
    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    private XYMultipleSeriesDataset mDataset2 = new XYMultipleSeriesDataset();
    private XYMultipleSeriesRenderer mRenderer2 = new XYMultipleSeriesRenderer();
    private XYSeries mCurrentSeries1;
    private XYSeries mCurrentSeries2;
    private XYSeries mCurrentSeries3;
    private XYSeries currentSeries;
    private XYSeriesRenderer mCurrentRenderer1;
    private XYSeriesRenderer mCurrentRenderer2;
    private XYSeriesRenderer mCurrentRenderer3;
    private XYSeriesRenderer currentRenderer;

    public static String TAG="TAAG";

    String path = "http://192.168.1.103:4444/server/getValues";
    //10.60.0.171

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = MainActivity.this;

        editCount = (EditText)findViewById(R.id.edit_count);


        bt1 = (Button)findViewById(R.id.button_calc);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = editCount.getText().toString();
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

                str = val10.split(",");
                n = str.length;
                values10 = new double[n];
                for(int i = 0; i< n; i++)
                    values10[i] = Double.valueOf(str[i]);

                str = val9.split(",");
                n = str.length;
                values9 = new double[n];
                for(int i = 0; i< n; i++)
                    values9[i] = Double.valueOf(str[i]);

                str = val8.split(",");
                n = str.length;
                values8 = new double[n];
                for(int i = 0; i< n; i++)
                    values8[i] = Double.valueOf(str[i]);

                str = val7.split(",");
                n = str.length;
                values7 = new double[n];
                for(int i = 0; i< n; i++)
                    values7[i] = Double.valueOf(str[i]);

                str = val6.split(",");
                n = str.length;
                values6 = new double[n];
                for(int i = 0; i< n; i++)
                    values6[i] = Double.valueOf(str[i]);

                str = val5.split(",");
                n = str.length;
                values5 = new double[n];
                for(int i = 0; i< n; i++)
                    values5[i] = Double.valueOf(str[i]);

                str = val4.split(",");
                n = str.length;
                values4 = new double[n];
                for(int i = 0; i< n; i++)
                    values4[i] = Double.valueOf(str[i]);

                Log.d(TAG, "End convert");

                ///////////////////////////////////////////////
                LinearLayout layout = (LinearLayout) findViewById(R.id.linear);
                //if (mChart == null)
                {
                    layout.removeAllViews();
                    initChart();
                    addSampleData();
                    mRenderer.setXTitle("Наблюдения");
                    mRenderer.setYTitle("Оценка");
                    mChart = ChartFactory.getCubeLineChartView(MainActivity.this, mDataset, mRenderer, 0.3f);
                    layout.addView(mChart);
                } /*else {
                    layout.removeAllViews();
                    //mChart.repaint();
                    layout.addView(mChart);
                }*/

                bt3.setEnabled(true);
            }
        });

        bt3 = (Button)findViewById(R.id.button_show_2);
        bt3.setEnabled(false);
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = values10.length;
                double sum = 0;

                sum = 0;
                for(int i = 0; i<n;i++){
                    sum += values4[i]*values4[i]/2;
                }
                values[0] = sum/n;

                sum = 0;
                for(int i = 0; i<n;i++){
                    sum += values5[i]*values5[i]/2;
                }
                values[1] = sum/n;

                sum = 0;
                for(int i = 0; i<n;i++){
                    sum += values6[i]*values6[i]/2;
                }
                values[2] = sum/n;

                sum = 0;
                for(int i = 0; i<n;i++){
                    sum += values7[i]*values7[i]/2;
                }
                values[3] = sum/n;

                sum = 0;
                for(int i = 0; i<n;i++){
                    sum += values8[i]*values8[i]/2;
                }
                values[4] = sum/n;

                sum = 0;
                for(int i = 0; i<n;i++){
                    sum += values9[i]*values9[i]/2;
                }
                values[5] = sum/n;

                sum = 0;
                for(int i = 0; i<n;i++){
                    sum += values10[i]*values10[i]/2;
                }
                values[6] = sum/n;

                LinearLayout layout = (LinearLayout) findViewById(R.id.linear);
                //if (mChart2 == null)
                {
                    layout.removeAllViews();
                    initChart2();
                    addSampleData2();
                    mRenderer2.setXTitle("Признаки");
                    mRenderer2.setYTitle("Оценка");
                    mRenderer2.setXAxisMin(4);
                    mChart2 = ChartFactory.getCubeLineChartView(MainActivity.this, mDataset2, mRenderer2, 0.3f);
                    layout.addView(mChart2);
                } /*else {
                    layout.removeAllViews();
                    //mChart2.repaint();
                    layout.addView(mChart2);
                }*/
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
        for(int i = 0; i<values10.length; i++) {
            mCurrentSeries1.add(i,values10[i]);
            mCurrentSeries2.add(i,values7[i]);
            mCurrentSeries3.add(i,values5[i]);
        }
    }

    private void initChart2() {
        currentSeries = new XYSeries("Оценка");
        mDataset2.addSeries(currentSeries);

        currentRenderer = new XYSeriesRenderer();
        currentRenderer.setColor(Color.RED);
        mRenderer2.addSeriesRenderer(currentRenderer);
    }

    private void addSampleData2() {
        for(int i = 0; i<values.length; i++) {
            currentSeries.add(i+4,values[i]);
            Log.d(TAG, i+" "+values[i]);
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
            path = "http://192.168.1.103:4444/server/getResult/"+count;
            //path = "http://10.60.0.171:4444/server/getResult/"+count;
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
                    if(i==0)  val10 = res;
                    else if(i==1) val9 = res;
                    else if(i==2) val8 = res;
                    else if(i==3) val7 = res;
                    else if(i==4) val6 = res;
                    else if(i==5) val5 = res;
                    else val4 = res;
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
