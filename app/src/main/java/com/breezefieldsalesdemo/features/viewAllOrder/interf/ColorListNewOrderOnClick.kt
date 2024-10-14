package com.breezefieldsalesdemo.features.viewAllOrder.interf

import com.breezefieldsalesdemo.app.domain.NewOrderColorEntity
import com.breezefieldsalesdemo.app.domain.NewOrderProductEntity

interface ColorListNewOrderOnClick {
    fun productListOnClick(color: NewOrderColorEntity)
}