package com.breezefieldsalesdemo.features.billing.presentation

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.InputFilter
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.pnikosis.materialishprogress.ProgressWheel
import com.breezefieldsalesdemo.R
import com.breezefieldsalesdemo.app.AppDatabase
import com.breezefieldsalesdemo.app.NetworkConstant
import com.breezefieldsalesdemo.app.Pref
import com.breezefieldsalesdemo.app.domain.*
import com.breezefieldsalesdemo.app.types.FragType
import com.breezefieldsalesdemo.app.utils.AppUtils
import com.breezefieldsalesdemo.app.utils.ImagePickerManager
import com.breezefieldsalesdemo.app.utils.InputFilterDecimal
import com.breezefieldsalesdemo.app.utils.PermissionUtils
import com.breezefieldsalesdemo.app.utils.ToasterCustom
import com.breezefieldsalesdemo.app.utils.swipemenulayout.SwipeMenuRecyclerView
import com.breezefieldsalesdemo.base.BaseResponse
import com.breezefieldsalesdemo.base.presentation.BaseActivity
import com.breezefieldsalesdemo.base.presentation.BaseFragment
import com.breezefieldsalesdemo.features.addshop.api.AddShopRepositoryProvider
import com.breezefieldsalesdemo.features.addshop.api.assignToPPList.AssignToPPListRepoProvider
import com.breezefieldsalesdemo.features.addshop.api.assignedToDDList.AssignToDDListRepoProvider
import com.breezefieldsalesdemo.features.addshop.api.typeList.TypeListRepoProvider
import com.breezefieldsalesdemo.features.addshop.model.AddShopRequestData
import com.breezefieldsalesdemo.features.addshop.model.AddShopResponse
import com.breezefieldsalesdemo.features.addshop.model.AssignedToShopListResponseModel
import com.breezefieldsalesdemo.features.addshop.model.assigntoddlist.AssignToDDListResponseModel
import com.breezefieldsalesdemo.features.addshop.model.assigntopplist.AssignToPPListResponseModel
import com.breezefieldsalesdemo.features.billing.api.AddBillingRepoProvider
import com.breezefieldsalesdemo.features.billing.model.AddBillingInputParamsModel
import com.breezefieldsalesdemo.features.commondialog.presentation.CommonDialog
import com.breezefieldsalesdemo.features.commondialog.presentation.CommonDialogClickListener
import com.breezefieldsalesdemo.features.dashboard.presentation.DashboardActivity
import com.breezefieldsalesdemo.features.location.LocationWizard
import com.breezefieldsalesdemo.features.location.model.ShopDurationRequest
import com.breezefieldsalesdemo.features.location.model.ShopDurationRequestData
import com.breezefieldsalesdemo.features.location.shopdurationapi.ShopDurationRepositoryProvider
import com.breezefieldsalesdemo.features.viewAllOrder.api.addorder.AddOrderRepoProvider
import com.breezefieldsalesdemo.features.viewAllOrder.model.AddOrderInputParamsModel
import com.breezefieldsalesdemo.features.viewAllOrder.model.AddOrderInputProductList
import com.breezefieldsalesdemo.features.viewAllOrder.orderOptimized.OrderProductCartFrag
import com.breezefieldsalesdemo.widgets.AppCustomEditText
import com.breezefieldsalesdemo.widgets.AppCustomTextView
import com.themechangeapp.pickimage.PermissionHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import timber.log.Timber
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Saikat on 19-02-2019.
 */
// 1.0 AddBillingFragment AppV 4.0.6 saheli 12-01-2023 multiple contact Data added on Api called
// 2.0 AddBillingFragment AppV 4.2.2 Suman 16-10-2023 mantis id 26908
class AddBillingFragment : BaseFragment(), View.OnClickListener {

    private lateinit var mContext: Context
    private lateinit var progress_wheel: ProgressWheel
    private lateinit var rl_add_bill_main: RelativeLayout
    private lateinit var tv_submit_btn: AppCustomTextView
    private lateinit var et_invoice_no: AppCustomEditText
    private lateinit var et_invoice_amount: AppCustomEditText
    private lateinit var et_remark: AppCustomEditText
    private lateinit var tv_invoice_date: AppCustomTextView
    private lateinit var rv_cart_list: SwipeMenuRecyclerView
    private lateinit var tv_total_order_amount: AppCustomTextView
    private lateinit var tv_total_order_value: AppCustomTextView
    private lateinit var ll_attachment: LinearLayout
    private lateinit var tv_attachment: AppCustomTextView
    private lateinit var et_patient: AppCustomEditText
    private lateinit var et_address: AppCustomEditText
    private lateinit var et_phone: AppCustomEditText
    private lateinit var ll_patient_info: LinearLayout

    private var permissionUtils: PermissionUtils? = null
    private var dataPath = ""
    private var myCalendar = Calendar.getInstance(Locale.ENGLISH)
    private var orderId = ""
    private var productList: ArrayList<OrderProductListEntity>? = null

    companion object {

        private var order: OrderDetailsListEntity? = null

        fun newInstance(objects: Any): AddBillingFragment {
            val fragment = AddBillingFragment()

            if (objects is OrderDetailsListEntity)
                order = objects

            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context

        //orderId = arguments?.getString("order_id").toString()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_new_add_billing, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        progress_wheel = view.findViewById(R.id.progress_wheel)
        progress_wheel.stopSpinning()
        rl_add_bill_main = view.findViewById(R.id.rl_add_bill_main)
        tv_submit_btn = view.findViewById(R.id.tv_submit_btn)
        et_invoice_no = view.findViewById(R.id.et_invoice_no)
        et_invoice_amount = view.findViewById(R.id.et_invoice_amount)
        et_remark = view.findViewById(R.id.et_remark)
        tv_invoice_date = view.findViewById(R.id.tv_invoice_date)
        rv_cart_list = view.findViewById(R.id.rv_cart_list)
        rv_cart_list.layoutManager = LinearLayoutManager(mContext)
        tv_total_order_amount = view.findViewById(R.id.tv_total_order_amount)
        tv_total_order_value = view.findViewById(R.id.tv_total_order_value)
        tv_attachment = view.findViewById(R.id.tv_attachment)
        ll_attachment = view.findViewById(R.id.ll_attachment)
        et_patient = view.findViewById(R.id.et_patient)
        et_address = view.findViewById(R.id.et_address)
        et_phone = view.findViewById(R.id.et_phone)
        ll_patient_info = view.findViewById(R.id.ll_patient_info)

        tv_invoice_date.text = AppUtils.getBillFormattedDate(myCalendar.time)

        et_invoice_amount.filters = arrayOf<InputFilter>(InputFilterDecimal(8, 2))

        if (Pref.isPatientDetailsShowInOrder)
            ll_patient_info.visibility = View.VISIBLE
        else
            ll_patient_info.visibility = View.GONE

        if (!TextUtils.isEmpty(order?.patient_no))
            et_phone.setText(order?.patient_no)

        if (!TextUtils.isEmpty(order?.patient_name))
            et_patient.setText(order?.patient_name)

        if (!TextUtils.isEmpty(order?.patient_address))
            et_address.setText(order?.patient_address)


        et_remark.setOnTouchListener(View.OnTouchListener { v, event ->
            if (et_remark.hasFocus()) {
                v?.parent?.requestDisallowInterceptTouchEvent(true)
                when (event?.action /*& MotionEvent.ACTION_MASK*/) {
                    MotionEvent.ACTION_SCROLL -> {
                        v?.parent?.requestDisallowInterceptTouchEvent(false)
                        return@OnTouchListener true
                    }
                }
            }
            false
        })

        tv_submit_btn.setOnClickListener(this)
        rl_add_bill_main.setOnClickListener(null)
        tv_invoice_date.setOnClickListener(this)
        ll_attachment.setOnClickListener(this)


        productList = AppDatabase.getDBInstance()!!.orderProductListDao().getDataAccordingToOrderId(order?.order_id!!) as ArrayList<OrderProductListEntity>

        if (productList != null) {

            var totalAmount = 0.00

            for (i in productList!!.indices) {
                (mContext as DashboardActivity).qtyList.add(productList!![i].qty!!.toDouble().toInt().toString())
                (mContext as DashboardActivity).rateList.add(productList!![i].rate!!)
                (mContext as DashboardActivity).totalPrice.add(productList!![i].total_price?.toDouble()!!)

                totalAmount += (mContext as DashboardActivity).totalPrice[i]
            }

            //val finalTotalAmount = String.format("%.2f", totalAmount.toFloat())
            //mantis id 26274
            val finalTotalAmount = String.format("%.2f", totalAmount.toDouble())
            tv_total_order_amount.text = finalTotalAmount

            tv_total_order_value.text = productList!!.size.toString()

            initAdapter()
        }
    }

    private fun showDeleteAlert(adapterPosition: Int) {

        CommonDialog.getInstance("Delete Alert", "Do you really want to delete this product?", getString(R.string.cancel), getString(R.string.ok), object : CommonDialogClickListener {
            override fun onLeftClick() {
            }

            override fun onRightClick(editableData: String) {
                productList?.removeAt(adapterPosition)
                (mContext as DashboardActivity).tv_cart_count.text = productList?.size.toString()
                (mContext as DashboardActivity).qtyList.removeAt(adapterPosition)
                (mContext as DashboardActivity).rateList.removeAt(adapterPosition)
                (mContext as DashboardActivity).totalPrice.removeAt(adapterPosition)

                initAdapter()

                tv_total_order_value.text = productList?.size.toString()

                Handler().postDelayed(Runnable {
                    var totalAmount = 0.0

                    for (i in (mContext as DashboardActivity).totalPrice.indices) {
                        totalAmount += (mContext as DashboardActivity).totalPrice[i]
                    }

                    tv_total_order_amount.text = totalAmount.toString()
                }, 200)
            }

        }).show((mContext as DashboardActivity).supportFragmentManager, "")
    }

    private fun initAdapter() {
        rv_cart_list.setItemViewCacheSize(productList?.size!!)
        rv_cart_list.adapter = AddBillingCartAdapter(mContext,order!!.shop_id!!, productList, object : AddBillingCartAdapter.OnProductClickListener {
            override fun onDelete(adapterPosition: Int) {
                showDeleteAlert(adapterPosition)
            }

            override fun onEdit(adapterPosition: Int) {

                try {

                    if (!TextUtils.isEmpty((mContext as DashboardActivity).rateList[adapterPosition]) &&
                            !TextUtils.isEmpty((mContext as DashboardActivity).qtyList[adapterPosition])) {
                        val totalPrice = String.format("%.2f", ((mContext as DashboardActivity).rateList[adapterPosition].toFloat()
                                * (mContext as DashboardActivity).qtyList[adapterPosition].toDouble().toInt()))
                        (mContext as DashboardActivity).totalPrice[adapterPosition] = totalPrice.toDouble()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                Handler().postDelayed(Runnable {
                    var totalAmount = 0.00

                    for (i in (mContext as DashboardActivity).totalPrice.indices) {
                        totalAmount += (mContext as DashboardActivity).totalPrice[i]
                    }
                    val finalTotalAmount = String.format("%.2f", totalAmount.toFloat())
                    tv_total_order_amount.text = finalTotalAmount
                }, 200)


            }

            override fun onProductClick(brand: ProductListEntity?, adapterPosition: Int, isSelected: Boolean) {
                /*if (isSelected)
                    selectedItems.add(adapterPosition)
                else {
                    try {
                        selectedItems.remove(adapterPosition)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }*/
            }
        })
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_submit_btn -> {
                AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
                checkValidation()
            }

            R.id.tv_invoice_date -> {
                val datePicker = DatePickerDialog(mContext, R.style.DatePickerTheme, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))
                datePicker.datePicker.maxDate = Calendar.getInstance(Locale.ENGLISH).timeInMillis
                datePicker.datePicker.minDate = AppUtils.getLongTimeStampFromDate(order?.only_date!!)
                datePicker.show()
            }

            R.id.ll_attachment -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    initPermissionCheck()
                else {
                    showPictureDialog()
                }
            }
        }
    }

    private fun initPermissionCheck() {

        //begin mantis id 26741 Storage permission updation Suman 22-08-2023
        var permissionList = arrayOf<String>( Manifest.permission.CAMERA)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            permissionList += Manifest.permission.READ_MEDIA_IMAGES
            permissionList += Manifest.permission.READ_MEDIA_AUDIO
            permissionList += Manifest.permission.READ_MEDIA_VIDEO
        }else{
            permissionList += Manifest.permission.WRITE_EXTERNAL_STORAGE
            permissionList += Manifest.permission.READ_EXTERNAL_STORAGE
        }
//end mantis id 26741 Storage permission updation Suman 22-08-2023

        permissionUtils = PermissionUtils(mContext as Activity, object : PermissionUtils.OnPermissionListener {
            override fun onPermissionGranted() {
                showPictureDialog()
            }

            override fun onPermissionNotGranted() {
                (mContext as DashboardActivity).showSnackMessage(getString(R.string.accept_permission))
            }

        },permissionList)// arrayOf<String>(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
    }

    fun onRequestPermission(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        permissionUtils?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(mContext)
        pictureDialog.setTitle("Select Action")
        //val pictureDialogItems = arrayOf("Select photo from gallery", "Capture Image", "Select file from file manager")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture Image")
        pictureDialog.setItems(pictureDialogItems,
                DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        0 -> selectImageInAlbum()
                        1 -> {
                            //(mContext as DashboardActivity).openFileManager()
                            launchCamera()
                        }
                        /*2 -> {
                            (mContext as DashboardActivity).openFileManager()
                        }*/
                    }
                })
        pictureDialog.show()
    }

    private fun launchCamera() {
        (mContext as DashboardActivity).captureImage()
    }

    private fun selectImageInAlbum() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        (mContext as DashboardActivity).startActivityForResult(galleryIntent, PermissionHelper.REQUEST_CODE_STORAGE)
    }

    private fun checkValidation() {

        val list = AppDatabase.getDBInstance()!!.billingDao().getDataOrderIdWise(order?.order_id!!)
        var isInvoiceNoAvailable = false

        if (TextUtils.isEmpty(et_invoice_no.text.toString().trim()))
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.error_enter_invoice_no))
        else if (TextUtils.isEmpty(tv_invoice_date.text.toString().trim()))
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.error_enter_invoice_date))
        else if (TextUtils.isEmpty(et_invoice_amount.text.toString().trim()))
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.error_enter_invoice_amount))
        else if (list != null && list.isNotEmpty()) {
            for (i in list.indices) {
                if (list[i].invoice_no.equals(et_invoice_no.text.toString().trim(), ignoreCase = true)) {
                    isInvoiceNoAvailable = true
                    break
                } else
                    isInvoiceNoAvailable = false
            }

            if (isInvoiceNoAvailable)
                (mContext as DashboardActivity).showSnackMessage(getString(R.string.error_invoice_no_available))
            else
                showBillingCheckAlert()
        } else {
            showBillingCheckAlert()
        }
    }

    private fun showBillingCheckAlert() {
        CommonDialog.getInstance("Billing Confirmation", "Do you want to recheck the billing?", getString(R.string.no), getString(R.string.yes), false, object : CommonDialogClickListener {
            override fun onLeftClick() {
                saveData()
            }

            override fun onRightClick(editableData: String) {
            }

        }).show((mContext as DashboardActivity).supportFragmentManager, "")
    }

    private fun saveData() {

        doAsync {

            val billing = BillingEntity()
            billing.bill_id = Pref.user_id + "_bill_" + System.currentTimeMillis()
            billing.invoice_no = et_invoice_no.text.toString().trim()
            billing.invoice_date = AppUtils.convertBilingDateToIdealFormat(tv_invoice_date.text.toString().trim())
            billing.invoice_amount = et_invoice_amount.text.toString().trim()
            billing.remarks = et_remark.text.toString().trim()
            billing.order_id = order?.order_id
            billing.attachment = dataPath
            billing.patient_no = et_phone.text.toString().trim()
            billing.patient_name = et_patient.text.toString().trim()
            billing.patient_address = et_address.text.toString().trim()

            AppDatabase.getDBInstance()!!.billingDao().insertAll(billing)

            for (i in productList?.indices!!) {
                val billingProductEntity = BillingProductListEntity()
                billingProductEntity.bill_id = billing.bill_id
                billingProductEntity.order_id = billing.order_id
                billingProductEntity.brand = productList?.get(i)?.brand
                billingProductEntity.brand_id = productList?.get(i)?.brand_id
                billingProductEntity.category = productList?.get(i)?.category
                billingProductEntity.category_id = productList?.get(i)?.category_id
                billingProductEntity.product_name = productList?.get(i)?.product_name
                billingProductEntity.product_id = productList?.get(i)?.product_id
                billingProductEntity.qty = (mContext as DashboardActivity).qtyList[i] //productList?.get(i)?.qty
                billingProductEntity.rate = (mContext as DashboardActivity).rateList[i] //productList?.get(i)?.rate
                billingProductEntity.total_price = (mContext as DashboardActivity).totalPrice[i].toString() //productList?.get(i)?.total_price
                billingProductEntity.watt = productList?.get(i)?.watt
                billingProductEntity.watt_id = productList?.get(i)?.watt_id

                AppDatabase.getDBInstance()!!.billProductDao().insert(billingProductEntity)
            }

            //stock entry
            try {
                if (Pref.IsStockCheckFeatureOn && Pref.StockCheckOnOrder1OrInvioce0==false) {

                    for(l in 0..productList!!.size-1){
                        var stock :StockAllEntity = StockAllEntity()
                        if(Pref.IsShowDistributorWiseCurrentStockInOrder){
                            stock = (AppDatabase.getDBInstance()!!.stockAllDao().getStockDtlsForDD(productList!!.get(l).product_id!!,productList!!.get(l).shop_id!!  ) as ArrayList<StockAllEntity>).first()
                        }else{
                            stock = (AppDatabase.getDBInstance()!!.stockAllDao().getStockDtls(productList!!.get(l).product_id!!,productList!!.get(l).shop_id!!  ) as ArrayList<StockAllEntity>).first()
                        }

                        var objStock: StockTransEntity = StockTransEntity()
                        objStock.order_id = order?.order_id!!
                        objStock.stock_shopcode = stock.stock_shopcode
                        objStock.stock_productid = stock.stock_productid
                        objStock.stock_productqty = stock.stock_productqty

                        objStock.stock_productOrderqty = (mContext as DashboardActivity).qtyList[l]
                        objStock.isUploaded = false


                        var stockOb = AppDatabase.getDBInstance()?.stockAllDao()!!
                            .getParticularStock(
                                objStock.stock_productid,
                                objStock.stock_shopcode
                            )
                        var currentStock =
                            stockOb.stock_productbalqty.toDouble() - objStock.stock_productOrderqty.toDouble()
                        var cs = String.format("%.02f", currentStock)
                        objStock.stock_productbalqty = cs
                        AppDatabase.getDBInstance()?.stockTransDao()!!.insert(objStock)

                        AppDatabase.getDBInstance()?.stockAllDao()!!.updateStock(
                            String.format("%.02f", currentStock).toString(),
                            objStock.stock_productid, objStock.stock_shopcode
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            uiThread {

                if (AppUtils.isOnline(mContext)) {

                    if (!(mContext as DashboardActivity).shop?.isUploaded!!) {
                        syncShop(billing, (mContext as DashboardActivity).shop!!)
                    } else {
                        checkToCallOrderApi(billing)
                    }

                } else {
                    backPress("Billing added successfully")
                }
            }
        }
    }


    private fun syncShop(billing: BillingEntity, shop: AddShopDBModelEntity) {
        val addShopData = AddShopRequestData()
        //if (!shop.isUploaded) {
        addShopData.session_token = Pref.session_token
        addShopData.address = shop.address
        addShopData.owner_contact_no = shop.ownerContactNumber
        addShopData.owner_email = shop.ownerEmailId
        addShopData.owner_name = shop.ownerName
        addShopData.pin_code = shop.pinCode
        addShopData.shop_lat = shop.shopLat.toString()
        addShopData.shop_long = shop.shopLong.toString()
        addShopData.shop_name = shop.shopName.toString()
        addShopData.type = shop.type.toString()
        addShopData.shop_id = shop.shop_id
        addShopData.user_id = Pref.user_id

        if (!TextUtils.isEmpty(shop.dateOfBirth))
            addShopData.dob = AppUtils.changeAttendanceDateFormatToCurrent(shop.dateOfBirth)

        if (!TextUtils.isEmpty(shop.dateOfAniversary))
            addShopData.date_aniversary = AppUtils.changeAttendanceDateFormatToCurrent(shop.dateOfAniversary)

        addShopData.assigned_to_dd_id = shop.assigned_to_dd_id
        addShopData.assigned_to_pp_id = shop.assigned_to_pp_id
        addShopData.added_date = shop.added_date
        addShopData.amount = shop.amount
        addShopData.area_id = shop.area_id
        addShopData.model_id = shop.model_id
        addShopData.primary_app_id = shop.primary_app_id
        addShopData.secondary_app_id = shop.secondary_app_id
        addShopData.lead_id = shop.lead_id
        addShopData.stage_id = shop.stage_id
        addShopData.funnel_stage_id = shop.funnel_stage_id
        addShopData.booking_amount = shop.booking_amount
        addShopData.type_id = shop.type_id

        addShopData.director_name = shop.director_name
        addShopData.key_person_name = shop.person_name
        addShopData.phone_no = shop.person_no

        if (!TextUtils.isEmpty(shop.family_member_dob))
            addShopData.family_member_dob = AppUtils.changeAttendanceDateFormatToCurrent(shop.family_member_dob)

        if (!TextUtils.isEmpty(shop.add_dob))
            addShopData.addtional_dob = AppUtils.changeAttendanceDateFormatToCurrent(shop.add_dob)

        if (!TextUtils.isEmpty(shop.add_doa))
            addShopData.addtional_doa = AppUtils.changeAttendanceDateFormatToCurrent(shop.add_doa)

        addShopData.specialization = shop.specialization
        addShopData.category = shop.category
        addShopData.doc_address = shop.doc_address
        addShopData.doc_pincode = shop.doc_pincode
        addShopData.is_chamber_same_headquarter = shop.chamber_status.toString()
        addShopData.is_chamber_same_headquarter_remarks = shop.remarks
        addShopData.chemist_name = shop.chemist_name
        addShopData.chemist_address = shop.chemist_address
        addShopData.chemist_pincode = shop.chemist_pincode
        addShopData.assistant_contact_no = shop.assistant_no
        addShopData.average_patient_per_day = shop.patient_count
        addShopData.assistant_name = shop.assistant_name

        if (!TextUtils.isEmpty(shop.doc_family_dob))
            addShopData.doc_family_member_dob = AppUtils.changeAttendanceDateFormatToCurrent(shop.doc_family_dob)

        if (!TextUtils.isEmpty(shop.assistant_dob))
            addShopData.assistant_dob = AppUtils.changeAttendanceDateFormatToCurrent(shop.assistant_dob)

        if (!TextUtils.isEmpty(shop.assistant_doa))
            addShopData.assistant_doa = AppUtils.changeAttendanceDateFormatToCurrent(shop.assistant_doa)

        if (!TextUtils.isEmpty(shop.assistant_family_dob))
            addShopData.assistant_family_dob = AppUtils.changeAttendanceDateFormatToCurrent(shop.assistant_family_dob)

        addShopData.entity_id = shop.entity_id
        addShopData.party_status_id = shop.party_status_id
        addShopData.retailer_id = shop.retailer_id
        addShopData.dealer_id = shop.dealer_id
        addShopData.beat_id = shop.beat_id
        addShopData.assigned_to_shop_id = shop.assigned_to_shop_id
        addShopData.actual_address = shop.actual_address

        var uniqKeyObj=AppDatabase.getDBInstance()!!.shopActivityDao().getNewShopActivityKey(shop.shop_id,false)
        addShopData.shop_revisit_uniqKey=uniqKeyObj?.shop_revisit_uniqKey!!

        addShopData.project_name = shop.project_name
        addShopData.landline_number = shop.landline_number
        addShopData.agency_name = shop.agency_name

        addShopData.alternateNoForCustomer = shop.alternateNoForCustomer
        addShopData.whatsappNoForCustomer = shop.whatsappNoForCustomer

        // duplicate shop api call
        addShopData.isShopDuplicate=shop.isShopDuplicate

        addShopData.purpose=shop.purpose
        //start AppV 4.2.2 tufan    20/09/2023 FSSAI Lic No Implementation 26813
        try {
            addShopData.FSSAILicNo = shop.FSSAILicNo
        }catch (ex:Exception){
            ex.printStackTrace()
            addShopData.FSSAILicNo = ""
        }
//end AppV 4.2.2 tufan    20/09/2023 FSSAI Lic No Implementation 26813


        addShopData.GSTN_Number=shop.gstN_Number
        addShopData.ShopOwner_PAN=shop.shopOwner_PAN

        callAddShopApi(addShopData, shop.shopImageLocalPath, shop.doc_degree, billing)
        //}
    }

    private fun callAddShopApi(addShop: AddShopRequestData, shop_imgPath: String?, degree_imgPath: String?, billing: BillingEntity) {

        if (BaseActivity.isApiInitiated)
            return

        BaseActivity.isApiInitiated = true


        progress_wheel.spin()


        Timber.d("=======SyncShop Input Params (Add Billing)=============")
        Timber.d("shop id=======> " + addShop.shop_id)
        val index = addShop.shop_id!!.indexOf("_")
        Timber.d("decoded shop id=======> " + addShop.user_id + "_" + AppUtils.getDate(addShop.shop_id!!.substring(index + 1, addShop.shop_id!!.length).toLong()))
        Timber.d("shop added date=======> " + addShop.added_date)
        Timber.d("shop address=======> " + addShop.address)
        Timber.d("assigned to dd id=======> " + addShop.assigned_to_dd_id)
        Timber.d("assigned to pp id=======> " + addShop.assigned_to_pp_id)
        Timber.d("date aniversery=======> " + addShop.date_aniversary)
        Timber.d("dob=======> " + addShop.dob)
        Timber.d("shop owner phn no=======> " + addShop.owner_contact_no)
        Timber.d("shop owner email=======> " + addShop.owner_email)
        Timber.d("shop owner name=======> " + addShop.owner_name)
        Timber.d("shop pincode=======> " + addShop.pin_code)
        Timber.d("session token=======> " + addShop.session_token)
        Timber.d("shop lat=======> " + addShop.shop_lat)
        Timber.d("shop long=======> " + addShop.shop_long)
        Timber.d("shop name=======> " + addShop.shop_name)
        Timber.d("shop type=======> " + addShop.type)
        Timber.d("user id=======> " + addShop.user_id)
        Timber.d("amount=======> " + addShop.amount)
        Timber.d("area id=======> " + addShop.area_id)
        Timber.d("model id=======> " + addShop.model_id)
        Timber.d("primary app id=======> " + addShop.primary_app_id)
        Timber.d("secondary app id=======> " + addShop.secondary_app_id)
        Timber.d("lead id=======> " + addShop.lead_id)
        Timber.d("stage id=======> " + addShop.stage_id)
        Timber.d("funnel stage id=======> " + addShop.funnel_stage_id)
        Timber.d("booking amount=======> " + addShop.booking_amount)
        Timber.d("type id=======> " + addShop.type_id)
        Timber.d("director name=======> " + addShop.director_name)
        Timber.d("family member dob=======> " + addShop.family_member_dob)
        Timber.d("key person's name=======> " + addShop.key_person_name)
        Timber.d("phone no=======> " + addShop.phone_no)
        Timber.d("additional dob=======> " + addShop.addtional_dob)
        Timber.d("additional doa=======> " + addShop.addtional_doa)
        Timber.d("doctor family member dob=======> " + addShop.doc_family_member_dob)
        Timber.d("specialization=======> " + addShop.specialization)
        Timber.d("average patient count per day=======> " + addShop.average_patient_per_day)
        Timber.d("category=======> " + addShop.category)
        Timber.d("doctor address=======> " + addShop.doc_address)
        Timber.d("doctor pincode=======> " + addShop.doc_pincode)
        Timber.d("chambers or hospital under same headquarter=======> " + addShop.is_chamber_same_headquarter)
        Timber.d("chamber related remarks=======> " + addShop.is_chamber_same_headquarter_remarks)
        Timber.d("chemist name=======> " + addShop.chemist_name)
        Timber.d("chemist name=======> " + addShop.chemist_address)
        Timber.d("chemist pincode=======> " + addShop.chemist_pincode)
        Timber.d("assistant name=======> " + addShop.assistant_name)
        Timber.d("assistant contact no=======> " + addShop.assistant_contact_no)
        Timber.d("assistant dob=======> " + addShop.assistant_dob)
        Timber.d("assistant date of anniversary=======> " + addShop.assistant_doa)
        Timber.d("assistant family dob=======> " + addShop.assistant_family_dob)
        Timber.d("entity id=======> " + addShop.entity_id)
        Timber.d("party status id=======> " + addShop.party_status_id)
        Timber.d("retailer id=======> " + addShop.retailer_id)
        Timber.d("dealer id=======> " + addShop.dealer_id)
        Timber.d("beat id=======> " + addShop.beat_id)
        Timber.d("assigned to shop id=======> " + addShop.assigned_to_shop_id)
        Timber.d("actual address=======> " + addShop.actual_address)

        if (shop_imgPath != null)
            Timber.d("shop image path=======> $shop_imgPath")

        if (degree_imgPath != null)
            Timber.d("doctor degree image path=======> $degree_imgPath")
        Timber.d("======================================================")

        if (TextUtils.isEmpty(shop_imgPath) && TextUtils.isEmpty(degree_imgPath)) {
            val repository = AddShopRepositoryProvider.provideAddShopWithoutImageRepository()
            BaseActivity.compositeDisposable.add(
                    repository.addShop(addShop)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                val addShopResult = result as AddShopResponse
                                Timber.d("syncShopFromShopList : " + ", SHOP: " + addShop.shop_name + ", RESPONSE:" + result.message)
                                if (addShopResult.status == NetworkConstant.SUCCESS) {
                                    AppDatabase.getDBInstance()!!.addShopEntryDao().updateIsUploaded(true, addShop.shop_id)

                                    doAsync {
                                        val resultAs = runLongTask(addShop.shop_id)
                                        uiThread {
                                            if (resultAs == true) {
                                                BaseActivity.isApiInitiated = false
                                                getAssignedPPListApi(addShop.shop_id, billing)
                                            }
                                        }
                                    }
                                    progress_wheel.stopSpinning()

                                } else if (addShopResult.status == NetworkConstant.DUPLICATE_SHOP_ID) {
                                    Timber.d("DuplicateShop : " + ", SHOP: " + addShop.shop_name)
                                    AppDatabase.getDBInstance()!!.addShopEntryDao().updateIsUploaded(true, addShop.shop_id)
                                    progress_wheel.stopSpinning()
                                    //(mContext as DashboardActivity).showSnackMessage(addShopResult.message!!)
                                    if (AppDatabase.getDBInstance()!!.addShopEntryDao().getDuplicateShopData(addShop.owner_contact_no).size > 0) {
                                        AppDatabase.getDBInstance()!!.addShopEntryDao().deleteShopById(addShop.shop_id)
                                        AppDatabase.getDBInstance()!!.shopActivityDao().deleteShopByIdAndDate(addShop.shop_id!!, AppUtils.getCurrentDateForShopActi())
                                    }
                                    doAsync {
                                        val resultAs = runLongTask(addShop.shop_id)
                                        uiThread {
                                            if (resultAs == true) {
                                                BaseActivity.isApiInitiated = false
                                                getAssignedPPListApi(addShop.shop_id, billing)
                                            }
                                        }
                                    }
                                } else {
                                    BaseActivity.isApiInitiated = false
                                    progress_wheel.stopSpinning()
                                    backPress("Billing added successfully")
                                }

                            }, { error ->
                                error.printStackTrace()
                                BaseActivity.isApiInitiated = false
                                progress_wheel.stopSpinning()
                                if (error != null)
                                    Timber.d("syncShopFromShopList : " + ", SHOP: " + addShop.shop_name + error.localizedMessage)
                                backPress("Billing added successfully")
                            })
            )
        }
        else {
            val repository = AddShopRepositoryProvider.provideAddShopRepository()
            BaseActivity.compositeDisposable.add(
                    repository.addShopWithImage(addShop, shop_imgPath, degree_imgPath, mContext)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                val addShopResult = result as AddShopResponse
                                Timber.d("syncShopFromShopList : " + ", SHOP: " + addShop.shop_name + ", RESPONSE:" + result.message)
                                if (addShopResult.status == NetworkConstant.SUCCESS) {
                                    AppDatabase.getDBInstance()!!.addShopEntryDao().updateIsUploaded(true, addShop.shop_id)

                                    doAsync {
                                        val resultAs = runLongTask(addShop.shop_id)
                                        uiThread {
                                            if (resultAs == true) {
                                                BaseActivity.isApiInitiated = false
                                                getAssignedPPListApi(addShop.shop_id, billing)
                                            }
                                        }
                                    }
                                    progress_wheel.stopSpinning()

                                } else if (addShopResult.status == NetworkConstant.DUPLICATE_SHOP_ID) {
                                    Timber.d("DuplicateShop : " + ", SHOP: " + addShop.shop_name)
                                    AppDatabase.getDBInstance()!!.addShopEntryDao().updateIsUploaded(true, addShop.shop_id)
                                    progress_wheel.stopSpinning()
                                    //(mContext as DashboardActivity).showSnackMessage(addShopResult.message!!)
                                    if (AppDatabase.getDBInstance()!!.addShopEntryDao().getDuplicateShopData(addShop.owner_contact_no).size > 0) {
                                        AppDatabase.getDBInstance()!!.addShopEntryDao().deleteShopById(addShop.shop_id)
                                        AppDatabase.getDBInstance()!!.shopActivityDao().deleteShopByIdAndDate(addShop.shop_id!!, AppUtils.getCurrentDateForShopActi())
                                    }
                                    doAsync {
                                        val resultAs = runLongTask(addShop.shop_id)
                                        uiThread {
                                            if (resultAs == true) {
                                                BaseActivity.isApiInitiated = false
                                                getAssignedPPListApi(addShop.shop_id, billing)
                                            }
                                        }
                                    }
                                } else {
                                    BaseActivity.isApiInitiated = false
                                    progress_wheel.stopSpinning()
                                    backPress("Billing added successfully")
                                }

                            }, { error ->
                                error.printStackTrace()
                                BaseActivity.isApiInitiated = false
                                progress_wheel.stopSpinning()
                                if (error != null)
                                    Timber.d("syncShopFromShopList : " + ", SHOP: " + addShop.shop_name + error.localizedMessage)
                                backPress("Billing added successfully")
                            })
            )
        }
    }

    private fun runLongTask(shop_id: String?): Any {
        val shopActivity = AppDatabase.getDBInstance()!!.shopActivityDao().durationAvailableForShop(shop_id!!, true, false)
        if (shopActivity != null)
            callShopActivitySubmit(shop_id)
        return true
    }

    private var shop_duration = ""
    private var startTimeStamp = ""
    private fun callShopActivitySubmit(shopId: String) {
        var list = AppDatabase.getDBInstance()!!.shopActivityDao().getShopForDay(shopId, AppUtils.getCurrentDateForShopActi())
        if (list.isEmpty())
            return
        var shopDataList: MutableList<ShopDurationRequestData> = java.util.ArrayList()
        var shopDurationApiReq = ShopDurationRequest()
        shopDurationApiReq.user_id = Pref.user_id
        shopDurationApiReq.session_token = Pref.session_token

        if (!Pref.isMultipleVisitEnable) {
            var shopActivity = list[0]

            var shopDurationData = ShopDurationRequestData()
            shopDurationData.shop_id = shopActivity.shopid
            if (shopActivity.startTimeStamp != "0" && !shopActivity.isDurationCalculated) {
                val totalMinute = AppUtils.getMinuteFromTimeStamp(shopActivity.startTimeStamp, System.currentTimeMillis().toString())
                val duration = AppUtils.getTimeFromTimeSpan(shopActivity.startTimeStamp, System.currentTimeMillis().toString())

                AppDatabase.getDBInstance()!!.shopActivityDao().updateTotalMinuteForDayOfShop(shopActivity.shopid!!, totalMinute, AppUtils.getCurrentDateForShopActi())
                AppDatabase.getDBInstance()!!.shopActivityDao().updateTimeDurationForDayOfShop(shopActivity.shopid!!, duration, AppUtils.getCurrentDateForShopActi())

                shopDurationData.spent_duration = duration
            } else {
                shopDurationData.spent_duration = shopActivity.duration_spent
            }
            shopDurationData.visited_date = shopActivity.visited_date
            shopDurationData.visited_time = shopActivity.visited_date
            if (TextUtils.isEmpty(shopActivity.distance_travelled))
                shopActivity.distance_travelled = "0.0"
            shopDurationData.distance_travelled = shopActivity.distance_travelled
            var sList = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdList(shopDurationData.shop_id)
            if (sList != null && sList.isNotEmpty())
                shopDurationData.total_visit_count = sList[0].totalVisitCount

            if (!TextUtils.isEmpty(shopActivity.feedback))
                shopDurationData.feedback = shopActivity.feedback
            else
                shopDurationData.feedback = ""

            shopDurationData.isFirstShopVisited = shopActivity.isFirstShopVisited
            shopDurationData.distanceFromHomeLoc = shopActivity.distance_from_home_loc
            shopDurationData.next_visit_date = shopActivity.next_visit_date

            if (!TextUtils.isEmpty(shopActivity.early_revisit_reason))
                shopDurationData.early_revisit_reason = shopActivity.early_revisit_reason
            else
                shopDurationData.early_revisit_reason = ""

            shopDurationData.device_model = shopActivity.device_model
            shopDurationData.android_version = shopActivity.android_version
            shopDurationData.battery = shopActivity.battery
            shopDurationData.net_status = shopActivity.net_status
            shopDurationData.net_type = shopActivity.net_type
            shopDurationData.in_time = shopActivity.in_time
            shopDurationData.out_time = shopActivity.out_time
            shopDurationData.start_timestamp = shopActivity.startTimeStamp
            shopDurationData.in_location = shopActivity.in_loc
            shopDurationData.out_location = shopActivity.out_loc

            shopDurationData.shop_revisit_uniqKey = shopActivity.shop_revisit_uniqKey!!

            /*10-12-2021*/
            shopDurationData.updated_by = Pref.user_id
            try{
                shopDurationData.updated_on = shopActivity.updated_on!!
            }catch (Ex:Exception){
                shopDurationData.updated_on = ""
            }


            if (!TextUtils.isEmpty(shopActivity.pros_id!!))
                shopDurationData.pros_id = shopActivity.pros_id!!
            else
                shopDurationData.pros_id = ""

            if (!TextUtils.isEmpty(shopActivity.agency_name!!))
                shopDurationData.agency_name =shopActivity.agency_name!!
            else
                shopDurationData.agency_name = ""

            if (!TextUtils.isEmpty(shopActivity.approximate_1st_billing_value))
                shopDurationData.approximate_1st_billing_value = shopActivity.approximate_1st_billing_value!!
            else
                shopDurationData.approximate_1st_billing_value = ""
            //duration garbage fix
            try{
                if(shopDurationData.spent_duration!!.contains("-") || shopDurationData.spent_duration!!.length != 8)
                {
                    shopDurationData.spent_duration="00:00:10"
                }
            }catch (ex:Exception){
                shopDurationData.spent_duration="00:00:10"
            }

            // Suman 06-05-2024 Suman SyncActivity update mantis 27335  begin
            try {
                var shopOb = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(shopDurationData.shop_id)
                shopDurationData.shop_lat=shopOb.shopLat.toString()
                shopDurationData.shop_long=shopOb.shopLong.toString()
                shopDurationData.shop_addr=shopOb.address.toString()
            }catch (ex:Exception){
                ex.printStackTrace()
            }
            // Suman 06-05-2024 Suman SyncActivity update mantis 27335  end

            shopDataList.add(shopDurationData)
        }
        else {
            for (i in list.indices) {
                var shopActivity = list[i]

                var shopDurationData = ShopDurationRequestData()
                shopDurationData.shop_id = shopActivity.shopid
                if (shopActivity.startTimeStamp != "0" && !shopActivity.isDurationCalculated) {
                    val totalMinute = AppUtils.getMinuteFromTimeStamp(shopActivity.startTimeStamp, System.currentTimeMillis().toString())
                    val duration = AppUtils.getTimeFromTimeSpan(shopActivity.startTimeStamp, System.currentTimeMillis().toString())

                    AppDatabase.getDBInstance()!!.shopActivityDao().updateTotalMinuteForDayOfShop(shopActivity.shopid!!, totalMinute, AppUtils.getCurrentDateForShopActi(), shopActivity.startTimeStamp)
                    AppDatabase.getDBInstance()!!.shopActivityDao().updateTimeDurationForDayOfShop(shopActivity.shopid!!, duration, AppUtils.getCurrentDateForShopActi(), shopActivity.startTimeStamp)

                    shopDurationData.spent_duration = duration
                } else {
                    shopDurationData.spent_duration = shopActivity.duration_spent
                }
                shopDurationData.visited_date = shopActivity.visited_date
                shopDurationData.visited_time = shopActivity.visited_date

                if (TextUtils.isEmpty(shopActivity.distance_travelled))
                    shopActivity.distance_travelled = "0.0"

                shopDurationData.distance_travelled = shopActivity.distance_travelled

                var sList = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdList(shopDurationData.shop_id)
                if (sList != null && sList.isNotEmpty())
                    shopDurationData.total_visit_count = sList[0].totalVisitCount

                if (!TextUtils.isEmpty(shopActivity.feedback))
                    shopDurationData.feedback = shopActivity.feedback
                else
                    shopDurationData.feedback = ""

                shopDurationData.isFirstShopVisited = shopActivity.isFirstShopVisited
                shopDurationData.distanceFromHomeLoc = shopActivity.distance_from_home_loc
                shopDurationData.next_visit_date = shopActivity.next_visit_date

                if (!TextUtils.isEmpty(shopActivity.early_revisit_reason))
                    shopDurationData.early_revisit_reason = shopActivity.early_revisit_reason
                else
                    shopDurationData.early_revisit_reason = ""

                shopDurationData.device_model = shopActivity.device_model
                shopDurationData.android_version = shopActivity.android_version
                shopDurationData.battery = shopActivity.battery
                shopDurationData.net_status = shopActivity.net_status
                shopDurationData.net_type = shopActivity.net_type
                shopDurationData.in_time = shopActivity.in_time
                shopDurationData.out_time = shopActivity.out_time
                shopDurationData.start_timestamp = shopActivity.startTimeStamp
                shopDurationData.in_location = shopActivity.in_loc
                shopDurationData.out_location = shopActivity.out_loc

                shopDurationData.shop_revisit_uniqKey = shopActivity.shop_revisit_uniqKey!!

                /*10-12-2021*/
                shopDurationData.updated_by = Pref.user_id

                try{
                    shopDurationData.updated_on = shopActivity.updated_on!!
                }catch (ex:Exception){
                    shopDurationData.updated_on =""
                }


                if (!TextUtils.isEmpty(shopActivity.pros_id!!))
                    shopDurationData.pros_id = shopActivity.pros_id!!
                else
                    shopDurationData.pros_id = ""

                if (!TextUtils.isEmpty(shopActivity.agency_name!!))
                    shopDurationData.agency_name =shopActivity.agency_name!!
                else
                    shopDurationData.agency_name = ""

                if (!TextUtils.isEmpty(shopActivity.approximate_1st_billing_value))
                    shopDurationData.approximate_1st_billing_value = shopActivity.approximate_1st_billing_value!!
                else
                    shopDurationData.approximate_1st_billing_value = ""
                //duration garbage fix
                try{
                    if(shopDurationData.spent_duration!!.contains("-") || shopDurationData.spent_duration!!.length != 8)
                    {
                        shopDurationData.spent_duration="00:00:10"
                    }
                }catch (ex:Exception){
                    shopDurationData.spent_duration="00:00:10"
                }
                //New shop Create issue
                shopDurationData.isnewShop = shopActivity.isnewShop!!

                // 1.0 AddBillingFragment AppV 4.0.6  multiple contact Data added on Api called
                shopDurationData.multi_contact_name = shopActivity.multi_contact_name
                shopDurationData.multi_contact_number = shopActivity.multi_contact_number

                // Suman 06-05-2024 Suman SyncActivity update mantis 27335  begin
                try {
                    var shopOb = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(shopDurationData.shop_id)
                    shopDurationData.shop_lat=shopOb.shopLat.toString()
                    shopDurationData.shop_long=shopOb.shopLong.toString()
                    shopDurationData.shop_addr=shopOb.address.toString()
                }catch (ex:Exception){
                    ex.printStackTrace()
                }
                // Suman 06-05-2024 Suman SyncActivity update mantis 27335  end

                shopDataList.add(shopDurationData)
            }
        }

        if (shopDataList.isEmpty()) {
            return
        }

        shopDurationApiReq.shop_list = shopDataList
        val repository = ShopDurationRepositoryProvider.provideShopDurationRepository()

        BaseActivity.compositeDisposable.add(
                repository.shopDuration(shopDurationApiReq)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            Timber.d("syncShopActivityFromShopList : " + ", SHOP: " + list[0].shop_name + ", RESPONSE:" + result.message)
                            if (result.status == NetworkConstant.SUCCESS) {

                            }

                        }, { error ->
                            error.printStackTrace()
                            if (error != null)
                                Timber.d("syncShopActivityFromShopList : " + ", SHOP: " + list[0].shop_name + error.localizedMessage)
//                                (mContext as DashboardActivity).showSnackMessage("ERROR")
                        })
        )

    }

    private fun getAssignedPPListApi(shop_id: String?, billing: BillingEntity) {

        val shopActivityList = AppDatabase.getDBInstance()!!.shopActivityDao().getShopForDay(shop_id!!, AppUtils.getCurrentDateForShopActi())

        /*if (shopActivityList[0].isVisited && shopActivityList[0].isDurationCalculated) {
            if (!Pref.isMultipleVisitEnable)
                AppDatabase.getDBInstance()!!.shopActivityDao().updateisUploaded(true, shop_id, AppUtils.getCurrentDateForShopActi())
            else
                AppDatabase.getDBInstance()!!.shopActivityDao().updateisUploaded(true, shop_id, AppUtils.getCurrentDateForShopActi(), startTimeStamp)
            Timber.d("============sync locally shop visited (Add Billing)==========")
        }*/

        shopActivityList?.forEach {
            if (it.isVisited && it.isDurationCalculated) {
                if (!Pref.isMultipleVisitEnable)
                    AppDatabase.getDBInstance()!!.shopActivityDao().updateisUploaded(true, shop_id, AppUtils.getCurrentDateForShopActi())
                else
                    AppDatabase.getDBInstance()!!.shopActivityDao().updateisUploaded(true, shop_id, AppUtils.getCurrentDateForShopActi(), startTimeStamp)
                Timber.d("============sync locally shop visited (Add Billing)==========")
            }
        }

        if (BaseActivity.isApiInitiated)
            return

        BaseActivity.isApiInitiated = true

        val repository = AssignToPPListRepoProvider.provideAssignPPListRepository()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.assignToPPList(Pref.profile_state)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as AssignToPPListResponseModel
                            if (response.status == NetworkConstant.SUCCESS) {
                                val list = response.assigned_to_pp_list

                                if (list != null && list.isNotEmpty()) {

                                    doAsync {

                                        val assignPPList = AppDatabase.getDBInstance()?.ppListDao()?.getAll()
                                        if (assignPPList != null)
                                            AppDatabase.getDBInstance()?.ppListDao()?.delete()

                                        for (i in list.indices) {
                                            val assignToPP = AssignToPPEntity()
                                            assignToPP.pp_id = list[i].assigned_to_pp_id
                                            assignToPP.pp_name = list[i].assigned_to_pp_authorizer_name
                                            assignToPP.pp_phn_no = list[i].phn_no
                                            AppDatabase.getDBInstance()?.ppListDao()?.insert(assignToPP)
                                        }

                                        uiThread {
                                            BaseActivity.isApiInitiated = false
                                            progress_wheel.stopSpinning()
                                            getAssignedDDListApi(shop_id, billing)
                                        }
                                    }
                                } else {
                                    BaseActivity.isApiInitiated = false
                                    progress_wheel.stopSpinning()
                                    getAssignedDDListApi(shop_id, billing)
                                }
                            } else {
                                BaseActivity.isApiInitiated = false
                                progress_wheel.stopSpinning()
                                getAssignedDDListApi(shop_id, billing)
                            }

                        }, { error ->
                            error.printStackTrace()
                            BaseActivity.isApiInitiated = false
                            progress_wheel.stopSpinning()
                            getAssignedDDListApi(shop_id, billing)
                        })
        )
    }

    private fun getAssignedDDListApi(shop_id: String?, billing: BillingEntity) {

        if (BaseActivity.isApiInitiated)
            return

        BaseActivity.isApiInitiated = true

        val repository = AssignToDDListRepoProvider.provideAssignDDListRepository()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.assignToDDList(Pref.profile_state)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as AssignToDDListResponseModel
                            if (response.status == NetworkConstant.SUCCESS) {
                                val list = response.assigned_to_dd_list

                                if (list != null && list.isNotEmpty()) {

                                    doAsync {

                                        val assignDDList = AppDatabase.getDBInstance()?.ddListDao()?.getAll()
                                        if (assignDDList != null)
                                            AppDatabase.getDBInstance()?.ddListDao()?.delete()

                                        for (i in list.indices) {
                                            val assignToDD = AssignToDDEntity()
                                            assignToDD.dd_id = list[i].assigned_to_dd_id
                                            assignToDD.dd_name = list[i].assigned_to_dd_authorizer_name
                                            assignToDD.dd_phn_no = list[i].phn_no
                                            assignToDD.pp_id = list[i].assigned_to_pp_id
                                            assignToDD.type_id = list[i].type_id
                                            assignToDD.dd_latitude = list[i].dd_latitude
                                            assignToDD.dd_longitude = list[i].dd_longitude
                                            AppDatabase.getDBInstance()?.ddListDao()?.insert(assignToDD)
                                        }

                                        uiThread {
                                            BaseActivity.isApiInitiated = false
                                            progress_wheel.stopSpinning()
                                            getAssignedToShopApi(shop_id, billing)
                                        }
                                    }
                                } else {
                                    BaseActivity.isApiInitiated = false
                                    progress_wheel.stopSpinning()
                                    getAssignedToShopApi(shop_id, billing)
                                }
                            } else {
                                BaseActivity.isApiInitiated = false
                                progress_wheel.stopSpinning()
                                getAssignedToShopApi(shop_id, billing)
                            }

                        }, { error ->
                            error.printStackTrace()
                            BaseActivity.isApiInitiated = false
                            progress_wheel.stopSpinning()
                            getAssignedToShopApi(shop_id, billing)
                        })
        )
    }

    private fun getAssignedToShopApi(shop_id: String?, billing: BillingEntity) {

        if (BaseActivity.isApiInitiated)
            return

        BaseActivity.isApiInitiated = true

        val repository = TypeListRepoProvider.provideTypeListRepository()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.assignToShopList(Pref.profile_state)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as AssignedToShopListResponseModel
                            if (response.status == NetworkConstant.SUCCESS) {
                                val list = response.shop_list

                                AppDatabase.getDBInstance()?.assignToShopDao()?.delete()

                                doAsync {
                                    list?.forEach {
                                        val shop = AssignToShopEntity()
                                        AppDatabase.getDBInstance()?.assignToShopDao()?.insert(shop.apply {
                                            assigned_to_shop_id = it.assigned_to_shop_id
                                            name = it.name
                                            phn_no = it.phn_no
                                            type_id = it.type_id
                                        })
                                    }

                                    uiThread {
                                        progress_wheel.stopSpinning()
                                        BaseActivity.isApiInitiated = false
                                        checkToCallOrderApi(billing)
                                    }
                                }
                            }
                            else {
                                progress_wheel.stopSpinning()
                                BaseActivity.isApiInitiated = false
                                checkToCallOrderApi(billing)
                            }

                        }, { error ->
                            progress_wheel.stopSpinning()
                            error.printStackTrace()
                            BaseActivity.isApiInitiated = false
                            checkToCallOrderApi(billing)
                        })
        )
    }


    private fun checkToCallOrderApi(billing: BillingEntity) {
        if (!order?.isUploaded!!) {
            syncAddOrderApi(order?.shop_id, order?.order_id, order?.amount!!, order?.date!!, order?.remarks, order?.signature, order?.order_lat,
                    order?.order_long, billing, order)
        } else {
            callAddBillApi(billing)
        }
    }


    private fun syncAddOrderApi(shop_id: String?, order_id: String?, amount: String, date: String, remarks: String?, signature: String?,
                                orderLat: String?, orderLong: String?, billing: BillingEntity, orderListDetails: OrderDetailsListEntity?) {

        if (BaseActivity.isApiInitiated)
            return

        BaseActivity.isApiInitiated = true

        val addOrder = AddOrderInputParamsModel()
        addOrder.collection = ""
        addOrder.description = ""
        addOrder.order_amount = amount
        addOrder.order_date = date //AppUtils.getCurrentDateFormatInTa(date)
        addOrder.order_id = order_id
        addOrder.shop_id = shop_id
        addOrder.session_token = Pref.session_token
        addOrder.user_id = Pref.user_id
        addOrder.latitude = orderLat
        addOrder.longitude = orderLong

        if (orderListDetails!!.scheme_amount != null)
            addOrder.scheme_amount = orderListDetails!!.scheme_amount
        else
            addOrder.scheme_amount = ""



        if (remarks != null)
            addOrder.remarks = remarks
        else
            addOrder.remarks = ""

        if (orderListDetails?.patient_name != null)
            addOrder.patient_name = orderListDetails.patient_name
        else
            addOrder.patient_name = ""

        if (orderListDetails?.patient_address != null)
            addOrder.patient_address = orderListDetails.patient_address
        else
            addOrder.patient_address = ""

        if (orderListDetails?.patient_no != null)
            addOrder.patient_no = orderListDetails.patient_no
        else
            addOrder.patient_no = ""

        val shopActivity = AppDatabase.getDBInstance()!!.shopActivityDao().getShopActivityForId(shop_id!!)
        if (shopActivity != null) {
            if (shopActivity.isVisited && !shopActivity.isDurationCalculated && shopActivity.date == AppUtils.getCurrentDateForShopActi()) {
                val shopDetail = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(shop_id)

                if (!TextUtils.isEmpty(shopDetail.address))
                    addOrder.address = shopDetail.address
                else
                    addOrder.address = ""
            } else {
                if (!TextUtils.isEmpty(orderLat) && !TextUtils.isEmpty(orderLong))
                    addOrder.address = LocationWizard.getLocationName(mContext, orderLat!!.toDouble(), orderLong!!.toDouble())
                else
                    addOrder.address = ""
            }
        } else {
            if (!TextUtils.isEmpty(orderLat) && !TextUtils.isEmpty(orderLong))
                addOrder.address = LocationWizard.getLocationName(mContext, orderLat!!.toDouble(), orderLong!!.toDouble())
            else
                addOrder.address = ""
        }

        /*06-01-2022*/
        if (orderListDetails?.Hospital != null)
            addOrder.Hospital = orderListDetails?.Hospital
        else
            addOrder.Hospital = ""

        if (orderListDetails?.Email_Address != null)
            addOrder.Email_Address = orderListDetails?.Email_Address
        else
            addOrder.Email_Address = ""

        val list = AppDatabase.getDBInstance()!!.orderProductListDao().getDataAccordingToShopAndOrderId(order_id!!, shop_id!!)
        val productList = ArrayList<AddOrderInputProductList>()

        for (i in list.indices) {
            val product = AddOrderInputProductList()
            product.id = list[i].product_id
            product.qty = list[i].qty
            product.rate = list[i].rate
            product.total_price = list[i].total_price
            product.product_name = list[i].product_name
            product.scheme_qty = list[i].scheme_qty
            product.scheme_rate = list[i].scheme_rate
            product.total_scheme_price = list[i].total_scheme_price

            product.MRP = list[i].MRP

            //mantis 25601
            product.order_mrp = list[i].order_mrp
            product.order_discount = list[i].order_discount

            productList.add(product)
        }

        addOrder.product_list = productList

        progress_wheel.spin()

        if (TextUtils.isEmpty(signature)) {
            val repository = AddOrderRepoProvider.provideAddOrderRepository()
            BaseActivity.compositeDisposable.add(
                    repository.addNewOrder(addOrder)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                val orderList = result as BaseResponse
                                progress_wheel.stopSpinning()
                                BaseActivity.isApiInitiated = false
                                if (orderList.status == NetworkConstant.SUCCESS) {
                                    AppDatabase.getDBInstance()!!.orderDetailsListDao().updateIsUploaded(true, order_id)
                                    callAddBillApi(billing)
                                } else
                                    backPress("Billing added successfully")

                            }, { error ->
                                error.printStackTrace()
                                BaseActivity.isApiInitiated = false
                                progress_wheel.stopSpinning()
                                backPress("Billing added successfully")
                            })
            )
        }
        else {
            val repository = AddOrderRepoProvider.provideAddOrderImageRepository()
            BaseActivity.compositeDisposable.add(
                    repository.addNewOrder(addOrder, signature!!, mContext)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                val orderList = result as BaseResponse
                                progress_wheel.stopSpinning()
                                BaseActivity.isApiInitiated = false
                                if (orderList.status == NetworkConstant.SUCCESS) {
                                    AppDatabase.getDBInstance()!!.orderDetailsListDao().updateIsUploaded(true, order_id)
                                    callAddBillApi(billing)
                                } else
                                    backPress("Billing added successfully")

                            }, { error ->
                                error.printStackTrace()
                                BaseActivity.isApiInitiated = false
                                progress_wheel.stopSpinning()
                                backPress("Billing added successfully")
                            })
            )
        }
    }


    private fun callAddBillApi(billing: BillingEntity) {

        if (BaseActivity.isApiInitiated)
            return

        BaseActivity.isApiInitiated = true

        val addBill = AddBillingInputParamsModel()
        addBill.bill_id = billing.bill_id
        addBill.invoice_amount = billing.invoice_amount
        addBill.invoice_date = billing.invoice_date
        addBill.invoice_no = billing.invoice_no
        addBill.remarks = billing.remarks
        addBill.order_id = billing.order_id
        addBill.session_token = Pref.session_token!!
        addBill.user_id = Pref.user_id!!
        addBill.patient_no = billing.patient_no
        addBill.patient_name = billing.patient_name
        addBill.patient_address = billing.patient_address

        val list = AppDatabase.getDBInstance()!!.billProductDao().getDataAccordingToBillId(addBill.bill_id)
        val productList = ArrayList<AddOrderInputProductList>()

        for (i in list.indices) {
            val product = AddOrderInputProductList()
            product.id = list[i].product_id
            product.qty = list[i].qty
            product.rate = list[i].rate
            product.total_price = list[i].total_price
            product.product_name = list[i].product_name
            productList.add(product)
        }

        addBill.product_list = productList

        Timber.d("======ADD BILLING DETAILS INPUT PARAMS (ADD BILLING FRAGMENT)======")
        Timber.d("USER ID===> " + addBill.user_id)
        Timber.d("SESSION ID====> " + addBill.session_token)
        Timber.d("BILL ID====> " + addBill.bill_id)
        Timber.d("INVOICE NO.====> " + addBill.invoice_no)
        Timber.d("INVOICE DATE====> " + addBill.invoice_date)
        Timber.d("INVOICE AMOUNT====> " + addBill.invoice_amount)
        Timber.d("REMARKS====> " + addBill.remarks)
        Timber.d("ORDER ID====> " + addBill.order_id)

        try {
            Timber.d("PATIENT NO====> " + addBill.patient_no)
            Timber.d("PATIENT NAME====> " + addBill.patient_name)
            Timber.d("PATIENT ADDRESS====> " + addBill.patient_address)
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

        if (!TextUtils.isEmpty(billing.attachment))
            Timber.d("ATTACHMENT=======> " + billing.attachment)

        Timber.d("PRODUCT LIST SIZE====> " + addBill.product_list?.size)
        Timber.d("=====================================================================")


        if (!TextUtils.isEmpty(billing.attachment)) {
            val repository = AddBillingRepoProvider.addBillImageRepository()
            progress_wheel.spin()
            BaseActivity.compositeDisposable.add(
                    repository.addBillingDetailsMultipart(addBill, billing.attachment, mContext)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                val baseResponse = result as BaseResponse
                                Timber.d("ADD BILLING DETAILS : " + "RESPONSE : " + baseResponse.status + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ", MESSAGE : " + baseResponse.message)

                                progress_wheel.stopSpinning()
                                BaseActivity.isApiInitiated = false

                                if (baseResponse.status == NetworkConstant.SUCCESS) {

                                    AppDatabase.getDBInstance()!!.billingDao().updateIsUploadedBillingIdWise(true, addBill.bill_id)
                                    backPress(baseResponse.message!!)

                                } else {
                                    backPress("Billing added successfully")
                                }
                            }, { error ->
                                Timber.d("ADD BILLING DETAILS : " + "ERROR : " + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ", MESSAGE : " + error.localizedMessage)
                                error.printStackTrace()
                                BaseActivity.isApiInitiated = false
                                progress_wheel.stopSpinning()
                                backPress("Billing added successfully")
                            })
            )
        } else {
            val repository = AddBillingRepoProvider.addBillRepository()
            progress_wheel.spin()
            BaseActivity.compositeDisposable.add(
                    repository.addBillingDetails(addBill)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({ result ->
                                val baseResponse = result as BaseResponse
                                Timber.d("ADD BILLING DETAILS : " + "RESPONSE : " + baseResponse.status + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ", MESSAGE : " + baseResponse.message)

                                progress_wheel.stopSpinning()
                                BaseActivity.isApiInitiated = false

                                if (baseResponse.status == NetworkConstant.SUCCESS) {

                                    AppDatabase.getDBInstance()!!.billingDao().updateIsUploadedBillingIdWise(true, addBill.bill_id)
                                    backPress(baseResponse.message!!)

                                } else {
                                    backPress("Billing added successfully")
                                }
                            }, { error ->
                                Timber.d("ADD BILLING DETAILS : " + "ERROR : " + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ", MESSAGE : " + error.localizedMessage)
                                error.printStackTrace()
                                BaseActivity.isApiInitiated = false
                                progress_wheel.stopSpinning()
                                backPress("Billing added successfully")
                            })
            )
        }
    }

    val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        // TODO Auto-generated method stub
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, monthOfYear)
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        tv_invoice_date.text = AppUtils.getBillFormattedDate(myCalendar.time)
    }

    private fun backPress(msg: String) {
        (mContext as DashboardActivity).showSnackMessage(msg)

        if (Pref.isVoiceEnabledForInvoiceSaved) {
            val speechStatus = (mContext as DashboardActivity).textToSpeech.speak("Hi, Invoice  saved successfully.", TextToSpeech.QUEUE_FLUSH, null)
            if (speechStatus == TextToSpeech.ERROR)
                Log.e("Add Billing", "TTS error in converting Text to Speech!");

        }

        Handler().postDelayed(Runnable {
            (mContext as DashboardActivity).onBackPressed()
        }, 500)
    }

    fun setGalleryImage(data: Intent) {
        val filePath = ImagePickerManager.getImagePathFromData(data, mContext)
        val file = File(filePath)
        val strFileName = file.name
        tv_attachment.text = strFileName

        dataPath = file.absolutePath
    }

    fun setCameraImage(file: File) {
        tv_attachment.text = file.name

        dataPath = file.absolutePath
    }

    fun onConfirmClick() {

        // stock check work
        try {
            if(Pref.IsStockCheckFeatureOn && Pref.StockCheckOnOrder1OrInvioce0 == false){
                var temp = productList!!

                for(i in 0..temp.size-1){
                    var ob = temp.get(i)
                    var stock :StockAllEntity = StockAllEntity()
                    if(Pref.IsShowDistributorWiseCurrentStockInOrder){
                        stock = (AppDatabase.getDBInstance()!!.stockAllDao().getStockDtlsForDD(ob.product_id!!,ob.shop_id!!  ) as ArrayList<StockAllEntity>).first()
                    }else{
                        stock = (AppDatabase.getDBInstance()!!.stockAllDao().getStockDtls(ob.product_id!!,ob.shop_id!!  ) as ArrayList<StockAllEntity>).first()
                    }
                    if(stock!=null && Pref.IsAllowNegativeStock == false){
                        var balanceStock = stock.stock_productbalqty.toDouble()
                        var enteringQty = (mContext as DashboardActivity).qtyList[i].toDouble()
                        if(enteringQty>balanceStock){
                            ToasterCustom.msgShort(mContext,"Stock not available.")
                            return
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //begin 2.0 AddBillingFragment AppV 4.2.2 Suman 16-10-2023 mantis id 26908
        var ordAmt = 0.0
        try{
            var ordDtls = AppDatabase.getDBInstance()!!.orderDetailsListDao().getSingleOrder(order!!.order_id!!)
            ordAmt = ordDtls.amount.toString().toDouble()
        }catch (ex:Exception){
            ex.printStackTrace()
        }
        //end 2.0 AddBillingFragment AppV 4.2.2 Suman 16-10-2023 mantis id 26908

        val list = AppDatabase.getDBInstance()!!.billingDao().getDataOrderIdWise(order?.order_id!!)
        var isInvoiceNoAvailable = false

        if (TextUtils.isEmpty(et_invoice_no.text.toString().trim()))
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.error_enter_invoice_no))
        else if (TextUtils.isEmpty(tv_invoice_date.text.toString().trim()))
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.error_enter_invoice_date))
        else if (TextUtils.isEmpty(et_invoice_amount.text.toString().trim()))
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.error_enter_invoice_amount))
        else if (Pref.willAttachmentCompulsory && TextUtils.isEmpty(/*tv_attachment.text.toString().trim()*/ dataPath))
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.error_select_attachment))
        else if(ordAmt<et_invoice_amount.text.toString().trim().toDouble()){//begin 2.0 AddBillingFragment AppV 4.2.2 Suman 16-10-2023 mantis id 26908
            AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
            //(mContext as DashboardActivity).showSnackMessage("Invoice value is more than Order value. Cannot Proceed.")
            val simpleDialog = Dialog(mContext)
            simpleDialog.setCancelable(false)
            simpleDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            simpleDialog.setContentView(R.layout.dialog_ok)

            try {
                simpleDialog.setCancelable(true)
                simpleDialog.setCanceledOnTouchOutside(false)
                val dialogName = simpleDialog.findViewById(R.id.tv_dialog_ok_name) as AppCustomTextView
                val dialogCross = simpleDialog.findViewById(R.id.tv_dialog_ok_cancel) as ImageView
                dialogName.text = AppUtils.hiFirstNameText()
                dialogCross.setOnClickListener {
                    simpleDialog.cancel()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val dialogHeader = simpleDialog.findViewById(R.id.dialog_yes_header_TV) as AppCustomTextView
            dialogHeader.text = "Invoice value is more than Order value. Cannot Proceed."
            val dialogYes = simpleDialog.findViewById(R.id.tv_dialog_yes) as AppCustomTextView
            dialogYes.setOnClickListener({ view ->
                simpleDialog.cancel()
            })
            simpleDialog.show()
        }//end 2.0 AddBillingFragment AppV 4.2.2 Suman 16-10-2023 mantis id 26908
        else {
            if (list != null && list.isNotEmpty()) {

                var savedInvoiceAmount = 0.0

                for (i in list.indices) {

                    savedInvoiceAmount += list[i].invoice_amount.toDouble()

                    if (list[i].invoice_no.equals(et_invoice_no.text.toString().trim(), ignoreCase = true)) {
                        isInvoiceNoAvailable = true
                        break
                    } else
                        isInvoiceNoAvailable = false
                }

                if (!et_invoice_amount.text.toString().trim().toDouble().toString().equals(tv_total_order_amount.text.toString().trim().toDouble().toString(), ignoreCase = true)) {
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.error_invoice_amount_mismatch))
                }
                /*else if (et_invoice_amount.text.toString().trim().toDouble() > (order?.amount?.toDouble()!! - savedInvoiceAmount))
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.error_invoice_amount_cannot_greater_than_order))*/
                else {
                    if (isInvoiceNoAvailable)
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.error_invoice_no_available))
                    else
                        checkForEmptyProductValue()

                }
            } else {
                if (!et_invoice_amount.text.toString().trim().toDouble().toString().equals(tv_total_order_amount.text.toString().trim().toDouble().toString(), ignoreCase = true)) {
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.error_invoice_amount_mismatch))
                }
                /*else if (et_invoice_amount.text.toString().trim().toDouble() > order?.amount?.toDouble()!!)
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.error_invoice_amount_cannot_greater_than_order))*/
                else
                    checkForEmptyProductValue()
            }
        }

        /*if (TextUtils.isEmpty(et_invoice_no.text.toString().trim()))
            (mContext as DashboardActivity).showSnackMessage("Please enter invoice number")
        else if (TextUtils.isEmpty(et_invoice_amount.text.toString().trim()))
            (mContext as DashboardActivity).showSnackMessage("Please enter invoice amount")
        else {
            if (!et_invoice_amount.text.toString().trim().toDouble().toString().equals(tv_total_order_amount.text.toString().trim().toDouble().toString(), ignoreCase = true)) {
                (mContext as DashboardActivity).showSnackMessage("Invoice amount mismatch. Check rate & qty.")
            } else
                saveData()
        }*/
    }

    private fun checkForEmptyProductValue() {
        var isQtyEmpty = false
        var isRateEmpty = false

        for (i in (mContext as DashboardActivity).qtyList.indices) {
            if (TextUtils.isEmpty((mContext as DashboardActivity).qtyList[i])) {
                isQtyEmpty = true
                break
            }
        }

        for (i in (mContext as DashboardActivity).rateList.indices) {
            if (TextUtils.isEmpty((mContext as DashboardActivity).rateList[i])) {
                isRateEmpty = true
                break
            }
        }

        if (!isQtyEmpty && !isRateEmpty)
            showBillingCheckAlert()
        else if (isQtyEmpty)
            (mContext as DashboardActivity).showSnackMessage("Please enter all quantity")
        else if (isRateEmpty)
            (mContext as DashboardActivity).showSnackMessage("Please enter all rate")

    }
}