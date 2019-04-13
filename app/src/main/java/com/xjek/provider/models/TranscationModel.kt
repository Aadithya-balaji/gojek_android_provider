package com.xjek.provider.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TranscationModel {
    @SerializedName("current_page")
    @Expose
    private var currentPage: Int? = null
    @SerializedName("data")
    @Expose
    private var data: List<TransactionDatum>? = null
    @SerializedName("first_page_url")
    @Expose
    private var firstPageUrl: String? = null
    @SerializedName("from")
    @Expose
    private var from: Int? = null
    @SerializedName("last_page")
    @Expose
    private var lastPage: Int? = null
    @SerializedName("last_page_url")
    @Expose
    private var lastPageUrl: String? = null
    @SerializedName("next_page_url")
    @Expose
    private var nextPageUrl: Any? = null
    @SerializedName("path")
    @Expose
    private var path: String? = null
    @SerializedName("per_page")
    @Expose
    private var perPage: Int? = null
    @SerializedName("prev_page_url")
    @Expose
    private var prevPageUrl: Any? = null
    @SerializedName("to")
    @Expose
    private var to: Int? = null
    @SerializedName("total")
    @Expose
    private var total: Int? = null

    fun getCurrentPage(): Int? {
        return currentPage
    }

    fun setCurrentPage(currentPage: Int?) {
        this.currentPage = currentPage
    }

    fun getData(): List<TransactionDatum>? {
        return data
    }

    fun setData(data: List<TransactionDatum>) {
        this.data = data
    }

    fun getFirstPageUrl(): String? {
        return firstPageUrl
    }

    fun setFirstPageUrl(firstPageUrl: String) {
        this.firstPageUrl = firstPageUrl
    }

    fun getFrom(): Int? {
        return from
    }

    fun setFrom(from: Int?) {
        this.from = from
    }

    fun getLastPage(): Int? {
        return lastPage
    }

    fun setLastPage(lastPage: Int?) {
        this.lastPage = lastPage
    }

    fun getLastPageUrl(): String? {
        return lastPageUrl
    }

    fun setLastPageUrl(lastPageUrl: String) {
        this.lastPageUrl = lastPageUrl
    }

    fun getNextPageUrl(): Any? {
        return nextPageUrl
    }

    fun setNextPageUrl(nextPageUrl: Any) {
        this.nextPageUrl = nextPageUrl
    }

    fun getPath(): String? {
        return path
    }

    fun setPath(path: String) {
        this.path = path
    }

    fun getPerPage(): Int? {
        return perPage
    }

    fun setPerPage(perPage: Int?) {
        this.perPage = perPage
    }

    fun getPrevPageUrl(): Any? {
        return prevPageUrl
    }

    fun setPrevPageUrl(prevPageUrl: Any) {
        this.prevPageUrl = prevPageUrl
    }

    fun getTo(): Int? {
        return to
    }

    fun setTo(to: Int?) {
        this.to = to
    }

    fun getTotal(): Int? {
        return total
    }

    fun setTotal(total: Int?) {
        this.total = total
    }
}
