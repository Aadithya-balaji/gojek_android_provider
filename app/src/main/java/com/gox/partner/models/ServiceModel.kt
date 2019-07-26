package com.gox.partner.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ServiceModel {
    @SerializedName("id")
    @Expose
    private var id: Int? = null
    @SerializedName("provider_id")
    @Expose
    private var providerId: Int? = null
    @SerializedName("admin_service_id")
    @Expose
    private var adminServiceId: Int? = null
    @SerializedName("provider_vehicle_id")
    @Expose
    private var providerVehicleId: Int? = null
    @SerializedName("ride_delivery_id")
    @Expose
    private var rideDeliveryId: Any? = null
    @SerializedName("service_id")
    @Expose
    private var serviceId: Any? = null
    @SerializedName("admin_service")
    @Expose
    private var admin_service: Any? = null
    @SerializedName("category_id")
    @Expose
    private var categoryId: Int? = null
    @SerializedName("sub_category_id")
    @Expose
    private var subCategoryId: Any? = null
    @SerializedName("company_id")
    @Expose
    private var companyId: Int? = null
    @SerializedName("base_fare")
    @Expose
    private var baseFare: String? = null
    @SerializedName("per_miles")
    @Expose
    private var perMiles: String? = null
    @SerializedName("per_mins")
    @Expose
    private var perMins: String? = null
    @SerializedName("status")
    @Expose
    private var status: String? = null

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

    fun getAdminServiceId(): Int? {
        return adminServiceId
    }

    fun setAdminServiceId(adminServiceId: Int?) {
        this.adminServiceId = adminServiceId
    }

    fun getProviderVehicleId(): Int? {
        return providerVehicleId
    }

    fun setProviderVehicleId(providerVehicleId: Int?) {
        this.providerVehicleId = providerVehicleId
    }

    fun getRideDeliveryId(): Any? {
        return rideDeliveryId
    }

    fun setRideDeliveryId(rideDeliveryId: Any) {
        this.rideDeliveryId = rideDeliveryId
    }

    fun getServiceId(): Any? {
        return serviceId
    }

    fun setServiceId(serviceId: Any) {
        this.serviceId = serviceId
    }

    fun getCategoryId(): Int? {
        return categoryId
    }

    fun setCategoryId(categoryId: Int?) {
        this.categoryId = categoryId
    }

    fun getSubCategoryId(): Any? {
        return subCategoryId
    }

    fun setSubCategoryId(subCategoryId: Any) {
        this.subCategoryId = subCategoryId
    }

    fun getCompanyId(): Int? {
        return companyId
    }

    fun setCompanyId(companyId: Int?) {
        this.companyId = companyId
    }

    fun getBaseFare(): String? {
        return baseFare
    }

    fun setBaseFare(baseFare: String) {
        this.baseFare = baseFare
    }

    fun getPerMiles(): String? {
        return perMiles
    }

    fun setPerMiles(perMiles: String) {
        this.perMiles = perMiles
    }

    fun getPerMins(): String? {
        return perMins
    }

    fun setPerMins(perMins: String) {
        this.perMins = perMins
    }

    fun getStatus(): String? {
        return status
    }

    fun setStatus(status: String) {
        this.status = status
    }
}