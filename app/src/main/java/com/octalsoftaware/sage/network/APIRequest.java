package com.octalsoftaware.sage.network;

import com.google.firebase.iid.FirebaseInstanceId;
import com.octalsoftaware.sage.util.ConvertJsonToMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class APIRequest {

    public static Map<String, Object> login(String email, String password, String remember) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", email);
        jsonObject.put("password", password);
        jsonObject.put("remember_me", remember);
        jsonObject.put("device_token", FirebaseInstanceId.getInstance().getToken());
        jsonObject.put("device_type", "android");
        return new ConvertJsonToMap().jsonToMap(jsonObject);
    }

    public static Map<String, Object> sendSwitchButton(String user_id, String txt, String audio,String video) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_id", user_id);
        jsonObject.put("audio_chat", audio);
        jsonObject.put("video_chat", video);
        jsonObject.put("text_chat", txt);
        return new ConvertJsonToMap().jsonToMap(jsonObject);
    }

    public static Map<String, Object> registerOne(String countryCode, String phoneNumber) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("country_code", countryCode);
        jsonObject.put("contact", phoneNumber);
        return new ConvertJsonToMap().jsonToMap(jsonObject);
    }

    public static Map<String, Object> verifyOtp(String otp, String phoneNumber) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("otp", otp);
        jsonObject.put("contact", phoneNumber);
        return new ConvertJsonToMap().jsonToMap(jsonObject);
    }

    public static Map<String, Object> conslantant(String id) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("category_id", id);
        return new ConvertJsonToMap().jsonToMap(jsonObject);
    }
    public static Map<String, Object> logout(String userId) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_id", userId);
        return new ConvertJsonToMap().jsonToMap(jsonObject);
    }
    public static Map<String, Object> conslantantDetail(String id) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("consultant_id", id);
        return new ConvertJsonToMap().jsonToMap(jsonObject);
    }

    public static Map<String, Object> fetchRequest(String id) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_id", id);
        return new ConvertJsonToMap().jsonToMap(jsonObject);
    }



    public static Map<String, Object> sendStatusRight(String userId, String requestId, String type) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_id", userId);
        jsonObject.put("request_id", requestId);
        jsonObject.put("status", type);
        return new ConvertJsonToMap().jsonToMap(jsonObject);
    }

    public static Map<String, Object> categories() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        return new ConvertJsonToMap().jsonToMap(jsonObject);
    }

    public static RequestBody registerTwo(String userId, String first_name, String last_name,
                                          String email, String password, String device_token) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_id", userId);
        jsonObject.put("first_name", first_name);
        jsonObject.put("last_name", last_name);
        jsonObject.put("email", email);
        jsonObject.put("password", password);
        jsonObject.put("device_token", FirebaseInstanceId.getInstance().getToken());
        jsonObject.put("device_type", "android");
        jsonObject.put("role", "3");
        return RequestBody.create(MediaType.parse("multipart/form-data"), jsonObject.toString());
    }


}