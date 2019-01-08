package com.octalsoftaware.sage.response;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

public class ServerResponse {
    @SerializedName("status_code")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private JsonArray data;

    public int getStatus() {
        return status;
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

    public JsonArray getData() {
        return data;
    }

    public void setData(JsonArray data) {
        this.data = data;
    }
}
