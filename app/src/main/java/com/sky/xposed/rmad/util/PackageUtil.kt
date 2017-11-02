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

package com.sky.xposed.rmad.util

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import java.io.Serializable

/**
 * Created by sky on 17-11-1.
 */
object PackageUtil {

    /**
     * 获取指定包的版本名
     * @param context
     * @param packageName
     * @return
     */
    fun getVersionName(context: Context, packageName: String): String? {

        val packageInfo = getPackageInfo(context, packageName, 0)

        return packageInfo?.versionName
    }

    /**
     * 获取指定包的版本号
     * @param context
     * @param packageName
     * @return
     */
    fun getVersionCode(context: Context, packageName: String): Int {

        val packageInfo = getPackageInfo(context, packageName, 0)

        return packageInfo?.versionCode ?: 0
    }

    /**
     * 获取简单的包信息
     * @param context
     * @param packageName
     * @return
     */
    fun getSimplePackageInfo(context: Context, packageName: String): SimplePackageInfo? {

        val packageInfo = getPackageInfo(context, packageName, 0)

        return if (packageInfo != null)
            SimplePackageInfo(packageName, packageInfo.versionName, packageInfo.versionCode)
        else
            null
    }

    /**
     * 获取指定包信息
     * @param context
     * @param packageName
     * @param flag
     * @return
     */
    fun getPackageInfo(context: Context, packageName: String, flag: Int): PackageInfo? {

        try {
            val manager = context.packageManager
            return manager.getPackageInfo(packageName, flag)
        } catch (e: PackageManager.NameNotFoundException) {
            Alog.e("获取的包不存在", e)
        }
        return null
    }

    class SimplePackageInfo(var packageName: String, var versionName: String, var versionCode: Int) : Serializable
}