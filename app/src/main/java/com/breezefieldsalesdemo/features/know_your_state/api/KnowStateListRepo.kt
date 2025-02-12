package com.breezefieldsalesdemo.features.know_your_state.api

import com.breezefieldsalesdemo.app.Pref
import com.breezefieldsalesdemo.features.know_your_state.model.KnowYourStateListResponseModel
import io.reactivex.Observable

/**
 * Created by Saikat on 27-11-2019.
 */
class KnowStateListRepo(val apiService: KnowStateListApi) {
    fun knowStateList(month: String, year: String): Observable<KnowYourStateListResponseModel> {
        return apiService.getKnowStateList(Pref.session_token!!, Pref.user_id!!, month, year)
    }
}