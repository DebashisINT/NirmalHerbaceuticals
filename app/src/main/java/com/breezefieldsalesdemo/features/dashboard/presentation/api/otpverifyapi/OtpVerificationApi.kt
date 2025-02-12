package com.breezefieldsalesdemo.features.dashboard.presentation.api.otpverifyapi

import com.breezefieldsalesdemo.app.NetworkConstant
import com.breezefieldsalesdemo.base.BaseResponse
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by Saikat on 22-11-2018.
 */
interface OtpVerificationApi {
    @FormUrlEncoded
    @POST("OTPConfiguration/OTPVerification")
    fun otpVerify(@Field("session_token") session_token: String, @Field("user_id") user_id: String, @Field("shop_id") shop_id: String,
                @Field("otp") otp: String): Observable<BaseResponse>

    /**
     * Companion object to create the GithubApiService
     */
    companion object Factory {
        fun create(): OtpVerificationApi {
            val retrofit = Retrofit.Builder()
                    .client(NetworkConstant.setTimeOut())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(NetworkConstant.BASE_URL)
                    .build()

            return retrofit.create(OtpVerificationApi::class.java)
        }
    }
}