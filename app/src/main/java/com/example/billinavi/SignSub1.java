package com.example.billinavi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignSub1 extends AppCompatActivity {
    public static String sEmail, sPw, sPw2;

    private Toolbar toolbar;
    private Button signBtn;
    private EditText email, pw, pw2;
    private Date now;
    private String sDate, sDay, sTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_sub1);
        signBtn = (Button) findViewById(R.id.btn_sign);
        email = findViewById(R.id.email_edit_sign1);
        pw = findViewById(R.id.pw_edit_sign1);
        pw2 = findViewById(R.id.pw2_edit_sign1);

        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sEmail = email.getText().toString();
                sPw = pw.getText().toString();
                sPw2 = pw2.getText().toString();
                if (emailCheck() && (sPw.equals(sPw2)) && sPw.endsWith("") && checkEmailFormat(sEmail)) {
                    setTime();
                    insertInfo();
                    Toast.makeText(getApplicationContext(), "회원가입이 되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignSub1.this, SignSub2.class);
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.putExtra("email", sEmail);
                    intent.putExtra("pw", sPw);
                    startActivity(intent);
                    finish();
                } else if (!checkEmailFormat(sEmail)) {
                    Toast.makeText(getApplicationContext(), "이메일 형식을 확인해주세요", Toast.LENGTH_SHORT).show();
                } else if (emailCheck() == false) {
                    Toast.makeText(getApplicationContext(), "이미 존재하는 이메일입니다.", Toast.LENGTH_SHORT).show();
                } else if (emailCheck() && !(sPw.equals(sPw2))) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "이메일과 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        setToolbar();
    }

    public void setToolbar() {
        toolbar = findViewById(R.id.toolbar_sub);
        TextView title = findViewById(R.id.toolbar_title);//title설정하려고
        title.setText("회원가입");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

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

    public boolean checkEmailFormat(String email) {
        String sample = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(sample);
        Matcher m = p.matcher(email);
        boolean isNormal = m.matches();
        return isNormal;
    }

    public boolean emailCheck() {
        StrictMode.enableDefaults();
        ArrayList<String> listEmail = new ArrayList<>();
        boolean bEmail = false;

        try {
            URL url = new URL("http://192.168.0.145:8088/xml/emailCheck.asp?email=" + sEmail);

            XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("EMAIL")) {
                            bEmail = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        if (bEmail) {
                            listEmail.add(parser.getText());
                            Log.i("asdfasdf", "get: " + parser.getText());
                            bEmail = false;
                        }
                        break;

                }
                parserEvent = parser.next();
            }
        } catch (Exception e) {
        }
        Log.i("asdfasdf", "size : " + listEmail.size());
        if (listEmail.size() == 0)
            return true;
        else
            return false;
    }

    public void insertInfo() {
        String url = "http://192.168.0.145:8088/xml/SignUp.asp";
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

                buffer.append("email").append("=").append(sEmail).append("&");
                buffer.append("pw").append("=").append(sPw).append("&");
                buffer.append("right").append("=").append("1").append("&");
                buffer.append("day").append("=").append(sDay).append("&");
                buffer.append("time").append("=").append(sTime);

                Log.i("asdfasdf", "buffer : " + buffer.toString());

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

    public void setTime() {
        now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sDate = format.format(now);
        sDay = sDate.substring(0, 10);
        sTime = sDate.substring(11, 19);
    }
}
