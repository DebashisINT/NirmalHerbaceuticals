package com.breezefieldsalesdemo.features.mylearning.apiCall

import com.breezefieldsalesdemo.features.login.api.opportunity.OpportunityListApi
import com.breezefieldsalesdemo.features.login.api.opportunity.OpportunityListRepo

object LMSRepoProvider {
    fun getTopicList(): LMSRepo {
        return LMSRepo(LMSApi.create())
    }
}