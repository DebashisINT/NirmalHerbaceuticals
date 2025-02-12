package com.breezefieldsalesdemo.features.viewPPDDStock

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.breezefieldsalesdemo.R
import com.breezefieldsalesdemo.app.domain.StockListEntity
import com.breezefieldsalesdemo.app.utils.AppUtils
import kotlinx.android.synthetic.main.inflate_view_stock_item.view.*

/**
 * Created by Saikat on 12-11-2018.
 */
class ViewStockAdapter(context: Context, list: List<StockListEntity>) : RecyclerView.Adapter<ViewStockAdapter.MyViewHolder>() {

    private val layoutInflater: LayoutInflater
    private var context: Context
    private var mList: List<StockListEntity>?

    init {
        layoutInflater = LayoutInflater.from(context)
        this.context = context
        mList = list
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(context, mList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = layoutInflater.inflate(R.layout.inflate_view_stock_item, parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mList?.size!!
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(context: Context, list: List<StockListEntity>?) {

            try {

                if (adapterPosition % 2 == 0)
                    itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.report_screen_bg))
                else
                    itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white))

                if (!TextUtils.isEmpty(list?.get(adapterPosition)?.current_date))
                    itemView.tv_stock_date.text = AppUtils.convertToCommonFormat(list?.get(adapterPosition)?.current_date!!)
                itemView.tv_stock_amount.text = "₹" + list?.get(adapterPosition)?.stock_value
                itemView.tv_mo_value.text = "₹" + list?.get(adapterPosition)?.mo

                /*itemView.tv_mo_value.visibility = View.GONE
                itemView.tv_co_value.visibility = View.GONE*/

                if (!TextUtils.isEmpty(list?.get(adapterPosition)?.co) && list?.get(adapterPosition)?.co != "0.00") {
                    itemView.tv_co_value.visibility = View.VISIBLE
                    itemView.tv_co_value.text = "₹" + list?.get(adapterPosition)?.co
                    itemView.tv_po_value.visibility = View.GONE
                } else if (!TextUtils.isEmpty(list?.get(adapterPosition)?.po) && list?.get(adapterPosition)?.po != "0.00") {
                    itemView.tv_po_value.visibility = View.VISIBLE
                    itemView.tv_po_value.text = "₹" + list?.get(adapterPosition)?.po
                    itemView.tv_co_value.visibility = View.GONE
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }
}