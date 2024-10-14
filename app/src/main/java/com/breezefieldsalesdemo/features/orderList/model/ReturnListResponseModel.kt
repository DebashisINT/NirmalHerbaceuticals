package com.breezefieldsalesdemo.features.orderList.model

import com.breezefieldsalesdemo.base.BaseResponse


class ReturnListResponseModel: BaseResponse() {
    var return_list: ArrayList<ReturnDataModel>? = null
}