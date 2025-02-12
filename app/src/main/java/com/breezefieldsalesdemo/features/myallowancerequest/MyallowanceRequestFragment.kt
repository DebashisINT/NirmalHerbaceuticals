package com.breezefieldsalesdemo.features.myallowancerequest

import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.breezefieldsalesdemo.R
import com.breezefieldsalesdemo.app.types.FragType
import com.breezefieldsalesdemo.base.presentation.BaseFragment
import com.breezefieldsalesdemo.features.dashboard.presentation.DashboardActivity
import com.breezefieldsalesdemo.features.myorder.presentation.MyOrderListClickListener

/**
 * Created by Kinsuk on 30-10-2017.
 */

class MyallowanceRequestFragment : BaseFragment(){

    private lateinit var mMyallowanceRequestAdapter: MyallowanceRequestAdapter
    private lateinit var myallowanceRecyclerView: RecyclerView
    private lateinit var mContext: Context
    private lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater!!.inflate(R.layout.fragment_my_allowance_request, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        myallowanceRecyclerView=view.findViewById(R.id.my_allowance_list_RCV)
        initAdapter()
    }

    private fun initAdapter() {
        mMyallowanceRequestAdapter = MyallowanceRequestAdapter(this!!.context!!, object : MyOrderListClickListener {
            override fun OnOrderListClick(position: Int) {
                (mContext as DashboardActivity).loadFragment(FragType.OrderDetailFragment,true,"")
            }
        })
        layoutManager = LinearLayoutManager(mContext, LinearLayout.VERTICAL, false)
        myallowanceRecyclerView.layoutManager=layoutManager
        myallowanceRecyclerView.adapter=mMyallowanceRequestAdapter

    }



}