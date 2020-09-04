package com.example.billinavi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RankingRecyclerAdapter extends RecyclerView.Adapter<RankingRecyclerAdapter.ItemViewHolder> {
    private ArrayList<RankingData> listData = new ArrayList<>();
    private Context mContext;

    @NonNull
    @Override
    public RankingRecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_recycleitem, parent, false);
        mContext = parent.getContext();
        return new RankingRecyclerAdapter.ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingRecyclerAdapter.ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    void addItem(RankingData data) {
        listData.add(data);

    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView rankingImage, changeImage;
        private TextView nameText, winRateText;

        ItemViewHolder(View v) {
            super(v);
            rankingImage = v.findViewById(R.id.rankingNum_image);
            changeImage = v.findViewById(R.id.rankingChange_image);
            nameText = v.findViewById(R.id.userID_text);
            winRateText = v.findViewById(R.id.winRate_text);
        }

        void onBind(RankingData data) {
            rankingImage.setImageResource(data.getNumId());
            changeImage.setImageResource(data.getChangeId());
            nameText.setText(data.getUserID());
            winRateText.setText(data.getRate());
        }

        @Override
        public void onClick(View view) {

        }
    }
}