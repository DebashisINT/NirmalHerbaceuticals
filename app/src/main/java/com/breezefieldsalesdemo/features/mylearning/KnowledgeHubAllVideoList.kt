package com.breezefieldsalesdemo.features.mylearning

import GridRVAdapter
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.breezefieldsalesdemo.DialogLoading
import com.breezefieldsalesdemo.R
import com.breezefieldsalesdemo.app.NetworkConstant
import com.breezefieldsalesdemo.app.Pref
import com.breezefieldsalesdemo.app.types.FragType
import com.breezefieldsalesdemo.app.utils.AppUtils
import com.breezefieldsalesdemo.base.presentation.BaseActivity
import com.breezefieldsalesdemo.base.presentation.BaseFragment
import com.breezefieldsalesdemo.features.dashboard.presentation.DashboardActivity
import com.breezefieldsalesdemo.features.mylearning.apiCall.LMSRepoProvider
import com.breezefieldsalesdemo.widgets.AppCustomEditText
import com.pnikosis.materialishprogress.ProgressWheel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import timber.log.Timber


class KnowledgeHubAllVideoList : BaseFragment(), View.OnClickListener , GridRVAdapter.OnItemClickListener {
    private lateinit var mContext: Context
    private var  suffixText:String = ""

    private lateinit var ll_lms_performance: LinearLayout
    private lateinit var iv_lms_performance: ImageView
    private lateinit var tv_lms_performance: TextView

    private lateinit var ll_lms_mylearning: LinearLayout
    private lateinit var iv_lms_mylearning: ImageView
    private lateinit var tv_lms_mylearning: TextView

    private lateinit var ll_lms_leaderboard: LinearLayout
    private lateinit var iv_lms_leaderboard: ImageView
    private lateinit var tv_lms_leaderboard: TextView

    private lateinit var ll_lms_knowledgehub: LinearLayout
    private lateinit var iv_lms_knowledgehub: ImageView
    private lateinit var tv_lms_knowledgehub: TextView
    private lateinit var gv_vdo: GridView
    lateinit var videoList: List<GridViewAllVideoModal>
    var contentL : ArrayList<ContentL> = ArrayList()

    private lateinit var rv_video_view: RecyclerView
    private lateinit var progress_wheel_all_video_frag: ProgressWheel
    private lateinit var ll_searchKnowledgehub: LinearLayout
    private lateinit var ll_video_not_found: LinearLayout
    private lateinit var et_search: AppCustomEditText
    private lateinit var ll_voice: ImageView


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater!!.inflate(R.layout.fragment_knowledgehub_all_video_list, container, false)
        initView(view)

        return view
    }

    private fun initView(view: View) {
        //performance
        ll_lms_performance=view.findViewById(R.id.ll_lms_performance)
        iv_lms_performance=view.findViewById(R.id.iv_lms_performance)
        tv_lms_performance=view.findViewById(R.id.tv_lms_performance)

        //mylearning
        ll_lms_mylearning=view.findViewById(R.id.ll_lms_mylearning)
        iv_lms_mylearning=view.findViewById(R.id.iv_lms_mylearning)
        tv_lms_mylearning=view.findViewById(R.id.tv_lms_mylearning)

        //leaderboard
        ll_lms_leaderboard=view.findViewById(R.id.ll_lms_leaderboard)
        iv_lms_leaderboard=view.findViewById(R.id.iv_lms_leaderboard)
        tv_lms_leaderboard=view.findViewById(R.id.tv_lms_leaderboard)

        //knowledgehub
        ll_lms_knowledgehub=view.findViewById(R.id.ll_lms_knowledgehub)
        iv_lms_knowledgehub=view.findViewById(R.id.iv_lms_knowledgehub)
        tv_lms_knowledgehub=view.findViewById(R.id.tv_lms_knowledgehub)

        rv_video_view=view.findViewById(R.id.rv_video_view)
        progress_wheel_all_video_frag=view.findViewById(R.id.progress_wheel_all_video_frag)
        ll_searchKnowledgehub=view.findViewById(R.id.ll_searchKnowledgehub)
        ll_video_not_found=view.findViewById(R.id.ll_video_not_found)
        et_search=view.findViewById(R.id.et_frag_contacts_search)
        ll_voice=view.findViewById(R.id.iv_frag_knowledge_all_video_mic)

       // gv_vdo=view.findViewById(R.id.idGRV)

        iv_lms_knowledgehub.setImageResource(R.drawable.knowledge_hub_filled_clr)
        iv_lms_performance.setImageResource(R.drawable.my_performance_new)
        iv_lms_mylearning.setImageResource(R.drawable.my_learning_new)
        iv_lms_leaderboard.setImageResource(R.drawable.leaderboard_new)
        iv_lms_performance.setColorFilter(ContextCompat.getColor(mContext, R.color.black), android.graphics.PorterDuff.Mode.MULTIPLY)
        iv_lms_mylearning.setColorFilter(ContextCompat.getColor(mContext, R.color.black), android.graphics.PorterDuff.Mode.MULTIPLY)
        iv_lms_leaderboard.setColorFilter(ContextCompat.getColor(mContext, R.color.black), android.graphics.PorterDuff.Mode.MULTIPLY)

        tv_lms_performance.setTextColor(getResources().getColor(R.color.black))
        tv_lms_mylearning.setTextColor(getResources().getColor(R.color.black))
        tv_lms_leaderboard.setTextColor(getResources().getColor(R.color.black))
        tv_lms_knowledgehub.setTextColor(getResources().getColor(R.color.toolbar_lms))
      /*  videoList = ArrayList<GridViewAllVideoModal>()

        videoList = videoList + GridViewAllVideoModal("Top Free Courses | ProductManagement | Product Manager", "http://3.7.30.86:8073/Commonfolder/LMS/ContentUpload/Sell Me This Pen.mp4")
        videoList = videoList + GridViewAllVideoModal("Top 3 Degrees to become a Product Manager", "http://3.7.30.86:8073/Commonfolder/LMS/ContentUpload/nature shorts video.mp4")
        videoList = videoList + GridViewAllVideoModal("Salary of Product Manager", "http://3.7.30.86:8073/Commonfolder/LMS/ContentUpload/Sell These 5 things To Become Rich.mp4")
        videoList = videoList + GridViewAllVideoModal("How to Transition to ProductManagement?!", "http://3.7.30.86:8073/Commonfolder/LMS/ContentUpload/The GOLDEN Rule Of Selling.mp4")
        videoList = videoList + GridViewAllVideoModal("How to Transition to ProductManagement?!", "http://3.7.30.86:8073/Commonfolder/LMS/ContentUpload/Sales ki mol baat Basics of Sales.mp4")
        videoList = videoList + GridViewAllVideoModal("How to Transition to ProductManagement?!", "http://3.7.30.86:8073/Commonfolder/LMS/ContentUpload/How to approach Sales Management.mp4")
        videoList = videoList + GridViewAllVideoModal("How to Transition to ProductManagement?!", "http://3.7.30.86:8073/Commonfolder/LMS/ContentUpload/Attendance Marking.mp4")
*/

        //val courseAdapter = GridRVAllVideoAdapter(courseList = videoList, mContext)
        //val courseAdapter = VideoGridAdapter(mContext, videoList)

        contentL = ArrayList()

        getVideoTopicWise()

        et_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //AppUtils.hideSoftKeyboard(mContext as DashboardActivity)
                //if (!et_search.text.toString().trim().equals("")) {
                    progress_wheel_all_video_frag.spin()
                    doAsync {
                        var tempSearchL = contentL.filter { it.content_title.contains(et_search.text.toString().trim(), ignoreCase = true) || it.content_description.contains(et_search.text.toString().trim(), ignoreCase = true) }
                        uiThread {
                            progress_wheel_all_video_frag.stopSpinning()
                            if(tempSearchL.size>0){
                                //setTopicAdapter(tempSearchL)
                                rv_video_view.visibility = View.VISIBLE
                                lifecycleScope.launch(Dispatchers.Main) {
                                    DialogLoading.show((mContext as DashboardActivity).supportFragmentManager, "")
                                    val courseAdapter = GridRVAdapter(mContext, tempSearchL , this@KnowledgeHubAllVideoList)
                                    val layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
                                    rv_video_view.setLayoutManager(layoutManager)
                                    rv_video_view.adapter = courseAdapter
                                    progress_wheel_all_video_frag.stopSpinning()
                                }.invokeOnCompletion {
                                    DialogLoading.dismiss()
                                }
                            }else{
                                rv_video_view.visibility = View.GONE
                            }
                        }
                    }
               // }
            }

            override fun afterTextChanged(s: Editable) {}
        })


        progress_wheel_all_video_frag.spin()
        /*Handler().postDelayed(Runnable {
            val courseAdapter = GridRVAdapter(mContext, videoList,this@KnowledgeHubAllVideoList)
            rv_video_view.setLayoutManager(GridLayoutManager(mContext, 2))
            rv_video_view.adapter = courseAdapter
            progress_wheel_all_video_frag.stopSpinning()
        }, 1000)
        */

       /* gv_vdo.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            *//* Toast.makeText(
                 mContext, videoList[position].videoName + " selected",
                 Toast.LENGTH_SHORT
             ).show()*//*

            (mContext as DashboardActivity).loadFragment(FragType.VideoPlayLMS, true, "")
        }*/

        ll_lms_performance.setOnClickListener(this)
        ll_lms_mylearning.setOnClickListener(this)
        ll_lms_leaderboard.setOnClickListener(this)
        ll_lms_knowledgehub.setOnClickListener(this)
        ll_voice.setOnClickListener(this)


    }

    private fun getVideoTopicWise() {
        try {
            progress_wheel_all_video_frag.visibility = View.VISIBLE
            Timber.d("deleteImei call" + AppUtils.getCurrentDateTime())
            val repository = LMSRepoProvider.getTopicList()
            BaseActivity.compositeDisposable.add(
                repository.getTopicsWiseVideo(Pref.user_id!!,topic_id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        val response = result as VideoTopicWiseResponse
                        if (response.status == NetworkConstant.SUCCESS) {
                            progress_wheel_all_video_frag.visibility = View.GONE
                            try {
                                if (response.content_list!=null && response.content_list.size>0) {
                                    ll_video_not_found.visibility =View.GONE
                                    rv_video_view.visibility =View.VISIBLE
                                    ll_searchKnowledgehub.visibility =View.VISIBLE

                                    contentL = response.content_list
                                    // Sort the content list by content_play_sequence
                                    val sortedList = contentL.sortedBy { it.content_play_sequence }
                                        .toCollection(ArrayList())
                                    Log.d("sortedList", "" + sortedList)

                                    lifecycleScope.launch(Dispatchers.Main) {
                                        DialogLoading.show((mContext as DashboardActivity).supportFragmentManager, "")
                                        val courseAdapter = GridRVAdapter(mContext, contentL , this@KnowledgeHubAllVideoList)
                                        val layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
                                        rv_video_view.setLayoutManager(layoutManager)
                                        rv_video_view.adapter = courseAdapter
                                        progress_wheel_all_video_frag.stopSpinning()
                                    }.invokeOnCompletion {
                                        DialogLoading.dismiss()
                                    }

                                   /* Handler().postDelayed(Runnable {
                                        val courseAdapter = GridRVAdapter(mContext, videoList,this@KnowledgeHubAllVideoList)
                                        rv_video_view.setLayoutManager(GridLayoutManager(mContext, 2))
                                        rv_video_view.adapter = courseAdapter
                                        progress_wheel_all_video_frag.stopSpinning()
                                    }, 1000)*/


                                }else{
                                    Toast.makeText(mContext, "No video found", Toast.LENGTH_SHORT).show()
                                    ll_video_not_found.visibility =View.VISIBLE
                                    rv_video_view.visibility =View.GONE
                                    ll_searchKnowledgehub.visibility =View.GONE
                                }
                            }catch (ex:Exception){
                                ex.printStackTrace()
                            }
                        }else{
                            progress_wheel_all_video_frag.visibility = View.GONE
                            ll_video_not_found.visibility =View.VISIBLE
                            ll_searchKnowledgehub.visibility =View.GONE
                            rv_video_view.visibility =View.GONE
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_data_found))

                        }
                    }, { error ->
                        progress_wheel_all_video_frag.visibility = View.GONE
                        ll_video_not_found.visibility =View.GONE
                        ll_searchKnowledgehub.visibility =View.GONE
                        rv_video_view.visibility =View.GONE
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                    })
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            progress_wheel_all_video_frag.visibility = View.GONE
            ll_video_not_found.visibility =View.GONE
            ll_searchKnowledgehub.visibility =View.GONE
            rv_video_view.visibility =View.GONE
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
        }

    }

    companion object {
            var topic_id: String = ""
            fun getInstance(objects: Any): KnowledgeHubAllVideoList {
                val knowledgeHubAllVideoList = KnowledgeHubAllVideoList()
                if (!TextUtils.isEmpty(objects.toString())) {
                    topic_id=objects.toString()
                }else{
                    topic_id = ""
                }
                println("tag_topic_id"+ topic_id)
                return knowledgeHubAllVideoList
            }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){

            ll_lms_mylearning.id -> {
                (mContext as DashboardActivity).loadFragment(FragType.SearchLmsFrag, true, "")
            }

            ll_lms_leaderboard.id -> {
                (mContext as DashboardActivity).loadFragment(FragType.MyLearningFragment, true, "")
            }

            ll_lms_knowledgehub.id -> {
                (mContext as DashboardActivity).loadFragment(FragType.SearchLmsKnowledgeFrag, true, "")
            }

            ll_lms_performance.id -> {
                (mContext as DashboardActivity).loadFragment(FragType.allPerformanceFrag, true, "")
            }
            ll_voice.id ->{
                suffixText = et_search.text.toString().trim()
                startVoiceInput()
            }
        }
    }

    private fun startVoiceInput() {
        val intent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en-US")
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US")
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?")
        try {
            startActivityForResult(intent, 7009)
        } catch (a: ActivityNotFoundException) {
            a.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 7009){
            try{
                val result = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                var t= result!![0]
                if(suffixText.length>0 && !suffixText.equals("")){
                    var setFullText = suffixText+t
                    et_search.setText(suffixText+t)
                    et_search.setSelection(setFullText.length);
                }else{
                    var SuffixPostText = t+et_search.text.toString()
                    et_search.setText(SuffixPostText)
                    et_search.setSelection(SuffixPostText.length);
                }
            }
            catch (ex:Exception) {
                ex.printStackTrace()
            }
        }
    }

    override fun onItemClick(item: ContentL) {
        //Toast.makeText(mContext, "Clicked: ${item.videoName}", Toast.LENGTH_SHORT).show()
        VideoPlayLMS.previousFrag = FragType.KnowledgeHubAllVideoList.toString()
        Pref.videoCompleteCount = "0"
        (mContext as DashboardActivity).loadFragment(FragType.VideoPlayLMS, true, "")
        //(mContext as DashboardActivity).loadFragment(FragType.LmsQuestionAnswerSet, true, "")
    }


}