package com.breezefieldsalesdemo.features.achievement

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.breezefieldsalesdemo.R

class Fragment_Revisit : Fragment() {

    private lateinit var rv_order_taken: RecyclerView
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_revisit, container, false)

        rv_order_taken = view.findViewById<View>(R.id.rv_revisit_list) as RecyclerView
        rv_order_taken.layoutManager = LinearLayoutManager(mContext)
        rv_order_taken.addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))

        val adapter = CustomRecyclerViewAdapter(mContext, "revisit")
        rv_order_taken.adapter = adapter

        return view
    }
}
