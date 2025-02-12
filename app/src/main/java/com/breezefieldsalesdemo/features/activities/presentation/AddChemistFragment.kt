package com.breezefieldsalesdemo.features.activities.presentation

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.breezefieldsalesdemo.R
import com.breezefieldsalesdemo.app.AppDatabase
import com.breezefieldsalesdemo.app.NetworkConstant
import com.breezefieldsalesdemo.app.Pref
import com.breezefieldsalesdemo.app.domain.AddChemistEntity
import com.breezefieldsalesdemo.app.domain.AddChemistProductListEntity
import com.breezefieldsalesdemo.app.domain.AddShopDBModelEntity
import com.breezefieldsalesdemo.app.utils.AppUtils
import com.breezefieldsalesdemo.app.utils.InputFilterDecimal
import com.breezefieldsalesdemo.base.BaseResponse
import com.breezefieldsalesdemo.base.presentation.BaseActivity
import com.breezefieldsalesdemo.base.presentation.BaseFragment
import com.breezefieldsalesdemo.features.activities.api.ActivityRepoProvider
import com.breezefieldsalesdemo.features.activities.model.AddChemistProductModel
import com.breezefieldsalesdemo.features.activities.model.AddChemistVisitInputModel
import com.breezefieldsalesdemo.features.activities.model.ProductListModel
import com.breezefieldsalesdemo.features.dashboard.presentation.DashboardActivity
import com.breezefieldsalesdemo.widgets.AppCustomEditText
import com.breezefieldsalesdemo.widgets.AppCustomTextView

import com.pnikosis.materialishprogress.ProgressWheel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Saikat on 07-01-2020.
 */
class AddChemistFragment : BaseFragment(), View.OnClickListener {

    private lateinit var mContext: Context

    private lateinit var ic_search_icon: AppCompatImageView
    private lateinit var et_search: AppCustomEditText
    private lateinit var rv_search_list: RecyclerView
    private lateinit var ll_yes: LinearLayout
    private lateinit var iv_yes: ImageView
    private lateinit var ll_no: LinearLayout
    private lateinit var iv_no: ImageView
    private lateinit var ll_pob_product: LinearLayout
    private lateinit var ic_pob_search_icon: AppCompatImageView
    private lateinit var et_search_pob: AppCustomEditText
    private lateinit var rv_pob_search_list: RecyclerView
    private lateinit var et_volume: AppCustomEditText
    private lateinit var et_remarks: AppCustomEditText
    private lateinit var tv_visit_date: AppCustomTextView
    private lateinit var et_remarks_mr: AppCustomEditText
    private lateinit var tv_save: AppCustomTextView
    private lateinit var progress_wheel: ProgressWheel
    private lateinit var ic_pob_close_icon: AppCompatImageView
    private lateinit var ic_close_icon: AppCompatImageView
    private lateinit var rl_add_chemist_main: RelativeLayout
    private lateinit var tv_pod_product_count: AppCustomTextView
    private lateinit var tv_product_count: AppCustomTextView

    private var pobProductList: ArrayList<ProductListModel>? = null
    private var productAdapter: AddChemistProductAdapter? = null
    private var pobProductAdapter: AddChemistPobProductAdapter? = null
    private var productList: ArrayList<ProductListModel>? = null
    private var selectedProductList: ArrayList<ProductListModel>? = null
    private var selectedPobProductList: ArrayList<ProductListModel>? = null

    private var myCalendar = Calendar.getInstance(Locale.ENGLISH)
    private var nextDate = ""

    companion object {

        var mAddShopDataObj: AddShopDBModelEntity? = null

        fun newInstance(objects: Any): AddChemistFragment {
            val fragment = AddChemistFragment()

            if (!TextUtils.isEmpty(objects.toString())) {
                if (objects is AddShopDBModelEntity) {
                    mAddShopDataObj = objects
                }
            }

            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context

        productList = ArrayList()
        pobProductList = ArrayList()
        selectedProductList = ArrayList()
        selectedPobProductList = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_add_chemist, container, false)

        initView(view)
        initClickListener()
        initTextListener()

        return view
    }

    private fun initView(view: View) {
        ic_search_icon = view.findViewById(R.id.ic_search_icon)
        et_search = view.findViewById(R.id.et_search)
        rv_search_list = view.findViewById(R.id.rv_search_list)
        rv_search_list.layoutManager = LinearLayoutManager(mContext)

        ll_yes = view.findViewById(R.id.ll_yes)
        iv_yes = view.findViewById(R.id.iv_yes)
        ll_no = view.findViewById(R.id.ll_no)
        iv_no = view.findViewById(R.id.iv_no)
        ll_pob_product = view.findViewById(R.id.ll_pob_product)
        ic_pob_search_icon = view.findViewById(R.id.ic_pob_search_icon)
        et_search_pob = view.findViewById(R.id.et_search_pob)
        rv_pob_search_list = view.findViewById(R.id.rv_pob_search_list)
        rv_pob_search_list.layoutManager = LinearLayoutManager(mContext)

        et_volume = view.findViewById(R.id.et_volume)
        et_volume.filters = arrayOf<InputFilter>(InputFilterDecimal(8, 2))

        et_remarks = view.findViewById(R.id.et_remarks)
        tv_visit_date = view.findViewById(R.id.tv_visit_date)
        et_remarks_mr = view.findViewById(R.id.et_remarks_mr)
        tv_save = view.findViewById(R.id.tv_save)
        progress_wheel = view.findViewById(R.id.progress_wheel)
        progress_wheel.stopSpinning()

        ic_pob_close_icon = view.findViewById(R.id.ic_pob_close_icon)
        ic_close_icon = view.findViewById(R.id.ic_close_icon)
        rl_add_chemist_main = view.findViewById(R.id.rl_add_chemist_main)
        tv_pod_product_count = view.findViewById(R.id.tv_pod_product_count)
        tv_product_count = view.findViewById(R.id.tv_product_count)


        val list = AppDatabase.getDBInstance()?.productListDao()?.getAll()

        if (list != null) {
            for (i in list.indices) {
                val productListModel = ProductListModel()
                productListModel.id = list[i].id
                /*productListModel.brand = list[i].brand
                productListModel.brand_id = list[i].brand_id
                productListModel.category = list[i].category
                productListModel.category_id = list[i].category_id
                productListModel.date = list[i].date*/
                productListModel.product_name = list[i].product_name
                /*productListModel.watt = list[i].watt
                productListModel.watt_id = list[i].watt_id*/

                productList?.add(productListModel)
            }

            for (i in list.indices) {
                val productListModel = ProductListModel()
                productListModel.id = list[i].id
                productListModel.product_name = list[i].product_name

                pobProductList?.add(productListModel)
            }
        }
        if (productList != null) {

            //pobProductList = ArrayList(productList)

            productAdapter = AddChemistProductAdapter(mContext, productList, object : AddChemistProductAdapter.OnProductClickListener {
                override fun showList(isShowList: Boolean) {
                    if (isShowList) {
                        if (!TextUtils.isEmpty(et_search.text.toString().trim()))
                            rv_search_list.visibility = View.VISIBLE
                        else
                            rv_search_list.visibility = View.GONE
                    } else
                        rv_search_list.visibility = View.GONE
                }

                override fun onCheckClick(product: ProductListModel, isSelected: Boolean) {
                    if (isSelected)
                        selectedProductList?.add(product)
                    else
                        selectedProductList?.remove(product)
                    productAdapter?.notifyDataSetChanged()

                    if (selectedProductList?.size!! > 0)
                        tv_product_count.text = "(" + selectedProductList?.size + " product selected)"
                    else
                        tv_product_count.text = ""
                }
            })
            rv_search_list.adapter = productAdapter


            pobProductAdapter = AddChemistPobProductAdapter(mContext, pobProductList, object : AddChemistPobProductAdapter.OnProductClickListener {
                override fun showList(isShowList: Boolean) {
                    if (isShowList) {
                        if (!TextUtils.isEmpty(et_search_pob.text.toString().trim()))
                            rv_pob_search_list.visibility = View.VISIBLE
                        else
                            rv_pob_search_list.visibility = View.GONE
                    } else
                        rv_pob_search_list.visibility = View.GONE
                }

                override fun onCheckClick(product: ProductListModel, isSelected: Boolean) {
                    if (isSelected)
                        selectedPobProductList?.add(product)
                    else
                        selectedPobProductList?.remove(product)
                    pobProductAdapter?.notifyDataSetChanged()

                    if (selectedPobProductList?.size!! > 0)
                        tv_pod_product_count.text = "(" + selectedPobProductList?.size + " POD product selected)"
                    else
                        tv_pod_product_count.text = ""
                }
            })
            rv_pob_search_list.adapter = pobProductAdapter
        }

    }


    private fun initTextListener() {
        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (!TextUtils.isEmpty(et_search.text.toString().trim())) {
                    ic_search_icon.visibility = View.GONE
                    ic_close_icon.visibility = View.VISIBLE
                } else {
                    ic_search_icon.visibility = View.VISIBLE
                    ic_close_icon.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //if (!TextUtils.isEmpty(et_grp_search.text.toString().trim()) /*&& et_grp_search.text.toString().trim().length >= 2*/)
                productAdapter?.filter?.filter(et_search.text.toString().trim())
            }
        })

        et_search_pob.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (!TextUtils.isEmpty(et_search_pob.text.toString().trim())) {
                    ic_pob_search_icon.visibility = View.GONE
                    ic_pob_close_icon.visibility = View.VISIBLE
                } else {
                    ic_pob_search_icon.visibility = View.VISIBLE
                    ic_pob_close_icon.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //if (!TextUtils.isEmpty(et_grp_search.text.toString().trim()) /*&& et_grp_search.text.toString().trim().length >= 2*/)
                pobProductAdapter?.filter?.filter(et_search_pob.text.toString().trim())
            }
        })
    }

    private fun initClickListener() {
        ll_yes.setOnClickListener(this)
        ll_no.setOnClickListener(this)
        tv_save.setOnClickListener(this)
        ic_pob_close_icon.setOnClickListener(this)
        ic_close_icon.setOnClickListener(this)
        rl_add_chemist_main.setOnClickListener(null)
        tv_visit_date.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {

            R.id.ll_yes -> {
                if (!iv_yes.isSelected) {
                    iv_yes.isSelected = true
                    iv_no.isSelected = false
                    ll_pob_product.visibility = View.VISIBLE
                }
            }

            R.id.ll_no -> {
                if (!iv_no.isSelected) {
                    iv_yes.isSelected = false
                    iv_no.isSelected = true
                    ll_pob_product.visibility = View.GONE
                }
            }

            R.id.tv_save -> {
                when {
                    selectedProductList!!.size == 0 -> (mContext as DashboardActivity).showSnackMessage(getString(R.string.error_select_product))
                    !iv_yes.isSelected && !iv_no.isSelected -> (mContext as DashboardActivity).showSnackMessage(getString(R.string.error_select_pob_options))
                    iv_yes.isSelected && selectedPobProductList!!.size == 0 -> (mContext as DashboardActivity).showSnackMessage(getString(R.string.error_select_pob_product))
                    TextUtils.isEmpty(et_volume.text.toString().trim()) -> (mContext as DashboardActivity).showSnackMessage(getString(R.string.error_enter_vol))
                    TextUtils.isEmpty(et_remarks.text.toString().trim()) -> (mContext as DashboardActivity).showSnackMessage(getString(R.string.error_enter_remarks))
                    TextUtils.isEmpty(tv_visit_date.text.toString().trim()) -> (mContext as DashboardActivity).showSnackMessage(getString(R.string.error_select_visit_date))
                    TextUtils.isEmpty(et_remarks_mr.text.toString().trim()) -> (mContext as DashboardActivity).showSnackMessage(getString(R.string.error_enter_remarks_mr))
                    else -> saveData()
                }
            }

            R.id.ic_pob_close_icon -> {
                et_search_pob.setText("")
                rv_pob_search_list.visibility = View.GONE
                ic_pob_search_icon.visibility = View.VISIBLE
                ic_pob_close_icon.visibility = View.GONE
            }

            R.id.ic_close_icon -> {
                et_search.setText("")
                rv_search_list.visibility = View.GONE
                ic_search_icon.visibility = View.VISIBLE
                ic_close_icon.visibility = View.GONE
            }

            R.id.tv_visit_date -> {
                val aniDatePicker = DatePickerDialog(mContext, R.style.DatePickerTheme, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))
                aniDatePicker.datePicker.minDate = Calendar.getInstance(Locale.ENGLISH).timeInMillis + (1000 * 60 * 60 * 24)
                aniDatePicker.show()
            }
        }
    }

    private fun saveData() {
        val chemistEntity = AddChemistEntity()
        chemistEntity.shop_id = mAddShopDataObj?.shop_id

        if (iv_yes.isSelected)
            chemistEntity.pob = 1
        else if (iv_no.isSelected)
            chemistEntity.pob = 0

        if (!TextUtils.isEmpty(et_remarks.text.toString().trim()))
            chemistEntity.remarks = et_remarks.text.toString().trim()
        else
            chemistEntity.remarks = ""

        if (!TextUtils.isEmpty(et_remarks_mr.text.toString().trim()))
            chemistEntity.remarks_mr = et_remarks_mr.text.toString().trim()
        else
            chemistEntity.remarks_mr = ""

        chemistEntity.visit_date = nextDate

        if (!TextUtils.isEmpty(et_volume.text.toString().trim()))
            chemistEntity.volume = et_volume.text.toString().trim()
        else
            chemistEntity.volume = ""

        chemistEntity.isUploaded = false

        chemistEntity.chemist_visit_id = Pref.user_id + "_chem_" + System.currentTimeMillis()

        if (selectedProductList?.size!! > 0 || selectedPobProductList?.size!! > 0) {

            for (i in selectedProductList?.indices!!) {
                val pobChemEntity = AddChemistProductListEntity()
                pobChemEntity.chemist_visit_id = chemistEntity.chemist_visit_id
                pobChemEntity.shop_id = chemistEntity.shop_id
                pobChemEntity.isPob = false
                pobChemEntity.product_id = selectedProductList?.get(i)?.id?.toString()
                pobChemEntity.product_name = selectedProductList?.get(i)?.product_name

                AppDatabase.getDBInstance()!!.addChemistProductDao().insertAll(pobChemEntity)
            }

            if (chemistEntity.pob == 1) {
                for (i in selectedPobProductList?.indices!!) {
                    val pobChemEntity = AddChemistProductListEntity()
                    pobChemEntity.chemist_visit_id = chemistEntity.chemist_visit_id
                    pobChemEntity.shop_id = chemistEntity.shop_id
                    pobChemEntity.isPob = true
                    pobChemEntity.product_id = selectedPobProductList?.get(i)?.id?.toString()
                    pobChemEntity.product_name = selectedPobProductList?.get(i)?.product_name

                    AppDatabase.getDBInstance()!!.addChemistProductDao().insertAll(pobChemEntity)
                }
            } else
                selectedPobProductList?.clear()
        }

        AppDatabase.getDBInstance()!!.addChemistDao().insertAll(chemistEntity)

        val chemistVisit = AddChemistVisitInputModel()

        if (!TextUtils.isEmpty(chemistEntity.chemist_visit_id))
            chemistVisit.chemist_visit_id = chemistEntity.chemist_visit_id!!

        chemistVisit.isPob = chemistEntity.pob
        chemistVisit.next_visit_date = nextDate

        if (!TextUtils.isEmpty(chemistEntity.remarks))
            chemistVisit.remarks = chemistEntity.remarks!!

        if (!TextUtils.isEmpty(chemistEntity.remarks_mr))
            chemistVisit.remarks_mr = chemistEntity.remarks_mr!!

        if (!TextUtils.isEmpty(chemistEntity.volume))
            chemistVisit.volume = chemistEntity.volume!!

        if (!TextUtils.isEmpty(chemistEntity.shop_id))
            chemistVisit.shop_id = chemistEntity.shop_id!!

        chemistVisit.user_id = Pref.user_id!!
        chemistVisit.session_token = Pref.session_token!!

        val productList = ArrayList<AddChemistProductModel>()
        for (i in selectedProductList?.indices!!) {
            val product = AddChemistProductModel()
            product.product_id = selectedProductList?.get(i)?.id?.toString()!!
            product.product_name = selectedProductList?.get(i)?.product_name!!
            productList.add(product)
        }
        chemistVisit.product_list = productList

        val podProductList = ArrayList<AddChemistProductModel>()
        for (i in selectedPobProductList?.indices!!) {
            val product = AddChemistProductModel()
            product.product_id = selectedPobProductList?.get(i)?.id?.toString()!!
            product.product_name = selectedPobProductList?.get(i)?.product_name!!
            podProductList.add(product)
        }
        chemistVisit.pob_product_list = podProductList

        callUploadChemistVisitApi(chemistVisit)
    }

    private val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        // TODO Auto-generated method stub
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, monthOfYear)
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        nextDate = AppUtils.getFormattedDateForApi(myCalendar.time)
        tv_visit_date.text = AppUtils.changeAttendanceDateFormat(AppUtils.getDobFormattedDate(myCalendar.time))
    }


    private fun callUploadChemistVisitApi(chemistVisit: AddChemistVisitInputModel) {

        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage("Chemist added successfully")
            (mContext as DashboardActivity).onBackPressed()
            return
        }

        val shop = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopDetail(mAddShopDataObj?.shop_id)

        if (!shop.isUploaded) {
            (mContext as DashboardActivity).showSnackMessage("Chemist added successfully")
            (mContext as DashboardActivity).onBackPressed()
            return
        }

        Timber.d("======ADD CHEMIST VISIT INPUT PARAMS (ADD CHEMIST)======")
        Timber.d("USER ID===> " + chemistVisit.user_id)
        Timber.d("SESSION ID====> " + chemistVisit.session_token)
        Timber.d("CHEMIST VISIT ID====> " + chemistVisit.chemist_visit_id)
        Timber.d("SHOP_ID====> " + chemistVisit.shop_id)
        Timber.d("IS POB====> " + chemistVisit.isPob)
        Timber.d("NEXT VISIT DATE====> " + chemistVisit.next_visit_date)
        Timber.d("VOLUME====> " + chemistVisit.volume)
        Timber.d("REMARKS====> " + chemistVisit.remarks)
        Timber.d("REMARKS MR====> " + chemistVisit.remarks_mr)
        Timber.d("PRODUCT LIST SIZE====> " + chemistVisit.product_list.size)
        Timber.d("POB PRODUCT LIST SIZE====> " + chemistVisit.pob_product_list.size)
        Timber.d("=========================================================")

        val repository = ActivityRepoProvider.activityRepoProvider()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.uploadChemistVisit(chemistVisit)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            progress_wheel.stopSpinning()
                            val response = result as BaseResponse

                            Timber.d("ADD CHEMIST VISIT DETAILS : " + "RESPONSE : " + response.status + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ", MESSAGE : " + response.message)

                            if (response.status == NetworkConstant.SUCCESS) {
                                AppDatabase.getDBInstance()!!.addChemistDao().updateIsUploaded(true, chemistVisit.chemist_visit_id)
                                (mContext as DashboardActivity).showSnackMessage(response.message!!)
                            } else
                                (mContext as DashboardActivity).showSnackMessage("Chemist added successfully")

                            (mContext as DashboardActivity).onBackPressed()
                        }, { error ->
                            Timber.d("ADD CHEMIST VISIT DETAILS : " + "ERROR : " + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ", MESSAGE : " + error.localizedMessage)
                            progress_wheel.stopSpinning()
                            error.printStackTrace()
                            (mContext as DashboardActivity).showSnackMessage("Chemist added successfully")
                            (mContext as DashboardActivity).onBackPressed()
                        })
        )

    }
}