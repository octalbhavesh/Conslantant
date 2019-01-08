package com.octalsoftaware.sage.network;

import com.octalsoftaware.sage.response.ServerImgResponse;
import com.octalsoftaware.sage.response.ServerLogin;
import com.octalsoftaware.sage.response.ServerLoginOne;
import com.octalsoftaware.sage.response.ServerResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface MyApiEndpointInterface {

    @POST("login")
    Call<ServerLoginOne> login(@Body Map<String, Object> commentRequest);

    @POST("consultant_chat_availablity")
    Call<ResponseBody> sendSwitchButton(@Body Map<String, Object> commentRequest);

    @POST("send_otp")
    Call<ServerLogin> register_one(@Body Map<String, Object> commentRequest);

    @Multipart
    @POST("register")
    Call<ServerImgResponse> register_two(@PartMap() Map<String, RequestBody> partMap);

    @Multipart
    @POST("register")
    Call<ServerImgResponse> register_two(@PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part imageFile);
    @POST("logout")
    Call<ResponseBody> logout(@Body Map<String, Object> commentRequest);
    @POST("verify_otp")
    Call<ServerLogin> verify_otp(@Body Map<String, Object> commentRequest);

    @POST("category_list")
    Call<ServerLogin> categories(@Body Map<String, Object> commentRequest);

    @POST("get_consultant_list")
    Call<ServerResponse> conslantant(@Body Map<String, Object> commentRequest);

    @POST("get_consultant_details")
    Call<ServerLogin> conslantantDetail(@Body Map<String, Object> commentRequest);

    @POST("get_consultant_recived_request")
    Call<ServerLogin> fetchRequest(@Body Map<String, Object> commentRequest);

    @POST("change_chat_request_status")
    Call<ServerLogin> sendStatus(@Body Map<String, Object> commentRequest);
}
