package com.breezefieldsalesdemo.features.mylearning

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldsalesdemo.R
import com.breezefieldsalesdemo.features.contacts.CustomDataLms
import kotlinx.android.synthetic.main.row_generic_dialog.view.tv_row_generic_name


class AdapterGenericDialogLMS(var mContext: Context, var genericList:ArrayList<CustomDataLms>, var listner:onCLick) :
        RecyclerView.Adapter<AdapterGenericDialogLMS.GenericDialogViewHolder>(),Filterable{

    private var arrayList_Bean: ArrayList<CustomDataLms>? = ArrayList()
    private var arrayList_Name: ArrayList<CustomDataLms>? = ArrayList()
    private var valueFilter: ValueFilter? = null

    init {
        arrayList_Bean?.addAll(genericList)
        arrayList_Name?.addAll(genericList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericDialogViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.row_generic_dialog,parent,false)
        return GenericDialogViewHolder(view)
    }

    override fun onBindViewHolder(holder: GenericDialogViewHolder, position: Int) {
        holder.bindItems()
    }

    override fun getItemCount(): Int {
        return arrayList_Name!!.size
    }

    inner class GenericDialogViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bindItems(){
            try {
                itemView.apply {
                    tv_row_generic_name.text = arrayList_Name!!.get(adapterPosition).name
                    tv_row_generic_name.setOnClickListener {
                        listner.onclick(arrayList_Name!!.get(adapterPosition))
                    }
                }
            }catch (ex:Exception){
                ex.printStackTrace()
            }
        }
    }

    inner class ValueFilter : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filterResults = FilterResults()
            if (constraint != null && constraint.length > 0) {
                val arrayList_filter: ArrayList<CustomDataLms> = ArrayList()
                for (i in 0..genericList!!.size-1) {
                    if (genericList!!.get(i).name!!.contains(constraint.toString(),ignoreCase = true)) {
                        arrayList_filter.add(CustomDataLms(genericList!!.get(i).id,genericList!!.get(i).name))
                    }
                }
                filterResults.count = arrayList_filter!!.size
                filterResults.values = arrayList_filter
            } else {
                filterResults.count = arrayList_Bean!!.size
                filterResults.values = arrayList_Bean
            }
            return filterResults
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            arrayList_Name = results.values as ArrayList<CustomDataLms>
            notifyDataSetChanged()
        }
    }

    interface onCLick{
        fun onclick(obj:CustomDataLms)
    }

    override fun getFilter(): Filter {
        if (valueFilter == null) {
            valueFilter = ValueFilter()
        }
        return valueFilter as ValueFilter
    }

}