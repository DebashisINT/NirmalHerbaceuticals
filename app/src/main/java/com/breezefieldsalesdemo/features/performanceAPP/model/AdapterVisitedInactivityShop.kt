package com.breezefieldsalesdemo.features.performanceAPP.model

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldsalesdemo.R
import com.breezefieldsalesdemo.app.AppDatabase
import com.breezefieldsalesdemo.app.uiaction.IntentActionable
import com.breezefieldsalesdemo.app.utils.AppUtils
import com.breezefieldsalesdemo.features.performanceAPP.NoVisitedActivityInshop
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_list_ma_addr1
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_list_ma_shop_catagory_retailer1
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_name
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_phone_number
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_list_ma_name
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_list_ma_shop_count
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_list_shop_type1
import kotlinx.android.synthetic.main.row_no_order_shop.view.tv_row_shop_list_tv_text_dynamic
import kotlinx.android.synthetic.main.row_shop_list_ma.view.ll_row_shop_dtls_ma_contact1

class AdapterVisitedInactivityShop(var mContext: Context, var mList:ArrayList<NoVisitedActivityInshop>, var isFromCollection:Boolean=false, var isFromVisit:Boolean=false):
    RecyclerView.Adapter<AdapterVisitedInactivityShop.CrossSellProductViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrossSellProductViewHolder {
        var v = LayoutInflater.from(mContext).inflate(R.layout.row_no_order_shop,parent,false)
        return CrossSellProductViewHolder(v)
    }

    override fun onBindViewHolder(holder: CrossSellProductViewHolder, position: Int) {
        holder.bindItems()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class CrossSellProductViewHolder(itemview: View): RecyclerView.ViewHolder(itemview){
        @SuppressLint("ResourceType")
        fun bindItems(){
            itemView.tv_row_shop_list_ma_name.text = mList.get(adapterPosition).shop_name.get(0).toString()
            itemView.tv_row_shop_name.text = mList.get(adapterPosition).shop_name
            itemView.tv_row_shop_phone_number.text = mList.get(adapterPosition).owner_contact_number
            itemView.tv_row_shop_list_ma_addr1.text = mList.get(adapterPosition).address


            itemView.tv_row_shop_phone_number.setOnClickListener {
                IntentActionable.initiatePhoneCall(mContext, mList.get(adapterPosition).owner_contact_number)
            }

            try{
                var typeName = AppDatabase.getDBInstance()?.shopTypeDao()?.getSingleType(mList.get(adapterPosition).type)!!.shoptype_name
                itemView.tv_row_shop_list_shop_type1.text =typeName
            }catch (ex:Exception){
                itemView.tv_row_shop_list_shop_type1.text ="N/A"
            }

            try{
                if(!mList.get(adapterPosition).owner_name.equals("")){
                    itemView.tv_row_shop_list_ma_shop_catagory_retailer1.text = mList.get(adapterPosition).owner_name
                }
                else{
                    itemView.tv_row_shop_list_ma_shop_catagory_retailer1.text = "N/A"
                }

            }catch (ex:Exception){
                itemView.tv_row_shop_list_ma_shop_catagory_retailer1.text = "N/A"
            }


                try{
                    itemView.tv_row_shop_list_tv_text_dynamic.text = "last visit date"
                    itemView.tv_row_shop_list_ma_shop_count.text =  AppUtils.getFormatedDateNew( mList.get(adapterPosition).visitedDate.split("T").get(0), "yyyy-mm-dd", "dd-mm-yyyy")
                }catch (ex:Exception){
                    itemView.tv_row_shop_list_shop_type1.text ="N/A"
                }






            /*if(adapterPosition%2 == 0){
                itemView.ll_row_pro_cross_root.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white))
            }else{
                itemView.ll_row_pro_cross_root.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lightest_light_new_gray))
            }*/

        }
    }

}