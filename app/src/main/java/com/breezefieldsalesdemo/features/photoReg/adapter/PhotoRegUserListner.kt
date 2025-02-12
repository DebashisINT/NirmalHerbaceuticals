package com.breezefieldsalesdemo.features.photoReg.adapter

import com.breezefieldsalesdemo.features.photoReg.model.UserListResponseModel

interface PhotoRegUserListner {
    fun getUserInfoOnLick(obj: UserListResponseModel)
    fun getPhoneOnLick(phone: String)
    fun getWhatsappOnLick(phone: String)
    fun deletePicOnLick(obj: UserListResponseModel)
    fun viewPicOnLick(img_link: String,name : String)
    fun getAadhaarOnLick(obj: UserListResponseModel)
    fun updateTypeOnClick(obj: UserListResponseModel)
    fun updateContactOnClick(obj: UserListResponseModel)
    fun addContactOnClick(obj: UserListResponseModel)
    fun updateUserNameOnClick(obj: UserListResponseModel)

    fun updateOtherIDOnClick(obj: UserListResponseModel)
    fun updateLoginIDOnClick(obj: UserListResponseModel)
}