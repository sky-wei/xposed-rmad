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

import com.sky.xposed.rmad.Constant
import com.sky.xposed.rmad.helper.PreferenceHelper
import com.sky.xposed.rmad.helper.ReceiverHelper
import com.sky.xposed.rmad.hook.base.BaseHook
import com.sky.xposed.rmad.util.Alog
import com.sky.xposed.rmad.util.PackageUtil
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by sky on 17-11-1.
 */
class NewsHook : BaseHook() {

    private lateinit var mPreferenceHelper: PreferenceHelper

    override fun onHandleLoadPackage(param: XC_LoadPackage.LoadPackageParam) {

//        if (!checkVersion(getSimplePackageInfo(Constant.News.PACKAGE_NAME))) {
//            Alog.d("不支持当前版本")
//            return
//        }

        mPreferenceHelper = PreferenceHelper(getSystemContext())

        // 禁用广告
        findAndHookMethodReplacement(
                Support.ClassName.adClass,
                Support.MethodName.adMethod,
                String::class.java) {

            if (isCloseAllAd()) {
                null
            } else {
                XposedBridge.invokeOriginalMethod(it.method, it.thisObject, it.args)
            }
        }
    }

    private fun checkVersion(info: PackageUtil.SimplePackageInfo?): Boolean {

        if (info == null) return false

        return Support.Version.isSupport(info.versionName)
    }

    private fun isCloseAllAd(): Boolean {
        return mPreferenceHelper.getBoolean(Constant.Preference.NEWS_ALL_AD, true)
    }
}