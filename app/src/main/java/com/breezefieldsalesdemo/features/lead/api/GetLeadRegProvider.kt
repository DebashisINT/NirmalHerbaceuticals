package com.breezefieldsalesdemo.features.lead.api

import com.breezefieldsalesdemo.features.NewQuotation.api.GetQuotListRegRepository
import com.breezefieldsalesdemo.features.NewQuotation.api.GetQutoListApi


object GetLeadRegProvider {
    fun provideList(): GetLeadListRegRepository {
        return GetLeadListRegRepository(GetLeadListApi.create())
    }
}