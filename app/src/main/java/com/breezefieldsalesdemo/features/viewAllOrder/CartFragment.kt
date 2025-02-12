package com.breezefieldsalesdemo.features.viewAllOrder

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.breezefieldsalesdemo.R
import com.breezefieldsalesdemo.app.AppDatabase
import com.breezefieldsalesdemo.app.Pref
import com.breezefieldsalesdemo.app.domain.ProductListEntity
import com.breezefieldsalesdemo.app.domain.ProductRateEntity
import com.breezefieldsalesdemo.app.utils.AppUtils
import com.breezefieldsalesdemo.app.utils.swipemenulayout.SwipeMenuRecyclerView
import com.breezefieldsalesdemo.base.presentation.BaseFragment
import com.breezefieldsalesdemo.features.addshop.presentation.AddShopFragment
import com.breezefieldsalesdemo.features.commondialog.presentation.CommonDialog
import com.breezefieldsalesdemo.features.commondialog.presentation.CommonDialogClickListener
import com.breezefieldsalesdemo.features.dashboard.presentation.DashboardActivity
import com.breezefieldsalesdemo.features.returnsOrder.ReturnTypeListFragment
import com.breezefieldsalesdemo.features.shopdetail.presentation.ShopDetailFragment
import com.breezefieldsalesdemo.widgets.AppCustomEditText
import com.breezefieldsalesdemo.widgets.AppCustomTextView
import kotlinx.android.synthetic.main.cart_adapter_body_layout_newchange.view.*
import kotlinx.android.synthetic.main.fragment_cart_new.*


/**
 * Created by Saikat on 09-11-2018.
 */
// 1.0  AppV 4.0.6  CartFragment  Saheli    25/01/2023 0025623 discount is editable work
class CartFragment : BaseFragment(), View.OnClickListener {

    private lateinit var mContext: Context
    private lateinit var tv_total_order_value: AppCustomTextView
    private lateinit var tv_total_order_amount: AppCustomTextView
    private lateinit var tv_total_order_amount_sc: AppCustomTextView
    private lateinit var ll_schemeRoot: LinearLayout
    private lateinit var rv_cart_list: SwipeMenuRecyclerView
    private lateinit var tv_cancel: AppCustomTextView
    private lateinit var tv_continue: AppCustomTextView
    private lateinit var fab_add: FloatingActionButton
    private lateinit var rl_cart_main: RelativeLayout
    private var selectedItems = ArrayList<Int>()
    private lateinit var tv_no_data_available: AppCustomTextView
    private lateinit var tv_stock_value: AppCustomTextView
    private lateinit var et_patient: AppCustomEditText
    private lateinit var et_address: AppCustomEditText
    private lateinit var et_phone: AppCustomEditText
    private lateinit var rl_patient: RelativeLayout
    private lateinit var rl_address: RelativeLayout
    private lateinit var rl_phone: RelativeLayout
    private lateinit var tv_mrp: AppCustomTextView
    private lateinit var rl_lab: RelativeLayout
    private lateinit var rl_emailaddress: RelativeLayout



    private lateinit var et_lab: AppCustomEditText
    private lateinit var et_emailaddress: AppCustomEditText

    private var remarks = ""
    private var imagePath = ""

    private lateinit var tv_discount: AppCustomTextView  //1.0  AppV 4.0.6  CartFragment 0025623 discount is editable work

    companion object {
        private var selectedProductList: ArrayList<ProductListEntity>? = null

        fun newInstance(objects: Any): CartFragment {
            val fragment = CartFragment()
            if (objects != null && objects is ArrayList<*>)
                selectedProductList = objects as ArrayList<ProductListEntity>
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_cart_new, container, false)
        initView(view)
        initClickListener()
        return view
    }

    private fun initView(view: View) {
        tv_total_order_value = view.findViewById(R.id.tv_total_order_value)

        if (selectedProductList != null)
            tv_total_order_value.text = selectedProductList?.size.toString()

        tv_total_order_amount = view.findViewById(R.id.tv_total_order_amount)
        ll_schemeRoot = view.findViewById(R.id.ll_frag_cart_new_scheme_root)
        tv_total_order_amount_sc = view.findViewById(R.id.tv_total_scheme_amount)
        rv_cart_list = view.findViewById(R.id.rv_cart_list)
        tv_cancel = view.findViewById(R.id.tv_cancel)
        tv_continue = view.findViewById(R.id.tv_continue)
        fab_add = view.findViewById(R.id.fab_add)
        rl_cart_main = view.findViewById(R.id.rl_cart_main)
        tv_no_data_available = view.findViewById(R.id.tv_no_data_available)
        tv_stock_value = view.findViewById(R.id.tv_stock_value)
        et_patient = view.findViewById(R.id.et_patient)
        et_address = view.findViewById(R.id.et_address)
        et_phone = view.findViewById(R.id.et_phone)
        rl_patient = view.findViewById(R.id.rl_patient)
        rl_address = view.findViewById(R.id.rl_address)
        rl_phone = view.findViewById(R.id.rl_phone)

        tv_mrp = view.findViewById(R.id.tv_mrp)
        rl_lab = view.findViewById(R.id.rl_lab)
        rl_emailaddress =  view.findViewById(R.id.rl_emailaddress)
        et_lab = view.findViewById(R.id.et_lab)
        et_emailaddress = view.findViewById(R.id.et_emailaddress)

        tv_discount = view.findViewById(R.id.tv_frag_new_cart_discount)//1.0  AppV 4.0.6  CartFragment 0025623 discount is editable work




        rv_cart_list.layoutManager = LinearLayoutManager(mContext)

        if (selectedProductList != null && selectedProductList?.size!! > 0) {
            initAdapter()
            tv_no_data_available.visibility = View.GONE
        } else
            tv_no_data_available.visibility = View.VISIBLE

        Handler().postDelayed(Runnable {
            var totalAmount = 0.0

            for (i in (mContext as DashboardActivity).totalPrice.indices) {
                totalAmount += (mContext as DashboardActivity).totalPrice[i]
            }
            //val totalPrice = DecimalFormat("##.##").format(totalAmount)
            val totalPrice = String.format("%.2f", totalAmount.toFloat())
            tv_total_order_amount.text = totalPrice
        }, 200)

        Handler().postDelayed(Runnable {
            var totalScAmount = 0.0

            for (i in (mContext as DashboardActivity).totalScPrice.indices) {
                totalScAmount += (mContext as DashboardActivity).totalScPrice[i]
            }
            //val totalPrice = DecimalFormat("##.##").format(totalAmount)
            val totalScPrice = String.format("%.2f", totalScAmount.toFloat())
            tv_total_order_amount_sc.text = totalScPrice
        }, 200)

        if (AppUtils.stockStatus == 1)
            tv_stock_value.text = getString(R.string.total_stock_value_with_colon)
        else {
            if (Pref.isPatientDetailsShowInCollection) {
                rl_patient.visibility = View.VISIBLE
                rl_address.visibility = View.VISIBLE
                rl_phone.visibility = View.VISIBLE
                //added new lines
                rl_lab.visibility = View.VISIBLE
                rl_emailaddress.visibility = View.VISIBLE
            } else {
                rl_patient.visibility = View.GONE
                rl_address.visibility = View.GONE
                rl_phone.visibility = View.GONE
                //added new lines
                rl_lab.visibility = View.GONE
                rl_emailaddress.visibility = View.GONE
            }
        }
        if(Pref.MRPInOrderGlobal && Pref.MRPInOrder){
            tv_mrp.visibility = View.VISIBLE
        }
        else{
            tv_mrp.visibility = View.GONE
        }

        //1.0  AppV 4.0.6  CartFragment 0025623 discount is editable work
        if(Pref.IsDiscountEditableInOrder){
            tv_discount.visibility = View.VISIBLE
        }
        else{
            tv_discount.visibility = View.GONE
        }

        if(AppUtils.stockStatus != 0){
            ll_schemeRoot.visibility=View.GONE
        }
    }

    private fun initAdapter() {

        if (!Pref.IsnewleadtypeforRuby) {
            ll_schemeRoot.visibility = View.GONE
        } else {
            ll_schemeRoot.visibility = View.VISIBLE
        }

        rv_cart_list.setItemViewCacheSize(selectedProductList?.size!!)
        rv_cart_list.adapter = CartAdapter(mContext, selectedProductList, object : CartAdapter.OnProductClickListener {
            override fun onDelete(adapterPosition: Int) {
                showDeleteAlert(adapterPosition)
            }

            override fun onEdit(adapterPosition: Int) {

//                AddProductRateDialog.getInstance(selectedProductList?.get(adapterPosition), true, selectedProductList?.get(adapterPosition)?.product_name!!, true,
//                        adapterPosition, object : AddProductRateDialog.AddOrderClickLisneter {
//                    override fun onUpdateClick(amount: String, desc: String, collection: String) {
//                        /* if (!TextUtils.isEmpty(product.particulars))
//                             productName = product.particulars!!
//                         productList.removeAt(adapterPosition)*/
//                        (mContext as DashboardActivity).qtyList[adapterPosition] = desc
//                        (mContext as DashboardActivity).rateList[adapterPosition] = amount.toDouble().toString()
//
//                        val totalPrice = String.format("%.2f", (amount.toFloat() * desc.toInt()))
//                        (mContext as DashboardActivity).totalPrice[adapterPosition] = totalPrice.toDouble()
//
//                        //(mContext as DashboardActivity).loadFragment(FragType.CartFragment, true, selectedProductList)
//                        initAdapter()
//
//                        if (selectedProductList != null)
//                            tv_total_order_value.text = selectedProductList?.size.toString()
//
//                        Handler().postDelayed(Runnable {
//                            var totalAmount = 0.0
//
//                            for (i in (mContext as DashboardActivity).totalPrice.indices) {
//                                totalAmount += (mContext as DashboardActivity).totalPrice[i]
//                            }
//
//                            tv_total_order_amount.text = totalAmount.toString()
//                        }, 200)
//                    }
//                }).show((mContext as DashboardActivity).supportFragmentManager, "AddProductRateDialog")

                try {
                    if (!TextUtils.isEmpty((mContext as DashboardActivity).rateList[adapterPosition]) && !TextUtils.isEmpty((mContext as DashboardActivity).qtyList[adapterPosition])) {

                        //val totalPrice = String.format("%.2f", ((mContext as DashboardActivity).rateList[adapterPosition].toDouble() * (mContext as DashboardActivity).qtyList[adapterPosition].toInt()))
                        val totalPrice = String.format("%.2f", ((mContext as DashboardActivity).rateList[adapterPosition].toDouble() * (mContext as DashboardActivity).qtyList[adapterPosition].toDouble()))
                        (mContext as DashboardActivity).totalPrice[adapterPosition] = totalPrice.toDouble()

                    } else
                        (mContext as DashboardActivity).totalPrice[adapterPosition] = 0.00
                } catch (e: Exception) {
                    e.printStackTrace()
                    (mContext as DashboardActivity).totalPrice[adapterPosition] = 0.00
                }

                if (selectedProductList != null)
                    tv_total_order_value.text = selectedProductList?.size.toString()

                Handler().postDelayed(Runnable {
                    var totalAmount = 0.00
                    for (i in (mContext as DashboardActivity).totalPrice.indices) {
                        totalAmount += (mContext as DashboardActivity).totalPrice[i]
                    }
                    val finalTotalAmount = String.format("%.2f", totalAmount)
                    tv_total_order_amount.text = finalTotalAmount
                }, 200)


            }

            override fun onEditSchema(adapterPosition: Int) {

                try {
                    if (!TextUtils.isEmpty((mContext as DashboardActivity).schemarateList[adapterPosition]) && !TextUtils.isEmpty((mContext as DashboardActivity).schemaqtyList[adapterPosition])) {
                        val totalSc = String.format("%.2f", (mContext as DashboardActivity).schemaqtyList[adapterPosition].toInt() * (mContext as DashboardActivity).schemarateList[adapterPosition].toDouble())
                        (mContext as DashboardActivity).totalScPrice[adapterPosition] = totalSc.toDouble()
                    } else
                        (mContext as DashboardActivity).totalScPrice[adapterPosition] = 0.00
                } catch (e: Exception) {
                    e.printStackTrace()
                    (mContext as DashboardActivity).totalScPrice[adapterPosition] = 0.00
                }

                Handler().postDelayed(Runnable {
                    var totalScAmount = 0.00
                    for (i in (mContext as DashboardActivity).totalScPrice.indices) {
                        totalScAmount += (mContext as DashboardActivity).totalScPrice[i]
                    }
                    val finalTotalScAmount = String.format("%.2f", totalScAmount)
                    tv_total_order_amount_sc.text = finalTotalScAmount
                }, 200)

            }


            override fun onProductClick(brand: ProductListEntity?, adapterPosition: Int, isSelected: Boolean) {
                if (isSelected)
                    selectedItems.add(adapterPosition)
                else {
                    try {
                        selectedItems.remove(adapterPosition)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        })
    }

    private fun showDeleteAlert(adapterPosition: Int) {

        CommonDialog.getInstance("Delete Alert", "Do you really want to delete this product?", getString(R.string.cancel), getString(R.string.ok), object : CommonDialogClickListener {
            override fun onLeftClick() {
            }

            override fun onRightClick(editableData: String) {
                selectedProductList?.removeAt(adapterPosition)
                (mContext as DashboardActivity).tv_cart_count.text = selectedProductList?.size.toString()
                (mContext as DashboardActivity).qtyList.removeAt(adapterPosition)
                (mContext as DashboardActivity).rateList.removeAt(adapterPosition)
                (mContext as DashboardActivity).totalPrice.removeAt(adapterPosition)


                /*28-12-2021*/
                (mContext as DashboardActivity).schemarateList.removeAt(adapterPosition)
                (mContext as DashboardActivity).schemaqtyList.removeAt(adapterPosition)

                initAdapter()

                if (selectedProductList != null)
                    tv_total_order_value.text = selectedProductList?.size.toString()

                Handler().postDelayed(Runnable {
                    var totalAmount = 0.0

                    for (i in (mContext as DashboardActivity).totalPrice.indices) {
                        totalAmount += (mContext as DashboardActivity).totalPrice[i]
                    }

                    tv_total_order_amount.text = totalAmount.toString()
                }, 200)

                AppUtils.isAllSelect = false

                Handler().postDelayed(Runnable {
                    if (selectedProductList == null || selectedProductList?.size!! == 0) {
                        (mContext as DashboardActivity).onBackPressed()
                        (mContext as DashboardActivity).tv_cart_count.visibility = View.GONE
                    }
                }, 500)
            }

        }).show((mContext as DashboardActivity).supportFragmentManager, "")

    }


    private fun initClickListener() {
        tv_cancel.setOnClickListener(this)
        tv_continue.setOnClickListener(this)
        fab_add.setOnClickListener(this)
        rl_cart_main.setOnClickListener(null)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_cancel -> {
                showCancelAlert()
            }

            R.id.tv_continue -> {
                if (selectedProductList != null && selectedProductList?.size!! > 0) {
                    val list = (mContext as DashboardActivity).totalPrice
                    val listSc = (mContext as DashboardActivity).totalScPrice

                    (mContext as DashboardActivity).onBackPressed()
                    if ((mContext as DashboardActivity).getFragment() is OrderTypeListFragment)
                        ((mContext as DashboardActivity).getFragment() as OrderTypeListFragment).saveOrder(tv_total_order_amount.text.toString().trim(),
                                selectedProductList, list, remarks, imagePath, et_patient.text.toString().trim(), et_address.text.toString().trim(), et_phone.text.toString().trim(),tv_total_order_amount_sc.text.toString().trim(),listSc,
                        et_lab.text.toString().trim(),et_emailaddress.text.toString().trim())
                } else
                    (mContext as DashboardActivity).showSnackMessage("Please select a product first")
            }

            R.id.fab_add -> {
                (mContext as DashboardActivity).onBackPressed()
            }
        }
    }


    private fun showCancelAlert() {

        CommonDialog.getInstance("Cancel Alert", "Do you really want to cancel this order?", getString(R.string.cancel), getString(R.string.ok), object : CommonDialogClickListener {
            override fun onLeftClick() {
            }

            override fun onRightClick(editableData: String) {
                (mContext as DashboardActivity).isShowAlert = false
                (mContext as DashboardActivity).onBackPressed()
                (mContext as DashboardActivity).onBackPressed()
            }

        }).show((mContext as DashboardActivity).supportFragmentManager, "")

    }


    fun deleteProducts() {
        /*try {
            if (selectedItems.size > 0) {
                for (i in selectedItems.indices) {
                    selectedProductList?.removeAt(selectedItems[i])
                    (mContext as DashboardActivity).totalPrice.removeAt(selectedItems[i])
                }
                initAdapter()


                var totalAmount = 0
                for (i in (mContext as DashboardActivity).totalPrice.indices) {
                    totalAmount += (mContext as DashboardActivity).totalPrice[i]
                }

                tv_total_order_amount.text = totalAmount.toString()

                tv_total_order_value.text = selectedProductList?.size.toString()

            } else
                (mContext as DashboardActivity).showSnackMessage("Please select atleast any product")
        }
        catch (e: Exception) {
            e.printStackTrace()
        }*/
    }

    fun onConfirmClick() {
        AppUtils.hideSoftKeyboard(mContext as Activity)

        if (!Pref.isShowAllProduct && AppUtils.stockStatus == 0) {
            val qtyList = (mContext as DashboardActivity).qtyList
            val rateList = (mContext as DashboardActivity).rateList

            val qtyschemeList = (mContext as DashboardActivity).schemaqtyList
            val rateschemeList = (mContext as DashboardActivity).schemarateList

            for (i in rateList.indices) {

                if (TextUtils.isEmpty(rateList[i])) {
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.enter_rate))
                    return
                }

                if (rateList[i].endsWith(".")) {
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.enter_valid_rate))
                    return
                }

                if (!Pref.isRateNotEditable) {
                    /*if (rateList[i].toDouble() == 0.00 || rateList[i].toInt() == 0) {
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.enter_valid_rate))
                        return
                    }*/
                    try {
                        if (rateList[i].toDouble() == 0.00) {
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.enter_valid_rate))
                            return
                        }
                    } catch (e: NumberFormatException) {
                        e.printStackTrace()

                        if (rateList[i].toInt() == 0) {
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.enter_valid_rate))
                            return
                        }
                    }

                } else {
                    if (Pref.isRateOnline) {
                        val list = AppUtils.loadSharedPreferencesProductRateList(mContext)
                        if (list == null || list.size == 0) {
                            if (!TextUtils.isEmpty(rateList[i])) {
                                try {
                                    if (rateList[i].toDouble() == 0.00) {
                                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.enter_valid_rate))
                                        return
                                    }
                                } catch (e: NumberFormatException) {
                                    e.printStackTrace()

                                    if (rateList[i].toInt() == 0) {
                                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.enter_valid_rate))
                                        return
                                    }
                                }
                            }
                        }
                    } else {
                        val list = AppDatabase.getDBInstance()?.productRateDao()?.getAll() as ArrayList<ProductRateEntity>?
                        if (list == null || list.size == 0) {
                            if (!TextUtils.isEmpty(rateList[i])) {
                                try {
                                    if (rateList[i].toDouble() == 0.00) {
                                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.enter_valid_rate))
                                        return
                                    }
                                } catch (e: NumberFormatException) {
                                    e.printStackTrace()

                                    if (rateList[i].toInt() == 0) {
                                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.enter_valid_rate))
                                        return
                                    }
                                }
                            }
                        }
                    }
                }
            }

            for (i in qtyList.indices) {

                if (TextUtils.isEmpty(qtyList[i])) {
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.enter_qty))
                    return
                }

                if (qtyList[i] < "1") {
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.enter_valid_qty))
                    return
                }
            }
        } else {
            val qtyList = ArrayList<String>()
            val rateList = ArrayList<String>()
            val totalPriceList = ArrayList<Double>()



            val qtyschemeList = ArrayList<String>()
            val rateschemeList = ArrayList<String>()
            val totalschemeList = ArrayList<Double>()

            val tempschemeQtyList = ArrayList<String>()
            val tempschemeRateList = ArrayList<String>()
            val tempschemePriceList = ArrayList<Double>()

            val tempQtyList = ArrayList<String>()
            val tempRateList = ArrayList<String>()
            val tempPriceList = ArrayList<Double>()

            val mrpList = ArrayList<String>()
            val tempMrpList = ArrayList<String>()

            qtyList.addAll((mContext as DashboardActivity).qtyList)
            rateList.addAll((mContext as DashboardActivity).rateList)
            totalPriceList.addAll((mContext as DashboardActivity).totalPrice)
            tempQtyList.addAll((mContext as DashboardActivity).qtyList)
            tempRateList.addAll((mContext as DashboardActivity).rateList)
            tempPriceList.addAll((mContext as DashboardActivity).totalPrice)


            mrpList.addAll((mContext as DashboardActivity).mrpList)
            qtyschemeList.addAll((mContext as DashboardActivity).schemaqtyList)
            rateschemeList.addAll((mContext as DashboardActivity).schemarateList)
            totalschemeList.addAll((mContext as DashboardActivity).totalScPrice)

            tempschemeQtyList.addAll((mContext as DashboardActivity).schemaqtyList)
            tempschemeRateList.addAll((mContext as DashboardActivity).schemarateList)
            tempschemePriceList.addAll((mContext as DashboardActivity).totalScPrice)
            mrpList.addAll((mContext as DashboardActivity).mrpList)
            tempMrpList.addAll((mContext as DashboardActivity).mrpList)


            for (i in rateList.indices) {
                if (rateList[i].endsWith(".")) {
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.enter_valid_rate))
                    return
                }

                else if (rateList[i].toDouble() == 0.00) {
                    if (AppUtils.stockStatus == 0)
                        tempRateList.remove(rateList[i])
                }
            }

            for (i in qtyList.indices) {
                //if (qtyList[i].length > 1 && qtyList[i].startsWith("0")) {
                if (qtyList[i].length > 1 && qtyList[i].equals("0")) {
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.enter_valid_qty))
                    return
                //} else if (qtyList[i].toInt() == 0) {
                } else if (qtyList[i].toDouble() == 0.0) {
                    tempQtyList.remove(qtyList[i])
                }
            }

            if (AppUtils.stockStatus == 0) {

                if (tempQtyList.size == 0 || tempQtyList.size < tempRateList.size) {
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.enter_qty))
                    return
                }

                if (tempRateList.size == 0 || tempQtyList.size > tempRateList.size) {
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.enter_rate))
                    return
                }

                if (tv_total_order_amount.text.toString().trim() == "0.00" || tv_total_order_amount.text.toString().trim() == "0.0" ||
                        tv_total_order_amount.text.toString().trim() == "0") {
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.error_enter_values))
                    return
                }

                for (i in totalPriceList.indices) {
                    if (totalPriceList[i] == 0.0 || totalPriceList[i] == 0.00) {
                        tempPriceList.remove(totalPriceList[i])
                    }
                }

                if (tempPriceList.size == 0 || tempPriceList.size != tempRateList.size || tempPriceList.size != tempQtyList.size) {
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.error_enter_values))
                    return
                }
            } else {
                if (tempQtyList.size == 0) {
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.enter_qty))
                    return
                }

                try {
                    for (i in tempRateList.indices) {
                        Log.e("cart", "index======> $i")
                        Log.e("cart", "rate========> " + tempRateList[i])
                        Log.e("cart", "qty========> " + (mContext as DashboardActivity).qtyList[i])

                        if (tempRateList[i] != "0.00" && (mContext as DashboardActivity).qtyList[i] == "0") {
                            Log.e("cart", "========" + getString(R.string.enter_qty) + "========")
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.enter_qty))
                            return
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()

                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.enter_qty))
                    return
                }
            }
        }
        if (selectedProductList != null && selectedProductList?.size!! > 0) {
            /*val list = (mContext as DashboardActivity).totalPrice
            (mContext as DashboardActivity).onBackPressed()
            if ((mContext as DashboardActivity).getFragment() is OrderTypeListFragment)
                ((mContext as DashboardActivity).getFragment() as OrderTypeListFragment).saveOrder(tv_total_order_amount.text.toString().trim(),
                        selectedProductList, list)*/

            if (AppUtils.stockStatus == 1)
                showCheckAlert("Stock Confirmation", "Do you want to recheck the stock?")
            else
                showCheckAlert("Order Confirmation", "Do you want to recheck the order?")

        } else
            (mContext as DashboardActivity).showSnackMessage("Please select a product first")
    }

    private fun showCheckAlert(header: String, title: String) {
        CommonDialog.getInstance(header, title, getString(R.string.no), getString(R.string.yes), false, object : CommonDialogClickListener {
            override fun onLeftClick() {
                if (AppUtils.stockStatus == 0) {
                    if (!Pref.isShowOrderRemarks && !Pref.isShowOrderSignature)
                        saveData()
                    else
                        showRemarksAlert()
                } else
                    saveData()
            }
            
            override fun onRightClick(editableData: String) {
            }
        }).show((mContext as DashboardActivity).supportFragmentManager, "")
    }

    private fun showRemarksAlert() {
        AddRemarksSignDialog.getInstance(remarks, imagePath, { remark, imgPath ->
            remarks = remark
            imagePath = imgPath
            saveData()
        }, {
            saveData()
        }).show((mContext as DashboardActivity).supportFragmentManager, "")
    }

    private fun saveData() {
        val list = (mContext as DashboardActivity).totalPrice
        val listSc = (mContext as DashboardActivity).totalScPrice
        (mContext as DashboardActivity).onBackPressed()
        if ((mContext as DashboardActivity).getFragment() is OrderTypeListFragment) {

            ShopDetailFragment.isOrderEntryPressed = false
            AddShopFragment.isOrderEntryPressed = false

            if (AppUtils.stockStatus == 0) {
                ((mContext as DashboardActivity).getFragment() as OrderTypeListFragment).saveOrder(tv_total_order_amount.text.toString().trim(),
                        selectedProductList, list, remarks, imagePath, et_patient.text.toString().trim(), et_address.text.toString().trim(), et_phone.text.toString().trim(),tv_total_order_amount_sc.text.toString().trim(),listSc,
                        et_lab.text.toString().trim(),et_emailaddress.text.toString().trim())
            } else if (AppUtils.stockStatus == 1) {
                ((mContext as DashboardActivity).getFragment() as OrderTypeListFragment).saveStock(tv_total_order_amount.text.toString().trim(),
                        selectedProductList, list)
            }
        }

    }
}