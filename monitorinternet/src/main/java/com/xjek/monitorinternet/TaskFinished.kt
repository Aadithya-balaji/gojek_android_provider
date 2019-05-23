package com.xjek.monitorinternet

internal interface TaskFinished<T> {
    fun onTaskFinished(data: T)
}
