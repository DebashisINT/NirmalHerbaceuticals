package com.breezefieldsalesdemo.app.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.breezefieldsalesdemo.app.AppConstant

@Entity(tableName = AppConstant.NEW_ORDER_GENDER)
class NewOrderGenderEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "gender_id")
    var gender_id: Int = 0

    @ColumnInfo(name = "gender")
    var gender: String? = null
}