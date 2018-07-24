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

package com.sky.xposed.rmad.hook.base

import android.app.ActivityThread
import android.content.ContentResolver
import android.content.Context
import com.sky.xposed.rmad.helper.ReceiverHelper
import com.sky.xposed.rmad.util.Alog
import com.sky.xposed.rmad.util.PackageUtil
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by sky on 17-11-1.
 */
abstract class BaseHook : IXposedHookLoadPackage {

    private lateinit var param: XC_LoadPackage.LoadPackageParam
    private var mReceiverHelper: ReceiverHelper? = null

    final override fun handleLoadPackage(param: XC_LoadPackage.LoadPackageParam) {
        this.param = param

        try {
            // 处理
            onHandleLoadPackage(param)
        } catch (tr: Throwable) {
            Alog.e("handleLoadPackage异常", tr)
        }
    }

    abstract fun onHandleLoadPackage(param: XC_LoadPackage.LoadPackageParam)

    fun getSystemContext(): Context {
        return ActivityThread.currentActivityThread().systemContext
    }

    fun getContentResolver(): ContentResolver {
        return getSystemContext().contentResolver
    }

    fun getSimplePackageInfo(packageName: String): PackageUtil.SimplePackageInfo? {
        return PackageUtil.getSimplePackageInfo(getSystemContext(), packageName)
    }
}