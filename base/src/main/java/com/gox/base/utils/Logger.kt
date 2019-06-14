package com.gox.base.utils

import android.util.Log

import com.gox.base.BuildConfig

class Logger private constructor() {

    init {
        if (BuildConfig.DEBUG)
            isLoggingEnabled = true
    }

    companion object {

        private val TAG = Logger::class.java.simpleName
        private var isLoggingEnabled: Boolean = false

        private fun buildMsg(msg: String): String {
            val buffer = StringBuilder()
            val stackTraceElement = Thread.currentThread().stackTrace[4]
            buffer.append("[ ")
            buffer.append(Thread.currentThread().name)
            buffer.append(": ")
            buffer.append(stackTraceElement.fileName)
            buffer.append(": ")
            buffer.append(stackTraceElement.lineNumber)
            buffer.append(": ")
            buffer.append(stackTraceElement.methodName)
            buffer.append("() ] _____ ")
            buffer.append(msg)
            return buffer.toString()
        }

        fun v(msg: String) {
            if (isLoggingEnabled && Log.isLoggable(TAG, Log.VERBOSE)) Log.v(TAG, buildMsg(msg))
        }

        fun d(msg: String) {
            if (isLoggingEnabled && Log.isLoggable(TAG, Log.DEBUG)) Log.d(TAG, buildMsg(msg))
        }

        fun i(msg: String) {
            if (isLoggingEnabled && Log.isLoggable(TAG, Log.INFO)) Log.i(TAG, buildMsg(msg))
        }

        fun i(tag: String, msg: String) {
            if (isLoggingEnabled && Log.isLoggable(TAG, Log.ERROR)) Log.e(tag, buildMsg(msg))
        }

        fun w(msg: String) {
            if (isLoggingEnabled && Log.isLoggable(TAG, Log.WARN)) Log.w(TAG, buildMsg(msg))
        }

        fun w(msg: String, e: Exception) {
            if (isLoggingEnabled && Log.isLoggable(TAG, Log.WARN)) Log.w(TAG, buildMsg(msg), e)
        }

        fun e(msg: String) {
            if (isLoggingEnabled && Log.isLoggable(TAG, Log.ERROR)) Log.e(TAG, buildMsg(msg))
        }

        fun e(tag: String, msg: String) {
            if (isLoggingEnabled && Log.isLoggable(TAG, Log.ERROR)) Log.e(tag, buildMsg(msg))
        }

        fun e(msg: String, e: Exception) {
            if (isLoggingEnabled && Log.isLoggable(TAG, Log.ERROR)) Log.e(TAG, buildMsg(msg), e)
        }
    }
}