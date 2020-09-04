package com.example.billinavi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class SignSub5 extends AppCompatActivity {
    public static String aEmail, aNickName, aName, aPhone, aProfile, aBack;
    public static Bitmap profile, background;

    private Toolbar toolbar;
    private Button email_pw_find, sign_up, sign;
    private EditText email, pw;
    private String sEmail, sPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_sub5);
        sign = findViewById(R.id.btn_sign);
        email_pw_find = findViewById(R.id.email_pw_find_btn);
        sign_up = findViewById(R.id.sign_up_btn);
        email = findViewById(R.id.email_edit_sign5);
        pw = findViewById(R.id.pw_edit_sign5);

        {
            sign_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), SignSub1.class);
                    startActivity(intent);
                }
            });
            email_pw_find.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), SignSub6.class);
                    startActivity(intent);
                }
            });
            sign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sEmail = email.getText().toString();
                    sPw = pw.getText().toString();
                    if (loginCheck() && !sEmail.equals("") && !sPw.equals("")) {
                        infoLoad();
                        getProfile();
                        getBackground();
                        setMyStore();
                        getRecord();
                        getScore();
                        setIntent();
                    } else if (sEmail.equals("") || sPw.equals("")) {
                        Toast.makeText(getApplicationContext(), "이메일과 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "이메일과 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
        setToolbar();
    }

    public void setIntent() {
        Intent intent = new Intent();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
        profile.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        background.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
        byte[] bytes1 = stream1.toByteArray();
        byte[] bytes = stream.toByteArray();
        intent.putExtra("background", bytes1);
        intent.putExtra("image", bytes);
        intent.putExtra("nickname", aNickName);
        intent.putExtra("depart", "서울 관악구 승리당구 클럽");
        intent.putExtra("rating", "에버리지 321위");
        intent.putExtra("ave", MainActivity.average);
        intent.putExtra("high", MainActivity.highRun);
        intent.putExtra("rate", MainActivity.winningRate);
        setResult(RESULT_OK, intent);
        finish();
    }

    public static void getScore() {
        StrictMode.enableDefaults();
        ArrayList<String> listEmail = new ArrayList<>();
        boolean bTotal = false, bWin = false, bLose = false, bDraw = false;

        try {
            URL url = new URL("http://192.168.0.145:8088/xml/Score.asp?index=" + MainActivity.userIndex);

            XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("TOTAL")) {
                            bTotal = true;
                        }
                        if (parser.getName().equals("WIN")) {
                            bWin = true;
                        }
                        if (parser.getName().equals("DRAW")) {
                            bDraw = true;
                        }
                        if (parser.getName().equals("LOSE")) {
                            bLose = true;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if (bTotal) {
                            MainActivity.sTotal = parser.getText();
                            bTotal = false;
                        }
                        if (bWin) {
                            MainActivity.sWin = parser.getText();
                            bWin = false;
                        }
                        if (bDraw) {
                            MainActivity.sDraw = parser.getText();
                            bDraw = false;
                        }
                        if (bLose) {
                            MainActivity.sLose = parser.getText();
                            bLose = false;
                        }
                        break;

                }
                parserEvent = parser.next();
            }
        } catch (Exception e) {
        }
    }

    public static void getRecord() {
        StrictMode.enableDefaults();
        ArrayList<String> listEmail = new ArrayList<>();
        boolean bAverage = false, bHignRun = false, bWinningRate = false, bShot = false, bInterval = false, bLast = false, bFoul = false, bAchieve = false;

        try {
            URL url = new URL("http://192.168.0.145:8088/xml/SignRecord.asp?index=" + MainActivity.userIndex);

            XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("AVERAGE")) {
                            bAverage = true;
                        }
                        if (parser.getName().equals("HIGHRUN")) {
                            bHignRun = true;
                        }
                        if (parser.getName().equals("WINNINGRATE")) {
                            bWinningRate = true;
                        }
                        if (parser.getName().equals("SHOTSUCCESS")) {
                            bShot = true;
                        }
                        if (parser.getName().equals("INTERVAL")) {
                            bInterval = true;
                        }
                        if (parser.getName().equals("LASTINNING")) {
                            bLast = true;
                        }
                        if (parser.getName().equals("FOUL")) {
                            bFoul = true;
                        }
                        if (parser.getName().equals("ACHIEVERATE")) {
                            bAchieve = true;
                        }

                        break;
                    case XmlPullParser.TEXT:
                        if (bAverage) {
                            MainActivity.average = parser.getText();
                            bAverage = false;
                        }
                        if (bHignRun) {
                            MainActivity.highRun = parser.getText();
                            bHignRun = false;
                        }
                        if (bWinningRate) {
                            MainActivity.winningRate = parser.getText();
                            bWinningRate = false;
                        }
                        if (bShot) {
                            MainActivity.shotSucc = parser.getText();
                            bShot = false;
                        }
                        if (bInterval) {
                            MainActivity.interval = parser.getText();
                            bInterval = false;
                        }
                        if (bLast) {
                            MainActivity.lastinning = parser.getText();
                            bLast = false;
                        }
                        if (bFoul) {
                            MainActivity.foul = parser.getText();
                            bFoul = false;
                        }
                        if (bAchieve) {
                            MainActivity.achieverate = parser.getText();
                            bAchieve = false;
                        }
                        break;

                }
                parserEvent = parser.next();
            }
        } catch (Exception e) {
        }
    }

    public boolean loginCheck() {
        StrictMode.enableDefaults();
        ArrayList<String> listEmail = new ArrayList<>();
        boolean bEmail = false, bIndex = false, bStore = false;

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
                        if (parser.getName().equals("STORE")) {
                            bStore = true;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if (bIndex) {
                            MainActivity.userIndex = Integer.parseInt(parser.getText());
                            bIndex = false;
                        }
                        if (bStore) {
                            MainActivity.storeIndex = Integer.parseInt(parser.getText());
                            bStore = false;
                        }
                        if (bEmail) {
                            listEmail.add(parser.getText());
                            bEmail = false;
                        }
                        break;

                }
                parserEvent = parser.next();
            }
        } catch (Exception e) {
        }

        Log.i("asdfasdf", "size : " + listEmail.size());
        if (listEmail.size() == 1) {
            return true;
        } else {
            return false;
        }
    }

    public void infoLoad() {
        StrictMode.enableDefaults();
        boolean bEmail = false, bNick = false, bName = false, bBirth = false, bPhone = false, bProfile = false, bBack = false;

        try {
            URL url = new URL("http://192.168.0.145:8088/xml/InfoSet.asp?index=" + MainActivity.userIndex);

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
                        if (parser.getName().equals("NAME")) {
                            bName = true;
                        }
                        if (parser.getName().equals("PHONE")) {
                            bPhone = true;
                        }
                        if (parser.getName().equals("PROFILE")) {
                            bProfile = true;
                        }
                        if (parser.getName().equals("BACK")) {
                            bBack = true;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if (bEmail) {
                            aEmail = parser.getText();
                            bEmail = false;
                        }
                        if (bNick) {
                            aNickName = parser.getText();
                            bNick = false;
                        }
                        if (bName) {
                            aName = parser.getText();
                            bName = false;
                        }
                        if (bPhone) {
                            aPhone = parser.getText();
                            bPhone = false;
                        }
                        if (bProfile) {
                            aProfile = parser.getText();
                            bProfile = false;
                        }
                        if (bBack) {
                            aBack = parser.getText();
                            bBack = false;
                        }
                        break;

                }
                parserEvent = parser.next();
            }

        } catch (Exception e) {
        }

    }

    public void setToolbar() {
        toolbar = findViewById(R.id.toolbar_sub);
        TextView title = findViewById(R.id.toolbar_title);//title설정하려고
        title.setText("로그인");
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

    public void getProfile() {

        InputStream stream = null;
        try {
            URL url = new URL(aProfile);
            stream = url.openConnection().getInputStream();
            profile = BitmapFactory.decodeStream(stream);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void getBackground() {

        InputStream stream = null;
        try {
            URL url = new URL(aBack);
            stream = url.openConnection().getInputStream();
            background = BitmapFactory.decodeStream(stream);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setMyStore() {
        StrictMode.enableDefaults();
        boolean bIndex = false, bTitle = false, bAddr = false, bCall = false, bLocateX = false, bLocateY = false, bService = false;
        String sTitle = null, sAddr = null, sCall = null, sLocateX = null, sLocateY = null, sService = null;
        try {
            URL url = new URL("http://192.168.0.145:8088/xml/StoreSet.asp?index=" + MainActivity.storeIndex);

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
                        if (parser.getName().equals("TITLE")) {
                            bTitle = true;
                        }
                        if (parser.getName().equals("ADDRESS")) {
                            bAddr = true;
                        }
                        if (parser.getName().equals("CALL")) {
                            bCall = true;
                        }
                        if (parser.getName().equals("LOCATEX")) {
                            bLocateX = true;
                        }
                        if (parser.getName().equals("LOCATEY")) {
                            bLocateY = true;
                        }
                        if (parser.getName().equals("SERVICETIME")) {
                            bService = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        if (bIndex) {
                            Store.index = parser.getText();
                            bIndex = false;
                        }
                        if (bTitle) {
                            Store.store_title.setText(parser.getText());
                            bTitle = false;
                        }
                        if (bAddr) {
                            Store.store_address.setText(parser.getText());
                            bAddr = false;
                        }
                        if (bCall) {
                            Store.store_call.setText(parser.getText());
                            bCall = false;
                        }
                        if (bLocateX) {
                            Store.locateX = (parser.getText());
                            bLocateX = false;
                        }
                        if (bLocateY) {
                            Store.locateY = (parser.getText());
                            bLocateY = false;
                        }
                        if (bService) {
                            Store.service = (parser.getText());
                            bService = false;
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch (Exception e) {
        }
    }
}
