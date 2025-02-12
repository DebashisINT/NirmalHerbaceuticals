package com.breezefieldsalesdemo.features.addAttendence

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldsalesdemo.R
import com.breezefieldsalesdemo.app.domain.AssignToDDEntity
import com.breezefieldsalesdemo.widgets.AppCustomTextView

class DDAssignedWiseBeatListCustomDialog : DialogFragment() {

    private lateinit var header: AppCustomTextView
    private lateinit var close: ImageView
    private lateinit var rv_gender: RecyclerView
    private  var adapter: DDAssignedWiseBeatTypeListAdapter? = null
    private lateinit var mContext: Context

    companion object{
        private lateinit var onSelectItem: (AssignToDDEntity) -> Unit
        private var mList: ArrayList<AssignToDDEntity>? = null

        fun newInstance(gList: ArrayList<AssignToDDEntity>, function: (AssignToDDEntity) -> Unit): DDAssignedWiseBeatListCustomDialog {
            val dialogFragment = DDAssignedWiseBeatListCustomDialog()
            mList = gList
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
        dialog?.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val v = inflater.inflate(R.layout.dialog_gender_list, container, false)

        isCancelable = false

        initView(v)
        return v
    }

    private fun initView(v: View){
        header=v.findViewById(R.id.tv_dialog_gender_list_header)
        close=v.findViewById(R.id.iv_dialog_gender_list_close_icon)
        rv_gender=v.findViewById(R.id.rv_dialog_gender_list)
        rv_gender.layoutManager = LinearLayoutManager(mContext)


        var str = "Select Distributor"
        header.text=str



        adapter= DDAssignedWiseBeatTypeListAdapter(mContext, mList!!,object: DDAssignedWiseBeatTypeListAdapter.beatNameOnClick {
            override fun OnClick(data: AssignToDDEntity) {
                dismiss()
                onSelectItem(data)
            }
        })
        rv_gender.adapter=adapter

        close.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                dismiss()
            }
        }
    }

}