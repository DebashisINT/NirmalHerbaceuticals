package com.breezefieldsalesdemo.features.damageProduct.api

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import com.breezefieldsalesdemo.app.FileUtils
import com.breezefieldsalesdemo.base.BaseResponse
import com.breezefieldsalesdemo.features.NewQuotation.model.*
import com.breezefieldsalesdemo.features.addshop.model.AddShopRequestData
import com.breezefieldsalesdemo.features.addshop.model.AddShopResponse
import com.breezefieldsalesdemo.features.damageProduct.model.DamageProductResponseModel
import com.breezefieldsalesdemo.features.damageProduct.model.delBreakageReq
import com.breezefieldsalesdemo.features.damageProduct.model.viewAllBreakageReq
import com.breezefieldsalesdemo.features.login.model.userconfig.UserConfigResponseModel
import com.breezefieldsalesdemo.features.myjobs.model.WIPImageSubmit
import com.breezefieldsalesdemo.features.photoReg.model.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class GetDamageProductListRegRepository(val apiService : GetDamageProductListApi) {

    fun viewBreakage(req: viewAllBreakageReq): Observable<DamageProductResponseModel> {
        return apiService.viewBreakage(req)
    }

    fun delBreakage(req: delBreakageReq): Observable<BaseResponse>{
        return apiService.BreakageDel(req.user_id!!,req.breakage_number!!,req.session_token!!)
    }

}