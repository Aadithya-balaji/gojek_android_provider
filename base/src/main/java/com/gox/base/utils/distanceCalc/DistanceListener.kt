package com.gox.base.utils.distanceCalc

interface DistanceListener {

    fun whenDone(output: DistanceCalcModel)

    fun whenDirectionFail(statusCode: String)
}