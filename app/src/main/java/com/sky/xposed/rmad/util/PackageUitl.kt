package com.sky.xposed.rmad.util

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import java.io.Serializable

/**
 * Created by sky on 17-11-1.
 */
object PackageUitl {

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