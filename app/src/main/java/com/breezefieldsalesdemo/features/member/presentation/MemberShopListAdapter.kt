package com.breezefieldsalesdemo.features.member.presentation

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.breezefieldsalesdemo.R
import com.breezefieldsalesdemo.app.AppDatabase
import com.breezefieldsalesdemo.app.Pref
import com.breezefieldsalesdemo.app.domain.AddShopDBModelEntity
import com.breezefieldsalesdemo.app.utils.AppUtils
import com.breezefieldsalesdemo.features.member.model.TeamShopListDataModel
import kotlinx.android.synthetic.main.inflate_member_shop_list.view.*
import kotlinx.android.synthetic.main.inflate_nearby_shops.view.visit_TV
import kotlinx.android.synthetic.main.inflate_nearby_shops.view.visit_icon

/**
 * Created by Saikat on 31-01-2020.
 */
/*class MemberShopListAdapter(private val context: Context, private val teamShopList: ArrayList<TeamShopListDataModel>, val listener: OnClickListener) :
        RecyclerView.Adapter<MemberShopListAdapter.MyViewHolder>()*/

class MemberShopListAdapter(private val context: Context, private val teamShopList: ArrayList<TeamShopListDataModel>, private val isVisitShop: Boolean,
                            private val listener: (TeamShopListDataModel) -> Unit, private val onOrderClick: (TeamShopListDataModel) -> Unit,
                            private val getListSize: (Int) -> Unit, private val onDamageClick: (TeamShopListDataModel) -> Unit,private val onQuotClick: (TeamShopListDataModel) -> Unit,
                            private val onHistoryClick: (AddShopDBModelEntity) -> Unit) : RecyclerView.Adapter<MemberShopListAdapter.MyViewHolder>(),
        Filterable {

    private val layoutInflater: LayoutInflater by lazy {
        LayoutInflater.from(context)
    }

    private var shopList: ArrayList<TeamShopListDataModel>? = null
    private var tempshopList: ArrayList<TeamShopListDataModel>? = null
    private var filtershopList: ArrayList<TeamShopListDataModel>? = null

    init {
        shopList = ArrayList()
        tempshopList = ArrayList()
        filtershopList = ArrayList()

        shopList?.addAll(teamShopList)
        tempshopList?.addAll(teamShopList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = layoutInflater.inflate(R.layout.inflate_member_shop_list, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(context, shopList!!, listener, isVisitShop, onOrderClick,onDamageClick,onQuotClick,onHistoryClick)
    }

    override fun getItemCount(): Int {
        return shopList?.size!!
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(context: Context, teamShopList: ArrayList<TeamShopListDataModel>, listener: (TeamShopListDataModel) -> Unit, visitShop: Boolean, onOrderClick: (TeamShopListDataModel) -> Unit,
                      onDamageClick: (TeamShopListDataModel) -> Unit,onQuotClick: (TeamShopListDataModel) -> Unit,
                      onHistoryClick: (AddShopDBModelEntity) -> Unit) {
            itemView.apply {
                myshop_name_TV.text = teamShopList[adapterPosition].shop_name
                myshop_address_TV.text = teamShopList[adapterPosition].shop_address
                tv_shop_contact_no.text = teamShopList[adapterPosition].shop_contact
                total_visited_value_TV.text = teamShopList[adapterPosition].total_visited

                //if (visitShop)
                visit_rl.visibility = View.VISIBLE
                /*else
                    visit_rl.visibility = View.GONE*/

                val shopType = AppDatabase.getDBInstance()?.shopTypeDao()?.getSingleType(teamShopList[adapterPosition].shop_type)

                if (shopType != null && !TextUtils.isEmpty(shopType.shoptype_name)) {
                    tv_shop_type.text = shopType.shoptype_name
                    ll_shop_type.visibility = View.VISIBLE
                } else
                    ll_shop_type.visibility = View.GONE

                if (!TextUtils.isEmpty(teamShopList[adapterPosition].last_visit_date))
                    last_visited_date_TV.text = AppUtils.changeAttendanceDateFormat(teamShopList[adapterPosition].last_visit_date)
                else
                    last_visited_date_TV.text = "N.A."

                val drawable = TextDrawable.builder()
                        .buildRoundRect(teamShopList[adapterPosition].shop_name.trim().toUpperCase().take(1), ColorGenerator.MATERIAL.randomColor, 120)

                shop_IV.setImageDrawable(drawable)

                val isPresent = AppDatabase.getDBInstance()!!.shopActivityDao().isShopActivityAvailable(teamShopList[adapterPosition].shop_id, AppUtils.getCurrentDateForShopActi())
                if (isPresent) {
                    visit_icon.visibility = View.VISIBLE
                    visit_TV.text = "Revisited Today"
                    visit_TV.setTextColor(ContextCompat.getColor(context, R.color.maroon))
                    visit_icon.setColorFilter(ContextCompat.getColor(context,R.color.maroon),android.graphics.PorterDuff.Mode.SRC_IN)

                    iconWrapper_rl.visibility = View.VISIBLE
                } else {
                    visit_icon.visibility = View.GONE

                    visit_TV.text = "Revisit Now "
                   visit_TV.setTextColor(ContextCompat.getColor(context, R.color.color_custom_green))


                    iconWrapper_rl.visibility = View.GONE
                }

                visit_rl.setOnClickListener {
                    if (!isPresent)
                        listener(teamShopList[adapterPosition])
                }

                if (Pref.isEntityCodeVisible) {
                    if (!TextUtils.isEmpty(teamShopList[adapterPosition].entity_code)) {
                        ll_shop_code.visibility = View.VISIBLE
                        tv_shop_code.text = teamShopList[adapterPosition].entity_code
                    } else
                        ll_shop_code.visibility = View.GONE
                } else
                    ll_shop_code.visibility = View.GONE

                if (Pref.isCustomerFeatureEnable) {
                    ll_dd_name.visibility = View.GONE

                    if (!TextUtils.isEmpty(teamShopList[adapterPosition].stage_id)) {
                        val stage = AppDatabase.getDBInstance()?.stageDao()?.getSingleType(teamShopList[adapterPosition].stage_id)

                        if (stage == null) {
                            tv_stage_header.visibility = View.GONE
                            tv_stage.visibility = View.GONE
                        } else {
                            tv_stage_header.visibility = View.VISIBLE
                            tv_stage.visibility = View.VISIBLE

                            tv_stage.text = stage.stage_name
                        }
                    } else {
                        tv_stage_header.visibility = View.GONE
                        tv_stage.visibility = View.GONE
                    }

                    if (!TextUtils.isEmpty(teamShopList[adapterPosition].funnel_stage_id)) {
                        val funnelStage = AppDatabase.getDBInstance()?.funnelStageDao()?.getSingleType(teamShopList[adapterPosition].funnel_stage_id)

                        if (funnelStage == null) {
                            tv_funnel_stage_header.visibility = View.GONE
                            itemView.tv_funnel_stage.visibility = View.GONE
                        } else {
                            tv_funnel_stage_header.visibility = View.VISIBLE
                            tv_funnel_stage.visibility = View.VISIBLE

                            tv_funnel_stage.text = funnelStage.funnel_stage_name
                        }
                    } else {
                        tv_funnel_stage_header.visibility = View.GONE
                        tv_funnel_stage.visibility = View.GONE
                    }
                } else {
                    tv_funnel_stage_header.visibility = View.GONE
                    tv_funnel_stage.visibility = View.GONE
                    tv_stage_header.visibility = View.GONE
                    tv_stage.visibility = View.GONE

                    when {
                        teamShopList[adapterPosition].shop_type == "1" -> {
                            //tv_shop_type.text = context.getString(R.string.shop_type)
                            ll_dd_name.visibility = View.VISIBLE

                            if (!TextUtils.isEmpty(teamShopList[adapterPosition].dd_name))
                                tv_dd_name.text = teamShopList[adapterPosition].dd_name
                            else
                                tv_dd_name.text = "N.A."

                        }
                        teamShopList[adapterPosition].shop_type == "2" -> {
                            //tv_shop_type.text = context.getString(R.string.pp_type)
                            ll_dd_name.visibility = View.GONE
                        }
                        teamShopList[adapterPosition].shop_type == "3" -> {
                            //tv_shop_type.text = context.getString(R.string.new_party_type)
                            ll_dd_name.visibility = View.GONE
                        }
                        teamShopList[adapterPosition].shop_type == "4" -> {
                            //tv_shop_type.text = context.getString(R.string.distributor_type)
                            ll_dd_name.visibility = View.GONE
                        }
                        teamShopList[adapterPosition].shop_type == "5" -> {
                            //tv_shop_type.text = context.getString(R.string.diamond_type)
                            ll_dd_name.visibility = View.VISIBLE

                            if (!TextUtils.isEmpty(teamShopList[adapterPosition].dd_name))
                                tv_dd_name.text = teamShopList[adapterPosition].dd_name
                            else
                                tv_dd_name.text = "N.A."
                        }
                        else -> {
                            ll_dd_name.visibility = View.GONE
                        }
                    }
                }

                if (Pref.isQuotationShow) {
                    order_view.visibility = View.VISIBLE
                    add_quot_ll.visibility = View.VISIBLE
                } else {
                    order_view.visibility = View.GONE
                    add_quot_ll.visibility = View.GONE
                }


                add_quot_ll.setOnClickListener {
                    onQuotClick(teamShopList[adapterPosition])
                }

                add_order_ll.setOnClickListener {
                    onOrderClick(teamShopList[adapterPosition])
                }

                if(Pref.IsFeedbackHistoryActivated){
                    iconWrapper_rl.visibility = View.VISIBLE
                    add_order_ll.visibility = View.GONE
                    add_quot_ll.visibility = View.GONE
                }
                else{
                    iconWrapper_rl.visibility = View.GONE
                    add_order_ll.visibility = View.GONE
                    add_quot_ll.visibility = View.GONE
                }
                history_vvview.visibility = View.GONE
                history_llll.setOnClickListener {
                    var obj: AddShopDBModelEntity = AddShopDBModelEntity()
                    obj.apply {
                        shop_id=teamShopList[adapterPosition].shop_id
                        shopName=teamShopList[adapterPosition].shop_name
                        address=teamShopList[adapterPosition].shop_address
                        pinCode=teamShopList[adapterPosition].shop_pincode
                        shopLat=teamShopList[adapterPosition].shop_lat!!.toDouble()
                        shopLong=teamShopList[adapterPosition].shop_long!!.toDouble()
                        ownerContactNumber=teamShopList[adapterPosition].shop_contact
                        totalVisitCount=teamShopList[adapterPosition].total_visited
                        lastVisitedDate = teamShopList[adapterPosition].last_visit_date
                        type = teamShopList[adapterPosition].shop_type
                        assigned_to_dd_id = teamShopList[adapterPosition].assign_to_dd_id
                        assigned_to_pp_id = teamShopList[adapterPosition].assign_to_pp_id
                        entity_code = teamShopList[adapterPosition].entity_code
                    }
                    onHistoryClick(obj)
                }

                if (Pref.IsAllowBreakageTrackingunderTeam) {
                    itemView.shop_damage_ll.visibility = View.VISIBLE
                    itemView.shop_damage_view.visibility = View.VISIBLE
                    itemView.iconWrapper_rl.visibility = View.VISIBLE
                }
                else {
                    itemView.shop_damage_ll.visibility = View.GONE
                    itemView.shop_damage_view.visibility = View.GONE
                    itemView.iconWrapper_rl.visibility = View.GONE
                }
                itemView.shop_damage_ll.setOnClickListener{
                    onDamageClick(teamShopList[adapterPosition])
                }


                ////new code

                itemView.order_view.visibility = View.GONE

                if (Pref.IsAllowBreakageTrackingunderTeam ||Pref.IsFeedbackHistoryActivated ||Pref.isQuotationShow ){
                    iconWrapper_rl.visibility = View.VISIBLE
                }else{
                    iconWrapper_rl.visibility = View.GONE
                }

                if (Pref.IsAllowBreakageTrackingunderTeam  ){
                    itemView.shop_damage_ll.visibility = View.VISIBLE
                    itemView.shop_damage_view.visibility = View.VISIBLE

                }else{
                    itemView.shop_damage_ll.visibility = View.GONE
                }

                if (Pref.IsFeedbackHistoryActivated ){
                    itemView.history_llll.visibility = View.VISIBLE
                    itemView.history_vvview.visibility = View.VISIBLE

                }else{
                    itemView.history_llll.visibility = View.GONE
                }

                if (Pref.isQuotationShow ){
                    itemView.add_quot_ll.visibility = View.VISIBLE
                }else{
                    itemView.add_quot_ll.visibility = View.GONE
                }



            }
        }
    }

    override fun getFilter(): Filter {
        return SearchFilter()
    }

    inner class SearchFilter : Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val results = FilterResults()

            filtershopList?.clear()

            tempshopList?.indices!!
                    .filter { tempshopList?.get(it)?.shop_name?.toLowerCase()?.contains(p0?.toString()?.toLowerCase()!!)!! }
                    .forEach { filtershopList?.add(tempshopList?.get(it)!!) }

            results.values = filtershopList
            results.count = filtershopList?.size!!

            return results
        }

        override fun publishResults(p0: CharSequence?, results: FilterResults?) {

            try {
                filtershopList = results?.values as ArrayList<TeamShopListDataModel>?
                shopList?.clear()
                val hashSet = HashSet<String>()
                if (filtershopList != null) {

                    filtershopList?.indices!!
                            .filter { hashSet.add(filtershopList?.get(it)?.shop_id!!) }
                            .forEach { shopList?.add(filtershopList?.get(it)!!) }

                    getListSize(shopList?.size!!)

                    notifyDataSetChanged()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun refreshList(teamShopList: ArrayList<TeamShopListDataModel>) {
        shopList?.clear()
        shopList?.addAll(teamShopList)

        tempshopList?.clear()
        tempshopList?.addAll(teamShopList)

        notifyDataSetChanged()
    }
}