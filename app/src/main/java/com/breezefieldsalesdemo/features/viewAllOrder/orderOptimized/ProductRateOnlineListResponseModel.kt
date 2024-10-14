package com.breezefieldsalesdemo.features.viewAllOrder.orderOptimized

import com.breezefieldsalesdemo.app.domain.ProductOnlineRateTempEntity
import com.breezefieldsalesdemo.base.BaseResponse
import com.breezefieldsalesdemo.features.login.model.productlistmodel.ProductRateDataModel
import java.io.Serializable

class ProductRateOnlineListResponseModel: BaseResponse(), Serializable {
    var product_rate_list: ArrayList<ProductOnlineRateTempEntity>? = null
}