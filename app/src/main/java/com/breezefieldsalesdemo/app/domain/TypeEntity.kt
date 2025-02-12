package com.breezefieldsalesdemo.app.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.breezefieldsalesdemo.app.AppConstant

@Entity(tableName = AppConstant.TYPE)
class TypeEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "type_id")
    var type_id: String? = null

    @ColumnInfo(name = "name")
    var name: String? = null

    @ColumnInfo(name = "activity_id")
    var activity_id: String? = null
}