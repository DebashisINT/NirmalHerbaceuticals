package com.breezefieldsalesdemo.features.viewAllOrder.api

import com.breezefieldsalesdemo.app.NetworkConstant
import com.breezefieldsalesdemo.features.login.model.productlistmodel.NewOdrScrOrderListModel
import com.breezefieldsalesdemo.features.viewAllOrder.OrdResponse
import com.breezefieldsalesdemo.features.viewAllOrder.model.NewOrderDataModel
import com.breezefieldsalesdemo.features.viewAllOrder.model.NewOrderOrderHistoryModel
import com.breezefieldsalesdemo.features.viewAllOrder.model.ViewAllOrderListResponseModel
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by Saikat on 01-10-2018.
 */
interface OrderDetailsListApi {
    @FormUrlEncoded
    @POST("Order/OrderDetailsList")
    fun getOrderDetailsList(@Field("session_token") session_token: String, @Field("user_id") user_id: String, @Field("shop_id") shop_id: String,
                            @Field("order_id") order_id: String): Observable<ViewAllOrderListResponseModel>

    //03-09-2021
    @FormUrlEncoded
    @POST("OrderWithProductAttribute/ListForOrderedProduct")
    fun getNewOrderData(@Field("session_token") session_token: String, @Field("user_id") user_id: String): Observable<NewOrderDataModel>

    @FormUrlEncoded
    @POST("OrderWithProductAttribute/NewListForOrderedProduct")
    fun getNewOrderHistoryData(@Field("session_token") session_token: String, @Field("user_id") user_id: String): Observable<NewOrderOrderHistoryModel>


    @FormUrlEncoded
    @POST("OrderWithProductAttribute/NewProductOrderList")
    fun getNewOrderHistoryDataSimplefied(@Field("session_token") session_token: String, @Field("user_id") user_id: String): Observable<NewOdrScrOrderListModel>


    @FormUrlEncoded
    @POST("Order/OrderStatusList")
    fun getOrderStatusLApi(@Field("user_id") user_id: String): Observable<OrdResponse>


    /**
     * Companion object to create the GithubApiService
     */
    companion object Factory {
        fun create(): OrderDetailsListApi {
            val retrofit = Retrofit.Builder()
                    .client(NetworkConstant.setTimeOut())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(NetworkConstant.BASE_URL)
                    .build()

            return retrofit.create(OrderDetailsListApi::class.java)
        }
    }
}