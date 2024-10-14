package com.breezefieldsalesdemo.features.login.model.productlistmodel

import com.breezefieldsalesdemo.app.domain.ModelEntity
import com.breezefieldsalesdemo.app.domain.ProductListEntity
import com.breezefieldsalesdemo.base.BaseResponse

class ModelListResponse: BaseResponse() {
    var model_list: ArrayList<ModelEntity>? = null
}