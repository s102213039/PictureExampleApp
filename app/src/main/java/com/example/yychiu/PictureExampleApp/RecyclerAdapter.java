package com.example.yychiu.PictureExampleApp;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

/**
 * Created by yychiu on 2017/12/25.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private ArrayList<ItemData> itemsData;
    private Cursor mCursor;


    public RecyclerAdapter(ArrayList<ItemData> itemsData) {
        this.itemsData = itemsData;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.thumb_item, null);

        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ItemData data = itemsData.get(position);
        viewHolder.txtViewTitle.setText(data.getTitle());
        Glide.with(viewHolder.itemView.getContext())
                .load(data.getImageUrl())
                .apply(new RequestOptions().override(350,350))
                .into(viewHolder.imgViewIcon);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtViewTitle;
        public ImageView imgViewIcon;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtViewTitle = itemLayoutView.findViewById(R.id.item_title);
            imgViewIcon = itemLayoutView.findViewById(R.id.item_icon);
        }
    }

    @Override
    public int getItemCount() {return itemsData.size();}

    public void swapCursor(Cursor newCursor){
        mCursor = newCursor;
        notifyDataSetChanged();
    }
}