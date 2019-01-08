package com.octalsoftaware.sage.view.fragment;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.octalsoftaware.sage.SageSeekerApplication;
import com.octalsoftaware.sage.model.SimpleChatModel;
import com.octalsoftaware.sage.network.APIRequest;
import com.octalsoftaware.sage.network.MyApiEndpointInterface;
import com.octalsoftaware.sage.response.ServerResponse;
import com.octalsoftaware.sage.util.S;
import com.octalsoftaware.sage.util.SagePreference;
import com.octalsoftaware.sage.util.Utils;
import com.octalsoftaware.sage.view.adapter.VoiceChatAdapter;
import com.sage.conslantant.android.R;
import com.sage.conslantant.android.databinding.FragmentVoiceChatBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VoiceChatFragment extends Fragment {

    private ArrayList<SimpleChatModel> mChatList = new ArrayList<>();
    private VoiceChatAdapter adapter;
    private FragmentVoiceChatBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_voice_chat, container, false);
        View rootView = mBinding.getRoot();
        fetchConslantant();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.rvChatVoice.setLayoutManager(linearLayoutManager);
    }

    private void fetchConslantant() {
        if (Utils.isConnectingToInternet(getActivity())) {
            try {
                callApi(APIRequest.conslantant(SagePreference.getUnit(S.CATEGORY_ID, getActivity())));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Utils.warningToast(getActivity(), getString(R.string.no_internet_connection));
        }
    }

    private void callApi(Map<String, Object> categories) {
        Utils.showProgress(getActivity());
        MyApiEndpointInterface myApiEndpointInterface = SageSeekerApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
        Call<ServerResponse> call = null;
        call = myApiEndpointInterface.conslantant(categories);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                try {
                    Utils.hideProgress();
                    if (response.body().getStatus() == getResources().getInteger(R.integer.response_success)) {
                        JsonArray gson = new JsonParser().parse(response.body().getData().toString()).getAsJsonArray();
                        JSONArray jsonObject1 = new JSONArray(gson.toString());
                        mChatList.clear();
                        setResponseData(jsonObject1);
                    } else {
                        Utils.errorToast(getActivity(), response.body().getMessage());
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Utils.hideProgress();
            }
        });
    }

    private void setResponseData(JSONArray jsonObject1) {
        for (int i = 0; i < jsonObject1.length(); i++) {
            SimpleChatModel model = new SimpleChatModel();
            try {
                JSONObject jsonObject = jsonObject1.getJSONObject(i);
                String id = jsonObject.optString("id");
                model.setId(id);
                String name = jsonObject.optString("first_name");
                model.setName(name);
                String lastName = jsonObject.optString("last_name");
                model.setLastName(lastName);
                String description = jsonObject.optString("bio");
                model.setDescription(description);
                String image = jsonObject.optString("image");
                model.setImage(image);
                String price = jsonObject.optString("price");
                model.setPrice(price);
                double rating = jsonObject.optDouble("rating");
                model.setRating(rating);
                mChatList.add(model);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e("size", String.valueOf(mChatList.size()));
        if (mChatList.size() > 0) {
            adapter = new VoiceChatAdapter(getActivity(), mChatList);
            mBinding.rvChatVoice.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            Utils.errorToast(getActivity(), getString(R.string.no_data_found));
        }
    }
}
