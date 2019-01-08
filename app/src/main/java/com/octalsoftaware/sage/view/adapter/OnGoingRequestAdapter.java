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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.octalsoftaware.sage.SageSeekerApplication;
import com.octalsoftaware.sage.interfaces.OnItemClickListener;
import com.octalsoftaware.sage.model.NewRequestModel;
import com.octalsoftaware.sage.network.APIRequest;
import com.octalsoftaware.sage.network.MyApiEndpointInterface;
import com.octalsoftaware.sage.response.ServerLogin;
import com.octalsoftaware.sage.sinch.SinchService;
import com.octalsoftaware.sage.util.S;
import com.octalsoftaware.sage.util.SagePreference;
import com.octalsoftaware.sage.util.Utils;
import com.octalsoftaware.sage.view.activity.VideoCallingActivity;
import com.sage.conslantant.android.R;
import com.sinch.android.rtc.MissingPermissionException;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnGoingRequestAdapter extends RecyclerView.Adapter<OnGoingRequestAdapter.MyViewHolder> {

    private Context mContext;
    private List<NewRequestModel> chatList;
    private int tempPosition = -1;
    private int tempPositionClick = 0;
    private SinchService.SinchServiceInterface mSinchServiceInterface;
    public OnItemClickListener mOnItemClickListener;

    public OnGoingRequestAdapter(Context activity, List<NewRequestModel> chatList, OnItemClickListener mOnItemClickListener) {
        mContext = activity;
        this.chatList = chatList;
        this.mOnItemClickListener = mOnItemClickListener;
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

        holder.tvName.setText(chatList.get(position).getFirstName() + " " + chatList.get(position).getLastName());
        if (chatList.get(position).getDes().length() > 70) {
            holder.tvDescription.setText(chatList.get(position).getDes().substring(0, 69));
        } else {
            holder.tvDescription.setText(chatList.get(position).getDes());
        }
        try {
            if (chatList.get(position).getImage() != null || !chatList.get(position).getImage().equalsIgnoreCase("")) {
                Picasso.get()
                        .load(chatList.get(position).getImage())
                        .placeholder(R.mipmap.ic_launcher)
                        .into(holder.ivIcon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.layoutRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempPositionClick = position;
                sendStatusRight();
            }
        });

        holder.layoutCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, position);
               // callVideo();
               /* Intent i = new Intent(mContext, ChatDetailActivity.class);
                i.putExtra("seeker_id",chatList.get(position).getSeekerId());
                i.putExtra("first_name",chatList.get(position).getFirstName());
                i.putExtra("request_id",chatList.get(position).getConsaltantId());
                mContext.startActivity(i);*/

            }
        });


    }

    protected SinchService.SinchServiceInterface getSinchServiceInterface() {
        return mSinchServiceInterface;
    }

    private void callVideo() {
        try {
            String callId = null;

            com.sinch.android.rtc.calling.Call call = getSinchServiceInterface().callUserVideo("40", "", "40", "");
            callId = call.getCallId();

           /* try {
                try {
                    if (mTimer != null) {
                        mTimer.cancel();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/
           /* SexityApplication.flagChatVideo = true;
            SexityConstant.RECEIVER_ID = receiverId;*/
            Intent callScreen = new Intent(mContext, VideoCallingActivity.class);
            callScreen.putExtra(SinchService.CALL_ID, callId);
            callScreen.putExtra("tag", "outgoingCall");
            callScreen.putExtra("receiverUserImage", "");
            callScreen.putExtra("receiverUserName", "consaltant");
            callScreen.putExtra("receiverId", "42");
            callScreen.putExtra("threadId", "");
            callScreen.putExtra("walletBalance", "");
            callScreen.putExtra("videoPrice", "");
            callScreen.putExtra("receiverId", "42");
            mContext.startActivity(callScreen);
            // finish();


           /* SaveAllChatMessageResponse mSaveAllSentChatMessage = new SaveAllChatMessageResponse();
            mSaveAllSentChatMessage.setMessageId("");
            mSaveAllSentChatMessage.setMessage(receiverUserName + " Calling");
            mSaveAllSentChatMessage.setTimeStamp("");
            mSaveAllSentChatMessage.setSenderId(SexityPreferences.getUserId(ChatActivity.this));
            mSaveAllSentChatMessage.setSenderName(SexityPreferences.getUserName(ChatActivity.this));
            mSaveAllSentChatMessage.setSenderProfileImage(SexityPreferences.getProfileImage(ChatActivity.this));
            mSaveAllSentChatMessage.setReceiverId(receiverId);
            mSaveAllSentChatMessage.setReceiverName(receiverUserName);
            mSaveAllSentChatMessage.setReceiverProfileImage(receiverUserImage);
            mSaveAllSentChatMessage.setChatType("Call");
            mSaveAllSentChatMessage.setCallType("");*/

        } catch (MissingPermissionException e) {
            // ActivityCompat.requestPermissions(, new String[]{e.getRequiredPermission()}, 0);
        }
    }

    private void sendStatusRight() {
        if (Utils.isConnectingToInternet(mContext)) {
            try {
                callApiRight(APIRequest.sendStatusRight(SagePreference.getUnit(S.USER_ID, mContext), chatList.get(tempPositionClick).getConsaltantId(), "1"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Utils.warningToast(mContext, mContext.getString(R.string.no_internet_connection));
        }
    }

    private void callApiRight(Map<String, Object> stringObjectMap) {
        Utils.showProgress(mContext);
        MyApiEndpointInterface myApiEndpointInterface = SageSeekerApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
        Call<ServerLogin> call = null;
        call = myApiEndpointInterface.sendStatus(stringObjectMap);
        call.enqueue(new Callback<ServerLogin>() {
            @Override
            public void onResponse(Call<ServerLogin> call, Response<ServerLogin> response) {
                Utils.hideProgress();
                try {
                    if (response.body().getStatus() == mContext.getResources().getInteger(R.integer.response_success)) {
                        JsonObject gson = new JsonParser().parse(response.body().getData().toString()).getAsJsonObject();
                        JSONObject jsonObject1 = new JSONObject(gson.toString());
                        Utils.sucessToast(mContext, response.body().getMessage());
                        chatList.remove(tempPositionClick);
                        notifyDataSetChanged();
                    } else {
                        Utils.errorToast(mContext, response.body().getMessage());
                    }
                } catch (Exception e) {
                    Utils.hideProgress();
                }
            }

            @Override
            public void onFailure(Call<ServerLogin> call, Throwable t) {
                Utils.hideProgress();
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvStatus, tvDescription, tvName, tvPrice;
        ImageView ivStatus, ivMessage, ivIcon;
        LinearLayout layoutMain, layoutImgBackground, layoutRight, layoutCross;
        AppCompatRatingBar rating;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvDescription = itemView.findViewById(R.id.tv_chat_description);
            layoutRight = itemView.findViewById(R.id.layout_right);
            layoutCross = itemView.findViewById(R.id.layout_cross);
            layoutMain = itemView.findViewById(R.id.layout_simple_chat_main);

            layoutCross.setVisibility(View.GONE);
            layoutRight.setVisibility(View.GONE);

          /*  ivStatus = itemView.findViewById(R.id.iv_simple_chat_status);
            ivMessage = itemView.findViewById(R.id.iv_chat_message);
            */
        }
    }
}
