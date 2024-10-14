package com.breezefieldsalesdemo.features.document.api

import com.breezefieldsalesdemo.features.dymanicSection.api.DynamicApi
import com.breezefieldsalesdemo.features.dymanicSection.api.DynamicRepo

object DocumentRepoProvider {
    fun documentRepoProvider(): DocumentRepo {
        return DocumentRepo(DocumentApi.create())
    }

    fun documentRepoProviderMultipart(): DocumentRepo {
        return DocumentRepo(DocumentApi.createImage())
    }
}