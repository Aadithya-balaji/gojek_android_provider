package com.xjek.provider.model.ciity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlacesListResponseModel {
    @SerializedName("statusCode")
    @Expose
    private String statusCode;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responseData")
    @Expose
    private List<PlaceResponseModel> responseData = null;
    @SerializedName("error")
    @Expose
    private List<Object> error = null;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PlaceResponseModel> getResponseData() {
        return responseData;
    }

    public void setResponseData(List<PlaceResponseModel> responseData) {
        this.responseData = responseData;
    }

    public List<Object> getError() {
        return error;
    }

    public void setError(List<Object> error) {
        this.error = error;
    }
}
