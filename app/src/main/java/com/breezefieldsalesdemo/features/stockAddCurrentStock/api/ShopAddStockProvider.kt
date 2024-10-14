package com.breezefieldsalesdemo.features.stockAddCurrentStock.api

import com.breezefieldsalesdemo.features.location.shopRevisitStatus.ShopRevisitStatusApi
import com.breezefieldsalesdemo.features.location.shopRevisitStatus.ShopRevisitStatusRepository

object ShopAddStockProvider {
    fun provideShopAddStockRepository(): ShopAddStockRepository {
        return ShopAddStockRepository(ShopAddStockApi.create())
    }
}