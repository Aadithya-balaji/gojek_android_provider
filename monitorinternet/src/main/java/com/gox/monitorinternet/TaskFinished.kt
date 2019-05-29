package com.gox.monitorinternet

internal interface TaskFinished<T> {
    fun onTaskFinished(data: T)
}
