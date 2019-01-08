package com.octalsoftaware.sage.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.EditText;

import com.hbb20.CountryCodePicker;
import com.octalsoftaware.sage.SageSeekerApplication;
import com.octalsoftaware.sage.network.APIRequest;
import com.octalsoftaware.sage.network.MyApiEndpointInterface;
import com.octalsoftaware.sage.response.ServerLogin;
import com.sage.conslantant.android.R;
import com.sage.conslantant.android.databinding.ActivityRegisterOneBinding;

import org.json.JSONException;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterOneActivity extends BaseActivity {
    private ActivityRegisterOneBinding mBinding;
    private String mSelectedCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_register_one);
        mBinding.etRegisterMobile.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAllHint();
        mBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSelectedCode = mBinding.countryCode.getDefaultCountryCode();
        mBinding.countryCode.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                mSelectedCode = mBinding.countryCode.getSelectedCountryCode();
            }
        });

        mBinding.btnRegisterOneNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAnimation(mBinding.btnRegisterOneNext);
                if (isConnectingToInternet(mContext)) {
                    EditText etTxt[] = {mBinding.etRegisterMobile};
                    if (checkValidation(etTxt)) {
                        try {
                            callApi(APIRequest.registerOne(mSelectedCode, mBinding.etRegisterMobile.getText().toString().trim()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    errorToast(mContext, getString(R.string.no_internet_connection));
                }
            }
        });

        /*PushDownAnim.setPushDownAnimTo(mBinding.btnRegisterOneNext)
                .setDurationPush( PushDownAnim.DEFAULT_PUSH_DURATION )
                .setDurationRelease( PushDownAnim.DEFAULT_RELEASE_DURATION )
                .setInterpolatorPush( PushDownAnim.DEFAULT_INTERPOLATOR )
                .setInterpolatorRelease( PushDownAnim.DEFAULT_INTERPOLATOR )
                .setOnClickListener( new View.OnClickListener(){
                    @Override
                    public void onClick( View view ){
                        if (isConnectingToInternet(mContext)) {
                            EditText etTxt[] = {mBinding.etRegisterMobile};
                            if (checkValidation(etTxt)) {
                                try {
                                    callApi(APIRequest.registerOne(mSelectedCode, mBinding.etRegisterMobile.getText().toString().trim()));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            errorToast(mContext, getString(R.string.no_internet_connection));
                        }
                    }
                } );*/
    }

    private void setAllHint() {
        mBinding.etRegisterMobile.setHint(getString(R.string.hint_mobile));
    }

    private void callApi(Map<String, Object> registerOne) {
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
                        Intent i = new Intent(RegisterOneActivity.this, OtpActivity.class);
                        i.putExtra("mobile_no", mBinding.etRegisterMobile.getText().toString().trim());
                        i.putExtra("country_code", mSelectedCode);
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
}
