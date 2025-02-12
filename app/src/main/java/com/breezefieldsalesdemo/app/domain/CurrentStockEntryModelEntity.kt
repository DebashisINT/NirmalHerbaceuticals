package com.breezefieldsalesdemo.app.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.breezefieldsalesdemo.app.AppConstant

@Entity(tableName = AppConstant.SHOP_CURRENT_STOCK_TABLE)
class CurrentStockEntryModelEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0


    @ColumnInfo(name = "user_id")
    var user_id: String? = null

    @ColumnInfo(name = "stock_id")
    var stock_id: String? = null

    @ColumnInfo(name = "shop_id")
    var shop_id: String? = null

    @ColumnInfo(name = "visited_datetime")
    var visited_datetime: String? = null

    @ColumnInfo(name = "visited_date")
    var visited_date: String? = null

    @ColumnInfo(name = "total_product_stock_qty")
    var total_product_stock_qty: String? = null

    @ColumnInfo(name = "isUploaded")
    var isUploaded: Boolean = false
}