package com.breezefieldsalesdemo.features.photoReg.adapter

import com.breezefieldsalesdemo.features.photoReg.model.UserListResponseModel

interface PhotoAttendanceListner {
    fun getUserInfoOnLick(obj: UserListResponseModel)
    fun getUserInfoAttendReportOnLick(obj: UserListResponseModel)
}