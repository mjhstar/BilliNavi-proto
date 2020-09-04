package com.example.billinavi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SignSub8 extends AppCompatActivity {
    private Toolbar toolbar;
    Spinner spinner;
    private EditText email, pw, name, secPhone, thrPhone;
    private String sEmail, sPw, sName, sSecPhone, sThrPhone, sIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_sub8);
        Button button = (Button) findViewById(R.id.leave);
        email = findViewById(R.id.email_sign8);
        pw = findViewById(R.id.pw_sign8);
        name = findViewById(R.id.name_sign8);
        secPhone = findViewById(R.id.secPhone_sign8);
        thrPhone = findViewById(R.id.thrPhone_sign8);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sEmail = email.getText().toString();
                sPw = pw.getText().toString();
                sName = name.getText().toString();
                if(infoCheck()) {
                    deleteInfo();
                    logout();
                    Intent intent = new Intent(SignSub8.this, SignSub9.class);
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"회원정보를 확인해주세요",Toast.LENGTH_SHORT).show();
                }


            }
        });
        setToolbar();
        setSpinner();
    }

    public void setToolbar() {
        toolbar = findViewById(R.id.toolbar_sub);
        TextView title = findViewById(R.id.toolbar_title);//title설정하려고
        title.setText("회원탈퇴");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //툴바 뒤로가기
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home: {
                finish();
                return true; //메니페스트 수정할거있슴다
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void setSpinner() {
        spinner = findViewById(R.id.spinnerPhone);
        ArrayAdapter<String> adapter;
        List<String> list;

        list = new ArrayList<String>();
        list.add("010");
        list.add("011");
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public boolean infoCheck() {
        StrictMode.enableDefaults();
        ArrayList<String> listIndex = new ArrayList<>();
        boolean bIndex = false;

        try {
            URL url = new URL("http://192.168.0.145:8088/xml/DeleteInfoCheck.asp?email=" + sEmail + "&pw=" + sPw + "&name=" + sName +"&index="+MainActivity.userIndex);
            Log.i("asdfasdf",sEmail +"        "+sPw+"           "+sName+"            "+MainActivity.userIndex);

            XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("INDEX")) {
                            bIndex = true;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if (bIndex) {
                            listIndex.add(parser.getText());
                            sIndex = parser.getText();
                            bIndex = false;
                            Log.i("asdfasdf",parser.getText());
                        }
                        break;

                }
                parserEvent = parser.next();
            }
        } catch (Exception e) {
        }
        if (listIndex.size()>0) {
            return true;
        } else {
            return false;
        }
    }

    public void deleteInfo(){
        String url = "http://192.168.0.145:8088/xml/DeleteInfo.asp";
        NetworkTask networkTask = new NetworkTask(url, null);
        networkTask.execute();
    }

    class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

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

            //output.setText
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

                buffer.append("index").append("=").append(sIndex);

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

    public void logout() {
        MainActivity.userIndex = -1;
        MainActivity.naviHeader.removeAllViews();
        MainActivity.naviHeader_winrate.removeAllViews();
        MainActivity.naviHeader_icon.removeAllViews();

        HomeSub1.home_pager.removeAllViews();
        HomeSub2.home_pager.removeAllViews();
        HomeSub3.home_pager.removeAllViews();

        RankingSub1.rankingSub1_user.removeAllViews();
        RankingSub2.rankingSub2_user.removeAllViews();
        RankingSub3.rankingSub3_user.removeAllViews();

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.side_menu_nav_header_nonmem, MainActivity.naviHeader);
        LayoutInflater inflaterM1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflaterM1.inflate(R.layout.side_menu_winrate_non, MainActivity.naviHeader_winrate);
        LayoutInflater inflater3 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater3.inflate(R.layout.side_menu_icon_non, MainActivity.naviHeader_icon);

        LayoutInflater inflater4 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater4.inflate(R.layout.home_sub1, HomeSub1.home_pager);
        LayoutInflater inflater5 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater5.inflate(R.layout.home_sub2, HomeSub2.home_pager);
        LayoutInflater inflater6 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater6.inflate(R.layout.home_sub3, HomeSub3.home_pager);

        LayoutInflater inflater7 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater7.inflate(R.layout.ranking_sub1_non, RankingSub1.rankingSub1_user);
        LayoutInflater inflater8 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater8.inflate(R.layout.ranking_sub2_non, RankingSub2.rankingSub2_user);
        LayoutInflater inflater9 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater9.inflate(R.layout.ranking_sub3_non, RankingSub3.rankingSub3_user);

        HomeSub2.user = findViewById(R.id.user_non_image);
        HomeSub2.user.setBackground(new ShapeDrawable(new OvalShape()));
        HomeSub2.user.setClipToOutline(true);

        RankingSub1.userImage = findViewById(R.id.user_non_image_rankSub1);
        RankingSub2.userImage = findViewById(R.id.user_non_image_rankSub2);
        RankingSub3.userImage = findViewById(R.id.user_non_image_rankSub3);

        RankingSub1.userImage.setBackground(new ShapeDrawable(new OvalShape()));
        RankingSub1.userImage.setClipToOutline(true);
        RankingSub2.userImage.setBackground(new ShapeDrawable(new OvalShape()));
        RankingSub2.userImage.setClipToOutline(true);
        RankingSub3.userImage.setBackground(new ShapeDrawable(new OvalShape()));
        RankingSub3.userImage.setClipToOutline(true);

        MyRoom.user_profile.setImageResource(R.drawable.user_bg);
        MyRoom.user_profile.setBackground(new ShapeDrawable(new OvalShape()));
        MyRoom.user_profile.setClipToOutline(true);
        MyRoom.userName.setText("로그인이 필요합니다");
        MyRoom.userTeam.setText("");
        MyRoom.userRate.setText("-");
        MyRoom.userHigh.setText("-");
        MyRoom.userWinRate.setText("-");

    }
}