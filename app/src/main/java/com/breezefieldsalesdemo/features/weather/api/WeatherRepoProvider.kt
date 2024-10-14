package com.breezefieldsalesdemo.features.weather.api

import com.breezefieldsalesdemo.features.task.api.TaskApi
import com.breezefieldsalesdemo.features.task.api.TaskRepo

object WeatherRepoProvider {
    fun weatherRepoProvider(): WeatherRepo {
        return WeatherRepo(WeatherApi.create())
    }
}