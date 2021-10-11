package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName

data class RealtimeResponse(val status: String, val result: Result){

    //将类定义在内部，防止出现和其他数据模型重名
    data class Result(val realtime: Realtime)

    data class Realtime(val skycon: String, val temperature: Float,
        @SerializedName("air_quality") val airQuality: AirQuality)

    data class AirQuality(val aqi: AQI)

    data class AQI(val chn: Float)
}