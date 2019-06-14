package com.gox.partner.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReasonModel {
    @SerializedName("id")
    @Expose
    private var id: Int? = null
    @SerializedName("service")
    @Expose
    private var service: String? = null
    @SerializedName("type")
    @Expose
    private var type: String? = null
    @SerializedName("reason")
    @Expose
    private var reason: String? = null
    @SerializedName("created_type")
    @Expose
    private var createdType: String? = null
    @SerializedName("created_by")
    @Expose
    private var createdBy: Int? = null
    @SerializedName("modified_type")
    @Expose
    private var modifiedType: String? = null
    @SerializedName("modified_by")
    @Expose
    private var modifiedBy: Int? = null
    @SerializedName("deleted_type")
    @Expose
    private var deletedType: Any? = null
    @SerializedName("deleted_by")
    @Expose
    private var deletedBy: Any? = null
    @SerializedName("status")
    @Expose
    private var status: String? = null

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?) {
        this.id = id
    }

    fun getService(): String? {
        return service
    }

    fun setService(service: String) {
        this.service = service
    }

    fun getType(): String? {
        return type
    }

    fun setType(type: String) {
        this.type = type
    }

    fun getReason(): String? {
        return reason
    }

    fun setReason(reason: String) {
        this.reason = reason
    }

    fun getCreatedType(): String? {
        return createdType
    }

    fun setCreatedType(createdType: String) {
        this.createdType = createdType
    }

    fun getCreatedBy(): Int? {
        return createdBy
    }

    fun setCreatedBy(createdBy: Int?) {
        this.createdBy = createdBy
    }

    fun getModifiedType(): String? {
        return modifiedType
    }

    fun setModifiedType(modifiedType: String) {
        this.modifiedType = modifiedType
    }

    fun getModifiedBy(): Int? {
        return modifiedBy
    }

    fun setModifiedBy(modifiedBy: Int?) {
        this.modifiedBy = modifiedBy
    }

    fun getDeletedType(): Any? {
        return deletedType
    }

    fun setDeletedType(deletedType: Any) {
        this.deletedType = deletedType
    }

    fun getDeletedBy(): Any? {
        return deletedBy
    }

    fun setDeletedBy(deletedBy: Any) {
        this.deletedBy = deletedBy
    }

    fun getStatus(): String? {
        return status
    }

    fun setStatus(status: String) {
        this.status = status
    }

}