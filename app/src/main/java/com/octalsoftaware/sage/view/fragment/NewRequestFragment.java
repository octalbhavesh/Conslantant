package com.octalsoftaware.sage.view.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.octalsoftaware.sage.view.adapter.NewRequestAdapter;
import com.sage.conslantant.android.R;
import com.sage.conslantant.android.databinding.FragmentNewRequestBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewRequestFragment extends Fragment {
    private FragmentNewRequestBinding mBinding;
    private ArrayList<NewRequestModel> mRequestList = new ArrayList<>();
    private NewRequestAdapter adapter;
    private BroadcastReceiver broadcastReceiver;
    private boolean statusVisible = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_request, container, false);
        View rootView = mBinding.getRoot();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mBinding.rvHomeNew.setLayoutManager(layoutManager);

        fetchConslantant();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (statusVisible)
                    fetchConslantant();
            }
        };

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        statusVisible = true;
        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(broadcastReceiver,
                new IntentFilter("new_request"));
    }

    @Override
    public void onPause() {
        super.onPause();
        statusVisible = true;
        LocalBroadcastManager.getInstance(this.getActivity()).unregisterReceiver(broadcastReceiver);
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
                    if (response.body().getStatus() == getActivity().getResources().getInteger(R.integer.response_success)) {
                        JsonObject gson = new JsonParser().parse(response.body().getData().toString()).getAsJsonObject();
                        JSONObject jsonObject1 = new JSONObject(gson.toString());
                        mRequestList.clear();
                        Utils.hideProgress();
                        setResponseData(jsonObject1);
                    } else {
                        Utils.hideProgress();
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
        JSONArray jsonArray = jsonObject1.optJSONArray("new");
        if (jsonObject1 != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                NewRequestModel model = new NewRequestModel();
                String seekerId = jsonObject.optString("user_id");
                model.setSeekerId(seekerId);
                String status = jsonObject.optString("status");
                model.setStatus(status);
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
                adapter = new NewRequestAdapter(getActivity(), mRequestList);
                mBinding.rvHomeNew.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                Utils.errorToast(getActivity(), getString(R.string.no_data_found));
            }
        }
    }
}
