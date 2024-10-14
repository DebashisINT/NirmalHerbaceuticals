package com.breezefieldsalesdemo.features.location.api

import com.breezefieldsalesdemo.features.location.shopdurationapi.ShopDurationApi
import com.breezefieldsalesdemo.features.location.shopdurationapi.ShopDurationRepository


object LocationRepoProvider {
    fun provideLocationRepository(): LocationRepo {
        return LocationRepo(LocationApi.create())
    }
}