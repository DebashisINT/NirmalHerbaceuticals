package com.breezefieldsalesdemo.features.survey.api

import com.breezefieldsalesdemo.features.photoReg.api.GetUserListPhotoRegApi
import com.breezefieldsalesdemo.features.photoReg.api.GetUserListPhotoRegRepository

object SurveyDataProvider{

    fun provideSurveyQ(): SurveyDataRepository {
        return SurveyDataRepository(SurveyDataApi.create())
    }

    fun provideSurveyQMultiP(): SurveyDataRepository {
        return SurveyDataRepository(SurveyDataApi.createImage())
    }
}