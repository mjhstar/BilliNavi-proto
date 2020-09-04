package com.example.billinavi;


import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

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
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class LoungeSub5 extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    public static int SUNDAY = 1;
    public static int MONDAY = 2;
    public static int TUESDAY = 3;
    public static int WEDNSESDAY = 4;
    public static int THURSDAY = 5;
    public static int FRIDAY = 6;
    public static int SATURDAY = 7;
    public static ArrayList<String> contestTitle = new ArrayList<>();
    public static ArrayList<String> startYear = new ArrayList<>();
    public static ArrayList<String> startMonth = new ArrayList<>();
    public static ArrayList<String> startDay = new ArrayList<>();
    public static ArrayList<String> finishYear = new ArrayList<>();
    public static ArrayList<String> finishMonth = new ArrayList<>();
    public static ArrayList<String> finishDay = new ArrayList<>();
    public static ArrayList<String> start = new ArrayList<>(), finish = new ArrayList<>();
    public static Calendar thisCal;
    public static int month;
    private int year;
    private String sY, sM, sD, fY, fM, fD;

    private TextView ymText, startText, finishText;
    private GridView gridView;

    private ArrayList<DayInfo> list;
    private CalendarAdapter adapter;

    private ImageButton prev, next;
    private Calendar lastCal, nextCal;

    private RecyclerView recyclerView;
    private LoungeSub5RecyclerAdapter rAdapter;

    public LoungeSub5() {
    }

    public static LoungeSub5 newInstance() {
        Bundle args = new Bundle();
        LoungeSub5 fragment = new LoungeSub5();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lounge_sub5, container, false);
        list = new ArrayList<DayInfo>();
        recyclerView = view.findViewById(R.id.lounge_sub5_recy);
        prev = view.findViewById(R.id.prev_btn);
        next = view.findViewById(R.id.next_btn);
        ymText = view.findViewById(R.id.ymText);
        gridView = view.findViewById(R.id.gridView);


        init();
        thisCal = Calendar.getInstance();
        thisCal.set(Calendar.DAY_OF_MONTH, 1);
        getCalendar(thisCal);

        getData();
        /*//url입력
        {
            String url = "http://192.168.0.145:8088/xml/LoungeSub5_read.asp";
            NetworkTask networkTask = new NetworkTask(url, null);
            networkTask.execute();
        }*/


        {
            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!((thisCal.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH)) && (thisCal.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR))))
                    {
                        thisCal = getLastMonth(thisCal);
                        getCalendar(thisCal);
                        init();
                        getData();
                    }
                }
            });
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    thisCal = getNextMonth(thisCal);
                    getCalendar(thisCal);
                    init();
                    getData();
                }
            });
        }



        /*prev.setOnClickListener((View.OnClickListener) getActivity());
        next.setOnClickListener((View.OnClickListener) getActivity());
        gridView.setOnItemClickListener((AdapterView.OnItemClickListener) getActivity());*/
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
        getData();
    }

    private void getCalendar(Calendar calendar) {
        int dayOfMonth;
        int thisMonthLastDay;

        if ((calendar.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH)) && (calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)))
            prev.setImageResource(R.drawable.prev_btn_dis);
        else
            prev.setImageResource(R.drawable.prev_btn);

        list.clear();

        dayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, -1);


        calendar.add(Calendar.MONTH, 1);

        ymText.setText(thisCal.get(Calendar.YEAR) + ". " + (thisCal.get(Calendar.MONTH) + 1));
        month = thisCal.get(Calendar.MONTH) + 1;
        year = thisCal.get(Calendar.YEAR);
        setDate();
        DayInfo day;


        for (int i = 0; i < dayOfMonth - 1; i++) {
            day = new DayInfo();
            day.setDay("");
            day.setInMonth(false);
            list.add(day);
        }
        for (int i = 1; i <= thisMonthLastDay; i++) {
            day = new DayInfo();
            day.setDay(Integer.toString(i));
            day.setMonth(month + "");
            day.setYear(year + "");
            day.setInMonth(true);
            list.add(day);
        }
        initCalendarAdapter();
    }

    private void initCalendarAdapter() {
        adapter = new CalendarAdapter(getActivity(), list);
        gridView.setAdapter(adapter);
    }

    private Calendar getLastMonth(Calendar calendar) {

        Calendar thisMonth = Calendar.getInstance();
        if ((calendar.get(Calendar.MONTH) != thisMonth.get(Calendar.MONTH)) || (calendar.get(Calendar.YEAR) != thisMonth.get(Calendar.YEAR))) {
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
            calendar.add(Calendar.MONTH, -1);
            //ymText.setText(thisCal.get(Calendar.YEAR) + "." + (thisCal.get(Calendar.MONTH) + 1));
        }
        init();
        getData();
        return calendar;
    }

    private Calendar getNextMonth(Calendar calendar) {

        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, +1);
        //ymText.setText(thisCal.get(Calendar.YEAR) + "." + (thisCal.get(Calendar.MONTH) + 1));

        return calendar;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.prev_btn:
                thisCal = getLastMonth(thisCal);
                getCalendar(thisCal);
                break;
            case R.id.next_btn:
                thisCal = getNextMonth(thisCal);
                getCalendar(thisCal);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
    }

    public void setDate() {
        DayInfo day;
        day = new DayInfo();
        day.setDay("S");
        day.setInMonth(true);
        list.add(day);

        day = new DayInfo();
        day.setDay("M");
        day.setInMonth(true);
        list.add(day);

        day = new DayInfo();
        day.setDay("T");
        day.setInMonth(true);
        list.add(day);

        day = new DayInfo();
        day.setDay("W");
        day.setInMonth(true);
        list.add(day);

        day = new DayInfo();
        day.setDay("T");
        day.setInMonth(true);
        list.add(day);

        day = new DayInfo();
        day.setDay("F");
        day.setInMonth(true);
        list.add(day);

        day = new DayInfo();
        day.setDay("S");
        day.setInMonth(true);
        list.add(day);
    }

    private void init() {
        contestTitle.clear();
        startYear.clear();
        startMonth.clear();
        startDay.clear();
        finishYear.clear();
        finishMonth.clear();
        finishDay.clear();
        start.clear();
        finish.clear();
        recyclerView.removeAllViews();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        rAdapter = new LoungeSub5RecyclerAdapter();
        recyclerView.setAdapter(rAdapter);
    }

    private void getData() {
        StrictMode.enableDefaults();

        boolean bTitle = false, bStartYear = false, bStartMonth = false, bStartDay = false, bFinishYear = false, bFinishMonth = false, bFinishDay = false;

        try {
            URL url = new URL("http://192.168.0.145:8088/xml/LoungeSub5_read.asp?startMonth=" + month + "&finishMonth=" + month + "&startYear=" + year + "&finishYear=" + year);

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
                            sY = parser.getText();
                            bStartYear = false;
                        }
                        if (bStartMonth) {
                            startMonth.add(parser.getText());
                            sM = parser.getText();
                            bStartMonth = false;
                        }
                        if (bStartDay) {
                            startDay.add(parser.getText());
                            sD = parser.getText();
                            bStartDay = false;
                        }
                        if (bFinishYear) {
                            finishYear.add(parser.getText());
                            fY = parser.getText();
                            bFinishYear = false;
                        }
                        if (bFinishMonth) {
                            finishMonth.add(parser.getText());
                            fM = parser.getText();
                            bFinishMonth = false;
                        }
                        if (bFinishDay) {
                            finishDay.add(parser.getText());
                            fD = parser.getText();
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
        for (int i = 0; i < contestTitle.size(); i++) {
            LoungeSub5Data data = new LoungeSub5Data();
            data.setTitle(contestTitle.get(i));
            data.setStartDate(start.get(i));
            data.setFinishDate(finish.get(i));
            data.setIndex(i);

            rAdapter.addItem(data);
        }
        rAdapter.notifyDataSetChanged();
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
                String myId, myName;
                URL url = new URL(_url);
                urlConn = (HttpURLConnection) url.openConnection();

                urlConn.setDefaultUseCaches(false);
                urlConn.setDoInput(true);
                urlConn.setDoOutput(true);
                urlConn.setRequestMethod("POST");
                urlConn.setRequestProperty("Accept-Charset", "UTF-8");
                urlConn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;charset=UTF-8");

                StringBuffer buffer = new StringBuffer();
                buffer.append("startMonth").append("=").append(month).append("&");
                buffer.append("finishMonth").append("=").append(month);
                Log.i("asdf", "string : " + buffer.toString());

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