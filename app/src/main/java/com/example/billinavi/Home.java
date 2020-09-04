package com.example.billinavi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;

import java.net.URL;
import java.util.ArrayList;

public class Home extends Fragment {
    private ViewPager viewPager;
    private HomePagerAdapter hAdapter;
    private ImageButton next_btn, prev_btn;
    private NestedScrollView scrollView;
    private ImageView imageView1, imageView2, im, im2, im3, im4;
    private TextView best1_title, best2_title, best1_date, best2_date, contest1_title, contest1_start, contest1_finish, contest2_title, contest2_start, contest2_finish;
    private int a;
    private SwipeRefreshLayout layout;

    public static ArrayList<String> title = new ArrayList<>();
    public static ArrayList<String> writer = new ArrayList<>();
    public static ArrayList<String> context = new ArrayList<>();
    public static ArrayList<String> day = new ArrayList<>();
    public static ArrayList<String> time = new ArrayList<>();
    public static ArrayList<String> index = new ArrayList<>();
    public static ArrayList<String> image = new ArrayList<>();
    public static ArrayList<String> videoUrl = new ArrayList<>();
    public static ArrayList<String> contestTitle = new ArrayList<>();
    public static ArrayList<String> startYear = new ArrayList<>();
    public static ArrayList<String> startMonth = new ArrayList<>();
    public static ArrayList<String> startDay = new ArrayList<>();
    public static ArrayList<String> finishYear = new ArrayList<>();
    public static ArrayList<String> finishMonth = new ArrayList<>();
    public static ArrayList<String> finishDay = new ArrayList<>();
    public static ArrayList<String> start = new ArrayList<>(), finish = new ArrayList<>();

    public Home() {
    }

    public static Home newInstance() {
        Bundle args = new Bundle();
        Home fragment = new Home();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);
        //url입력

        //선언부
        {
            scrollView = view.findViewById(R.id.scrollView);
            layout = view.findViewById(R.id.home_swipe);
            viewPager = view.findViewById(R.id.home_viewpager);
            next_btn = view.findViewById(R.id.home_next_btn);
            prev_btn = view.findViewById(R.id.home_prev_btn);

            contest1_title = view.findViewById(R.id.league1_title);
            contest1_start = view.findViewById(R.id.league1_start);
            contest1_finish = view.findViewById(R.id.league1_finish);
            contest2_title = view.findViewById(R.id.league2_title);
            contest2_start = view.findViewById(R.id.league2_start);
            contest2_finish = view.findViewById(R.id.league2_finish);

            //이미지
            imageView1 = view.findViewById(R.id.best1_image);
            imageView2 = view.findViewById(R.id.best2_image);
            best1_title = view.findViewById(R.id.best1_title);
            best2_title = view.findViewById(R.id.best2_title);
            best1_date = view.findViewById(R.id.best1_date);
            best2_date = view.findViewById(R.id.best2_date);

            im = view.findViewById(R.id.user_photo);
            im2 = view.findViewById(R.id.user_photo2);
            im3 = view.findViewById(R.id.league1);
            im4 = view.findViewById(R.id.league2);
            MainActivity.pieChart = view.findViewById(R.id.piechart);
            MainActivity.lineChart = view.findViewById(R.id.lineChart_shot);
        }

        //개인정보 페이저
        {
            hAdapter = new HomePagerAdapter(getChildFragmentManager());
            viewPager.setAdapter(hAdapter);
            viewPager.setClipToPadding(false);
            //개인정보 페이저
            int dpValue = 16;
            float d = getResources().getDisplayMetrics().density;
            int margin = (int) (dpValue * d * 2);

            viewPager.setPadding(margin, 0, margin, 0);
            viewPager.setPageMargin(margin / 2);
            hAdapter.notifyDataSetChanged();
            viewPager.setCurrentItem(1);
            //개인정보 페이저

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    a = position;
                    if (position == 0) {
                        prev_btn.setImageResource(R.drawable.prev_btn_dis);
                        next_btn.setImageResource(R.drawable.next_btn);
                    } else if (position == 1) {
                        prev_btn.setImageResource(R.drawable.prev_btn);
                        next_btn.setImageResource(R.drawable.next_btn);
                        if (MainActivity.userIndex > -1)
                            MainActivity.lineChart.animateXY(1000, 1000);
                    } else if (position == 2) {
                        prev_btn.setImageResource(R.drawable.prev_btn);
                        next_btn.setImageResource(R.drawable.next_btn_dis);
                        if (MainActivity.userIndex > -1)
                            MainActivity.pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    viewPager.getParent().requestDisallowInterceptTouchEvent(true);
                }
            });
            {
                prev_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        a = a - 1;
                        if (a == 0) {
                            viewPager.setCurrentItem(a);
                        } else if (a == 1) {
                            viewPager.setCurrentItem(a);
                        } else if (a == 2) {
                            viewPager.setCurrentItem(a);
                        }
                        if (a < 0)
                            a = 0;
                        if (a > 2)
                            a = 2;
                    }
                });
                next_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        a = a + 1;
                        if (a == 0) {
                            viewPager.setCurrentItem(a);
                        } else if (a == 1) {
                            viewPager.setCurrentItem(a);
                        } else if (a == 2) {
                            viewPager.setCurrentItem(a);
                        }
                        if (a < 0)
                            a = 0;
                        if (a > 2)
                            a = 2;
                    }
                });
            } //홈 화면 페이저 버튼
            viewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });
        }
        init();
        getData();
       getDataContest();
        setInfo();
        setImage();

        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(MainActivity.userIndex>-1) {
                    init();
                    getData();
                    setInfo();
                    setImage();
                    SignSub5.getScore();
                    SignSub5.getRecord();
                    HomeSub3.totalText.setText(MainActivity.sTotal);
                    HomeSub3.winText.setText(MainActivity.sWin);
                    HomeSub3.drawText.setText(MainActivity.sDraw);
                    HomeSub3.loseText.setText(MainActivity.sLose);
                    HomeSub2.highRun.setText(MainActivity.highRun);
                    HomeSub2.average.setText(MainActivity.average);
                    HomeSub2.winningRate.setText(MainActivity.winningRate);
                    MainActivity.getShotData();
                    MainActivity.getAveData();
                    MainActivity.setLineChart();
                    MainActivity.setPieChart();
                    MainActivity.setScore();
                }
                layout.setRefreshing(false);
            }
        });
        return view;
    }

    public Bitmap getImage(String imageUrl) {
        Bitmap image = null;
        InputStream stream = null;
        try {
            URL url = new URL(imageUrl);
            stream = url.openConnection().getInputStream();
            image = BitmapFactory.decodeStream(stream);

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
        return image;
    }

    public void setInfo() {
        imageView1.setImageBitmap(getImage(image.get(0)));
        imageView2.setImageBitmap(getImage(image.get(1)));
        best1_title.setText(title.get(0));
        best2_title.setText(title.get(1));
        best1_date.setText(day.get(0));
        best2_date.setText(day.get(1));
    }

    public void init() {
        index.clear();
        image.clear();
        title.clear();
        day.clear();
        time.clear();
        writer.clear();
        context.clear();
        videoUrl.clear();
        contestTitle.clear();
        startYear.clear();
        startMonth.clear();
        startDay.clear();
        finishYear.clear();
        finishMonth.clear();
        finishDay.clear();
        start.clear();
        finish.clear();
    }

    public void getData() {
        StrictMode.enableDefaults();
        boolean bIndex = false, bImage = false, bTitle = false, bWriter = false, bContext = false, bDay = false, bTime = false, bVideoUrl = false;
        try {
            URL url = new URL("http://192.168.0.145:8088/xml/LoungeSub2_read.asp");

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
                        if (parser.getName().equals("IMAGE")) {
                            bImage = true;
                        }
                        if (parser.getName().equals("TITLE")) {
                            bTitle = true;
                        }
                        if (parser.getName().equals("WRITER")) {
                            bWriter = true;
                        }
                        if (parser.getName().equals("CONTEXT")) {
                            bContext = true;
                        }
                        if (parser.getName().equals("DAY")) {
                            bDay = true;
                        }
                        if (parser.getName().equals("TIME")) {
                            bTime = true;
                        }
                        if (parser.getName().equals("VIDEOURL")) {
                            bVideoUrl = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        if (bIndex) {
                            index.add(parser.getText());
                            Log.i("aaaaaaaaaaaaaaaaaaa", parser.getText());
                            bIndex = false;
                        }
                        if (bImage) {
                            image.add(parser.getText());
                            Log.i("aaaaaaaaaaaaaaaaaaa", parser.getText());
                            bImage = false;
                        }
                        if (bTitle) {
                            title.add(parser.getText());
                            bTitle = false;
                        }
                        if (bWriter) {
                            writer.add(parser.getText());
                            bWriter = false;
                        }
                        if (bContext) {
                            context.add(parser.getText());
                            bContext = false;
                        }
                        if (bDay) {
                            day.add(parser.getText());
                            bDay = false;
                        }
                        if (bTime) {
                            time.add(parser.getText().substring(0, 8));
                            bTime = false;
                        }
                        if (bVideoUrl) {
                            videoUrl.add(parser.getText());
                            bVideoUrl = false;
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch (Exception e) {

        }
    }
    private void getDataContest() {
        StrictMode.enableDefaults();

        boolean bTitle = false, bStartYear = false, bStartMonth = false, bStartDay = false, bFinishYear = false, bFinishMonth = false, bFinishDay = false;

        try {
            URL url = new URL("http://192.168.0.145:8088/xml/LoungeSub5_read.asp?startMonth=" + MainActivity.month + "&finishMonth=" + MainActivity.month + "&startYear=" + MainActivity.year + "&finishYear=" + MainActivity.year);

            XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("TITLE")) {
                            bTitle = true;
                        }
                        if (parser.getName().equals("STARTYEAR")) {
                            bStartYear = true;
                        }
                        if (parser.getName().equals("STARTMONTH")) {
                            bStartMonth = true;
                        }
                        if (parser.getName().equals("STARTDAY")) {
                            bStartDay = true;
                        }
                        if (parser.getName().equals("FINISHYEAR")) {
                            bFinishYear = true;
                        }
                        if (parser.getName().equals("FINISHMONTH")) {
                            bFinishMonth = true;
                        }
                        if (parser.getName().equals("FINISHDAY")) {
                            bFinishDay = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        if (bTitle) {
                            contestTitle.add(parser.getText());
                            bTitle = false;
                        }
                        if (bStartYear) {
                            startYear.add(parser.getText());
                            bStartYear = false;
                        }
                        if (bStartMonth) {
                            startMonth.add(parser.getText());
                            bStartMonth = false;
                        }
                        if (bStartDay) {
                            startDay.add(parser.getText());
                            bStartDay = false;
                        }
                        if (bFinishYear) {
                            finishYear.add(parser.getText());
                            bFinishYear = false;
                        }
                        if (bFinishMonth) {
                            finishMonth.add(parser.getText());
                            bFinishMonth = false;
                        }
                        if (bFinishDay) {
                            finishDay.add(parser.getText());
                            bFinishDay = false;
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch (Exception e) {
        }
        for (int i = 0; i < contestTitle.size(); i++) {
            start.add(startYear.get(i) + "." + startMonth.get(i) + "." + startDay.get(i));
            finish.add(finishYear.get(i) + "." + finishMonth.get(i) + "." + finishDay.get(i));
        }
        if(contestTitle.size()>0) {
            contest1_title.setText(contestTitle.get(0));
            contest1_start.setText(start.get(0));
            contest1_finish.setText(finish.get(0));
        }
        if(contestTitle.size()>1) {
            contest2_title.setText(contestTitle.get(1));
            contest2_start.setText(start.get(1));
            contest2_finish.setText(finish.get(1));
        }
    }

    //이미지 모서리 둥글게
    public void setImage() {
        //시발! 위에만 둥글게는 안되네여
        GradientDrawable drawable = (GradientDrawable) getContext().getDrawable(R.drawable.imageround);
        imageView1.setBackground(drawable);
        imageView1.setClipToOutline(true);
        imageView2.setBackground(drawable);
        imageView2.setClipToOutline(true);
        //동그랗게 만들기
        im.setBackground(new ShapeDrawable(new OvalShape()));
        im.setClipToOutline(true);
        im2.setBackground(new ShapeDrawable(new OvalShape()));
        im2.setClipToOutline(true);
        im3.setBackground(new ShapeDrawable(new OvalShape()));
        im3.setClipToOutline(true);
        im4.setBackground(new ShapeDrawable(new OvalShape()));
        im4.setClipToOutline(true);
    }
}