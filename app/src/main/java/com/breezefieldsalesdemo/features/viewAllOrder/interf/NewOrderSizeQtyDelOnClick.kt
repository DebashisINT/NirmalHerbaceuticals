package com.breezefieldsalesdemo.features.viewAllOrder.interf

import com.breezefieldsalesdemo.app.domain.NewOrderGenderEntity
import com.breezefieldsalesdemo.features.viewAllOrder.model.ProductOrder
import java.text.FieldPosition

interface NewOrderSizeQtyDelOnClick {
    fun sizeQtySelListOnClick(product_size_qty: ArrayList<ProductOrder>)
    fun sizeQtyListOnClick(product_size_qty: ProductOrder,position: Int)
}