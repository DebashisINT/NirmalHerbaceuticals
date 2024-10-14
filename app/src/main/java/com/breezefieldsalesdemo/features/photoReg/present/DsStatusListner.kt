package com.breezefieldsalesdemo.features.photoReg.present

import com.breezefieldsalesdemo.app.domain.ProspectEntity
import com.breezefieldsalesdemo.features.photoReg.model.UserListResponseModel

interface DsStatusListner {
    fun getDSInfoOnLick(obj: ProspectEntity)
}