package com.breezefieldsalesdemo.features.viewAllOrder.model

import com.breezefieldsalesdemo.app.domain.NewOrderColorEntity
import com.breezefieldsalesdemo.app.domain.NewOrderGenderEntity
import com.breezefieldsalesdemo.app.domain.NewOrderProductEntity
import com.breezefieldsalesdemo.app.domain.NewOrderSizeEntity
import com.breezefieldsalesdemo.features.stockCompetetorStock.model.CompetetorStockGetDataDtls

class NewOrderDataModel {
    var status:String ? = null
    var message:String ? = null
    var Gender_list :ArrayList<NewOrderGenderEntity>? = null
    var Product_list :ArrayList<NewOrderProductEntity>? = null
    var Color_list :ArrayList<NewOrderColorEntity>? = null
    var size_list :ArrayList<NewOrderSizeEntity>? = null
}

