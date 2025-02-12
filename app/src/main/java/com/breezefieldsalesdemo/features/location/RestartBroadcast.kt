package com.breezefieldsalesdemo.features.location

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

import com.google.android.exoplayer2.offline.DownloadService.startForeground
import timber.log.Timber

class RestartBroadcast : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Timber.d("RestartBroadcast: " + "=======================Received====================")

        //if (!FTStorageUtils.isMyServiceRunning(LocationFuzedService::class.java, context)) {
            Timber.e("RestartBroadcast: " + "=======================Start Service====================")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                context.startForegroundService(Intent(context, LocationFuzedService::class.java))
            else
                context.startService(Intent(context, LocationFuzedService::class.java))
        //}
    }
}