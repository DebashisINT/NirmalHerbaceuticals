package com.breezefieldsalesdemo.features.marketing.api.deletemarketingimage

import com.breezefieldsalesdemo.base.BaseResponse
import io.reactivex.Observable

/**
 * Created by Pratishruti on 28-02-2018.
 */
class DeleteMarketingImageRepo(val apiService: DeleteMarketingImageApi) {
    fun getMarketingCategoryList(user_id:String,shop_id:String,image_id:String): Observable<BaseResponse> {
        return apiService.deleteMarketingImage(user_id,shop_id,image_id)
    }
}