package com.example.billinavi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static int loginState = 0;
    public static int userIndex = -1, storeIndex = -1;
    public static float locateX, locateY;
    public static LinearLayout naviHeader, naviHeader_winrate, naviHeader_icon;
    public static final int loginCode = 3674;
    public static String average, highRun, winningRate, shotSucc, interval, lastinning, foul, achieverate, sTotal, sWin, sDraw, sLose;
    public static TextView totalText, winText, drawText, loseText;
    public static int allRate, winRate, drawRate, loseRate;
    public static LinearLayout win, draw, lose;
    public static PieChart pieChart;
    public static LineChart lineChart;
    public static LinearLayout.LayoutParams winL, drawL, loseL;
    public static ArrayList<Float> shotRate = new ArrayList<>(), recentAve = new ArrayList<>();
    public static ArrayList<String> shotDate = new ArrayList<>(), recentDate = new ArrayList<>();
    public static int[] color = new int[]{Color.parseColor("#f54c3f"), Color.parseColor("#dfdfdf"), Color.parseColor("#f4b817")}; //#00000000
    public static int year,month,day;

    private Calendar calendar;
    private ImageButton prev_btn, next_btn;
    private ViewPager mViewpager;
    private TabLayout mTap;
    private PagerAdapter mAdapter;
    private Dialog dialog;
    private Button headerLogin;
    private ImageView userImage_header;
    private TextView userName, userDepart;
    private int a, count = 0;
    private boolean DIALOG = true;
    private SharedPreferences prefs;

    boolean isGPSEnabled, isNetworkEnabled;
    LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Get_Internet(getApplicationContext()) > 0) {
            calendar=Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH)+1;
            day = calendar.get(Calendar.DATE);
            //선언부
            {
                dialog = new Dialog(MainActivity.this);
                mViewpager = findViewById(R.id.main_viewpager);
                prev_btn = findViewById(R.id.home_prev_btn);
                next_btn = findViewById(R.id.home_next_btn);
                mTap = findViewById(R.id.main_tabs);
                naviHeader = findViewById(R.id.navi_header);
                naviHeader_winrate = findViewById(R.id.navi_header_winrate);
                naviHeader_icon = findViewById(R.id.navi_header_icon);
                headerLogin = findViewById(R.id.login_header);
                prefs = getSharedPreferences("pref", MODE_PRIVATE);
            }

            //getGps();
            //뷰페이저 설정
            {
                LayoutInflater inflaterM = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                inflaterM.inflate(R.layout.side_menu_nav_header_nonmem, naviHeader);

                LayoutInflater inflaterM1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                inflaterM1.inflate(R.layout.side_menu_winrate_non, naviHeader_winrate);

                LayoutInflater inflaterM2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                inflaterM2.inflate(R.layout.side_menu_icon_non, naviHeader_icon);
            }
            getGps();
            setPager();
            setToolbar();
            checkFirstRun();
            a = -1;
            {
                mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        if (position == 0)
                            getGps();
                        if (a > -1) {
                            if (position == 1) {
                                Ranking.mViewpager.setCurrentItem(a);
                                a = -1;
                            } else if (position == 2) {
                                Lounge.mViewpager.setCurrentItem(a);
                                a = -1;
                            }
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });
                //위에 고정
            }
            mViewpager.setOffscreenPageLimit(5);
        } else
            Toast.makeText(getApplicationContext(), "네트워크 연결을 확인해주세요", Toast.LENGTH_SHORT).show();
        // getGps();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void eventBanner(View v) {
        switch (v.getId()) {
            case R.id.eventBanner_home:
            case R.id.eventBanner_myroom:
                mViewpager.setCurrentItem(3);
                getFragmentManager().beginTransaction().replace(R.id.store_frag, new StoreSub3()).commit();
                break;
            case R.id.banner_lesson:
                mViewpager.setCurrentItem(2);
                Lounge.mViewpager.setCurrentItem(0);
                break;
            case R.id.contestInfo_banner_home:
                mViewpager.setCurrentItem(2);
                Lounge.mViewpager.setCurrentItem(4);
                break;
        }
    }

    //꺾은선 그래프
    public static void setLineChart() {
        List<Entry> entries = new ArrayList<>();

        LineDataSet lineDataSet = new LineDataSet(entries, "속성명1");

        final ArrayList<String> labels = new ArrayList<>();
        labels.add("");
        for (int i = 0; i < shotRate.size(); i++) {
            entries.add(new Entry((i + 1), shotRate.get(i)));
            labels.add(shotDate.get(i));
        }
        labels.add("");

        //그래프 선이랑 점
        lineDataSet.setLineWidth(1);
        //lineDataSet.setCircleRadius(8);
        lineDataSet.setCircleColor(Color.parseColor("#f54c37"));
        lineDataSet.setCircleColorHole(Color.parseColor("#f54c37"));
        lineDataSet.setColor(Color.parseColor("#f54c37"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);

        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        //각 데이터값
        lineDataSet.setValueTextColor(Color.parseColor("#777777"));
        lineDataSet.setValueTextSize(8);
        lineDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return Float.toString(value);
            }
        });

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        //x축 설정
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(6);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(0, 24, 0);


        //y축 설정
        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setLabelCount(5, true);
        yLAxis.setAxisMaxValue(100);
        yLAxis.setAxisMinValue(0);
        yLAxis.setTextColor(Color.BLACK);
        yLAxis.setDrawAxisLine(false);//왼쪽 세로줄 없애기
        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);//오른쪽 세로줄, 값,그리드 없애기

        lineChart.getLegend().setEnabled(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(null);
        lineChart.setTouchEnabled(false);
        lineChart.animateXY(1000, 1000);
        lineChart.invalidate();
    }

    //원 그래프
    public static void setPieChart() {
        int totalInt, winInt, drawInt, loseInt;
        totalInt = Integer.parseInt(HomeSub3.totalText.getText().toString());
        winInt = Integer.parseInt(HomeSub3.winText.getText().toString());
        drawInt = Integer.parseInt(HomeSub3.drawText.getText().toString());
        loseInt = Integer.parseInt(HomeSub3.loseText.getText().toString());

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 5, 5, 5);


        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(78f);
        pieChart.setHoleColor(Color.parseColor("#00000000"));
        pieChart.setTransparentCircleRadius(61f);
        pieChart.getLegend().setEnabled(false);
        pieChart.setTouchEnabled(false);
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);
        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

        yValues.add(new PieEntry(loseInt, "", 0));
        yValues.add(new PieEntry(drawInt, "", 1));
        yValues.add(new PieEntry(winInt, "", 2));

        Description description = new Description();
        description.setText("세계 국가"); //라벨
        description.setTextSize(15);
        description.setEnabled(false);
        // pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션

        PieDataSet dataSet = new PieDataSet(yValues, "");
        //dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(0);
        dataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                //return Integer.toString((int)value);
                return "";
            }
        });
        dataSet.setColors(color);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.DKGRAY);
        pieChart.setData(data);
    }

    //어플이 첫번째 실행인지 아닌지 확인
    public void checkFirstRun() {
        boolean isFirstRun = prefs.getBoolean("mainFirst", true);
        if (isFirstRun) {
            dialog();
            prefs.edit().putBoolean("mainFirst", false).apply();
        } else {

        }
    }

    public void getGps() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isGPSEnabled || isNetworkEnabled) {
            Log.e("GPS Enable", "true");

            final List<String> m_lstProviders = locationManager.getProviders(false);
            // QQQ: 시간, 거리를 0 으로 설정하면 가급적 자주 위치 정보가 갱신되지만 베터리 소모가 많을 수 있다.

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (String name : m_lstProviders) {
                        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            locateX = (float) location.getLatitude();
                            locateY = (float) location.getLongitude();
                            return;
                        }
                        locationManager.requestLocationUpdates(name, 1000, 0, locationListener);
                    }

                }
            });

        } else {
            Log.e("GPS Enable", "false");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(getCurrentFocus(), "앰버를 발견했습니다 GPS를 잠시 켜주시기발바니다.", Snackbar.LENGTH_INDEFINITE).setAction("GPS켜기", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    }).show();
                }
            });
        }
    }
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.e("onLocationChanged", "onLocationChanged");
            Log.e("location", "[" + location.getProvider() + "] (" + location.getLatitude() + "," + location.getLongitude() + ")");
            locateX = (float) location.getLatitude();
            locateY = (float) location.getLongitude();
            locationManager.removeUpdates(locationListener);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e("onStatusChanged", "onStatusChanged");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e("onProviderEnabled", "onProviderEnabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e("onProviderDisabled", "onProviderDisabled");
        }
    };

    //홈 화면 메뉴 선택
    public void home_select(View v) {
        if (MainActivity.userIndex < 0) {
            Toast.makeText(getApplicationContext(), "로그인을 해주세요", Toast.LENGTH_SHORT).show();
        } else {
            switch (v.getId()) {
                case R.id.search_store_home:
                    mViewpager.setCurrentItem(3);
                    //getFragmentManager().beginTransaction().replace(R.id.store_frag, new StoreSub2()).commit();
                    break;
                case R.id.ranking_home:
                    mViewpager.setCurrentItem(1);
                    Ranking.mViewpager.setCurrentItem(0);
                    break;
                case R.id.contestInfo_home:
                    mViewpager.setCurrentItem(2);
                    Lounge.mViewpager.setCurrentItem(4);
                    break;
                case R.id.ARnavi_home:
                    break;
            }
        }
    }

    //로그인 버튼 누르면 헤더 교체
    public void headerLogin(View v) {
        Intent intent = new Intent(getApplicationContext(), SignSub5.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivityForResult(intent, loginCode);
        onBackPressed();
    }

    //메인 뷰페이저 로그인 버튼
    public void login(View v) {
        Intent intent = new Intent(getApplicationContext(), SignSub5.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivityForResult(intent, loginCode);
    }

    //QR코드
    public void qrScan(View v) {

    /*qrScan = new IntentIntegrator(MainActivity.this);
    qrScan.setCaptureActivity(ZxingActivity.class);
    qrScan.setOrientationLocked(false); // default가 세로모드인데 휴대폰 방향에 따라 가로, 세로로 자동 변경됩니다.
    qrScan.initiateScan();*/
        if (userIndex > -1) {
            Toast.makeText(getApplicationContext(), "이미 로그인 되어있습니다.", Toast.LENGTH_SHORT).show();
        } else {
            IntentIntegrator intentIntegrator = new IntentIntegrator(this);
            intentIntegrator.setBeepEnabled(false);//바코드 인식시 소리
            intentIntegrator.setCaptureActivity(QRCode.class);
            intentIntegrator.initiateScan();
        }
    }

    public static void setScore() {
        totalText.setText(sTotal);
        winText.setText(sWin);
        drawText.setText(sDraw);
        loseText.setText(sLose);
        allRate = Integer.parseInt(sTotal);
        winRate = Integer.parseInt(sWin);
        drawRate = Integer.parseInt(sDraw);
        loseRate = Integer.parseInt(sLose);

        winL.weight = (float) winRate / (float) allRate;
        drawL.weight = (float) drawRate / (float) allRate;
        loseL.weight = (float) loseRate / (float) allRate;
        win.setLayoutParams(winL);
        draw.setLayoutParams(drawL);
        lose.setLayoutParams(loseL);
    }

    //QR코드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        //로그인 정보 받아오기
        if (requestCode == loginCode) {
            if (resultCode == RESULT_OK) {
                naviHeader.removeAllViews();
                naviHeader_winrate.removeAllViews();
                naviHeader_icon.removeAllViews();

                HomeSub1.home_pager.removeAllViews();
                HomeSub2.home_pager.removeAllViews();
                HomeSub3.home_pager.removeAllViews();

                RankingSub1.rankingSub1_user.removeAllViews();
                RankingSub2.rankingSub2_user.removeAllViews();
                RankingSub3.rankingSub3_user.removeAllViews();

                LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                inflater1.inflate(R.layout.side_menu_nav_header, naviHeader);
                LayoutInflater inflater2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                inflater2.inflate(R.layout.side_menu_winrate, naviHeader_winrate);
                LayoutInflater inflater3 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                inflater3.inflate(R.layout.side_menu_icon, naviHeader_icon);
                LayoutInflater inflater4 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                inflater4.inflate(R.layout.home_sub1_login, HomeSub1.home_pager);

                lineChart = findViewById(R.id.lineChart_shot);
                getAveData();
                getShotData();
                setLineChart();


                LayoutInflater inflater5 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                inflater5.inflate(R.layout.home_sub2_login, HomeSub2.home_pager);
                LayoutInflater inflater6 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                inflater6.inflate(R.layout.home_sub3_login, HomeSub3.home_pager);

                HomeSub3.totalText = findViewById(R.id.total_homeSub3);
                HomeSub3.winText = findViewById(R.id.win_homeSub3);
                HomeSub3.drawText = findViewById(R.id.draw_homeSub3);
                HomeSub3.loseText = findViewById(R.id.lose_homeSub3);
                HomeSub3.totalText.setText(sTotal);
                HomeSub3.winText.setText(sWin);
                HomeSub3.drawText.setText(sDraw);
                HomeSub3.loseText.setText(sLose);
                win = findViewById(R.id.win_graph);
                draw = findViewById(R.id.draw_graph);
                lose = findViewById(R.id.lose_graph);
                totalText = findViewById(R.id.total_side);
                winText = findViewById(R.id.win_side);
                drawText = findViewById(R.id.draw_side);
                loseText = findViewById(R.id.lose_side);
                winL = (LinearLayout.LayoutParams) win.getLayoutParams();
                drawL = (LinearLayout.LayoutParams) draw.getLayoutParams();
                loseL = (LinearLayout.LayoutParams) lose.getLayoutParams();
                setScore();

                LayoutInflater inflater7 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                inflater7.inflate(R.layout.ranking_sub1_on, RankingSub1.rankingSub1_user);
                LayoutInflater inflater8 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                inflater8.inflate(R.layout.ranking_sub2_on, RankingSub2.rankingSub2_user);
                LayoutInflater inflater9 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                inflater9.inflate(R.layout.ranking_sub3_on, RankingSub3.rankingSub3_user);

                userImage_header = findViewById(R.id.userImage_header);
                userName = findViewById(R.id.userName_header);
                userDepart = findViewById(R.id.department_header);

                HomeSub2.home_id = findViewById(R.id.home_id);
                HomeSub2.home_image = findViewById(R.id.home_user_image);
                HomeSub2.average = findViewById(R.id.average_homeSub2);
                HomeSub2.highRun = findViewById(R.id.hignRun_homeSub2);
                HomeSub2.winningRate = findViewById(R.id.winningRate_homeSub2);
                HomeSub3.nickName = findViewById(R.id.nickName_homeSub3);

                RankingSub1.userName = findViewById(R.id.rankSub1_name);
                RankingSub1.userRate = findViewById(R.id.rankSub1_rate);
                RankingSub1.winRate = findViewById(R.id.rankSub1_winrate);
                RankingSub1.userImage = findViewById(R.id.rankSub1_Image);

                RankingSub2.userName = findViewById(R.id.rankSub2_name);
                RankingSub2.userRate = findViewById(R.id.rankSub2_rate);
                RankingSub2.winRate = findViewById(R.id.rankSub2_winrate);
                RankingSub2.userImage = findViewById(R.id.rankSub2_Image);

                RankingSub3.userName = findViewById(R.id.rankSub3_name);
                RankingSub3.userRate = findViewById(R.id.rankSub3_rate);
                RankingSub3.winRate = findViewById(R.id.rankSub3_winrate);
                RankingSub3.userImage = findViewById(R.id.rankSub3_Image);

                MyRoom.user_profile = findViewById(R.id.user_profile);
                MyRoom.userName = findViewById(R.id.userName_myroom);
                MyRoom.userTeam = findViewById(R.id.userTeam_myroom);
                MyRoom.userRate = findViewById(R.id.userRate_myroom);
                MyRoom.userHigh = findViewById(R.id.userHigh_myroom);
                MyRoom.userWinRate = findViewById(R.id.userWinRate_myroom);

                byte[] profile = data.getByteArrayExtra("image");
                byte[] background = data.getByteArrayExtra("background");
                Bitmap bProfile = BitmapFactory.decodeByteArray(profile, 0, profile.length);
                Bitmap bBackground = BitmapFactory.decodeByteArray(background, 0, background.length);
                String nickName = data.getExtras().getString("nickname");
                String depart = data.getExtras().getString("depart");
                String rate = data.getExtras().getString("rate");
                String ave = data.getExtras().getString("ave");
                String rating = data.getExtras().getString("rating");
                String high = data.getExtras().getString("high");

                userName.setText(nickName);
                userDepart.setText(depart);
                userImage_header.setImageBitmap(bProfile);
                userImage_header.setBackground(new ShapeDrawable(new OvalShape()));
                userImage_header.setClipToOutline(true);

                HomeSub2.home_id.setText(nickName);
                HomeSub2.home_image.setImageBitmap(bProfile);
                HomeSub2.home_image.setBackground(new ShapeDrawable(new OvalShape()));
                HomeSub2.home_image.setClipToOutline(true);
                HomeSub2.average.setText(ave);
                HomeSub2.highRun.setText(high);
                HomeSub2.winningRate.setText(rate);
                HomeSub3.nickName.setText(nickName);

                RankingSub1.userImage.setImageBitmap(bProfile);
                RankingSub1.userImage.setBackground(new ShapeDrawable(new OvalShape()));
                RankingSub1.userImage.setClipToOutline(true);
                RankingSub1.userName.setText(nickName);
                RankingSub1.userRate.setText(rating);
                RankingSub1.winRate.setText(ave);

                RankingSub2.userImage.setImageBitmap(bProfile);
                RankingSub2.userImage.setBackground(new ShapeDrawable(new OvalShape()));
                RankingSub2.userImage.setClipToOutline(true);
                RankingSub2.userName.setText(nickName);
                RankingSub2.userRate.setText(rating);
                RankingSub2.winRate.setText(ave);

                RankingSub3.userImage.setImageBitmap(bProfile);
                RankingSub3.userImage.setBackground(new ShapeDrawable(new OvalShape()));
                RankingSub3.userImage.setClipToOutline(true);
                RankingSub3.userName.setText(nickName);
                RankingSub3.userRate.setText(rating);
                RankingSub3.winRate.setText(ave);

                MyRoom.user_profile.setImageBitmap(bProfile);
                MyRoom.background.setImageBitmap(bBackground);
                MyRoom.background.setBackgroundColor(Color.BLACK);
                MyRoom.user_profile.setBackground(new ShapeDrawable(new OvalShape()));
                MyRoom.user_profile.setClipToOutline(true);
                MyRoom.userName.setText(nickName);
                MyRoom.userTeam.setText(depart);
                MyRoom.userRate.setText(ave);
                MyRoom.userHigh.setText(high);
                MyRoom.userWinRate.setText(rate);
                loginState = 1;
                pieChart = (PieChart) findViewById(R.id.piechart);
                setPieChart();
            }
        } else {
            if (result != null) {
                if (result.getContents() == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                    // todo
                } else {
                    Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                    // todo
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    //메인 페이저 설정
    public void setPager() {
        //프래그먼트 뷰페이저 어댑터 시작
        mAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewpager.setAdapter(mAdapter);
        mTap.setupWithViewPager(mViewpager);
        mAdapter.notifyDataSetChanged();
        mViewpager.setOffscreenPageLimit(5);
        mViewpager.setCurrentItem(0);
        //프래그먼트 뷰페이저 어댑터 끝
    }

    //툴바 네비게이션 설정
    public void setToolbar() {
        //툴바 설정 시작
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //툴바 설정 끝
        //네비게이션 메뉴 시작
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        //네비게이션 메뉴 끝
    }

    //다이얼로그
    public void dialog() {
        //AlertDialog 창 띄우기
        if (DIALOG) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//다이얼로그 타이틀바 없애기
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//배경 투명하게
            dialog.show();
        }
    }

    //네비게이션 메뉴
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("앱 종료").setMessage("종료하시겠습니까?").setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            }).setNegativeButton("아니오", null).show();
        }
    }

    //옵션메뉴 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.side_menu, menu);
        return true;
    }

    //액션메뉴 GPS 버튼
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_location:
                Intent intent = new Intent(MainActivity.this, GPS.class);
                getGps();
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                return true;
            case R.id.home:
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //네비게이션 메뉴 선택
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //매장정보 프래그먼트
    public void store_transfer(View v) {
        Fragment fr = null;
        switch (v.getId()) {
            case R.id.nearStore_btn_L:
                fr = new StoreSub1();
                getFragmentManager().beginTransaction().replace(R.id.store_frag, fr).commit();
                break;
            case R.id.storeSearch_btn_L:
                fr = new StoreSub2();
                getFragmentManager().beginTransaction().replace(R.id.store_frag, fr).commit();
                break;
            case R.id.storeEvent_btn_L:
                fr = new StoreSub3();
                getFragmentManager().beginTransaction().replace(R.id.store_frag, fr).commit();
                break;
        }

    }

    //매장 자세히 보기
    public void storeInfo(View v) {
        if (userIndex > -1) {
            Intent intent = new Intent(getApplicationContext(), StoreSub4.class);
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.putExtra("title", Store.store_title.getText().toString());
            intent.putExtra("address", Store.store_address.getText().toString());
            intent.putExtra("number", Store.store_call.getText().toString());
            intent.putExtra("service", Store.service);
            intent.putExtra("locateX", Store.locateX);
            intent.putExtra("locateY", Store.locateY);
            startActivity(intent);
        } else
            Toast.makeText(getApplicationContext(), "로그인이 필요합니다", Toast.LENGTH_SHORT).show();
    }

    public void viewStore(View v) {
        int position = StoreSub1RecyclerAdapter.thisPosition;
        Intent intent = new Intent(getApplicationContext(), StoreSub4.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.putExtra("title", StoreSub1.title.get(position));
        intent.putExtra("address", StoreSub1.addr.get(position));
        intent.putExtra("number", StoreSub1.call.get(position));
        intent.putExtra("index", StoreSub1.index);
        intent.putExtra("locateX", StoreSub1.locateX);
        intent.putExtra("locateY", StoreSub1.locateY);
        intent.putExtra("service", StoreSub1.service);
        startActivity(intent);
    }

    //마이룸 메뉴 선택
    public void myroom_menu(View v) {
        if (MainActivity.userIndex < 0) {
            Toast.makeText(getApplicationContext(), "로그인을 해주세요", Toast.LENGTH_SHORT).show();
        } else {

            switch (v.getId()) {
                case R.id.myRecord_myroom:
                    Intent myRecord = new Intent(getApplicationContext(), MyRoom2.class);
                    myRecord.setAction(Intent.ACTION_MAIN);
                    myRecord.addCategory(Intent.CATEGORY_HOME);
                    startActivity(myRecord);
                    break;
                case R.id.myMedia_myroom:
                    Intent myVideo = new Intent(getApplicationContext(), MyRoom3.class);
                    myVideo.setAction(Intent.ACTION_MAIN);
                    myVideo.addCategory(Intent.CATEGORY_HOME);
                    startActivity(myVideo);
                    break;
                case R.id.myStore_myroom:
                    if (storeIndex > -1) {
                        Intent intent = new Intent(getApplicationContext(), StoreSub4.class);
                        intent.setAction(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.putExtra("title", Store.store_title.getText().toString());
                        intent.putExtra("address", Store.store_address.getText().toString());
                        intent.putExtra("number", Store.store_call.getText().toString());
                        intent.putExtra("service", Store.service);
                        intent.putExtra("locateX", Store.locateX);
                        intent.putExtra("locateY", Store.locateY);
                        startActivity(intent);
                    } else
                        Toast.makeText(getApplicationContext(), "관심매장을 설정해주세요", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.ARnavi_myroom:

                    break;
                case R.id.ranking_myroom:
                    mViewpager.setCurrentItem(1);
                    a = 0;
                    break;
                case R.id.contestInfo_myroom:
                    a = 5;
                    mViewpager.setCurrentItem(2);
                    break;
                case R.id.lesson_myroom:
                    a = 0;
                    mViewpager.setCurrentItem(2);
                    break;
                case R.id.setting_myroom:
                    Intent intent1 = new Intent(getApplicationContext(), Setting.class);
                    intent1.setAction(Intent.ACTION_MAIN);
                    intent1.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent1);
                    break;
            }
        }
    }

    //다이얼로그 버튼 선택
    public void onClickDialog(View v) {
        switch (v.getId()) {
            case R.id.close_dialog:
                dialog.dismiss();
                break;
            case R.id.setting_dialog:
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), Setting.class);
                startActivity(intent);
                break;
        }
    }

    //4구모드 랭킹
    public void ranking_sub1(View v) {
        if (userIndex > -1) {
            switch (v.getId()) {
                case R.id.average_ranking1:
                    Intent intent = new Intent(getApplicationContext(), RankingAve.class);
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent);
                    break;
                case R.id.highrun_ranking1:
                    Intent intent1 = new Intent(getApplicationContext(), RankingHigh.class);
                    intent1.setAction(Intent.ACTION_MAIN);
                    intent1.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent1);
                    break;
                case R.id.winrate_ranking1:
                    Intent intent2 = new Intent(getApplicationContext(), RankingWin.class);
                    intent2.setAction(Intent.ACTION_MAIN);
                    intent2.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent2);
                    break;
            }
        } else
            Toast.makeText(getApplicationContext(), "로그인이 필요합니다", Toast.LENGTH_SHORT).show();
    }

    //3구모드 일반룰 랭킹
    public void ranking_sub2(View v) {
        if (userIndex > -1) {
            switch (v.getId()) {
                case R.id.average_ranking2:
                    Intent intent = new Intent(getApplicationContext(), RankingAve.class);
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent);
                    break;
                case R.id.highrun_ranking2:
                    Intent intent1 = new Intent(getApplicationContext(), RankingHigh.class);
                    intent1.setAction(Intent.ACTION_MAIN);
                    intent1.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent1);
                    break;
                case R.id.winrate_ranking2:
                    Intent intent2 = new Intent(getApplicationContext(), RankingWin.class);
                    intent2.setAction(Intent.ACTION_MAIN);
                    intent2.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent2);
                    break;
            }
        } else
            Toast.makeText(getApplicationContext(), "로그인이 필요합니다", Toast.LENGTH_SHORT).show();
    }

    //3구모드 대회룰 랭킹
    public void ranking_sub3(View v) {
        if (userIndex > -1) {
            switch (v.getId()) {
                case R.id.average_ranking3:
                    Intent intent = new Intent(getApplicationContext(), RankingAve.class);
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent);
                    break;
                case R.id.highrun_ranking3:
                    Intent intent1 = new Intent(getApplicationContext(), RankingHigh.class);
                    intent1.setAction(Intent.ACTION_MAIN);
                    intent1.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent1);
                    break;
                case R.id.winrate_ranking3:
                    Intent intent2 = new Intent(getApplicationContext(), RankingWin.class);
                    intent2.setAction(Intent.ACTION_MAIN);
                    intent2.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent2);
                    break;
            }
        } else
            Toast.makeText(getApplicationContext(), "로그인이 필요합니다", Toast.LENGTH_SHORT).show();
    }

    //사이드메뉴 선택
    public void sideMenuSelect(View v) {
        if (MainActivity.userIndex < 0) {
            Toast.makeText(getApplicationContext(), "로그인을 해주세요", Toast.LENGTH_SHORT).show();
        } else {
            switch (v.getId()) {
                case R.id.side_Info:
                    Intent intent2 = new Intent(getApplicationContext(), SettingSub2.class);
                    intent2.setAction(Intent.ACTION_MAIN);
                    intent2.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent2);
                    onBackPressed();
                    return;
                case R.id.side_event:
                    mViewpager.setCurrentItem(3);
                    getFragmentManager().beginTransaction().replace(R.id.store_frag, new StoreSub3()).commit();
                    onBackPressed();
                    return;
                case R.id.side_ARnavi:

                    return;
                case R.id.side_menu1:
                    mViewpager.setCurrentItem(0);
                    onBackPressed();
                    return;
                case R.id.side_menu2:
                    mViewpager.setCurrentItem(1);
                    Ranking.mViewpager.setCurrentItem(0);
                    onBackPressed();
                    return;
                case R.id.side_menu3:
                    mViewpager.setCurrentItem(2);
                    Lounge.mViewpager.setCurrentItem(0);
                    onBackPressed();
                    return;
                case R.id.side_menu4:
                    mViewpager.setCurrentItem(3);
                    onBackPressed();
                    return;
                case R.id.side_menu5:
                    mViewpager.setCurrentItem(4);
                    onBackPressed();
                    return;
                case R.id.side_menu6:
                    Intent intent = new Intent(MainActivity.this, Setting.class);
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent);
                    onBackPressed();
                    return;
                case R.id.side_menu7:

                    onBackPressed();
                    return;
                case R.id.side_menu8:
                    logout();
                    onBackPressed();
                    return;
            }
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    //로그아웃
    public void logout() {
        userIndex = -1;
        naviHeader.removeAllViews();
        naviHeader_winrate.removeAllViews();
        naviHeader_icon.removeAllViews();

        HomeSub1.home_pager.removeAllViews();
        HomeSub2.home_pager.removeAllViews();
        HomeSub3.home_pager.removeAllViews();

        RankingSub1.rankingSub1_user.removeAllViews();
        RankingSub2.rankingSub2_user.removeAllViews();
        RankingSub3.rankingSub3_user.removeAllViews();

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.side_menu_nav_header_nonmem, naviHeader);
        LayoutInflater inflaterM1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflaterM1.inflate(R.layout.side_menu_winrate_non, naviHeader_winrate);
        LayoutInflater inflater3 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater3.inflate(R.layout.side_menu_icon_non, naviHeader_icon);

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
        MyRoom.background.setImageResource(R.drawable.best_img_2);
        MyRoom.user_profile.setBackground(new ShapeDrawable(new OvalShape()));
        MyRoom.user_profile.setClipToOutline(true);
        MyRoom.userName.setText("로그인이 필요합니다");
        MyRoom.userTeam.setText("");
        MyRoom.userRate.setText("-");
        MyRoom.userHigh.setText("-");
        MyRoom.userWinRate.setText("-");
        ActivityCompat.finishAffinity(this);
        restart();

    }

    public void home_bestVideo(View v) {
        Intent intent;
        if (userIndex > -1) {
            switch (v.getId()) {
                case R.id.best1_layout:
                    intent = new Intent(getApplicationContext(), LoungeVideo.class);
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.putExtra("index", Home.index.get(0));
                    intent.putExtra("title", Home.title.get(0));
                    intent.putExtra("writer", Home.writer.get(0));
                    intent.putExtra("context", Home.context.get(0));
                    intent.putExtra("day", Home.day.get(0));
                    intent.putExtra("time", Home.time.get(0));
                    intent.putExtra("image", Home.image.get(0));
                    intent.putExtra("video", Home.videoUrl.get(0));
                    startActivity(intent);
                    break;
                case R.id.best2_layout:
                    intent = new Intent(getApplicationContext(), LoungeVideo.class);
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.putExtra("index", Home.index.get(1));
                    intent.putExtra("title", Home.title.get(1));
                    intent.putExtra("writer", Home.writer.get(1));
                    intent.putExtra("context", Home.context.get(1));
                    intent.putExtra("day", Home.day.get(1));
                    intent.putExtra("time", Home.time.get(1));
                    intent.putExtra("image", Home.image.get(1));
                    intent.putExtra("video", Home.videoUrl.get(1));
                    startActivity(intent);
                    break;
            }
        } else
            Toast.makeText(getApplicationContext(), "로그인이 필요합니다", Toast.LENGTH_SHORT).show();
    }

    public void restart() {

        Intent i = getBaseContext().getPackageManager().
                getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    public static int Get_Internet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwrok = cm.getActiveNetworkInfo();
        if (activeNetwrok != null) {
            if (activeNetwrok.getType() == ConnectivityManager.TYPE_WIFI) {
                return 1;
            } else if (activeNetwrok.getType() == ConnectivityManager.TYPE_MOBILE) {
                return 2;
            }
        }
        return 0;
    }

    public static void getShotData() {
        shotDate.clear();
        shotRate.clear();
        StrictMode.enableDefaults();
        boolean bRate = false, bDate = false;
        try {
            URL url = new URL("http://192.168.0.145:8088/xml/ShotRate.asp?userindex=" + userIndex);

            XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("RATE")) {
                            bRate = true;
                        }
                        if (parser.getName().equals("DATE")) {
                            bDate = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        if (bRate) {
                            shotRate.add(Float.parseFloat(parser.getText()));
                            bRate = false;
                        }
                        if (bDate) {
                            shotDate.add(parser.getText().substring(2, 4) + "." + parser.getText().substring(5, 7) + "." + parser.getText().substring(8, 10));
                            bDate = false;
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch (Exception e) {

        }
    }

    public static void getAveData() {
        recentAve.clear();
        recentDate.clear();
        StrictMode.enableDefaults();
        boolean bAverage = false, bDate = false;
        try {
            URL url = new URL("http://192.168.0.145:8088/xml/recentAve.asp?userindex=" + userIndex);

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
                        if (parser.getName().equals("DATE")) {
                            bDate = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        if (bAverage) {
                            recentAve.add(Float.parseFloat(parser.getText()));
                            bAverage = false;
                        }
                        if (bDate) {
                            recentDate.add(parser.getText().substring(2, 4) + "." + parser.getText().substring(5, 7) + "." + parser.getText().substring(8, 10));
                            bDate = false;
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch (Exception e) {}
    }
}