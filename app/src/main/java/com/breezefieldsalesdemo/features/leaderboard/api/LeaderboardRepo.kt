package com.breezefieldsalesdemo.features.leaderboard.api

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import com.fasterxml.jackson.databind.ObjectMapper
import com.breezefieldsalesdemo.app.FileUtils
import com.breezefieldsalesdemo.app.Pref
import com.breezefieldsalesdemo.base.BaseResponse
import com.breezefieldsalesdemo.features.addshop.model.AddLogReqData
import com.breezefieldsalesdemo.features.addshop.model.AddShopRequestData
import com.breezefieldsalesdemo.features.addshop.model.AddShopResponse
import com.breezefieldsalesdemo.features.addshop.model.LogFileResponse
import com.breezefieldsalesdemo.features.addshop.model.UpdateAddrReq
import com.breezefieldsalesdemo.features.contacts.CallHisDtls
import com.breezefieldsalesdemo.features.contacts.CompanyReqData
import com.breezefieldsalesdemo.features.contacts.ContactMasterRes
import com.breezefieldsalesdemo.features.contacts.SourceMasterRes
import com.breezefieldsalesdemo.features.contacts.StageMasterRes
import com.breezefieldsalesdemo.features.contacts.StatusMasterRes
import com.breezefieldsalesdemo.features.contacts.TypeMasterRes
import com.breezefieldsalesdemo.features.dashboard.presentation.DashboardActivity
import com.breezefieldsalesdemo.features.login.model.WhatsappApiData
import com.breezefieldsalesdemo.features.login.model.WhatsappApiFetchData
import com.google.gson.Gson
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * Created by Puja on 10-10-2024.
 */
class LeaderboardRepo(val apiService: LeaderboardApi) {

    fun branchlist(session_token: String): Observable<LeaderboardBranchData> {
        return apiService.branchList(session_token)
    }
    fun ownDatalist(user_id: String,activitybased: String,branchwise: String,flag: String): Observable<LeaderboardOwnData> {
        return apiService.ownDatalist(user_id,activitybased,branchwise,flag)
    }
    fun overAllAPI(user_id: String,activitybased: String,branchwise: String,flag: String): Observable<LeaderboardOverAllData> {
        return apiService.overAllDatalist(user_id,activitybased,branchwise,flag)
    }
}