package com.example.billinavi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;


import net.daum.android.map.MapViewEventListener;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class StoreSub4 extends AppCompatActivity {
    private Toolbar toolbar;
    LinearLayout line_store_call2;
    TextView store_call2, store_title, store_title_sub, store_address, store_service;
    float locateX, locateY;
    MapView mapView;
    String sTitle, sAddress, sNumber, sService, sIndex;
    Button myStoreChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_sub4);
        Intent intent = getIntent();
        store_title = findViewById(R.id.interStore_title);
        store_title_sub = findViewById(R.id.interStore_title_sub);
        store_address = findViewById(R.id.interStore_address);
        line_store_call2 = findViewById(R.id.line_store_call2);
        store_call2 = findViewById(R.id.store_call2);
        store_service = findViewById(R.id.interStore_service);
        myStoreChange = findViewById(R.id.myStore_change);

        mapView = new MapView(this);
        ViewGroup mapViewContainer = findViewById(R.id.map_view);

        sIndex = intent.getExtras().getString("index");
        sTitle = intent.getExtras().getString("title");
        sAddress = intent.getExtras().getString("address");
        sNumber = intent.getExtras().getString("number");
        sService = intent.getExtras().getString("service");
        locateX = Float.parseFloat(intent.getExtras().getString("locateX"));
        locateY = Float.parseFloat(intent.getExtras().getString("locateY"));

        store_title.setText(sTitle);
        store_title_sub.setText(sTitle);
        store_address.setText(sAddress);
        store_call2.setText(sNumber);
        store_service.setText(sService);

        MapPOIItem marker = new MapPOIItem();
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(locateX, locateY);
        MapPoint currentPoint = MapPoint.mapPointWithGeoCoord(MainActivity.locateX, MainActivity.locateY);
        Log.i("GPSlocate", "" + locateX + "       " + locateY);
        mapView.setMapCenterPoint(mapPoint, true);
        mapView.setZoomLevel(2, true);

        marker.setItemName(sTitle);
        marker.setTag(1);
        marker.setMapPoint(mapPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        mapView.addPOIItem(marker);

        mapViewContainer.addView(mapView);


        myStoreChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.userIndex > -1) {
                    Store.store_title.setText(store_title.getText().toString());
                    Store.store_address.setText(store_address.getText().toString());
                    Store.store_call.setText(store_call2.getText().toString());
                    Store.index = sIndex;
                    Store.service = sService;
                    Store.locateX = Float.toString(locateX);
                    Store.locateY = Float.toString(locateY);
                    insertInfo();
                    Toast.makeText(getApplicationContext(), "관심매장이 변경되었습니다", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "로그인이 필요합니다", Toast.LENGTH_SHORT).show();
            }
        });


        {
            toolbar = findViewById(R.id.toolbar_sub);
            TextView title = findViewById(R.id.toolbar_title);//title설정하려고
            title.setText("매장정보");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            //전화걸기 시작
            line_store_call2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String call2 = store_call2.getText().toString();
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + call2));
                    startActivity(intent);
                }
            });
        }
    }

    public void insertInfo() {
        String url = "http://192.168.0.145:8088/xml/MyStoreSet.asp";
        NetworkTask networkTask = new NetworkTask(url, null);
        networkTask.execute();
    }

    //네트워크 연결
    class NetworkTask extends AsyncTask<Void, Void, String> {
        private String url;
        private ContentValues values;
        private int count;

        public NetworkTask(String url, ContentValues values) {
            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result;
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //output.setText(s);
        }
    }

    //연결요청
    public class RequestHttpURLConnection {
        public String request(String _url, ContentValues _params) {
            HttpURLConnection urlConn = null;
            StringBuffer sbParams = new StringBuffer();

            if (_params == null)
                sbParams.append("");
            else {
                boolean isAnd = false;
                String key;
                String value;

                for (Map.Entry<String, Object> parameter : _params.valueSet()) {
                    key = parameter.getKey();
                    value = parameter.getValue().toString();

                    if (isAnd)
                        sbParams.append("&");
                    sbParams.append(key).append("=").append(value);

                    if (!isAnd)
                        if (_params.size() >= 2)
                            isAnd = true;
                }
            }
            try {
                URL url = new URL(_url);
                urlConn = (HttpURLConnection) url.openConnection();

                urlConn.setDefaultUseCaches(false);
                urlConn.setDoInput(true);
                urlConn.setDoOutput(true);
                urlConn.setRequestMethod("POST");
                urlConn.setRequestProperty("Accept-Charset", "UTF-8");
                urlConn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;charset=UTF-8");

                StringBuffer buffer = new StringBuffer();
                buffer.append("userIndex").append("=").append(MainActivity.userIndex).append("&");
                buffer.append("storeIndex").append("=").append(sIndex);
                Log.i("asdfasdfasdfasdf", "" + MainActivity.userIndex + "   " + sIndex + "||" + buffer.toString());

                OutputStreamWriter os = new OutputStreamWriter(urlConn.getOutputStream(), "UTF-8");
                PrintWriter writer = new PrintWriter(os);
                writer.write(buffer.toString());
                writer.flush();

                if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK)
                    return null;

                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));

                String line;
                String page = "";

                while ((line = reader.readLine()) != null) {
                    page += line + "\n";
                }
                return page;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConn != null)
                    urlConn.disconnect();
            }
            return null;
        }
    }
}
