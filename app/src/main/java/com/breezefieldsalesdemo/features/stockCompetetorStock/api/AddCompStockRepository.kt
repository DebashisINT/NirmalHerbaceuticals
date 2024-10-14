package com.breezefieldsalesdemo.features.stockCompetetorStock.api

import com.breezefieldsalesdemo.base.BaseResponse
import com.breezefieldsalesdemo.features.orderList.model.NewOrderListResponseModel
import com.breezefieldsalesdemo.features.stockCompetetorStock.ShopAddCompetetorStockRequest
import com.breezefieldsalesdemo.features.stockCompetetorStock.model.CompetetorStockGetData
import io.reactivex.Observable

class AddCompStockRepository(val apiService:AddCompStockApi){

    fun addCompStock(shopAddCompetetorStockRequest: ShopAddCompetetorStockRequest): Observable<BaseResponse> {
        return apiService.submShopCompStock(shopAddCompetetorStockRequest)
    }

    fun getCompStockList(sessiontoken: String, user_id: String, date: String): Observable<CompetetorStockGetData> {
        return apiService.getCompStockList(sessiontoken, user_id, date)
    }
}