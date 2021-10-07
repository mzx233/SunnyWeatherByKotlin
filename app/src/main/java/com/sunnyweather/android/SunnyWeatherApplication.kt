package com.sunnyweather.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class SunnyWeatherApplication : Application() {

    companion object{
        //加了const就是真的static变量，不加就是调用getter的加static变量
        const val TOKEN = "NxAKUIltpfxz5BpU"
        //将Context设置成静态变量会产生内存泄露问题，但这里是Application的Context所以不用担心，加上注解让忽略警告。
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}