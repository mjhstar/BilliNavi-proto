package com.example.billinavi;

import android.content.ContentValues;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class HomeSub2 extends Fragment {
    static public TextView home_id, average, highRun, winningRate;
    static public ImageView home_image;
    static public LinearLayout home_pager;
    static public ImageView user;
    private static ImageButton prev_btn, next_btn;

    public HomeSub2() {
        // Required empty public constructor
    }

    public static HomeSub2 newInstance() {
        Bundle args = new Bundle();
        HomeSub2 fragment = new HomeSub2();
        fragment.setArguments(args);
        /*prev_btn.setImageResource(R.drawable.prev_btn);
        next_btn.setImageResource(R.drawable.next_btn);*/
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_sub2, container, false);
        //선언부
        {
            prev_btn = view.findViewById(R.id.home_prev_btn);
            next_btn = view.findViewById(R.id.home_next_btn);
            home_pager = view.findViewById(R.id.home2_pager);
            user = view.findViewById(R.id.user_non_image);
        }
        //url입력
        {
            String url = "";
            NetworkTask networkTask = new NetworkTask(url, null);
            networkTask.execute();
        }

        user.setBackground(new ShapeDrawable(new OvalShape()));
        user.setClipToOutline(true);
        return view;
    }

    //네트워크 연결
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
           /* buffer.append("id").append("=").append(myId).append("&");
            buffer.append("name").append("=").append(myName);*/

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