package com.breezefieldsalesdemo.features.dashboard.presentation.api.dashboardApi

import android.content.Context
import android.net.Uri
import com.breezefieldsalesdemo.app.FileUtils
import com.breezefieldsalesdemo.app.Pref
import com.breezefieldsalesdemo.app.utils.AppUtils
import com.breezefieldsalesdemo.base.BaseResponse
import com.breezefieldsalesdemo.features.addAttendence.model.AddAttendenceImageInput
import com.breezefieldsalesdemo.features.dashboard.presentation.DashboardActivity
import com.breezefieldsalesdemo.features.dashboard.presentation.model.DayStartEndImageInput
import com.breezefieldsalesdemo.features.location.LocationFuzedService
import com.breezefieldsalesdemo.features.login.model.AlarmSelfieInput
import com.breezefieldsalesdemo.features.login.presentation.LoginActivity
import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * Created by Saikat on 26-Jun-20.
 */
class DashboardRepo(val apiService: DashboardApi) {

    fun alarmWithSelfie(image: String, context: Context, reportId: String): Observable<BaseResponse> {
        var profile_img_data: MultipartBody.Part? = null

        val profile_img_file = File(image) //FileUtils.getFile(context, Uri.parse(image))
        if (profile_img_file != null && profile_img_file.exists()) {
            val profileImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), profile_img_file)
            profile_img_data = MultipartBody.Part.createFormData("image", profile_img_file.name, profileImgBody)
        } else {
            var mFile: File? = null
            mFile = (context as DashboardActivity).getShopDummyImageFile()
            val profileImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile)
            profile_img_data = MultipartBody.Part.createFormData("image", mFile.name, profileImgBody)
        }

        //var shopObject: RequestBody? = null
        var jsonInString = ""
        try {
            jsonInString = ObjectMapper().writeValueAsString(AlarmSelfieInput(Pref.user_id!!, Pref.session_token!!, Pref.current_latitude,
                    Pref.current_longitude, AppUtils.getCurrentISODateTime(), reportId))
            //  shopObject = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInString)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return apiService.alarmSelfie(jsonInString, profile_img_data)
        // return apiService.getAddShopWithoutImage(jsonInString)
    }

    fun submitHomeLocReason(reason: String): Observable<BaseResponse> {
        return apiService.submitHomeLocReason(Pref.session_token!!, Pref.user_id!!, reason)
    }

    fun submiLogoutReason(reason: String): Observable<BaseResponse> {
        return apiService.submitLogoutReason(Pref.session_token!!, Pref.user_id!!, reason)
    }



    fun dayStartWithImage(image: String, context: Context): Observable<BaseResponse> {
        var profile_img_data: MultipartBody.Part? = null

        val profile_img_file = File(image) //FileUtils.getFile(context, Uri.parse(image))
        if (profile_img_file != null && profile_img_file.exists()) {
            val profileImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), profile_img_file)
            profile_img_data = MultipartBody.Part.createFormData("image", profile_img_file.name, profileImgBody)
        } else {
            var mFile: File? = null
            if (context is DashboardActivity)
                mFile = (context as DashboardActivity).getShopDummyImageFile()
            else
                mFile = (context as LocationFuzedService).getShopDummyImageFile()
            val profileImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile)
            profile_img_data = MultipartBody.Part.createFormData("image", mFile.name, profileImgBody)
        }

        val attendanceImg = DayStartEndImageInput()
        attendanceImg.session_token = Pref.session_token!!
        attendanceImg.user_id = Pref.user_id!!
        attendanceImg.date_time = AppUtils.getCurrentDateTime()
        attendanceImg.day_start = "1"
        attendanceImg.day_end = "0"

        //var shopObject: RequestBody? = null
        var jsonInString = ""
        try {
            jsonInString = ObjectMapper().writeValueAsString(attendanceImg)
            //  shopObject = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInString)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return apiService.dayStartEndWithImage(jsonInString, profile_img_data)
        // return apiService.getAddShopWithoutImage(jsonInString)
    }

    fun dayEndWithImage(image: String, context: Context): Observable<BaseResponse> {
        var profile_img_data: MultipartBody.Part? = null

        val profile_img_file = File(image) //FileUtils.getFile(context, Uri.parse(image))
        if (profile_img_file != null && profile_img_file.exists()) {
            val profileImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), profile_img_file)
            profile_img_data = MultipartBody.Part.createFormData("image", profile_img_file.name, profileImgBody)
        } else {
            var mFile: File? = null
            if (context is DashboardActivity)
                mFile = (context as DashboardActivity).getShopDummyImageFile()
            else
                mFile = (context as LocationFuzedService).getShopDummyImageFile()
            val profileImgBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile)
            profile_img_data = MultipartBody.Part.createFormData("image", mFile.name, profileImgBody)
        }

        val attendanceImg = DayStartEndImageInput()
        attendanceImg.session_token = Pref.session_token!!
        attendanceImg.user_id = Pref.user_id!!
        attendanceImg.date_time = AppUtils.getCurrentDateTime()
        attendanceImg.day_start = "0"
        attendanceImg.day_end = "1"

        //var shopObject: RequestBody? = null
        var jsonInString = ""
        try {
            jsonInString = ObjectMapper().writeValueAsString(attendanceImg)
            //  shopObject = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonInString)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return apiService.dayStartEndWithImage(jsonInString, profile_img_data)
        // return apiService.getAddShopWithoutImage(jsonInString)
    }

}