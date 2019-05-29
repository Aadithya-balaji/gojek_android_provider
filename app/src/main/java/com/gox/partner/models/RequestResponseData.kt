package com.gox.partner.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestResponseData {
    @SerializedName("account_status")
    @Expose
    private var accountStatus: String? = null
    @SerializedName("service_status")
    @Expose
    private var serviceStatus: String? = null
    @SerializedName("requests")
    @Expose
    private var requests: List<Any>? = null
    @SerializedName("provider_details")
    @Expose
    private var providerDetails: ProviderDetailsModel? = null
    @SerializedName("reasons")
    @Expose
    private var reasons: List<ReasonModel>? = null
    @SerializedName("referral_count")
    @Expose
    private var referralCount: String? = null
    @SerializedName("referral_amount")
    @Expose
    private var referralAmount: String? = null
    @SerializedName("ride_otp")
    @Expose
    private var rideOtp: Int? = null
    @SerializedName("referral_total_count")
    @Expose
    private var referralTotalCount: String? = null
    @SerializedName("referral_total_amount")
    @Expose
    private var referralTotalAmount: Int? = null

    fun getAccountStatus(): String? {
        return accountStatus
    }

    fun setAccountStatus(accountStatus: String) {
        this.accountStatus = accountStatus
    }

    fun getServiceStatus(): String? {
        return serviceStatus
    }

    fun setServiceStatus(serviceStatus: String) {
        this.serviceStatus = serviceStatus
    }

    fun getRequests(): List<Any>? {
        return requests
    }

    fun setRequests(requests: List<Any>) {
        this.requests = requests
    }

    fun getProviderDetails(): ProviderDetailsModel? {
        return providerDetails
    }

    fun setProviderDetails(providerDetails: ProviderDetailsModel) {
        this.providerDetails = providerDetails
    }

    fun getReasons(): List<ReasonModel>? {
        return reasons
    }

    fun setReasons(reasons: List<ReasonModel>) {
        this.reasons = reasons
    }

    fun getReferralCount(): String? {
        return referralCount
    }

    fun setReferralCount(referralCount: String) {
        this.referralCount = referralCount
    }

    fun getReferralAmount(): String? {
        return referralAmount
    }

    fun setReferralAmount(referralAmount: String) {
        this.referralAmount = referralAmount
    }

    fun getRideOtp(): Int? {
        return rideOtp
    }

    fun setRideOtp(rideOtp: Int?) {
        this.rideOtp = rideOtp
    }

    fun getReferralTotalCount(): String? {
        return referralTotalCount
    }

    fun setReferralTotalCount(referralTotalCount: String) {
        this.referralTotalCount = referralTotalCount
    }

    fun getReferralTotalAmount(): Int? {
        return referralTotalAmount
    }

    fun setReferralTotalAmount(referralTotalAmount: Int?) {
        this.referralTotalAmount = referralTotalAmount
    }
}