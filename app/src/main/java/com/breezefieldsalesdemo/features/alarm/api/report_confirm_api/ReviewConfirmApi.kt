package com.breezefieldsalesdemo.features.alarm.api.report_confirm_api

import com.breezefieldsalesdemo.app.NetworkConstant
import com.breezefieldsalesdemo.base.BaseResponse
import com.breezefieldsalesdemo.features.alarm.model.ReviewConfirmInputModel
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by Saikat on 21-02-2019.
 */
interface ReviewConfirmApi {

    @POST("AlarmConfig/ReviewConfirm")
    fun reviewConfirm(@Body reviewConfirm: ReviewConfirmInputModel?): Observable<BaseResponse>

    /**
     * Companion object to create the ShopDurationApi
     */
    companion object Factory {
        fun create(): ReviewConfirmApi {
            val retrofit = Retrofit.Builder()
                    .client(NetworkConstant.setTimeOutNoRetry())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(NetworkConstant.BASE_URL)
                    .build()

            return retrofit.create(ReviewConfirmApi::class.java)
        }
    }
}