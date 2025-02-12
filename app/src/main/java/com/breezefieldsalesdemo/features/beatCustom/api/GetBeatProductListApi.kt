package com.breezefieldsalesdemo.features.beatCustom.api

import com.breezefieldsalesdemo.app.NetworkConstant
import com.breezefieldsalesdemo.base.BaseResponse
import com.breezefieldsalesdemo.features.beatCustom.BeatGetStatusModel
import com.breezefieldsalesdemo.features.beatCustom.BeatUpdateModel
import com.breezefieldsalesdemo.features.damageProduct.model.DamageProductResponseModel
import com.breezefieldsalesdemo.features.damageProduct.model.delBreakageReq
import com.breezefieldsalesdemo.features.damageProduct.model.viewAllBreakageReq
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface GetBeatProductListApi {

    @FormUrlEncoded
    @POST("ShopAttendance/BeatDetailList")
    fun getBeatstatus(@Field("user_id") user_id: String, @Field("beat_date") beat_date: String,@Field("session_token") session_token: String):
            Observable<BeatGetStatusModel>

    @FormUrlEncoded
    @POST("ShopAttendance/UpdateBeat")
    fun updateBeatstatus(@Field("user_id") user_id: String, @Field("updating_beat_id") updating_beat_id: String,@Field("updating_date") updating_date: String):
            Observable<BeatUpdateModel>

    companion object Factory {
        fun create(): GetBeatProductListApi {
            val retrofit = Retrofit.Builder()
                .client(NetworkConstant.setTimeOutNoRetry())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(NetworkConstant.BASE_URL)
                .build()

            return retrofit.create(GetBeatProductListApi::class.java)
        }


        fun createFacePic(): GetBeatProductListApi {
            val retrofit = Retrofit.Builder()
                .client(NetworkConstant.setTimeOut())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(NetworkConstant.ADD_SHOP_BASE_URL)
                .build()

            return retrofit.create(GetBeatProductListApi::class.java)
        }


        fun createMultiPart(): GetBeatProductListApi {
            val retrofit = Retrofit.Builder()
                .client(NetworkConstant.setTimeOut())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(NetworkConstant.ADD_SHOP_BASE_URL)
                .build()

            return retrofit.create(GetBeatProductListApi::class.java)
        }

    }
}