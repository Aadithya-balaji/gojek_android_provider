package com.xjek.provider.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SupportdetailsModel {
    @SerializedName("contact_number")
    @Expose
    private List<ContactNumberModel> ContactNumberModel = null;
    @SerializedName("contact_email")
    @Expose
    private String contactEmail;

    public List<ContactNumberModel> getContactNumber() {
        return ContactNumberModel;
    }

    public void setContactNumber(List<ContactNumberModel> ContactNumberModel) {
        this.ContactNumberModel = ContactNumberModel;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

}
