package com.breezefieldsalesdemo.features.returnsOrder

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldsalesdemo.R
import com.breezefieldsalesdemo.app.AppDatabase
import com.breezefieldsalesdemo.app.Pref
import com.breezefieldsalesdemo.app.domain.ReturnDetailsEntity
import com.breezefieldsalesdemo.app.uiaction.IntentActionable
import com.breezefieldsalesdemo.app.utils.AppUtils
import com.breezefieldsalesdemo.base.presentation.BaseFragment
import com.breezefieldsalesdemo.widgets.AppCustomTextView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ViewCartReturnFragment : BaseFragment() {

    private lateinit var mContext: Context
    private lateinit var tv_total_order_value: AppCustomTextView
    private lateinit var tv_total_order_amount: AppCustomTextView
    private lateinit var rv_cart_list: RecyclerView
    private lateinit var tv_cancel: AppCustomTextView
    private lateinit var tv_continue: AppCustomTextView
    private lateinit var fab_add: FloatingActionButton
    private lateinit var rl_cart_main: RelativeLayout
    private var selectedItems = ArrayList<Int>()
    private lateinit var ll_btns: LinearLayout
    private var returnId = ""
    private lateinit var tv_no_data_available: AppCustomTextView
    private lateinit var ll_top: LinearLayout
    private lateinit var cart_view: View
    private lateinit var tv_order_id: AppCustomTextView
    private lateinit var tv_order_date: AppCustomTextView
    private lateinit var tv_shop_name: AppCustomTextView
    private lateinit var iv_call_icon: ImageView
    private lateinit var ll_patient_info: LinearLayout
    private lateinit var tv_patient_name: AppCustomTextView
    private lateinit var tv_patient_no: AppCustomTextView
    private lateinit var tv_patient_address: AppCustomTextView

    companion object {

        private var orderDetails: ReturnDetailsEntity? = null

        fun newInstance(objects: Any): ViewCartReturnFragment {
            val fragment = ViewCartReturnFragment()
            if (objects is ReturnDetailsEntity)
                orderDetails = objects
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        returnId = orderDetails?.return_id!!
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_cart_return, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        cart_view = view.findViewById(R.id.cart_view)
        cart_view.visibility = View.VISIBLE

        ll_top = view.findViewById(R.id.ll_top)
        ll_top.visibility = View.VISIBLE

        tv_total_order_value = view.findViewById(R.id.tv_total_order_value)
        tv_shop_name = view.findViewById(R.id.tv_shop_name)

        val shop = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopDetail(orderDetails?.shop_id)
        tv_shop_name.text = shop.shopName

        tv_order_id = view.findViewById(R.id.tv_order_id)
        tv_order_id.text = returnId

        tv_order_date = view.findViewById(R.id.tv_order_date)
        tv_order_date.text = AppUtils.convertDateTimeToCommonFormat(orderDetails?.date!!)

        ll_btns = view.findViewById(R.id.ll_btns)
        ll_btns.visibility = View.GONE

        tv_total_order_amount = view.findViewById(R.id.tv_total_order_amount)
        rv_cart_list = view.findViewById(R.id.rv_cart_list)
        tv_cancel = view.findViewById(R.id.tv_cancel)
        tv_continue = view.findViewById(R.id.tv_continue)
        fab_add = view.findViewById(R.id.fab_add)
        iv_call_icon = view.findViewById(R.id.iv_call_icon)
        tv_no_data_available = view.findViewById(R.id.tv_no_data_available)
        ll_patient_info = view.findViewById(R.id.ll_patient_info)
        tv_patient_name = view.findViewById(R.id.tv_patient_name)
        tv_patient_no = view.findViewById(R.id.tv_patient_no)
        tv_patient_address = view.findViewById(R.id.tv_patient_address)

        rl_cart_main = view.findViewById(R.id.rl_cart_main)
        rl_cart_main.setOnClickListener(null)

        rv_cart_list.layoutManager = LinearLayoutManager(mContext)

//        if (Pref.isPatientDetailsShowInOrder)
//            ll_patient_info.visibility = View.VISIBLE
//        else
//            ll_patient_info.visibility = View.GONE

        val list = AppDatabase.getDBInstance()!!.returnProductListDao().getDataAccordingToOrderId(returnId)


        if (list != null)
            tv_total_order_value.text = list.size.toString()

        if (list != null && list.isNotEmpty()) {
            rv_cart_list.adapter = ViewReturnCartAdapter(mContext, list)
            tv_no_data_available.visibility = View.GONE
        } else
            tv_no_data_available.visibility = View.VISIBLE


        Handler().postDelayed(Runnable {
            var totalAmount = 0.0
            for (i in list.indices) {
                totalAmount += list[i].total_price?.toDouble()!!
            }
            val totalPrice = String.format("%.2f", totalAmount.toFloat())
            tv_total_order_amount.text = totalPrice
        }, 200)


        if (!TextUtils.isEmpty(shop.ownerContactNumber) && AppUtils.isValidateMobile(shop.ownerContactNumber)) {
            iv_call_icon.visibility = View.VISIBLE

            if (shop.is_otp_verified.equals("true", ignoreCase = true)){
                iv_call_icon.setImageResource(R.drawable.ic_registered_shop_call_select_green)
            }
            else {
                iv_call_icon.setImageResource(R.drawable.ic_registered_shop_call_deselect)
            }

            iv_call_icon.setOnClickListener {
                IntentActionable.initiatePhoneCall(context, shop.ownerContactNumber)
            }
        }
        else
            iv_call_icon.visibility = View.GONE
    }

}