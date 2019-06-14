package com.gox.partner.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CardResponseModel {
    @SerializedName("id")
    @Expose
    private var id: Int? = null
    @SerializedName("provider_id")
    @Expose
    private var providerId: Int? = null
    @SerializedName("company_id")
    @Expose
    private var companyId: Int? = null
    @SerializedName("last_four")
    @Expose
    private var lastFour: String? = null
    @SerializedName("card_id")
    @Expose
    private var cardId: String? = null
    @SerializedName("brand")
    @Expose
    private var brand: String? = null
    @SerializedName("is_default")
    @Expose
    private var isDefault: Int? = null
    @SerializedName("holder_name")
    @Expose
    private var holderName: String? = null
    @SerializedName("month")
    @Expose
    private var month: String? = null
    @SerializedName("year")
    @Expose
    private var year: String? = null
    @SerializedName("funding")
    @Expose
    private var funding: String? = null
    var isCardSelected: Boolean? = false
    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?) {
        this.id = id
    }

    fun getProviderId(): Int? {
        return providerId
    }

    fun setProviderId(providerId: Int?) {
        this.providerId = providerId
    }

    fun getCompanyId(): Int? {
        return companyId
    }

    fun setCompanyId(companyId: Int?) {
        this.companyId = companyId
    }

    fun getLastFour(): String? {
        return lastFour
    }

    fun setLastFour(lastFour: String) {
        this.lastFour = lastFour
    }

    fun getCardId(): String? {
        return cardId
    }

    fun setCardId(cardId: String) {
        this.cardId = cardId
    }

    fun getBrand(): String? {
        return brand
    }

    fun setBrand(brand: String) {
        this.brand = brand
    }

    fun getIsDefault(): Int? {
        return isDefault
    }

    fun setIsDefault(isDefault: Int?) {
        this.isDefault = isDefault
    }

    fun getHolderName(): String? {
        return holderName
    }

    fun setHolderName(holderName: String) {
        this.holderName = holderName
    }

    fun getMonth(): String? {
        return month
    }

    fun setMonth(month: String) {
        this.month = month
    }

    fun getYear(): String? {
        return year
    }

    fun setYear(year: String) {
        this.year = year
    }

    fun getFunding(): String? {
        return funding
    }

    fun setFunding(funding: String) {
        this.funding = funding
    }

}