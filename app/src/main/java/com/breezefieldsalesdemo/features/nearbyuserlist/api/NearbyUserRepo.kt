package com.breezefieldsalesdemo.features.nearbyuserlist.api

import com.breezefieldsalesdemo.app.Pref
import com.breezefieldsalesdemo.features.nearbyuserlist.model.NearbyUserResponseModel
import com.breezefieldsalesdemo.features.newcollection.model.NewCollectionListResponseModel
import com.breezefieldsalesdemo.features.newcollection.newcollectionlistapi.NewCollectionListApi
import io.reactivex.Observable

class NearbyUserRepo(val apiService: NearbyUserApi) {
    fun nearbyUserList(): Observable<NearbyUserResponseModel> {
        return apiService.getNearbyUserList(Pref.session_token!!, Pref.user_id!!)
    }
}