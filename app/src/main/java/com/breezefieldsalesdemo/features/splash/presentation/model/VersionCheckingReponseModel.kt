package com.breezefieldsalesdemo.features.splash.presentation.model

import com.breezefieldsalesdemo.base.BaseResponse

/**
 * Created by Saikat on 02-01-2019.
 */
class VersionCheckingReponseModel : BaseResponse() {
    var min_req_version: String? = null
    var play_store_version: String? = null
    var mandatory_msg: String? = null
    var optional_msg: String? = null
    var apk_url: String? = null
}