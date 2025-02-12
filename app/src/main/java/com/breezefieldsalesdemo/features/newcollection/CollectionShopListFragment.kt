package com.breezefieldsalesdemo.features.newcollection

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.breezefieldsalesdemo.R
import com.breezefieldsalesdemo.app.AppDatabase
import com.breezefieldsalesdemo.app.NetworkConstant
import com.breezefieldsalesdemo.app.SearchListener
import com.breezefieldsalesdemo.app.types.FragType
import com.breezefieldsalesdemo.app.utils.AppUtils
import com.breezefieldsalesdemo.base.presentation.BaseActivity
import com.breezefieldsalesdemo.base.presentation.BaseFragment
import com.breezefieldsalesdemo.features.dashboard.presentation.DashboardActivity
import com.breezefieldsalesdemo.features.newcollection.model.CollectionShopListDataModel
import com.breezefieldsalesdemo.features.newcollection.model.CollectionShopListResponseModel
import com.breezefieldsalesdemo.features.newcollection.newcollectionlistapi.NewCollectionListRepoProvider
import com.breezefieldsalesdemo.features.photoReg.adapter.AdapterUserList
import com.breezefieldsalesdemo.widgets.AppCustomTextView
import com.pnikosis.materialishprogress.ProgressWheel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

// Rev 1.0 CollectionShopListFragment Suman 04/05/2023 Search option for shops  mantis id - 26026

class CollectionShopListFragment : BaseFragment() {

    private lateinit var mContext: Context

    private lateinit var rv_collection_shop_list: RecyclerView
    private lateinit var tv_no_data_available: AppCustomTextView
    private lateinit var progress_wheel: ProgressWheel
    private lateinit var rl_collection_shop_list_main: RelativeLayout

    private var isToday = false
    private var date = ""

    private var amountL: ArrayList<CollectionShopListDataModel> = ArrayList()
    private var adapter: CollectionShopAdapter? = null

    companion object {
        fun newInstance(isToday: Any): CollectionShopListFragment {
            val fragment = CollectionShopListFragment()

            if (isToday is Boolean) {
                val bundle = Bundle()
                bundle.putBoolean("isToday", isToday)
                fragment.arguments = bundle
            }

            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context

        isToday = arguments?.getBoolean("isToday")!!
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_collection_shop_list, container, false)

        initView(view)

        if (isToday)
            date = AppUtils.getCurrentDateForShopActi()

        getList()

        // Begin Rev 1.0 CollectionShopListFragment Suman 04/05/2023 Search option for shops  mantis id - 26026
        (mContext as DashboardActivity).setSearchListener(object : SearchListener {
            override fun onSearchQueryListener(query: String) {
                if (query.isBlank()) {
                    amountL?.let {
                        adapter?.refreshList(it)
                    }
                } else {
                    adapter?.filter?.filter(query)
                }
            }
        })
        //End of Rev 1.0 CollectionShopListFragment Suman 04/05/2023 Search option for shops  mantis id - 26026

        return view
    }

    private fun initView(view: View) {
        view.apply {
            rv_collection_shop_list = findViewById(R.id.rv_collection_shop_list)
            tv_no_data_available = findViewById(R.id.tv_no_data_available)
            progress_wheel = findViewById(R.id.progress_wheel)
            rl_collection_shop_list_main = findViewById(R.id.rl_collection_shop_list_main)
        }

        progress_wheel.stopSpinning()
        rv_collection_shop_list.layoutManager = LinearLayoutManager(mContext)
        rl_collection_shop_list_main.setOnClickListener(null)
    }

    private fun getList() {
        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }

        val repository = NewCollectionListRepoProvider.newCollectionListRepository()
        progress_wheel.spin()
        BaseActivity.compositeDisposable.add(
                repository.collectionShopList(date)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            progress_wheel.stopSpinning()
                            val response = result as CollectionShopListResponseModel
                            if (response.status == NetworkConstant.SUCCESS) {
                                try{
                                    amountL= response.amount_list!!
                                    initAdapter(response.amount_list)
                                }catch (ex:Exception){
                                    ex.printStackTrace()
                                }

                            }
                            else
                                (mContext as DashboardActivity).showSnackMessage(response.message!!)

                        }, { error ->
                            error.printStackTrace()
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                        })
        )
    }

    private fun initAdapter(amountList: ArrayList<CollectionShopListDataModel>?) {
        tv_no_data_available.visibility = View.GONE
        /*rv_collection_shop_list.adapter = CollectionShopAdapter(mContext, amountList) {
            val shop = AppDatabase.getDBInstance()?.addShopEntryDao()?.getShopByIdN(it.shop_id)
            if (shop != null)
                (mContext as DashboardActivity).loadFragment(FragType.CollectionDetailsFragment, true, shop)
        }*/

        //Begin Rev 1.0 CollectionShopListFragment Suman 04/05/2023 Search option for shops  mantis id - 26026
        adapter = CollectionShopAdapter(mContext, amountL) {
            val shop = AppDatabase.getDBInstance()?.addShopEntryDao()?.getShopByIdN(it.shop_id)
            if (shop != null)
                (mContext as DashboardActivity).loadFragment(FragType.CollectionDetailsFragment, true, shop)
        }
        rv_collection_shop_list.adapter = adapter
        //End of Rev 1.0 CollectionShopListFragment Suman 04/05/2023 Search option for shops  mantis id - 26026

    }
}