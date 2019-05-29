package com.gox.base.session

interface SessionListener{
    fun invalidate()
    fun refresh()
}