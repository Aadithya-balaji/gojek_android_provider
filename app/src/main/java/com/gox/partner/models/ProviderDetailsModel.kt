package com.gox.partner.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProviderDetailsModel {
    @SerializedName("id")
    @Expose
    private var id: Int? = null
    @SerializedName("first_name")
    @Expose
    private var firstName: String? = null
    @SerializedName("last_name")
    @Expose
    private var lastName: String? = null
    @SerializedName("payment_mode")
    @Expose
    private var paymentMode: String? = null
    @SerializedName("email")
    @Expose
    private var email: String? = null
    @SerializedName("country_code")
    @Expose
    private var countryCode: String? = null
    @SerializedName("mobile")
    @Expose
    private var mobile: String? = null
    @SerializedName("gender")
    @Expose
    private var gender: Any? = null
    @SerializedName("device_token")
    @Expose
    private var deviceToken: Any? = null
    @SerializedName("device_id")
    @Expose
    private var deviceId: Any? = null
    @SerializedName("device_type")
    @Expose
    private var deviceType: Any? = null
    @SerializedName("login_by")
    @Expose
    private var loginBy: String? = null
    @SerializedName("social_unique_id")
    @Expose
    private var socialUniqueId: Any? = null
    @SerializedName("latitude")
    @Expose
    private var latitude: String? = null
    @SerializedName("longitude")
    @Expose
    private var longitude: String? = null
    @SerializedName("stripe_cust_id")
    @Expose
    private var stripeCustId: String? = null
    @SerializedName("wallet_balance")
    @Expose
    private var walletBalance: Double? = null
    @SerializedName("rating")
    @Expose
    private var rating: Int? = null
    @SerializedName("status")
    @Expose
    private var status: String? = null
    @SerializedName("admin_id")
    @Expose
    private var adminId: Any? = null
    @SerializedName("payment_gateway_id")
    @Expose
    private var paymentGatewayId: Any? = null
    @SerializedName("otp")
    @Expose
    private var otp: String? = null
    @SerializedName("language")
    @Expose
    private var language: String? = null
    @SerializedName("picture")
    @Expose
    private var picture: String? = null
    @SerializedName("referral_unique_id")
    @Expose
    private var referralUniqueId: String? = null
    @SerializedName("qrcode_url")
    @Expose
    private var qrcodeUrl: Any? = null
    @SerializedName("country_id")
    @Expose
    private var countryId: Int? = null
    @SerializedName("currency_symbol")
    @Expose
    private var currencySymbol: String? = null
    @SerializedName("city_id")
    @Expose
    private var cityId: Int? = null
    @SerializedName("currency")
    @Expose
    private var currency: Any? = null
    @SerializedName("activation_status")
    @Expose
    private var activationStatus: Int? = null
    @SerializedName("isService")
    @Expose
    private var isService: Int? = null
    @SerializedName("isDocument")
    @Expose
    private var isDocument: Int? = null
    @SerializedName("isBankDetail")
    @Expose
    private var isBankdetail: Int? = null
    @SerializedName("is_online")
    @Expose
    private var isOnline: Int? = null
    @SerializedName("state_id")
    @Expose
    private var stateId: Any? = null
    @SerializedName("service")
    @Expose
    private var service: ServiceModel? = null

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?) {
        this.id = id
    }

    fun getFirstName(): String? {
        return firstName
    }

    fun setFirstName(firstName: String) {
        this.firstName = firstName
    }

    fun getLastName(): String? {
        return lastName
    }

    fun setLastName(lastName: String) {
        this.lastName = lastName
    }

    fun getPaymentMode(): String? {
        return paymentMode
    }

    fun setPaymentMode(paymentMode: String) {
        this.paymentMode = paymentMode
    }

    fun getEmail(): String? {
        return email
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun getCountryCode(): String? {
        return countryCode
    }

    fun setCountryCode(countryCode: String) {
        this.countryCode = countryCode
    }

    fun getMobile(): String? {
        return mobile
    }

    fun setMobile(mobile: String) {
        this.mobile = mobile
    }

    fun getGender(): Any? {
        return gender
    }

    fun setGender(gender: Any) {
        this.gender = gender
    }

    fun getDeviceToken(): Any? {
        return deviceToken
    }

    fun setDeviceToken(deviceToken: Any) {
        this.deviceToken = deviceToken
    }

    fun getDeviceId(): Any? {
        return deviceId
    }

    fun setDeviceId(deviceId: Any) {
        this.deviceId = deviceId
    }

    fun getDeviceType(): Any? {
        return deviceType
    }

    fun setDeviceType(deviceType: Any) {
        this.deviceType = deviceType
    }

    fun getLoginBy(): String? {
        return loginBy
    }

    fun setLoginBy(loginBy: String) {
        this.loginBy = loginBy
    }

    fun getSocialUniqueId(): Any? {
        return socialUniqueId
    }

    fun setSocialUniqueId(socialUniqueId: Any) {
        this.socialUniqueId = socialUniqueId
    }

    fun getLatitude(): String? {
        return latitude
    }

    fun setLatitude(latitude: String) {
        this.latitude = latitude
    }

    fun getLongitude(): String? {
        return longitude
    }

    fun setLongitude(longitude: String) {
        this.longitude = longitude
    }

    fun getStripeCustId(): String? {
        return stripeCustId
    }

    fun setStripeCustId(stripeCustId: String) {
        this.stripeCustId = stripeCustId
    }

    fun getWalletBalance(): Double? {
        return walletBalance
    }

    fun setWalletBalance(walletBalance: Double?) {
        this.walletBalance = walletBalance
    }

    fun getRating(): Int? {
        return rating
    }

    fun setRating(rating: Int?) {
        this.rating = rating
    }

    fun getStatus(): String? {
        return status
    }

    fun setStatus(status: String) {
        this.status = status
    }

    fun getAdminId(): Any? {
        return adminId
    }

    fun setAdminId(adminId: Any) {
        this.adminId = adminId
    }

    fun getPaymentGatewayId(): Any? {
        return paymentGatewayId
    }

    fun setPaymentGatewayId(paymentGatewayId: Any) {
        this.paymentGatewayId = paymentGatewayId
    }

    fun getOtp(): String? {
        return otp
    }

    fun setOtp(otp: String) {
        this.otp = otp
    }

    fun getLanguage(): String? {
        return language
    }

    fun setLanguage(language: String) {
        this.language = language
    }

    fun getPicture(): String? {
        return picture
    }

    fun setPicture(picture: String) {
        this.picture = picture
    }

    fun getReferralUniqueId(): String? {
        return referralUniqueId
    }

    fun setReferralUniqueId(referralUniqueId: String) {
        this.referralUniqueId = referralUniqueId
    }

    fun getQrcodeUrl(): Any? {
        return qrcodeUrl
    }

    fun setQrcodeUrl(qrcodeUrl: Any) {
        this.qrcodeUrl = qrcodeUrl
    }

    fun getCountryId(): Int? {
        return countryId
    }

    fun setCountryId(countryId: Int?) {
        this.countryId = countryId
    }

    fun getCurrencySymbol(): String? {
        return currencySymbol
    }

    fun setCurrencySymbol(currencySymbol: String) {
        this.currencySymbol = currencySymbol
    }

    fun getCityId(): Int? {
        return cityId
    }

    fun setCityId(cityId: Int?) {
        this.cityId = cityId
    }

    fun getCurrency(): Any? {
        return currency
    }

    fun setCurrency(currency: Any) {
        this.currency = currency
    }

    fun getActivationStatus(): Int? {
        return activationStatus
    }

    fun setActivationStatus(activationStatus: Int?) {
        this.activationStatus = activationStatus
    }

    fun getIsService(): Int? {
        return isService
    }

    fun setIsService(isService: Int?) {
        this.isService = isService
    }

    fun getIsDocument(): Int? {
        return isDocument
    }

    fun setIsDocument(isDocument: Int?) {
        this.isDocument = isDocument
    }

    fun getIsBankdetail(): Int? {
        return isBankdetail
    }

    fun setIsBankdetail(isBankdetail: Int?) {
        this.isBankdetail = isBankdetail
    }

    fun getIsOnline(): Int? {
        return isOnline
    }

    fun setIsOnline(isOnline: Int?) {
        this.isOnline = isOnline
    }

    fun getStateId(): Any? {
        return stateId
    }

    fun setStateId(stateId: Any) {
        this.stateId = stateId
    }

    fun getService(): ServiceModel? {
        return service
    }

    fun setService(service: ServiceModel) {
        this.service = service
    }

}