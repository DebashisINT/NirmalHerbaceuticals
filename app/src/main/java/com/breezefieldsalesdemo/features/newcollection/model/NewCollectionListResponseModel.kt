package com.breezefieldsalesdemo.features.newcollection.model

import com.breezefieldsalesdemo.app.domain.CollectionDetailsEntity
import com.breezefieldsalesdemo.base.BaseResponse
import com.breezefieldsalesdemo.features.shopdetail.presentation.model.collectionlist.CollectionListDataModel

/**
 * Created by Saikat on 15-02-2019.
 */
class NewCollectionListResponseModel : BaseResponse() {
    //var collection_list: ArrayList<CollectionListDataModel>? = null
    var collection_list: ArrayList<CollectionDetailsEntity>? = null
}