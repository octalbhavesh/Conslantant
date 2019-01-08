package com.octalsoftaware.sage.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.octalsoftaware.sage.SageSeekerApplication;
import com.octalsoftaware.sage.network.APIRequest;
import com.octalsoftaware.sage.network.MyApiEndpointInterface;
import com.octalsoftaware.sage.util.S;
import com.octalsoftaware.sage.util.SagePreference;
import com.sage.conslantant.android.R;
import com.sage.conslantant.android.databinding.ActivityAvailabilityBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AvailabilityActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    private ActivityAvailabilityBinding mBinding;
    private String statusAudio, statusVideo, statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_availability);
        mBinding.ivAvailabilityBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        sendStatus();
    }

    private void sendStatus() {
        if (isConnectingToInternet(mContext)) {
            try {
                callApi(APIRequest.sendSwitchButton(SagePreference.getUnit(S.USER_ID, mContext), statusText, statusAudio, statusVideo));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            warningToast(mContext, getString(R.string.no_internet_connection));
        }
    }

    private void callApi(Map<String, Object> login) {
        showProgress(mContext);
        MyApiEndpointInterface myApiEndpointInterface = SageSeekerApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
        Call<ResponseBody> call = null;
        call = myApiEndpointInterface.sendSwitchButton(login);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    hideProgress();
                    String response1 = response.body().string();
                    JSONObject jsonObject = new JSONObject(response1);
                    if (jsonObject.optInt("status_code") == 1) {
                        sucessToast(mContext, jsonObject.optString("message"));
                        JSONObject jsonObject1 = new JSONObject("data");


                    } else {
                        errorToast(mContext, jsonObject.optString("message"));
                    }
                } catch (Exception e) {
                    hideProgress();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgress();
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
