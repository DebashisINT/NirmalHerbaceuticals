package com.breezefieldsalesdemo.features.location.shopRevisitStatus

import com.breezefieldsalesdemo.features.location.shopdurationapi.ShopDurationApi
import com.breezefieldsalesdemo.features.location.shopdurationapi.ShopDurationRepository

object ShopRevisitStatusRepositoryProvider {
    fun provideShopRevisitStatusRepository(): ShopRevisitStatusRepository {
        return ShopRevisitStatusRepository(ShopRevisitStatusApi.create())
    }
}