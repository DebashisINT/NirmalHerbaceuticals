package com.breezefieldsalesdemo.features.location

import android.app.IntentService
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log

import com.breezefieldsalesdemo.R
import com.breezefieldsalesdemo.app.AppDatabase
import com.breezefieldsalesdemo.app.NetworkConstant
import com.breezefieldsalesdemo.app.Pref
import com.breezefieldsalesdemo.app.domain.ShopActivityEntity
import com.breezefieldsalesdemo.app.utils.AppUtils
import com.breezefieldsalesdemo.app.utils.NotificationUtils
import com.breezefieldsalesdemo.features.location.model.ShopDurationRequest
import com.breezefieldsalesdemo.features.location.model.ShopDurationRequestData
import com.breezefieldsalesdemo.features.location.shopdurationapi.ShopDurationRepositoryProvider
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by Saikat on 04-01-2019.
 */
// 1.0 GeofenceTransitionsIntentService AppV 4.0.6 saheli 12-01-2023 multiple contact Data added on Api called
class GeofenceTransitionsIntentService : IntentService("GeofenceTransitionsIntentService") {

    private val TAG = "GeofenceTransitionsIS"
    private val CHANNEL_ID = "channel_01"


    override fun onHandleIntent(intent: Intent?) {

        Timber.d("Geofence: GeofenceTransitionsIntentService : ENTRY")
        val geofencingEvent = GeofencingEvent.fromIntent(intent!!)
        if (geofencingEvent!!.hasError()) {
//            val errorMessage = GeofenceErrorMessages.getErrorString(this,
//                    geofencingEvent.errorCode)
            Log.e(TAG, "${geofencingEvent!!.errorCode}")
            return
        }

        // Get the transition type.
        val geofenceTransition = geofencingEvent.geofenceTransition

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT || geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {

            // Get the geofences that were triggered. A single event can trigger multiple geofences.
            val triggeringGeofences = geofencingEvent!!.triggeringGeofences

            // Get the transition details as a String.
            val geofenceTransitionDetails = getGeofenceTransitionDetails(geofenceTransition,
                    triggeringGeofences!!)
            triggeringGeofences.forEach {
                //                it.requestId
                // Send notification and log the transition details.
                Timber.d("=====================Geofence=======================")
                when (geofenceTransition) {
                    Geofence.GEOFENCE_TRANSITION_ENTER -> {
                        Timber.d("Geofence: GeofenceTransitionsJobIntentService : ENTER")
                        if (!TextUtils.isEmpty(Pref.user_id) && !Pref.isAutoLogout)
                            sendNotification(it.requestId)
                    }

                    Geofence.GEOFENCE_TRANSITION_EXIT -> {
                        Timber.d("Geofence: GeofenceTransitionsJobIntentService : EXIT")
                        cancelNotification(it.requestId)
                        endShopDuration(it.requestId)
                    }

                    Geofence.GEOFENCE_TRANSITION_DWELL -> {
                        Timber.d("Geofence: GeofenceTransitionsJobIntentService : DWELL")
                        if (!TextUtils.isEmpty(Pref.user_id) && !Pref.isAutoLogout)
                            sendNotification(it.requestId)
    //                        calculateShopDuration(it.requestId)
                    }
                }
            }


            Timber.e(TAG, geofenceTransitionDetails)
        } else {
            // Log the error.
            Timber.e(TAG, "Invalid Type $geofenceTransition")
        }
        Timber.d("Geofence: GeofenceTransitionsJobIntentService : EXIT")

    }

    fun cancelNotification(shopId: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(shopId.hashCode())
    }

    private fun endShopDuration(requestId: String?) {
        var shopActiList = AppDatabase.getDBInstance()!!.shopActivityDao().getShopForDay(requestId!!, AppUtils.getCurrentDateForShopActi())
        if (shopActiList.isEmpty())
            return
        Timber.d("Geofence: FarFromShop : " + "ShopName : " + shopActiList[0].shop_name!!)
        if (!shopActiList[0].isDurationCalculated && !shopActiList[0].isUploaded) {
            var endTimeStamp = System.currentTimeMillis().toString()
            var startTimestamp = shopActiList[0].startTimeStamp

            var duration = AppUtils.getTimeFromTimeSpan(startTimestamp, endTimeStamp)
            var totalMinute = AppUtils.getMinuteFromTimeStamp(startTimestamp, endTimeStamp)
            AppDatabase.getDBInstance()!!.shopActivityDao().updateEndTimeOfShop(endTimeStamp, shopActiList[0].shopid!!, AppUtils.getCurrentDateForShopActi())
            AppDatabase.getDBInstance()!!.shopActivityDao().updateIsUploaded(false, shopActiList[0].shopid!!, AppUtils.getCurrentDateForShopActi())
            AppDatabase.getDBInstance()!!.shopActivityDao().updateTotalMinuteForDayOfShop(shopActiList[0].shopid!!, totalMinute, AppUtils.getCurrentDateForShopActi())
            AppDatabase.getDBInstance()!!.shopActivityDao().updateTimeDurationForDayOfShop(shopActiList[0].shopid!!, duration, AppUtils.getCurrentDateForShopActi())
            AppDatabase.getDBInstance()!!.shopActivityDao().updateDurationAvailable(true, shopActiList[0].shopid!!, AppUtils.getCurrentDateForShopActi())
//            callShopDurationApi(requestId)
//            AppUtils.isShopVisited = false
            Pref.isShopVisited=false
        }


    }

    private fun calculateShopDuration(requestId: String?) {
        var shopActiList = AppDatabase.getDBInstance()!!.shopActivityDao().getShopForDay(requestId!!, AppUtils.getCurrentDateForShopActi())
        if (shopActiList.isEmpty())
            return
        if (shopActiList[0].isDurationCalculated)
            return
        for (i in 0 until shopActiList.size) {
            var totalMinute = AppUtils.getMinuteFromTimeStamp(shopActiList[i].startTimeStamp, System.currentTimeMillis().toString())
            //If duration is greater than 20 hour then stop incrementing
            if (totalMinute.toInt() > 20 * 60) {
                AppDatabase.getDBInstance()!!.shopActivityDao().updateDurationAvailable(true, shopActiList[i].shopid!!, AppUtils.getCurrentDateForShopActi())
                return
            }
            Timber.d("Geofence: ShopDurationIncrement : " + "ShopName : " + shopActiList[i].shop_name + "," + shopActiList[i].duration_spent)
            AppDatabase.getDBInstance()!!.shopActivityDao().updateTotalMinuteForDayOfShop(shopActiList[i].shopid!!, totalMinute, AppUtils.getCurrentDateForShopActi())
            var duration = AppUtils.getTimeFromTimeSpan(shopActiList[i].startTimeStamp, System.currentTimeMillis().toString())
            AppDatabase.getDBInstance()!!.shopActivityDao().updateTimeDurationForDayOfShop(shopActiList[i].shopid!!, duration, AppUtils.getCurrentDateForShopActi())
        }

    }

    private fun callShopDurationApi(shopId: String) {

        if (Pref.user_id.isNullOrEmpty())
            return
        /* Get all the shop list that has been synched successfully*/
        var shopObj = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopDetail(shopId)
        if (!shopObj.isUploaded)
            return

        var shopDataList: MutableList<ShopDurationRequestData> = ArrayList()
        /* Get shop activity that has completed time duration calculation*/
        var shopActivity: ShopActivityEntity? = AppDatabase.getDBInstance()!!.shopActivityDao().durationAvailableForShop(shopId, true, false)
                ?: return
        var shopDurationData = ShopDurationRequestData()
        shopDurationData.shop_id = shopActivity!!.shopid
        shopDurationData.spent_duration = shopActivity!!.duration_spent
        shopDurationData.visited_date = shopActivity!!.visited_date
        shopDurationData.visited_time = shopActivity!!.visited_date
        shopDurationData.distance_travelled = shopActivity.distance_travelled
        if (AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(shopActivity!!.shopid) != null)
            shopDurationData.total_visit_count = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(shopActivity!!.shopid).totalVisitCount
        else
            shopDurationData.total_visit_count = "1"

        if (!TextUtils.isEmpty(shopActivity.feedback))
            shopDurationData.feedback = shopActivity.feedback
        else
            shopDurationData.feedback = ""

        shopDurationData.isFirstShopVisited = shopActivity.isFirstShopVisited
        shopDurationData.distanceFromHomeLoc = shopActivity.distance_from_home_loc
        shopDurationData.next_visit_date = shopActivity.next_visit_date

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

        // 1.0 GeofenceTransitionsIntentService  AppV 4.0.6  multiple contact Data added on Api called
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

        if (shopDataList.isEmpty()) {
            return
        }

        var shopDurationApiReq = ShopDurationRequest()
        shopDurationApiReq.user_id = Pref.user_id
        shopDurationApiReq.session_token = Pref.session_token
        shopDurationApiReq.shop_list = shopDataList

        val repository = ShopDurationRepositoryProvider.provideShopDurationRepository()
        repository.shopDuration(shopDurationApiReq)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    if (result.status == NetworkConstant.SUCCESS) {
                        for (i in 0 until shopDataList.size) {
                            AppDatabase.getDBInstance()!!.shopActivityDao().updateisUploaded(true, shopDataList[i].shop_id!!, AppUtils.getCurrentDateForShopActi())
                        }
                    }

                }, { error ->
                    error.printStackTrace()
                })


    }


    /**
     * Gets transition details and returns them as a formatted string.
     *
     * @param geofenceTransition    The ID of the geofence transition.
     * @param triggeringGeofences   The geofence(s) triggered.
     * @return                      The transition details formatted as String.
     */
    private fun getGeofenceTransitionDetails(
            geofenceTransition: Int,
            triggeringGeofences: List<Geofence>): String {

        val geofenceTransitionString = getTransitionString(geofenceTransition)

        // Get the Ids of each geofence that was triggered.
        val triggeringGeofencesIdsList = arrayListOf<String>()
        for (geofence in triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.requestId)
        }
        val triggeringGeofencesIdsString = TextUtils.join(", ", triggeringGeofencesIdsList)

        return geofenceTransitionString + ": " + triggeringGeofencesIdsString
    }

    /**
     * Posts a notification in the notification bar when a transition is detected.
     * If the user clicks the notification, control goes to the MainActivity.
     */
    private fun sendNotification(shopId: String) {
        val list = AppDatabase.getDBInstance()!!.shopActivityDao().getShopForDay(shopId, AppUtils.getCurrentDateForShopActi())
        var isDurationCalculated = false
        var isVisited = false
        var shopName = ""
        if (list.isEmpty()) {
            if (AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(shopId) == null)
                return
            shopName = AppDatabase.getDBInstance()!!.addShopEntryDao().getShopByIdN(shopId).shopName
        } else {
            isDurationCalculated = list[0].isDurationCalculated
            isVisited = list[0].isVisited
            shopName = list[0].shop_name!!
        }
        Timber.d("Geofence: ENTER : " + "ShopName : " + shopName + ",IS_DURATION_CALCULATED : " + isDurationCalculated)
        if (isDurationCalculated || isVisited)
            return

        Timber.d("Geofence: NearToShop : " + "ShopName : " + shopName)
        // Get an instance of the Notification manager
        val notification = NotificationUtils(getString(R.string.app_name), shopName, shopId, "")
        notification.CreateNotification(this, shopId)
//        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    }

    /**
     * Maps geofence transition types to their human-readable equivalents.
     *
     * @param transitionType    A transition type constant defined in Geofence
     * @return                  A String indicating the type of transition
     */
    private fun getTransitionString(transitionType: Int): String {
        when (transitionType) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> return "Enter"
            Geofence.GEOFENCE_TRANSITION_EXIT -> return "Exit"
            Geofence.GEOFENCE_TRANSITION_DWELL -> return "Dwell"
            else -> return "Invalid"
        }
    }
}