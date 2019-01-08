package com.octalsoftaware.sage.response;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class ServerImgResponse {
    @SerializedName("status_code")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private JsonObject data;

    @SerializedName("errors")
    private String errors;

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

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

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }
}
