package com.octalsoftaware.sage.view.adapter;

import android.content.Context;
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
import com.octalsoftaware.sage.model.NewRequestModel;
import com.octalsoftaware.sage.network.APIRequest;
import com.octalsoftaware.sage.network.MyApiEndpointInterface;
import com.octalsoftaware.sage.response.ServerLogin;
import com.octalsoftaware.sage.util.S;
import com.octalsoftaware.sage.util.SagePreference;
import com.octalsoftaware.sage.util.Utils;
import com.sage.conslantant.android.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewRequestAdapter extends RecyclerView.Adapter<NewRequestAdapter.MyViewHolder> {

    private Context mContext;
    private List<NewRequestModel> chatList;
    private int tempPosition = -1;
    private int tempPositionClick = 0;

    public NewRequestAdapter(Context activity, List<NewRequestModel> chatList) {
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

        if (chatList.get(position).getStatus().equalsIgnoreCase("Declined")) {
            holder.layoutMain.setVisibility(View.GONE);
        } else {
            holder.layoutMain.setVisibility(View.VISIBLE);
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
                    tempPositionClick = position;
                    sendStatusCross();
                }
            });
        }
    }

    private void sendStatusCross() {
        if (Utils.isConnectingToInternet(mContext)) {
            try {
                callApiRight(APIRequest.sendStatusRight(SagePreference.getUnit(S.USER_ID, mContext),
                        chatList.get(tempPositionClick).getConsaltantId(), "2"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Utils.warningToast(mContext, mContext.getString(R.string.no_internet_connection));
        }
    }

    private void sendStatusRight() {
        if (Utils.isConnectingToInternet(mContext)) {
            try {
                callApiRight(APIRequest.sendStatusRight(SagePreference.getUnit(S.USER_ID, mContext),
                        chatList.get(tempPositionClick).getConsaltantId(), "1"));

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

          /*  ivStatus = itemView.findViewById(R.id.iv_simple_chat_status);
            ivMessage = itemView.findViewById(R.id.iv_chat_message);
            */
        }
    }
}
