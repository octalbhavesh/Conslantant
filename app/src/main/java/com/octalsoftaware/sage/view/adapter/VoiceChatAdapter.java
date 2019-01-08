package com.octalsoftaware.sage.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.octalsoftaware.sage.model.SimpleChatModel;
import com.octalsoftaware.sage.view.activity.ConslantantDetailActivity;
import com.sage.conslantant.android.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VoiceChatAdapter extends RecyclerView.Adapter<VoiceChatAdapter.MyViewHolder> {

    private Context mContext;
    private List<SimpleChatModel> chatList;
    private int tempPosition = -1;

    public VoiceChatAdapter(Context activity, List<SimpleChatModel> chatList) {
        mContext = activity;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_voice_chat, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        /*if (!TextUtils.isEmpty(newsModalList.get(position).getImage()))
            Picasso.get().load(newsModalList.get(position).getImage()).placeholder(R.drawable.logo).into(holder.user_profile_pic_iv);*/
        //    Log.e("imageUrl", imagesModalList.get(position).getImgUrl());
        //  holder.tvNotification.setText(notificationList.get(position).getNotification());
        /*if (chatList.get(position).getStatus().equalsIgnoreCase("1")) {
            holder.tvStatus.setText("Online");
            holder.ivStatus.setVisibility(View.VISIBLE);
        } else {
            holder.tvStatus.setText("Offline");
            holder.ivStatus.setVisibility(View.GONE);
        }*/
        holder.ivAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        /*holder.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ConslantantDetailActivity.class));
            }
        });
        holder.ivVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, VideoCallingActivity.class));
            }
        });*/

        holder.tvName.setText(chatList.get(position).getName() + " " + chatList.get(position).getLastName());
        if (chatList.get(position).getDescription().length() > 100) {
            holder.tvDescription.setText(chatList.get(position).getDescription().substring(0, 99));
        } else {
            holder.tvDescription.setText(chatList.get(position).getDescription());
        }

        holder.rating.setRating((float) chatList.get(position).getRating());
        holder.tvPrice.setText("$ " + chatList.get(position).getPrice()+"/");
        if (chatList.get(position).getImage() != null) {
            Picasso.get()
                    .load(chatList.get(position).getImage())
                    .into(holder.ivIcon);
        }
        holder.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, ConslantantDetailActivity.class);
                i.putExtra("id", chatList.get(position).getId());
                mContext.startActivity(i);
               // mContext.startActivity(new Intent(mContext, ConslantantDetailActivity.class));
            }
        });
       /* holder.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempPosition = position;
                notifyDataSetChanged();

                mContext.startActivity(new Intent(mContext, ChatListingActivity.class));
            }
        });*/
        /*if (tempPosition == position) {
            holder.layoutMain.setBackgroundColor(mContext.getColor(R.color.theme_yellow));
            holder.tvName.setTextColor(mContext.getColor(R.color.colorWhite));
            holder.tvDescription.setTextColor(mContext.getColor(R.color.colorWhite));
            holder.layoutImgBackground.setBackgroundColor(mContext.getColor(R.color.theme_yellow));
            holder.ivIcon.setColorFilter(Color.parseColor("#ffffff"));
        } else {
            holder.layoutMain.setBackgroundColor(mContext.getColor(R.color.colorWhite));
            holder.tvName.setTextColor(mContext.getColor(R.color.colorBlack));
            holder.tvDescription.setTextColor(Color.parseColor("#99333333"));
            holder.layoutImgBackground.setBackgroundColor(Color.parseColor("#e9e9e9"));
            holder.ivIcon.setColorFilter(Color.parseColor("#99333333"));
        }*/
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvStatus, tvName, tvDescription, tvPrice;
        ImageView ivVideo, ivAudio, ivStatus, ivIcon;
        LinearLayout layoutMain, layoutImgBackground;
        AppCompatRatingBar rating;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvPrice = itemView.findViewById(R.id.tv_chat_price);
            tvName = itemView.findViewById(R.id.tv_chat_name);
            ivVideo = itemView.findViewById(R.id.iv_video);
            ivAudio = itemView.findViewById(R.id.iv_audio);
            ivStatus = itemView.findViewById(R.id.iv_voice_chat_status);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvDescription = itemView.findViewById(R.id.tv_chat_description);
            layoutMain = itemView.findViewById(R.id.layout_chat_voice_main);
            rating = itemView.findViewById(R.id.rating);
        }
    }
}
