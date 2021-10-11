package com.sunnyweather.android.logic

import androidx.lifecycle.liveData
import com.sunnyweather.android.logic.dao.PlaceDao
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.logic.model.Weather
import com.sunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

object Repository {

    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
            val response = SunnyWeatherNetwork.searchPlaces(query)
            if (response.status == "ok"){
                val places = response.places
                Result.success(places)
            }else{
                Result.failure(
                    RuntimeException("response status is ${response.status}")
                )
            }
    }

    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO){
            //开启协程
            coroutineScope {
                //async必须在协程作用域中才能调用,返回一个Deferred对象，调用.await()方法就可以执行其中的代码，它会将当前协程阻塞住，直到执行结束
                val deferredRealtime = async {
                    SunnyWeatherNetwork.getRealtimeWeather(lng, lat)
                }
                val deferredDaily = async {
                    SunnyWeatherNetwork.getDailyWeather(lng, lat)
                }
                val realtimeResponse = deferredRealtime.await()
                val dailyResponse = deferredDaily.await()
                if (realtimeResponse.status == "ok" && dailyResponse.status == "ok"){
                    val weather = Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                    Result.success(weather)
                }else{
                    Result.failure(
                        RuntimeException(
                            "realtime response status is ${realtimeResponse.status}" +
                                    "daily response status is ${dailyResponse.status}"
                        )
                    )
                }
            }
    }

    private fun <T> fire(dispatcher: CoroutineDispatcher, block: suspend () -> Result<T>) =
        liveData(dispatcher){
            val result = try {
                block()
            }catch (e: Exception){
                Result.failure(e)
            }
            emit(result)
        }

    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()
}