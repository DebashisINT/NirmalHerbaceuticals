package com.breezefieldsalesdemo.features.weather.api

import com.breezefieldsalesdemo.base.BaseResponse
import com.breezefieldsalesdemo.features.task.api.TaskApi
import com.breezefieldsalesdemo.features.task.model.AddTaskInputModel
import com.breezefieldsalesdemo.features.weather.model.ForeCastAPIResponse
import com.breezefieldsalesdemo.features.weather.model.WeatherAPIResponse
import io.reactivex.Observable

class WeatherRepo(val apiService: WeatherApi) {
    fun getCurrentWeather(zipCode: String): Observable<WeatherAPIResponse> {
        return apiService.getTodayWeather(zipCode)
    }

    fun getWeatherForecast(zipCode: String): Observable<ForeCastAPIResponse> {
        return apiService.getForecast(zipCode)
    }
}