package com.breezefieldsalesdemo.features.stockAddCurrentStock.api

import com.breezefieldsalesdemo.base.BaseResponse
import com.breezefieldsalesdemo.features.location.model.ShopRevisitStatusRequest
import com.breezefieldsalesdemo.features.location.shopRevisitStatus.ShopRevisitStatusApi
import com.breezefieldsalesdemo.features.stockAddCurrentStock.ShopAddCurrentStockRequest
import com.breezefieldsalesdemo.features.stockAddCurrentStock.model.CurrentStockGetData
import com.breezefieldsalesdemo.features.stockCompetetorStock.model.CompetetorStockGetData
import io.reactivex.Observable

class ShopAddStockRepository (val apiService : ShopAddStockApi){
    fun shopAddStock(shopAddCurrentStockRequest: ShopAddCurrentStockRequest?): Observable<BaseResponse> {
        return apiService.submShopAddStock(shopAddCurrentStockRequest)
    }

    fun getCurrStockList(sessiontoken: String, user_id: String, date: String): Observable<CurrentStockGetData> {
        return apiService.getCurrStockListApi(sessiontoken, user_id, date)
    }

}