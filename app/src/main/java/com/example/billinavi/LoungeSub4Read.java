package com.example.billinavi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class LoungeSub4Read extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView title, writer, context, day, time;
    private Button back, delete;
    private String index, sTitle;
    private int userIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lounge_sub4_read);
        back = findViewById(R.id.backBtn_lounge4_read);
        title = findViewById(R.id.board_title);
        writer = findViewById(R.id.board_writer);
        context = findViewById(R.id.board_context);
        day = findViewById(R.id.board_day);
        time = findViewById(R.id.board_time);
        delete = findViewById(R.id.deleteBtn_lounge4_read);

        Intent intent = getIntent();
        title.setText(intent.getExtras().getString("title"));
        writer.setText(intent.getExtras().getString("writer"));
        context.setText(intent.getExtras().getString("context"));
        day.setText(intent.getExtras().getString("day"));
        time.setText(intent.getExtras().getString("time"));
        index = intent.getExtras().getString("index");
        sTitle = intent.getExtras().getString("title");
        userIndex = Integer.parseInt(intent.getExtras().getString("userIndex"));
        setToolbar();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userIndex==MainActivity.userIndex)
                    show();
                else
                    Toast.makeText(getApplicationContext(),"권한이 없습니다",Toast.LENGTH_SHORT).show();
                /*if(userIndex==MainActivity.userIndex) {
                    String url = "http://192.168.0.145:8088/xml/LoungeSub4_write.asp";
                    LoungeSub4Read.NetworkTask networkTask = new LoungeSub4Read.NetworkTask(url, null);
                    networkTask.execute();
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(),"권한이 없습니다",Toast.LENGTH_SHORT).show();*/
            }
        });

    }

    public void show(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("게시글 삭제");
        builder.setMessage("게시글을 삭제하시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String url = "http://192.168.0.145:8088/xml/LoungeSub4_write.asp";
                LoungeSub4Read.NetworkTask networkTask = new LoungeSub4Read.NetworkTask(url, null);
                networkTask.execute();
                finish();
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    public void setToolbar() {
        //툴바 설정하기
        toolbar = findViewById(R.id.toolbar_sub);
        TextView toolTitle = findViewById(R.id.toolbar_title);//
        toolTitle.setText(sTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            LoungeSub4Read.RequestHttpURLConnection requestHttpURLConnection = new LoungeSub4Read.RequestHttpURLConnection();
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
                buffer.append("title").append("=").append(index).append("&");
                buffer.append("writer").append("=").append("").append("&");
                buffer.append("context").append("=").append("").append("&");
                buffer.append("day").append("=").append("delete").append("&");
                buffer.append("time").append("=").append("");

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