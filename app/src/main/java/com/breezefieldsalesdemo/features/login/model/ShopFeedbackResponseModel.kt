package com.breezefieldsalesdemo.features.login.model

import com.breezefieldsalesdemo.base.BaseResponse

class ShopFeedbackResponseModel :BaseResponse(){
        var shop_list:List<Shop_list>? = null
}

data class Shop_list(var shop_id:String,var feedback_remark_list:List<Feedback_remark>)

data class Feedback_remark(var feedback:String,
                           var date_time:String,
                           var multi_contact_name:String,
                           var multi_contact_number:String,
)