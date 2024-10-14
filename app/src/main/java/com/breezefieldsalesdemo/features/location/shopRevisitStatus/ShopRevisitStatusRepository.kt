package com.breezefieldsalesdemo.features.location.shopRevisitStatus

import com.breezefieldsalesdemo.base.BaseResponse
import com.breezefieldsalesdemo.features.location.model.ShopDurationRequest
import com.breezefieldsalesdemo.features.location.model.ShopRevisitStatusRequest
import io.reactivex.Observable

class ShopRevisitStatusRepository(val apiService : ShopRevisitStatusApi) {
    fun shopRevisitStatus(shopRevisitStatus: ShopRevisitStatusRequest?): Observable<BaseResponse> {
        return apiService.submShopRevisitStatus(shopRevisitStatus)
    }
}