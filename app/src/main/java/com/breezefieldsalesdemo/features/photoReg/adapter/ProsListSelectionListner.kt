package com.breezefieldsalesdemo.features.photoReg.adapter

import com.breezefieldsalesdemo.features.photoReg.model.ProsCustom
import com.breezefieldsalesdemo.features.photoReg.model.UserListResponseModel

interface ProsListSelectionListner {
    fun getInfo(obj: ProsCustom)
}