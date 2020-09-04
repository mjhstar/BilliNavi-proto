package com.example.billinavi;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class StoreSub1RecyclerAdapter extends RecyclerView.Adapter<StoreSub1RecyclerAdapter.ItemViewHolder> {
    public static TextView storeName, storeAddr, storeContact, distance;
    public static int thisPosition;
    private ArrayList<StoreSub1Data> listData = new ArrayList<>();
    private LinearLayout item;
    private Button viewStore, setStore;
    private Context mContext;

    @NonNull
    @Override
    public StoreSub1RecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_sub1_item, parent, false);
        mContext = parent.getContext();
        return new StoreSub1RecyclerAdapter.ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreSub1RecyclerAdapter.ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    void addItem(StoreSub1Data data) {
        listData.add(data);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public ItemViewHolder(@NonNull View v) {
            super(v);
            storeName = v.findViewById(R.id.storeName);
            storeAddr = v.findViewById(R.id.storeAddr);
            storeContact = v.findViewById(R.id.storeNumber);
            distance = v.findViewById(R.id.distance);
            item = v.findViewById(R.id.store_sub1_item);
            viewStore = v.findViewById(R.id.myStore_view);
            setStore = v.findViewById(R.id.myStore_set);
        }

        void onBind(StoreSub1Data data) {
            storeName.setText(data.getTitle());
            storeAddr.setText(data.getAddr());
            storeContact.setText(data.getCall());
            distance.setText(data.getDistance());

            item.setOnClickListener(this);
            viewStore.setOnClickListener(this);
            setStore.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            thisPosition = getAdapterPosition();
            Log.i("asdfasdfasdf", "" + thisPosition);
            switch (view.getId()) {
                case R.id.myStore_view:
                    Intent intent = new Intent(view.getContext(), StoreSub4.class);
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.putExtra("index", StoreSub1.index.get(thisPosition));
                    intent.putExtra("title", StoreSub1.title.get(thisPosition));
                    intent.putExtra("address", StoreSub1.addr.get(thisPosition));
                    intent.putExtra("number", StoreSub1.call.get(thisPosition));
                    intent.putExtra("service", StoreSub1.service.get(thisPosition));
                    intent.putExtra("locateX", StoreSub1.locateX.get(thisPosition));
                    intent.putExtra("locateY", StoreSub1.locateY.get(thisPosition));
                    view.getContext().startActivity(intent);
                    break;
                case R.id.myStore_set:
                    if (MainActivity.userIndex > -1) {
                        Store.store_title.setText(StoreSub1.title.get(thisPosition));
                        Store.store_address.setText(StoreSub1.addr.get(thisPosition));
                        Store.store_call.setText(StoreSub1.call.get(thisPosition));
                        Store.index = StoreSub1.index.get(thisPosition);
                        Store.locateX = StoreSub1.locateX.get(thisPosition);
                        Store.locateY = StoreSub1.locateY.get(thisPosition);
                        Store.service = StoreSub1.service.get(thisPosition);
                        MainActivity.storeIndex = Integer.parseInt(StoreSub1.index.get(thisPosition));
                        insertInfo();
                    } else
                        Toast.makeText(view.getContext(), "로그인이 필요합니다", Toast.LENGTH_SHORT).show();
                    break;
            }

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
                buffer.append("storeIndex").append("=").append(MainActivity.storeIndex);
                //Log.i("asdfasdfasdfasdf",""+MainActivity.userIndex+"   "+MainActivity.storeIndex+"||"+buffer.toString());

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
