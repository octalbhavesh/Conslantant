package com.octalsoftaware.sage.view.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.octalsoftaware.sage.view.adapter.OnGoingRequestAdapter;
import com.sage.conslantant.android.R;
import com.sage.conslantant.android.databinding.FragmentOnGoingRequestBinding;
import com.sinch.android.rtc.MissingPermissionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.BIND_AUTO_CREATE;


public class OnGoingRequestFragment extends android.app.Fragment implements OnItemClickListener, ServiceConnection {
    private FragmentOnGoingRequestBinding mBinding;
    private ArrayList<NewRequestModel> mRequestList = new ArrayList<>();
    private OnGoingRequestAdapter adapter;
    private OnItemClickListener mOnItemClickListener;
    private SinchService.SinchServiceInterface mSinchServiceInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_on_going_request, container, false);
        View rootView = mBinding.getRoot();
        getActivity().getApplicationContext().bindService(new Intent(getActivity(), SinchService.class), this,
                BIND_AUTO_CREATE);
        mOnItemClickListener = this;
        fetchConslantant();
        return rootView;
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (SinchService.class.getName().equals(componentName.getClassName())) {
            mSinchServiceInterface = (SinchService.SinchServiceInterface) iBinder;
            onServiceConnected();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        if (SinchService.class.getName().equals(componentName.getClassName())) {
            mSinchServiceInterface = null;
            onServiceDisconnected();
        }
    }

    protected void onServiceConnected() {
        // for subclasses
    }

    protected void onServiceDisconnected() {
        // for subclasses
    }

    protected SinchService.SinchServiceInterface getSinchServiceInterface() {
        return mSinchServiceInterface;
    }

    @Override
    public void onResume() {
        super.onResume();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mBinding.rvHomeOngoing.setLayoutManager(layoutManager);
        adapter = new OnGoingRequestAdapter(getActivity(), mRequestList, mOnItemClickListener);
        mBinding.rvHomeOngoing.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void fetchConslantant() {
        if (Utils.isConnectingToInternet(getActivity())) {
            try {
                callApi(APIRequest.fetchRequest(SagePreference.getUnit(S.USER_ID, getActivity())));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Utils.warningToast(getActivity(), getActivity().getString(R.string.no_internet_connection));
        }
    }

    private void callApi(Map<String, Object> stringObjectMap) {
        Utils.showProgress(getActivity());
        MyApiEndpointInterface myApiEndpointInterface = SageSeekerApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
        Call<ServerLogin> call = null;
        call = myApiEndpointInterface.fetchRequest(stringObjectMap);
        call.enqueue(new Callback<ServerLogin>() {
            @Override
            public void onResponse(Call<ServerLogin> call, Response<ServerLogin> response) {
                Utils.hideProgress();
                try {
                    Utils.hideProgress();
                    if (response.body().getStatus() == getActivity().getResources().getInteger(R.integer.response_success)) {
                        JsonObject gson = new JsonParser().parse(response.body().getData().toString()).getAsJsonObject();
                        JSONObject jsonObject1 = new JSONObject(gson.toString());
                        mRequestList.clear();
                        setResponseData(jsonObject1);
                    } else {
                        Utils.errorToast(getActivity(), response.body().getMessage());
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

    private void setResponseData(JSONObject jsonObject1) {
        Utils.hideProgress();
        JSONArray jsonArray = jsonObject1.optJSONArray("ongoing");
        if (jsonObject1 != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                NewRequestModel model = new NewRequestModel();
                String seekerId = jsonObject.optString("user_id");
                model.setSeekerId(seekerId);
                String firstName = jsonObject.optString("first_name");
                model.setFirstName(firstName);
                String des = jsonObject.optString("bio");
                model.setDes(des);
                String lastName = jsonObject.optString("last_name");
                model.setLastName(lastName);
                String image = jsonObject.optString("image");
                model.setImage(image);
                String request_id = jsonObject.optString("request_id");
                model.setConsaltantId(request_id);
                mRequestList.add(model);
            }
            if (mRequestList.size() > 0) {
                adapter = new OnGoingRequestAdapter(getActivity(), mRequestList, mOnItemClickListener);
                mBinding.rvHomeOngoing.setAdapter(adapter);

                adapter.notifyDataSetChanged();

            } else {
                Utils.errorToast(getActivity(), getString(R.string.no_data_found));
            }
        }
    }


    @Override
    public void onItemClick(View view, int position) {
        Utils.sucessToast(getActivity(), String.valueOf(position));
        callVideo();
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
            Intent callScreen = new Intent(getActivity(), VideoCallingActivity.class);
            callScreen.putExtra(SinchService.CALL_ID, callId);
            callScreen.putExtra("tag", "outgoingCall");
            callScreen.putExtra("receiverUserImage", "");
            callScreen.putExtra("receiverUserName", "consaltant");
            callScreen.putExtra("receiverId", "42");
            callScreen.putExtra("threadId", "");
            callScreen.putExtra("walletBalance", "");
            callScreen.putExtra("videoPrice", "");
            callScreen.putExtra("receiverId", "42");
            startActivity(callScreen);
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
}
