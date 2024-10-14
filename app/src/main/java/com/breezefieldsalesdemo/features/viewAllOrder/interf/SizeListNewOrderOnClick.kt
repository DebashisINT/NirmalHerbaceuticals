package com.breezefieldsalesdemo.features.viewAllOrder.interf

import com.breezefieldsalesdemo.app.domain.NewOrderProductEntity
import com.breezefieldsalesdemo.app.domain.NewOrderSizeEntity

interface SizeListNewOrderOnClick {
    fun sizeListOnClick(size: NewOrderSizeEntity)
}