package com.sunnyweather.android.logic

import androidx.lifecycle.liveData
import com.sunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.lang.RuntimeException

object Repository {

    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            val response = SunnyWeatherNetwork.searchPlaces(query)
            if (response.status == "ok"){
                val places = response.places
                Result.success(places)
            }else{
                Result.failure(
                    RuntimeException("response status is ${response.status}")
                )
            }
        }catch (e: Exception){
            Result.failure(e)
        }
        emit(result)

    }
}