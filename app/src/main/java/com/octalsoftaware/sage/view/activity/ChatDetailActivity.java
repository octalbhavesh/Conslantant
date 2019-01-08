package com.octalsoftaware.sage.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.octalsoftaware.sage.SageSeekerApplication;
import com.octalsoftaware.sage.constants.S;
import com.octalsoftaware.sage.database.DataBase;
import com.octalsoftaware.sage.database.MessageDetail;
import com.octalsoftaware.sage.model.LiveChatModel;
import com.octalsoftaware.sage.network.APIRequest;
import com.octalsoftaware.sage.network.MyApiEndpointInterface;
import com.octalsoftaware.sage.response.ServerLogin;
import com.octalsoftaware.sage.sinch.SinchService;
import com.octalsoftaware.sage.util.SagePreference;
import com.octalsoftaware.sage.util.Utils;
import com.octalsoftaware.sage.view.adapter.LiveChatAdapter;
import com.sage.conslantant.android.R;
import com.sage.conslantant.android.databinding.ActivityChatDetailBinding;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.messaging.Message;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.MessageDeliveryInfo;
import com.sinch.android.rtc.messaging.MessageFailureInfo;
import com.sinch.android.rtc.messaging.WritableMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatDetailActivity extends BaseActivity implements MessageClientListener, SinchService.StartFailedListener {
    private ActivityChatDetailBinding mBinding;
    private ArrayList<LiveChatModel> mChatList = new ArrayList<>();
    private LiveChatAdapter adapter;
    private String tag = "text";
    private String enviroment = "sandbox.sinch.com";
    private DataBase dataBase;
    private SinchClient sinchClient;
    private MessageClient messageClient;
    private WritableMessage message;
    List<MessageDetail> messageList;
    private String seekerId, seekerName, requestId;
    private String messageType;
    private String receiverDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat_detail);
        dataBase = DataBase.getInMemoryDatabase(getApplicationContext());
        mBinding.ivChatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBinding.etChatMessage.getText().toString().trim().equalsIgnoreCase("")) {
                    errorToast(mContext, "Enter message");
                } else {
                    sendMessage(mBinding.etChatMessage.getText().toString(), tag);
                }
            }
        });

        mBinding.layoutEndChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isConnectingToInternet(mContext)) {
                    try {
                        if (requestId != null)
                            callApiEnd(APIRequest.sendStatusRight(SagePreference.getUnit
                                            (com.octalsoftaware.sage.util.S.USER_ID, mContext),
                                    requestId, "3"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Utils.warningToast(mContext, mContext.getString(R.string.no_internet_connection));
                }
            }
        });
    }

    private void callApiEnd(Map<String, Object> stringObjectMap) {
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
                        finish();
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

    private void sendMessage(String textBody, String tag) {
        /* seekerId=40
         * receivername=test
         * */
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM hh:mm");
        String currentDateandTime = sdf.format(new Date());
        /*getSinchServiceInterface().sendMessage("40", textBody,
                SagePreference.getUnit(S.USER_ID, mContext),
                "consaltant", "", "40", "test", "", "", "", "",
                SagePreference.getUnit(com.octalsoftaware.sage.util.S.PROFILE_URL, mContext), currentDateandTime);*/
        getSinchServiceInterface().sendMessage(seekerId, textBody,
                SagePreference.getUnit(S.USER_ID, mContext),
                SagePreference.getUnit(S.NAME, mContext), "", seekerId, seekerName, "", "", "", "",
                SagePreference.getUnit(com.octalsoftaware.sage.util.S.PROFILE_URL, mContext), currentDateandTime);
        mBinding.etChatMessage.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            seekerId = getIntent().getExtras().getString("seeker_id");
            seekerName = getIntent().getExtras().getString("first_name");
            requestId = getIntent().getExtras().getString("request_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        mBinding.ivHomeDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mBinding.rvChatTxt.setLayoutManager(layoutManager);

        try {
            if (mChatList.size() > 0) {
                Log.e("arrayListSize", String.valueOf(mChatList.size()));
                adapter = new LiveChatAdapter(mContext, mChatList);
                mBinding.rvChatTxt.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onServiceConnected() {
        System.out.println("sinch Connected");
        getSinchServiceInterface().setStartListener(ChatDetailActivity.this);
        if (!getSinchServiceInterface().isStarted()) {
            /* getSinchServiceInterface().startClient("42");*/
            getSinchServiceInterface().startClient(SagePreference.getUnit(S.USER_ID, mContext));
        } else {
            System.out.print("Connection start");
        }
        try {
            getSinchServiceInterface().addMessageClientListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStartFailed(SinchError error) {
        System.out.println("sinch error   " + error.getMessage().toString());
    }

    @Override
    public void onStarted() {
        System.out.println("sinch started");
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    protected void onDestroy() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().removeMessageClientListener(ChatDetailActivity.this);
        }
        super.onDestroy();
    }

    @Override
    public void onIncomingMessage(MessageClient messageClient, Message message) {
        if (message.getHeaders().size() > 0) {
            messageType = "inComing";
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM hh:mm");
            receiverDate = sdf.format(message.getTimestamp());
            Log.e("consaltant date", receiverDate);
            saveMessage(message);
        }
    }

    private void saveMessage(Message message) {
        MessageDetail messageDetail = new MessageDetail();
        messageDetail.sender_id = message.getHeaders().get("consaltantId");
        messageDetail.receiver_id = message.getHeaders().get("seekerId");
        messageDetail.sender_name = message.getHeaders().get("consaltantName");
        messageDetail.receiver_name = message.getHeaders().get("seekerName");
        messageDetail.text_body = message.getHeaders().get("message");
        messageDetail.device_type = message.getHeaders().get("device_type");
        messageDetail.type = messageType;
        messageDetail.img = message.getHeaders().get("img");
        messageDetail.sender_time = message.getHeaders().get("senderDate");
        messageDetail.receiver_time = receiverDate;
        dataBase.employDao().insertEmploy(messageDetail);

        messageList = dataBase.employDao().fetchAllMessage();
        mChatList.clear();
        for (int i = 0; i < messageList.size(); i++) {
            LiveChatModel model = new LiveChatModel();
            model.setSender_id(messageList.get(i).sender_id);
            model.setReceiver_id(messageList.get(i).receiver_id);
            model.setSender_message(messageList.get(i).text_body);
            model.setMessageType(messageList.get(i).type);
            model.setImg(messageList.get(i).img);
            model.setSender_date(messageList.get(i).sender_time);
            model.setReceiver_date(messageList.get(i).receiver_time);
            mChatList.add(model);
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            if (mChatList.size() > 0 && mChatList != null) {
                mBinding.rvChatTxt.scrollToPosition(mChatList.size() - 1);
            }
        } else {
            adapter = new LiveChatAdapter(mContext, mChatList);
            mBinding.rvChatTxt.setAdapter(adapter);
            mBinding.rvChatTxt.scrollToPosition(mChatList.size() - 1);
        }
        /*if (mChatList.size() > 0) {
            Log.e("arrayListSize", String.valueOf(mChatList.size()));
            adapter = new LiveChatAdapter(mContext, mChatList);
            mBinding.rvChatTxt.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }*/
    }

    @Override
    public void onMessageSent(MessageClient messageClient, Message message, String s) {
        if (message.getHeaders().size() > 0) {
            messageType = "sent";
            saveMessage(message);
        }
    }

    @Override
    public void onMessageFailed(MessageClient messageClient, Message message, MessageFailureInfo messageFailureInfo) {
        System.out.println("sinch Failed Message");
    }

    @Override
    public void onMessageDelivered(MessageClient messageClient, MessageDeliveryInfo messageDeliveryInfo) {
        System.out.println("sinch Delivered Message");
    }

    @Override
    public void onShouldSendPushData(MessageClient messageClient, Message message, List<PushPair> list) {
        System.out.println("sinch Send Push Data");
    }

}
