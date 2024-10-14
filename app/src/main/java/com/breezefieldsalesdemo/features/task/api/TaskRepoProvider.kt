package com.breezefieldsalesdemo.features.task.api

import com.breezefieldsalesdemo.features.timesheet.api.TimeSheetApi
import com.breezefieldsalesdemo.features.timesheet.api.TimeSheetRepo

/**
 * Created by Saikat on 12-Aug-20.
 */
object TaskRepoProvider {
    fun taskRepoProvider(): TaskRepo {
        return TaskRepo(TaskApi.create())
    }
}