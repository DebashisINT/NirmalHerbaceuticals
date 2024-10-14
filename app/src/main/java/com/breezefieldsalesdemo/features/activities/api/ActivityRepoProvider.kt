package com.breezefieldsalesdemo.features.activities.api

import com.breezefieldsalesdemo.features.member.api.TeamApi
import com.breezefieldsalesdemo.features.member.api.TeamRepo

object ActivityRepoProvider {
    fun activityRepoProvider(): ActivityRepo {
        return ActivityRepo(ActivityApi.create())
    }

    fun activityImageRepoProvider(): ActivityRepo {
        return ActivityRepo(ActivityApi.createImage())
    }
}