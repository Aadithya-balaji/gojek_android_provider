package com.xjek.provider.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.intellij.lang.annotations.Language;

import java.util.List;
public class AppsettingModel {
    @SerializedName("referral")
    @Expose
    private Integer referral;
    @SerializedName("social_login")
    @Expose
    private Integer socialLogin;
    @SerializedName("otp_verify")
    @Expose
    private Integer otpVerify;
    @SerializedName("cmspage")
    @Expose
    private CmspageModel CmspageModel;
    @SerializedName("supportdetails")
    @Expose
    private SupportdetailsModel SupportdetailsModel;
    @SerializedName("languages")
    @Expose
    private List<LanguageModel> languages = null;

    public Integer getReferral() {
        return referral;
    }

    public void setReferral(Integer referral) {
        this.referral = referral;
    }

    public Integer getSocialLogin() {
        return socialLogin;
    }

    public void setSocialLogin(Integer socialLogin) {
        this.socialLogin = socialLogin;
    }

    public Integer getOtpVerify() {
        return otpVerify;
    }

    public void setOtpVerify(Integer otpVerify) {
        this.otpVerify = otpVerify;
    }

    public CmspageModel getCmspage() {
        return CmspageModel;
    }

    public void setCmspage(CmspageModel CmspageModel) {
        this.CmspageModel = CmspageModel;
    }

    public SupportdetailsModel getSupportdetails() {
        return SupportdetailsModel;
    }

    public void setSupportdetails(SupportdetailsModel SupportdetailsModel) {
        this.SupportdetailsModel = SupportdetailsModel;
    }

    public List<LanguageModel> getLanguages() {
        return languages;
    }

    public void setLanguages(List<LanguageModel> languages) {
        this.languages = languages;
    }

}
