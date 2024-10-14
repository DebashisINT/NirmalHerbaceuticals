package com.breezefieldsalesdemo.features.viewAllOrder.interf

import com.breezefieldsalesdemo.app.domain.NewOrderGenderEntity
import com.breezefieldsalesdemo.app.domain.NewOrderProductEntity

interface ProductListNewOrderOnClick {
    fun productListOnClick(product: NewOrderProductEntity)
}