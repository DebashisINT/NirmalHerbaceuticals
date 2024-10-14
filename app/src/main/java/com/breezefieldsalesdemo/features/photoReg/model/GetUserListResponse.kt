package com.breezefieldsalesdemo.features.photoReg.model

import com.breezefieldsalesdemo.features.stockAddCurrentStock.model.CurrentStockGetDataDtls

class GetUserListResponse {
    var status:String ? = null
    var message:String ? = null
    var user_list :ArrayList<UserListResponseModel>? = null
}