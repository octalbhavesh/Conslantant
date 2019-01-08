package com.octalsoftaware.sage.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.octalsoftaware.sage.SageSeekerApplication;
import com.octalsoftaware.sage.model.ReviewModel;
import com.octalsoftaware.sage.network.APIRequest;
import com.octalsoftaware.sage.network.MyApiEndpointInterface;
import com.octalsoftaware.sage.response.ServerLogin;
import com.octalsoftaware.sage.util.Utils;
import com.octalsoftaware.sage.view.adapter.AllReviewsAdapter;
import com.sage.conslantant.android.R;
import com.sage.conslantant.android.databinding.ActivityConslantantDetailBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConslantantDetailActivity extends BaseActivity {

    private ActivityConslantantDetailBinding mBinding;
    private String mId;
    private ArrayList<ReviewModel> listReview1 = new ArrayList<>();
    private AllReviewsAdapter adapter;
    private JSONArray jsonReveiw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_conslantant_detail);

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mId = getIntent().getStringExtra("id");
            fetchDetail();
        } catch (Exception e) {
            e.printStackTrace();
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.rvDetailReviews.setLayoutManager(linearLayoutManager);
        mBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBinding.layoutDetailChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConslantantDetailActivity.this, ChatDetailActivity.class));
            }
        });

        mBinding.layoutReveiws.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ConslantantDetailActivity.this, AllReviewsActivity.class);
                i.putExtra("data", jsonReveiw.toString());
                startActivity(i);
            }
        });
    }

    private void fetchDetail() {
        if (Utils.isConnectingToInternet(mContext)) {
            try {
                // callApi(APIRequest.conslantantDetail(mId));
                callApi(APIRequest.conslantantDetail("32"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Utils.warningToast(mContext, getString(R.string.no_internet_connection));
        }
    }

    private void callApi(Map<String, Object> detail) {
        Utils.showProgress(mContext);
        MyApiEndpointInterface myApiEndpointInterface = SageSeekerApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
        Call<ServerLogin> call = null;
        call = myApiEndpointInterface.conslantantDetail(detail);
        call.enqueue(new Callback<ServerLogin>() {
            @Override
            public void onResponse(Call<ServerLogin> call, Response<ServerLogin> response) {
                try {
                    Utils.hideProgress();
                    if (response.body().getStatus() == getResources().getInteger(R.integer.response_success)) {
                        JsonObject gson = new JsonParser().parse(response.body().getData().toString()).getAsJsonObject();
                        JSONObject jsonObject1 = new JSONObject(gson.toString());
                        setResponseData(jsonObject1);
                    } else {
                        Utils.errorToast(mContext, response.body().getMessage());
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<ServerLogin> call, Throwable t) {
                Utils.hideProgress();
            }
        });
    }

    private void setResponseData(JSONObject jsonObject1) {
        if (jsonObject1 != null) {
            mBinding.tvDetailTitle.setText(jsonObject1.optString("first_name") + " " + jsonObject1.optString("last_name"));
            mBinding.tvDetailName.setText(jsonObject1.optString("first_name") + " " + jsonObject1.optString("last_name"));
            mBinding.rating.setRating((float) jsonObject1.optDouble("rating"));
            mBinding.tvDetailDescription.setText(jsonObject1.optString("bio"));

            if (jsonObject1.optString("image") != null) {
                Picasso.get()
                        .load(jsonObject1.optString("image"))
                        .into(mBinding.ivDetailImage);
            }

            jsonReveiw = jsonObject1.optJSONArray("review_array");
            mBinding.tvDetailReveiwsCount.setText("( " + jsonReveiw.length() + " )" + "Reviews");
            listReview1.clear();
            if (jsonReveiw.length() > 2) {
                mBinding.layoutReveiws.setVisibility(View.VISIBLE);
            } else {
                mBinding.layoutReveiws.setVisibility(View.GONE);
            }
            JSONArray jsonReviewLength = jsonObject1.optJSONArray("review_array");
            for (int i = 0; i < 2; i++) {
                JSONObject object = jsonReviewLength.optJSONObject(i);
                ReviewModel model = new ReviewModel();
                String des = object.optString("review");
                model.setDescription(des);
                listReview1.add(model);
            }
            if (listReview1.size() > 0) {
                adapter = new AllReviewsAdapter(mContext, listReview1);
                mBinding.rvDetailReviews.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                errorToast(mContext, getString(R.string.no_data_found));
            }

            mBinding.tvDetailReveiwsCountOne.setText("( " + jsonReveiw.length() + " )" + "Reviews");

            JSONArray jsonTools = jsonObject1.optJSONArray("tool");
            String value = "";
            for (int i = 0; i < jsonTools.length(); i++) {
                JSONObject object = jsonTools.optJSONObject(i);
                if (object != null) {
                    value += object.optString("tool_name") + "\n";
                }
            }
            mBinding.tvDetailTools.setText(value);

            JSONArray jsonHabit = jsonObject1.optJSONArray("habit");
            String habit = "";
            for (int i = 0; i < jsonHabit.length(); i++) {
                JSONObject object1 = jsonHabit.optJSONObject(i);
                if (object1 != null) {
                    habit += object1.optString("habit_name") + "\n";
                }
            }
            mBinding.tvDetailHabit.setText(habit);
        }

    }
}
