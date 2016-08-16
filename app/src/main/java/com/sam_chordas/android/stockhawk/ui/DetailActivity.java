package com.sam_chordas.android.stockhawk.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.SlidingTab.SlidingTabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DetailActivity extends AppCompatActivity{

    private Context mContext;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager pager;
    private SlidingTabLayout tabs;

    private CharSequence Title[];
    private int NumberOfTabs = 3;
    private ArrayList<String> stockCloseValueList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Title = new CharSequence[3];
        Title[0] = getString(R.string.stock_graph_type_line);
        Title[1] = getString(R.string.stock_graph_type_point);
        Title[2] = getString(R.string.stock_graph_type_bar);

        if(savedInstanceState != null){
            stockCloseValueList = savedInstanceState.getStringArrayList("stockCloseValueList");
        }
        mContext = getApplicationContext();
        setViewPager(savedInstanceState);
        fetchData(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(stockCloseValueList != null){
            outState.putStringArrayList("stockCloseValueList",stockCloseValueList);
        }
        super.onSaveInstanceState(outState);
    }

    private void setViewPager(final Bundle savedInstanceState){
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),Title, NumberOfTabs);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(viewPagerAdapter);

        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.white);
            }
        });
        tabs.setViewPager(pager);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(savedInstanceState == null){ // avoid calling the setGraphDetail in fragments two times(once from here and once from the onActivityCreated in fragments)
                    if(position == 0){
                        viewPagerAdapter.sendLineGraphDetail(stockCloseValueList);
                    }else if(position ==1){
                        viewPagerAdapter.sendPointGraphDetail(stockCloseValueList);
                    }else {
                        viewPagerAdapter.sendBarGraphDetail(stockCloseValueList);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



    private void fetchData(Bundle savedInstanceState){
        if(savedInstanceState == null){

            Toast.makeText(this, R.string.data_fetching_message, Toast.LENGTH_LONG).show();

            stockCloseValueList = new ArrayList<>();
            Intent intent = getIntent();
            String symbol = intent.getStringExtra("symbol");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            Calendar endPointCalendar = Calendar.getInstance();
            String currentDate = simpleDateFormat.format(endPointCalendar.getTime());

            Calendar startPointCalender = Calendar.getInstance();
            startPointCalender.add(Calendar.YEAR, -1);
            final String startDate = simpleDateFormat.format(startPointCalender.getTime());

            String baseUrl = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22";
            String queryUrl = symbol +
                    "%22%20and%20startDate%20%3D%20%22" +
                    startDate +
                    "%22%20and%20endDate%20%3D%20%22" +
                    currentDate +
                    "%22%0A&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=  ";

            String mUrl = baseUrl + queryUrl;

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(mUrl, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        JSONObject queryObject = response.getJSONObject("query");
                        JSONObject resultObject = queryObject.getJSONObject("results");
                        JSONArray stockData = resultObject.getJSONArray("quote");
                        JSONObject stockDataObject;
                        int count = stockData.length();
                        for(int i=0; i<count; i++){
                            stockDataObject = stockData.getJSONObject(i);
                            double closeValue = stockDataObject.getDouble("Close");
                            stockCloseValueList.add(String.valueOf(closeValue));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    viewPagerAdapter.sendLineGraphDetail(stockCloseValueList);

                }
            }, new com.android.volley.Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast toast = Toast.makeText(mContext, R.string.data_fetching_error, Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
        }
    }
}
