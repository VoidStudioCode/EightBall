package com.i605.eightball.presenter

import com.i605.eightball.helper.ExceptionHelper
import com.i605.eightball.view.ActivityAbout

class PresenterAbout(private val activity: ActivityAbout) {
    fun init() {
        activity.setVersion(version())
        activity.setUpdateHistory(updateHistory())
    }

    private fun version(): String {
        return try {
            activity.packageManager.getPackageInfo(activity.packageName, 0).versionName
        } catch (e: Exception) {
            ExceptionHelper.handleException(e)
            ""
        }
    }

    private fun updateHistory(): String {
        return "" +
                "版本 1.1\n" +
                "    01.优化数据库访问\n" +
                "    02.调整传感器敏感度\n" +
                "\n" +
                "版本 1.0\n" +
                "    01.初始版本\n"
    }

    fun disclaimer(): String {
        return ""
    }
}