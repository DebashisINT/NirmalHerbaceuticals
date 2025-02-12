/*
 *
 * Copyright (c) Microsoft. All rights reserved.
 * Licensed under the MIT license.
 *
 * Project Oxford: http://ProjectOxford.ai
 *
 * Project Oxford Mimicker Alarm Github:
 * https://github.com/Microsoft/ProjectOxford-Apps-MimickerAlarm
 *
 * Copyright (c) Microsoft Corporation
 * All rights reserved.
 *
 * MIT License:
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED ""AS IS"", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.breezefieldsalesdemo.app.utils

import android.content.Context
import android.media.AudioAttributes
import android.os.Vibrator

/**
 * This simple utility class is a wrapper of the system vibrator.  This class is called by the
 * AlarmRingingController.
 */
class AlarmVibrator(private val mContext: Context) {
    private var mVibrating: Boolean = false
    private var mVibrator: Vibrator? = null

    fun initialize() {
        mVibrator = mContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    fun cleanup() {
        mVibrator = null
    }

    fun vibrate() {
        if (!mVibrating) {
            // Start immediately
            // Vibrate for 200 milliseconds
            // Sleep for 500 milliseconds
            val vibrationPattern = longArrayOf(0, 200, 500)
            //val pattern = longArrayOf(0, 100, 1000, 300, 200, 100, 500, 200, 100)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                mVibrator!!.vibrate(vibrationPattern, 0,
                        AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build())
            } else {
                mVibrator!!.vibrate(vibrationPattern, 0)
            }
            mVibrating = true
        }
    }

    fun stop() {
        if (mVibrating) {
            mVibrator!!.cancel()
            mVibrating = false
        }
    }
}
