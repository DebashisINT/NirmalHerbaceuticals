package com.breezefieldsalesdemo.features.timesheet.presentation

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.breezefieldsalesdemo.R
import com.breezefieldsalesdemo.app.domain.ClientListEntity
import com.breezefieldsalesdemo.features.timesheet.model.TimeSheetClientDataModel
import com.breezefieldsalesdemo.widgets.AppCustomTextView
import java.util.ArrayList
import java.util.HashSet

/**
 * Created by Saikat on 07-Apr-20.
 */
class ClientAdapter(private val context: Context, private val clientList: ArrayList<ClientListEntity>,
                    private val onItemClickListener: (ClientListEntity) -> Unit) : RecyclerView.Adapter<ClientAdapter.ViewHolder>(), Filterable {

    private val inflater: LayoutInflater by lazy {
        LayoutInflater.from(context)
    }

    private var tempList: ArrayList<ClientListEntity>
    private var fileteredList: ArrayList<ClientListEntity>
    private var mClientList: ArrayList<ClientListEntity>

    init {
        tempList = ArrayList()
        fileteredList = ArrayList()
        mClientList = ArrayList()

        tempList.addAll(clientList)
        mClientList.addAll(clientList)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //val v = inflater.inflate(R.layout.inflate_month_item, parent, false)
        val v = inflater.inflate(R.layout.exp_popup_window_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == mClientList.size - 1)
            holder.view.visibility = View.GONE
        else
            holder.view.visibility = View.VISIBLE


        holder.list_item_tv.text = mClientList[position].client_name
        holder.tv_phn_no.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return mClientList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)/*, View.OnClickListener*/ {

        /*internal var tv_month: AppCustomTextView
        internal var view: View
        internal var ll_shop_details: LinearLayout
        internal var tv_shop_name: AppCustomTextView
        internal var tv_shop_area: AppCustomTextView*/

        var list_item_tv: AppCustomTextView
        var tv_phn_no: AppCustomTextView
        var view: View

        init {
            /*tv_month = itemView.findViewById<View>(R.id.tv_month) as AppCustomTextView
            view = itemView.findViewById(R.id.view) as View
            ll_shop_details = itemView.find(R.id.ll_shop_details)
            tv_shop_name = itemView.find(R.id.tv_shop_name)
            tv_shop_area = itemView.find(R.id.tv_shop_area)

            tv_month.setOnClickListener(this)
            ll_shop_details.setOnClickListener(this)*/

            list_item_tv = itemView.findViewById(R.id.list_item_tv)
            tv_phn_no = itemView.findViewById(R.id.tv_phn_no)
            view = itemView.findViewById(R.id.view)

            itemView.setOnClickListener {
                onItemClickListener(mClientList[adapterPosition])
            }
        }

        /*override fun onClick(v: View) {
            when (v.id) {
                R.id.tv_month -> onItemClickListener.onItemClick(tempList[adapterPosition])

                R.id.ll_shop_details -> onItemClickListener.onItemClick(tempList[adapterPosition])
            }
        }*/
    }

    /*interface OnItemClickListener {
        fun onItemClick(customer: CustomerDataModel)
    }*/

    override fun getFilter(): Filter {
        return SearchFilter()
    }

    inner class SearchFilter : Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val results = FilterResults()

            fileteredList.clear()

            tempList.indices
                    .filter { tempList.get(it).client_name?.toLowerCase()?.contains(p0?.toString()?.toLowerCase()!!)!! }
                    .forEach { fileteredList.add(tempList.get(it)) }

            results.values = fileteredList
            results.count = fileteredList.size

            return results
        }

        override fun publishResults(p0: CharSequence?, results: FilterResults?) {

            try {
                fileteredList = results?.values as ArrayList<ClientListEntity>
                mClientList.clear()
                val hashSet = HashSet<String>()
                if (fileteredList != null) {

                    fileteredList.indices
                            .filter { hashSet.add(fileteredList[it].client_id!!) }
                            .forEach { mClientList.add(fileteredList[it]) }

                    notifyDataSetChanged()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}