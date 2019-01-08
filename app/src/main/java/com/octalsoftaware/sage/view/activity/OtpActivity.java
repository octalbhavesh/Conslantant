package com.octalsoftaware.sage.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.octalsoftaware.sage.SageSeekerApplication;
import com.octalsoftaware.sage.network.APIRequest;
import com.octalsoftaware.sage.network.MyApiEndpointInterface;
import com.octalsoftaware.sage.response.ServerLogin;
import com.sage.conslantant.android.R;
import com.sage.conslantant.android.databinding.ActivityOtpBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActivity extends BaseActivity {

    private ActivityOtpBinding mBinding;
    int time = 30;
    private int etSize = 1;
    private String mobileNumber, countryCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_otp);
    }

    @Override
    protected void onResume() {
        super.onResume();
        etClick();

        mBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        try {
            mobileNumber = getIntent().getExtras().getString("mobile_no");
            countryCode = getIntent().getExtras().getString("country_code");
            if (mobileNumber != null)
                mBinding.tvOtpNumber.setText(getResources().getString(R.string.otp_description) + " " + mobileNumber);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        otpCounter();
        mBinding.tvOtpTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnectingToInternet(mContext)) {
                    try {
                        callApiResend(APIRequest.registerOne(countryCode, mobileNumber));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    errorToast(mContext, getString(R.string.no_internet_connection));
                }
            }
        });

    }

    private void otpCounter() {
        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                mBinding.tvOtpTimer1.setVisibility(View.VISIBLE);
                mBinding.tvOtpTimer.setVisibility(View.GONE);
                mBinding.tvOtpTimer1.setText("00:" + millisUntilFinished / 1000);
                time--;
            }

            public void onFinish() {
                mBinding.tvOtpTimer1.setVisibility(View.GONE);
                mBinding.tvOtpTimer.setVisibility(View.VISIBLE);
                //   mBinding.tvOtpTimer.setText("Resend Code");
            }

        }.start();
    }

    private void etClick() {
        mBinding.edtOne.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (mBinding.edtOne.getText().toString().length() == etSize)     //size as per your requirement
                {
                    mBinding.edtTwo.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        mBinding.edtTwo.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (mBinding.edtTwo.getText().toString().length() == etSize)     //size as per your requirement
                {
                    mBinding.edtThree.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub


            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (mBinding.edtTwo.getText().toString().trim().equalsIgnoreCase("")) {
                    mBinding.edtOne.requestFocus();
                }
            }

        });
        mBinding.edtThree.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (mBinding.edtThree.getText().toString().length() == etSize)     //size as per your requirement
                {
                    mBinding.edtFour.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub
               /* if (mBinding.edtThree.getText().toString().trim().equalsIgnoreCase("")) {
                    mBinding.edtTwo.requestFocus();
                }*/
            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (mBinding.edtThree.getText().toString().trim().equalsIgnoreCase("")) {
                    mBinding.edtTwo.requestFocus();
                }
            }

        });
        mBinding.edtFour.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (mBinding.edtFour.getText().toString().length() == etSize)     //size as per your requirement
                {
                   /* startActivity(new Intent(OtpActivity.this, HomeActivityNew.class));
                    finish();*/

                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (mBinding.edtFour.getText().toString().trim().equalsIgnoreCase("")) {
                    mBinding.edtThree.requestFocus();
                }
            }

        });

        mBinding.tvOtpSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAnimation(mBinding.tvOtpSubmit);
                if (isConnectingToInternet(mContext)) {
                    if (mBinding.edtOne.getText().toString().trim().equalsIgnoreCase("")
                            || mBinding.edtTwo.getText().toString().trim().equalsIgnoreCase("")
                            || mBinding.edtThree.getText().toString().trim().equalsIgnoreCase("")
                            || mBinding.edtFour.getText().toString().trim().equalsIgnoreCase("")) {
                        errorToast(mContext, getString(R.string.enter_valid_otp));
                    } else {
                        try {
                            String otp = mBinding.edtOne.getText().toString() +
                                    mBinding.edtTwo.getText().toString() +
                                    mBinding.edtThree.getText().toString() +
                                    mBinding.edtFour.getText().toString();
                            callApiVerifyOtp(APIRequest.verifyOtp(otp, mobileNumber));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    errorToast(mContext, getString(R.string.no_internet_connection));
                }
                /*startActivity(new Intent(OtpActivity.this, RegisterTwoActivity.class));*/
            }
        });
    }

    private void callApiVerifyOtp(Map<String, Object> verifyOtp) {
        showProgress(mContext);
        MyApiEndpointInterface myApiEndpointInterface = SageSeekerApplication.getInstance().getRequestQueue().
                create(MyApiEndpointInterface.class);
        Call<ServerLogin> call = null;
        call = myApiEndpointInterface.verify_otp(verifyOtp);
        call.enqueue(new Callback<ServerLogin>() {
            @Override
            public void onResponse(Call<ServerLogin> call, Response<ServerLogin> response) {
                try {
                    hideProgress();
                    if (response.body().getStatus() == getResources().getInteger(R.integer.response_success)) {
                        JsonObject gson = new JsonParser().parse(response.body().getData().toString()).getAsJsonObject();
                        JSONObject jsonObject = new JSONObject(gson.toString());
                        String user_id = jsonObject.optString("user_id");
                        Log.e("object", user_id);
                        sucessToast(mContext, response.body().getMessage());
                        mBinding.edtOne.setText("");
                        mBinding.edtTwo.setText("");
                        mBinding.edtThree.setText("");
                        mBinding.edtFour.setText("");
                        Intent i = new Intent(OtpActivity.this, RegisterTwoActivity.class);
                        i.putExtra("user_id", user_id);
                        startActivity(i);
                    } else {
                        errorToast(mContext, response.body().getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ServerLogin> call, Throwable t) {
                hideProgress();
            }
        });
    }

    private void callApiResend(Map<String, Object> registerOne) {
        showProgress(mContext);
        MyApiEndpointInterface myApiEndpointInterface = SageSeekerApplication.getInstance().getRequestQueue().
                create(MyApiEndpointInterface.class);
        Call<ServerLogin> call = null;
        call = myApiEndpointInterface.register_one(registerOne);
        call.enqueue(new Callback<ServerLogin>() {
            @Override
            public void onResponse(Call<ServerLogin> call, Response<ServerLogin> response) {
                try {
                    hideProgress();
                    if (response.body().getStatus() == getResources().getInteger(R.integer.response_success)) {
                        sucessToast(mContext, response.body().getMessage());
                        otpCounter();
                    } else {
                        errorToast(mContext, response.body().getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ServerLogin> call, Throwable t) {
                hideProgress();
            }
        });
    }
}
