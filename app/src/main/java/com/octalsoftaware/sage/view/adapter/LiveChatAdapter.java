package com.octalsoftaware.sage.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.octalsoftaware.sage.model.LiveChatModel;
import com.octalsoftaware.sage.util.S;
import com.octalsoftaware.sage.util.SagePreference;
import com.sage.conslantant.android.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LiveChatAdapter extends RecyclerView.Adapter<LiveChatAdapter.MyViewHolder> {

    private Context mContext;
    private List<LiveChatModel> chatList;
    private int tempPosition = -1;
    private int tempPositionClick = 0;

    public LiveChatAdapter(Context activity, List<LiveChatModel> chatList) {
        mContext = activity;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.adapter_chat_detail, parent, false));
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
        try {
            if (chatList.get(position).getSender_id().equalsIgnoreCase
                    (SagePreference.getUnit(S.USER_ID, mContext))
                    && chatList.get(position).getMessageType().equalsIgnoreCase("sent")) {
                holder.layoutReceiver.setVisibility(View.GONE);
                holder.layoutSender.setVisibility(View.VISIBLE);
                holder.tvSender.setText(chatList.get(position).getSender_message());
                holder.tvSenderTime.setVisibility(View.VISIBLE);
                holder.tvSenderTime.setText(chatList.get(position).getSender_date());

            } else {
                Picasso.get().
                        load(chatList.get(position).getImg()).
                        placeholder(R.mipmap.ic_launcher).
                        into(holder.ivReceiver);
                holder.tvReceiverTime.setVisibility(View.VISIBLE);
                holder.tvReceiverTime.setText(chatList.get(position).getReceiver_date());
                holder.layoutReceiver.setVisibility(View.VISIBLE);
                holder.layoutSender.setVisibility(View.GONE);
                holder.tvReceiver.setText(chatList.get(position).getSender_message());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutSender, layoutReceiver;
        TextView tvSender, tvReceiver, tvSenderTime, tvReceiverTime;
        ImageView ivSender, ivReceiver;


        public MyViewHolder(View itemView) {
            super(itemView);
            layoutSender = itemView.findViewById(R.id.layout_sender);
            layoutReceiver = itemView.findViewById(R.id.layout_receiver);
            tvSender = itemView.findViewById(R.id.tv_sender);
            tvReceiver = itemView.findViewById(R.id.tv_receiver);
            ivReceiver = itemView.findViewById(R.id.iv_receiver);
            tvSenderTime = itemView.findViewById(R.id.tv_sender_time);
            tvReceiverTime = itemView.findViewById(R.id.tv_receiver_time);
        }
    }
}
