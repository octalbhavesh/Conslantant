package com.octalsoftaware.sage.response;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class ServerLoginOne {
    @SerializedName("status_code")
    private int status;

    @SerializedName("token")
    private String token;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private JsonObject data;

    public int getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }
}
