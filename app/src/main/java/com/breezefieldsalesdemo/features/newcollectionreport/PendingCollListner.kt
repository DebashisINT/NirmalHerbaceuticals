package com.breezefieldsalesdemo.features.newcollectionreport

import com.breezefieldsalesdemo.features.photoReg.model.UserListResponseModel

interface PendingCollListner {
    fun getUserInfoOnLick(obj: PendingCollData)
}