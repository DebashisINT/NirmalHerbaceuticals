package com.breezefieldsalesdemo.features.addshop.presentation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import com.breezefieldsalesdemo.R
import com.breezefieldsalesdemo.app.domain.AreaListEntity
import com.breezefieldsalesdemo.widgets.AppCustomEditText
import com.breezefieldsalesdemo.widgets.AppCustomTextView

/**
 * Created by Saikat on 14-May-20.
 */
class AreaListDialog : DialogFragment() {

    private lateinit var rv_common_dialog_list: RecyclerView
    private lateinit var mContext: Context
    private lateinit var dialog_header_TV: AppCustomTextView
    private lateinit var et_search: AppCustomEditText
    private var adapter: AreaListAdapter? = null
    private lateinit var iv_close_icon: ImageView

    companion object {

        private lateinit var onSelectItem: (AreaListEntity) -> Unit
        private var mAreaList: ArrayList<AreaListEntity>? = null

        fun newInstance(areaList: ArrayList<AreaListEntity>?, function: (AreaListEntity) -> Unit): AreaListDialog {
            val dialogFragment = AreaListDialog()
            mAreaList = areaList
            onSelectItem = function
            return dialogFragment
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        dialog?.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCanceledOnTouchOutside(true)

        val v = inflater.inflate(R.layout.dialog_list, container, false)

        isCancelable = false

        initView(v)
        initTextChangeListener()
        return v
    }

    private fun initView(v: View) {
        dialog_header_TV = v.findViewById(R.id.dialog_header_TV)
        rv_common_dialog_list = v.findViewById(R.id.rv_common_dialog_list)
        rv_common_dialog_list.layoutManager = LinearLayoutManager(mContext)
        //rv_common_dialog_list.visibility = View.GONE
        adapter = AreaListAdapter(mContext, mAreaList, { area: AreaListEntity ->
            dismiss()
            //AppUtils.hideSoftKeyboardFromDialog((mContext as DashboardActivity))
            onSelectItem(area)

        }, { isShowList: Boolean ->
            if (isShowList) {
                if (!TextUtils.isEmpty(et_search.text.toString().trim()) && et_search.text.toString().trim().length >= 3)
                    rv_common_dialog_list.visibility = View.VISIBLE
                else
                    rv_common_dialog_list.visibility = View.GONE
            } else
                rv_common_dialog_list.visibility = View.GONE
        })

        rv_common_dialog_list.adapter = adapter

        dialog_header_TV.text = "Area List"
        et_search = v.findViewById(R.id.et_search)
        iv_close_icon = v.findViewById(R.id.iv_close_icon)

        iv_close_icon.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                dismiss()
            }
        }
    }

    private fun initTextChangeListener() {
        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!TextUtils.isEmpty(et_search.text.toString().trim()) && et_search.text.toString().trim().length >= 3)
                    adapter?.filter?.filter(et_search.text.toString().trim())
            }
        })
    }
}