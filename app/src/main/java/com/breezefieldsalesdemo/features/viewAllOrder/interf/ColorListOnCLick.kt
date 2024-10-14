package com.breezefieldsalesdemo.features.viewAllOrder.interf

import com.breezefieldsalesdemo.app.domain.NewOrderGenderEntity
import com.breezefieldsalesdemo.features.viewAllOrder.model.ProductOrder

interface ColorListOnCLick {
    fun colorListOnCLick(size_qty_list: ArrayList<ProductOrder>, adpPosition:Int)
}