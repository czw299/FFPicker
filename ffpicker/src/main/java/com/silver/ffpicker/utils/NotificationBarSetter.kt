package com.silver.ffpicker.utils

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import java.util.*

class NotificationBarSetter {
    companion object{
        fun setNotificationBarDarkFont(activity: Activity, isFullscreen: Boolean, isNavegationLightMode: Boolean) {
            val integerArrayList = ArrayList<Int>()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                integerArrayList.add(View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
                integerArrayList.add(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                if (isNavegationLightMode && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) { //XML需要8.1，Java代码可以8.0设置
                    activity.window.navigationBarColor = Color.parseColor("#FFFFFF")
                    integerArrayList.add(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
                }
                if (isFullscreen) {
                    activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    activity.window.statusBarColor = Color.TRANSPARENT // SDK21
                    integerArrayList.add(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
                }
                var sysUI = 0
                for (i in integerArrayList) {
                    sysUI = sysUI or i
                }
                activity.window.decorView.systemUiVisibility = sysUI
            }
        }
    }
}