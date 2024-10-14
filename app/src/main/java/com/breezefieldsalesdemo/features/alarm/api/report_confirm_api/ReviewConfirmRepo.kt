package com.breezefieldsalesdemo.features.alarm.api.report_confirm_api

import com.breezefieldsalesdemo.base.BaseResponse
import com.breezefieldsalesdemo.features.alarm.model.ReviewConfirmInputModel
import io.reactivex.Observable

/**
 * Created by Saikat on 21-02-2019.
 */
class ReviewConfirmRepo(val apiService: ReviewConfirmApi) {
    fun reviewConfirm(reviewConfirm: ReviewConfirmInputModel): Observable<BaseResponse> {
        return apiService.reviewConfirm(reviewConfirm)
    }
}