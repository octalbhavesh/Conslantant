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

public class SimpleChatAdapter extends RecyclerView.Adapter<SimpleChatAdapter.MyViewHolder> {

    private Context mContext;
    private List<SimpleChatModel> chatList;
    private int tempPosition = -1;

    public SimpleChatAdapter(Context activity, List<SimpleChatModel> chatList) {
        mContext = activity;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_simple_chat, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

       /* if (chatList.get(position).getStatus().equalsIgnoreCase("1")) {
            holder.tvStatus.setText("Online");
            holder.ivStatus.setVisibility(View.VISIBLE);
        } else {
            holder.tvStatus.setText("Offline");
            holder.ivStatus.setVisibility(View.GONE);
        }*/
        holder.tvName.setText(chatList.get(position).getName() + " " + chatList.get(position).getLastName());
        if (chatList.get(position).getDescription().length() > 100) {
            holder.tvDescription.setText(chatList.get(position).getDescription().substring(0, 99));
        } else {
            holder.tvDescription.setText(chatList.get(position).getDescription());
        }

        holder.rating.setRating((float) chatList.get(position).getRating());
        holder.tvPrice.setText("$ " + chatList.get(position).getPrice() + "/");
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
            }
        });
       /* holder.ivMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ChatDetailActivity.class));
            }
        });
        holder.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ConslantantDetailActivity.class));
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvStatus, tvDescription, tvName, tvPrice;
        ImageView ivStatus, ivMessage, ivIcon;
        LinearLayout layoutMain, layoutImgBackground;
        AppCompatRatingBar rating;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvDescription = itemView.findViewById(R.id.tv_chat_description);
            tvPrice = itemView.findViewById(R.id.tv_chat_price);
            rating = itemView.findViewById(R.id.rating);
            layoutMain = itemView.findViewById(R.id.layout_simple_chat_main);

          /*  ivStatus = itemView.findViewById(R.id.iv_simple_chat_status);
            ivMessage = itemView.findViewById(R.id.iv_chat_message);
            */
        }
    }
}
