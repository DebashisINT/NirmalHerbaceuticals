package com.breezefieldsalesdemo.features.dashboard.presentation.api.dayStartEnd

import com.breezefieldsalesdemo.features.stockCompetetorStock.api.AddCompStockApi
import com.breezefieldsalesdemo.features.stockCompetetorStock.api.AddCompStockRepository

object DayStartEndRepoProvider {
    fun dayStartRepositiry(): DayStartEndRepository {
        return DayStartEndRepository(DayStartEndApi.create())
    }

}