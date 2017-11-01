/*
 * Copyright (c) 2017 The sky Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sky.xposed.rmad.hook.news

import android.os.Bundle
import android.os.Handler
import android.view.View
import com.sky.xposed.rmad.Constant
import com.sky.xposed.rmad.hook.base.BaseHook
import com.sky.xposed.rmad.util.Alog
import com.sky.xposed.rmad.util.PackageUitl
import de.robv.android.xposed.XSharedPreferences
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by sky on 17-11-1.
 */
class NewsHook : BaseHook() {

    override fun handleLoadPackage(param: XC_LoadPackage.LoadPackageParam) {
        super.handleLoadPackage(param)

        if (!checkVersion(getSimplePackageInfo(Constant.News.PACKAGE_NAME))) {
            Alog.d("不支持当前版本")
            return
        }

        // 配置
        val preferences = XSharedPreferences(Constant.ByeAD.PACKAGE_NAME)

        val fragmentClass = findClass(
                Support.ClassName.newsListFragment)

        val joinPointClass = findClass(
                Support.ClassName.joinPoint)

        findAndBeforeHookMethod(
                Support.ClassName.newsListFragment,
                Support.MethodName.newsListAd,
                fragmentClass, List::class.java, joinPointClass) {

            if (isCloseStartAd(preferences)) {
                // 置空即可不添加广告
                it.args[1] = null
            }
        }

        findAndAfterHookMethod(
                Support.ClassName.adFragment,
                Support.MethodName.onViewCreated,
                View::class.java, Bundle::class.java) {

            if (isCloseListAd(preferences)) {
                // 跳过启动广告
                val handler = getObjectField(
                        it.thisObject,
                        Support.FieldName.adHandler) as Handler
                handler.sendEmptyMessageDelayed(3, 1)
            }
        }
    }

    private fun checkVersion(info: PackageUitl.SimplePackageInfo?): Boolean {

        if (info == null) return false

        return Support.Version.isSupport(info.versionName)
    }

    private fun isCloseStartAd(preferences: XSharedPreferences): Boolean {
        return preferences.getBoolean(Constant.Preference.NEWS_START_AD, true)
    }

    private fun isCloseListAd(preferences: XSharedPreferences): Boolean {
        return preferences.getBoolean(Constant.Preference.NEWS_LIST_AD, true)
    }
}