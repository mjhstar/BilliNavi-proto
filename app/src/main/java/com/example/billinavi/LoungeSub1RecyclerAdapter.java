package com.example.billinavi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;


public class LoungeSub1RecyclerAdapter extends RecyclerView.Adapter<LoungeSub1RecyclerAdapter.ItemViewHolder> {
    private ArrayList<LoungeSub1Data> listData = new ArrayList<>();
    private Context mContext;
    private int seq;

    LoungeSub1RecyclerAdapter(int a) {
        seq = a;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (seq == 4) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.myroom3_item, parent, false);
            mContext = parent.getContext();
            return new LoungeSub1RecyclerAdapter.ItemViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lounge_sub1_item, parent, false);
            mContext = parent.getContext();
            return new LoungeSub1RecyclerAdapter.ItemViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    void addItem(LoungeSub1Data data) {
        listData.add(data);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView image;
        private TextView title, content, user, date;
        private LinearLayout linearLayout;

        ItemViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.lounge_sub1_image_item);
            title = v.findViewById(R.id.lounge_sub1_title_item);
            content = v.findViewById(R.id.lounge_sub1_content_item);
            user = v.findViewById(R.id.lounge_sub1_user_item);
            date = v.findViewById(R.id.lounge_sub1_date_item);
            if (seq == 4) {
                linearLayout = v.findViewById(R.id.myRoom3_item);
            } else {
                linearLayout = v.findViewById(R.id.lounge_sub1_item);
            }
        }

        void onBind(LoungeSub1Data data) {
            image.setImageBitmap(getImage(data.getImage()));
            title.setText(data.getTitle());
            content.setText(data.getContent());
            user.setText(data.getUser());
            date.setText(data.getDate());
            linearLayout.setOnClickListener(this);
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

        @Override
        public void onClick(View view) {
            int position;
            if (MainActivity.userIndex < 0) {
                Toast.makeText(view.getContext(), "로그인을 해주세요", Toast.LENGTH_SHORT).show();
            } else {
                switch (view.getId()) {
                    case R.id.lounge_sub1_item:
                        position = getAdapterPosition();
                        if (seq == 1) {
                            Intent intent = new Intent(mContext, LoungeVideo.class);
                            intent.setAction(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.putExtra("index", LoungeSub1.index.get(position));
                            intent.putExtra("title", LoungeSub1.title.get(position));
                            intent.putExtra("writer", LoungeSub1.writer.get(position));
                            intent.putExtra("context", LoungeSub1.context.get(position));
                            intent.putExtra("day", LoungeSub1.day.get(position));
                            intent.putExtra("time", LoungeSub1.time.get(position));
                            intent.putExtra("image", LoungeSub1.image.get(position));
                            intent.putExtra("video", LoungeSub1.videoUrl.get(position));
                            mContext.startActivity(intent);
                            break;
                        }
                        if (seq == 2) {
                            Intent intent = new Intent(mContext, LoungeVideo.class);
                            intent.setAction(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.putExtra("index", LoungeSub2.index.get(position));
                            intent.putExtra("title", LoungeSub2.title.get(position));
                            intent.putExtra("writer", LoungeSub2.writer.get(position));
                            intent.putExtra("context", LoungeSub2.context.get(position));
                            intent.putExtra("day", LoungeSub2.day.get(position));
                            intent.putExtra("time", LoungeSub2.time.get(position));
                            intent.putExtra("image", LoungeSub2.image.get(position));
                            intent.putExtra("video", LoungeSub2.videoUrl.get(position));
                            mContext.startActivity(intent);
                            break;
                        }
                        if (seq == 3) {
                            Intent intent = new Intent(mContext, LoungeVideo.class);
                            intent.setAction(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.putExtra("index", LoungeSub3.index.get(position));
                            intent.putExtra("title", LoungeSub3.title.get(position));
                            intent.putExtra("writer", LoungeSub3.writer.get(position));
                            intent.putExtra("context", LoungeSub3.context.get(position));
                            intent.putExtra("day", LoungeSub3.day.get(position));
                            intent.putExtra("time", LoungeSub3.time.get(position));
                            intent.putExtra("image", LoungeSub3.image.get(position));
                            intent.putExtra("video", LoungeSub3.videoUrl.get(position));
                            mContext.startActivity(intent);
                            break;
                        }
                    case R.id.myRoom3_item:
                        position=getAdapterPosition();
                        Intent intent = new Intent(mContext, LoungeVideo.class);
                        intent.setAction(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.putExtra("index", MyRoom3.index.get(position));
                        intent.putExtra("title", MyRoom3.title.get(position));
                        intent.putExtra("writer", MyRoom3.writer.get(position));
                        intent.putExtra("context", MyRoom3.context.get(position));
                        intent.putExtra("day", MyRoom3.day.get(position));
                        intent.putExtra("time", MyRoom3.time.get(position));
                        intent.putExtra("image", MyRoom3.image.get(position));
                        intent.putExtra("video", MyRoom3.videoUrl.get(position));
                        mContext.startActivity(intent);
                        break;
                }
            }
        }
    }
}
