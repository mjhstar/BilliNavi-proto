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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class SettingSub2_Sign extends AppCompatActivity {
    private Toolbar toolbar;
    private String sEmail = null, sPw = null, sPw2, sNickName, sName, sYear, sMonth, sDay, sFriNum, sSecNum, sThrNum, sPhone, sBirth;
    Spinner spinnerYear, spinnerMonth, spinnerDay, spinnerPhone;
    private int index;
    private Calendar calendar;
    private EditText email, pwd, pwdCheck, nickName, name, secPhone, thrPhone;
    private CheckBox checkBox1, checkBox2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_sub2__sign);

        calendar = Calendar.getInstance();
        {
            toolbar = findViewById(R.id.toolbar_sub);
            TextView title = findViewById(R.id.toolbar_title);//title설정하려고
            title.setText("추가정보 입력");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        spinnerYear = findViewById(R.id.spinnerYear_sub);
        spinnerMonth = findViewById(R.id.spinnerMonth_sub);
        spinnerDay = findViewById(R.id.spinnerDay_sub);
        spinnerPhone = findViewById(R.id.spinnerPhone_sub);
        secPhone = findViewById(R.id.secPhone_sub);
        thrPhone = findViewById(R.id.thrPhone_sub);
        checkBox1 = (CheckBox) findViewById(R.id.checkbox1_sub);
        checkBox2 = (CheckBox) findViewById(R.id.checkbox2_sub);

        email = (EditText) findViewById(R.id.edit_email_sub);
        pwd = (EditText) findViewById(R.id.edit_pwd_sub);
        pwdCheck = (EditText) findViewById(R.id.edit_pwdCheck_sub);
        nickName = (EditText) findViewById(R.id.edit_nickname_sub);
        name = (EditText) findViewById(R.id.eidt_name_sub);

        Intent intent = getIntent();
        sEmail = intent.getExtras().getString("email");
        sPw = intent.getExtras().getString("pw");
        email.setText(sEmail);
        pwd.setText(sPw);
        pwdCheck.setText(sPw);
        loginCheck();
        index = MainActivity.userIndex;
        {
            Button buttonOK = (Button) findViewById(R.id.buttonOK_sub);
            buttonOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { //모든 항목에 입력되고, 체크박스 체크되어야만 화면 전환 일어남.
                    sEmail = email.getText().toString();
                    sPw = pwd.getText().toString();
                    sPw2 = pwdCheck.getText().toString();
                    sNickName = nickName.getText().toString();
                    sName = name.getText().toString();
                    sYear = spinnerYear.getSelectedItem().toString();
                    sMonth = spinnerMonth.getSelectedItem().toString();
                    sDay = spinnerDay.getSelectedItem().toString();
                    sFriNum = spinnerPhone.getSelectedItem().toString();
                    sSecNum = secPhone.getText().toString();
                    sThrNum = thrPhone.getText().toString();
                    sPhone = sFriNum + "-" + sSecNum + "-" + sThrNum;
                    sBirth = sYear.substring(0, 4) + "-" + sMonth.substring(0, 1) + "-" + sDay.substring(0, 1);
                    if (checkBox1.isChecked() && checkBox2.isChecked() && email.getText().toString().length() != 0 && pwd.getText().toString().length() != 0 && pwdCheck.getText().toString().length() != 0 && nickName.getText().toString().length() != 0 && name.getText().toString().length() != 0) {
                        if (pwd.getText().toString().equals(pwdCheck.getText().toString()) && infoCheck()) {
                            insertInfo();
                            Toast.makeText(getApplicationContext(), "정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                            finish();

                        } else if (!pwd.getText().toString().equals(pwdCheck.getText().toString())) {
                            Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "모든 항목에 응답해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            setSpinnerPhone();
            setSpinnerYear();
            setSpinnerMonth();
            setSpinnerDay();
        }
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

    public void setSpinnerPhone() {
        spinnerPhone = findViewById(R.id.spinnerPhone_sub);
        ArrayAdapter<String> adapter;
        List<String> list;
        list = new ArrayList<String>();
        list.add("010");
        list.add("011");
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPhone.setAdapter(adapter);
    }

    public void setSpinnerYear() {
        spinnerYear = findViewById(R.id.spinnerYear_sub);
        ArrayAdapter<String> adapter;
        List<String> list;
        list = new ArrayList<>();
        list.add("년");
        for (int i = calendar.get(Calendar.YEAR); i >= 1900; i--) {
            list.add(i + "년");
        }
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(adapter);
    }

    public void setSpinnerMonth() {
        spinnerMonth = findViewById(R.id.spinnerMonth_sub);
        ArrayAdapter<String> adapter;
        List<String> list;
        list = new ArrayList<>();
        list.add("월");
        for (int i = 1; i < 13; i++) {
            list.add(i + "월");
        }
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapter);

    }

    public void setSpinnerDay() {
        spinnerDay = findViewById(R.id.spinnerDay_sub);
        ArrayAdapter<String> adapter;
        List<String> list;
        list = new ArrayList<>();
        list.add("일");
        for (int i = 1; i < 32; i++) {
            list.add(i + "일");
        }
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(adapter);
    }

    public boolean infoCheck() {
        StrictMode.enableDefaults();
        ArrayList<String> listEmail = new ArrayList<>();
        ArrayList<String> listNick = new ArrayList<>();
        ArrayList<String> listPhone = new ArrayList<>();
        boolean bEmail = false, bNick = false, bPhone = false;

        try {
            URL url = new URL("http://192.168.0.145:8088/xml/CheckInfo.asp?email=" + sEmail + "&nickname=" + sNickName + "&phone=" + sPhone + "&index=" + MainActivity.userIndex);

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
                        if (parser.getName().equals("NICKNAME")) {
                            bNick = true;
                        }
                        if (parser.getName().equals("PHONE")) {
                            bPhone = true;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if (bEmail) {
                            listEmail.add(parser.getText());
                            Log.i("asdfasdf", "email: " + parser.getText());
                            bEmail = false;
                        }
                        if (bNick) {
                            listNick.add(parser.getText());
                            Log.i("asdfasdf", "Nick: " + parser.getText());
                            bNick = false;
                        }
                        if (bPhone) {
                            listPhone.add(parser.getText());
                            Log.i("asdfasdf", "Phone: " + parser.getText());
                            bPhone = false;
                        }
                        break;

                }
                parserEvent = parser.next();
            }
        } catch (Exception e) {
        }
        if (listEmail.size() == 0 && listNick.size() == 0 && listPhone.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void insertInfo() {
        String url = "http://192.168.0.145:8088/xml/InfoCorrect.asp";
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

                buffer.append("index").append("=").append("" + index).append("&");
                buffer.append("email").append("=").append(sEmail).append("&");
                buffer.append("pw").append("=").append(sPw).append("&");
                buffer.append("nickname").append("=").append(sNickName).append("&");
                buffer.append("phone").append("=").append(sPhone).append("&");
                buffer.append("name").append("=").append(sName).append("&");
                buffer.append("birthday").append("=").append(sBirth);

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

    public void loginCheck() {
        StrictMode.enableDefaults();
        ArrayList<String> listEmail = new ArrayList<>();
        boolean bEmail = false, bIndex = false;

        try {
            URL url = new URL("http://192.168.0.145:8088/xml/Login.asp?email=" + sEmail + "&pw=" + sPw);

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
                        if (parser.getName().equals("EMAIL")) {
                            bEmail = true;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if (bIndex) {
                            MainActivity.userIndex = Integer.parseInt(parser.getText());
                            bIndex = false;
                        }
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
    }
}
