package com.breezefieldsalesdemo.app.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.breezefieldsalesdemo.app.AppConstant

/**
 * Created by Saikat on 12-11-2018.
 */
@Dao
interface OrderProductListDao {
    @Query("SELECT * FROM " + AppConstant.ORDER_PRODUCT_LIST_TABLE)
    fun getAll(): List<OrderProductListEntity>

    @Query("SELECT * FROM " + AppConstant.ORDER_PRODUCT_LIST_TABLE + " where order_id=:order_id and shop_id=:shop_id")
    fun getDataAccordingToShopAndOrderId(order_id: String, shop_id: String): List<OrderProductListEntity>

    @Query("SELECT * FROM " + AppConstant.ORDER_PRODUCT_LIST_TABLE + " where order_id=:order_id")
    fun getDataAccordingToOrderId(order_id: String): List<OrderProductListEntity>

    @Insert
    fun insert(vararg orderProductList: OrderProductListEntity)

    @Query("DELETE FROM " + AppConstant.ORDER_PRODUCT_LIST_TABLE)
    fun delete()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    abstract fun insertAll(kist: List<OrderProductListEntity>)

    @Query("select sum(qty) as totalQty from order_product_list where order_id=:order_id")
    fun getTotalQnty(order_id: String): String

}