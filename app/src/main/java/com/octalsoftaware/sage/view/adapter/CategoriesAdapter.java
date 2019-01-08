package com.octalsoftaware.sage.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.octalsoftaware.sage.model.CategoriesModel;
import com.octalsoftaware.sage.util.S;
import com.octalsoftaware.sage.util.SagePreference;
import com.octalsoftaware.sage.view.activity.ChatListingActivity;
import com.sage.conslantant.android.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyViewHolder> {

    private Context mContext;
    private List<CategoriesModel> categoriesList;
    private int tempPosition = -1;

    public CategoriesAdapter(Context activity, List<CategoriesModel> categoriesList) {
        mContext = activity;
        this.categoriesList = categoriesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_categories, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        if (categoriesList.get(position).getImage() != null) {
            Picasso.get()
                    .load(categoriesList.get(position).getImage())
                    .into(holder.ivIcon);
        }

        holder.tvName.setText(categoriesList.get(position).getName());
        if (categoriesList.get(position).getDescription().length() > 200) {
            holder.tvDescription.setText(categoriesList.get(position).getDescription().substring(0, 200) + "...");
        } else {
            holder.tvDescription.setText(categoriesList.get(position).getDescription());
        }

        Random rnd = new Random();
        int color = Color.argb(100, rnd.nextInt(133), rnd.nextInt(38), rnd.nextInt(132));
        holder.ivDivider.setBackgroundColor(color);
        /*if (position == 0) {
            holder.ivDivider.setBackgroundColor(mContext.getColor(R.color.colorAccent));
            holder.ivIcon.setImageDrawable(mContext.getDrawable(R.drawable.ic_menu_gallery));
            holder.ivIcon.setColorFilter(Color.parseColor("#99333333"));
        } else if (position % 2 == 0) {
            holder.ivDivider.setBackgroundColor(mContext.getColor(R.color.theme_yellow));
            holder.ivIcon.setImageDrawable(mContext.getDrawable(R.drawable.ic_menu_share));
            holder.ivIcon.setColorFilter(Color.parseColor("#99333333"));
        } else if (position % 3 == 0) {
            holder.ivDivider.setBackgroundColor(mContext.getColor(R.color.dot_light_screen2));
            holder.ivIcon.setImageDrawable(mContext.getDrawable(R.drawable.ic_camera));
            holder.ivIcon.setColorFilter(Color.parseColor("#99333333"));
        } else if (position % 4 == 0) {
            holder.ivDivider.setBackgroundColor(mContext.getColor(R.color.theme_dark));
            holder.ivIcon.setImageDrawable(mContext.getDrawable(R.drawable.ic_menu_gallery));
            holder.ivIcon.setColorFilter(Color.parseColor("#99333333"));
        }*/


        holder.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempPosition = position;
                notifyDataSetChanged();
                SagePreference.setUnit(S.CATEGORY_ID, categoriesList.get(position).getId(), mContext);
                Intent i = new Intent(mContext, ChatListingActivity.class);
                mContext.startActivity(i);
            }
        });
        if (tempPosition == position) {
            holder.layoutMain.setBackgroundColor(mContext.getColor(R.color.theme_dark));
            holder.tvName.setTextColor(mContext.getColor(R.color.colorWhite));
            holder.tvDescription.setTextColor(mContext.getColor(R.color.colorWhite));
            holder.layoutImgBackground.setBackgroundColor(mContext.getColor(R.color.theme_dark));
            holder.ivIcon.setColorFilter(Color.parseColor("#ffffff"));
        } else {
            holder.layoutMain.setBackgroundColor(mContext.getColor(R.color.colorWhite));
            holder.tvName.setTextColor(mContext.getColor(R.color.colorBlack));
            holder.tvDescription.setTextColor(Color.parseColor("#99333333"));
            holder.layoutImgBackground.setBackgroundColor(Color.parseColor("#e9e9e9"));
            holder.ivIcon.setColorFilter(Color.parseColor("#99333333"));
        }
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDescription;
        ImageView ivIcon, ivDivider;
        LinearLayout layoutMain, layoutImgBackground;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDescription = itemView.findViewById(R.id.tv_description);
            ivIcon = itemView.findViewById(R.id.iv_categories_icon);
            ivDivider = itemView.findViewById(R.id.iv_divider);
            layoutMain = itemView.findViewById(R.id.layout_categories_main);
            layoutImgBackground = itemView.findViewById(R.id.img_background);
        }
    }
}
