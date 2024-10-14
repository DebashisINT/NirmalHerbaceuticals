package com.breezefieldsalesdemo.features.login.model.opportunitymodel

import com.breezefieldsalesdemo.app.domain.OpportunityStatusEntity
import com.breezefieldsalesdemo.app.domain.ProductListEntity
import com.breezefieldsalesdemo.base.BaseResponse

/**
 * Created by Puja on 30.05.2024
 */
class OpportunityStatusListResponseModel : BaseResponse() {
    var status_list: ArrayList<OpportunityStatusEntity>? = null
}