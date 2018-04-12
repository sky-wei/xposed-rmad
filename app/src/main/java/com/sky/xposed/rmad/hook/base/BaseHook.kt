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

    fun findClass(className: String): Class<*> {
        return findClass(className, param.classLoader)
    }

    fun findClass(className: String, classLoader: ClassLoader): Class<*> {
        return XposedHelpers.findClass(className, classLoader)
    }

    fun findAndBeforeHookMethod(className: String, methodName: String,
                                vararg parameterTypes: Any,
                                beforeHook: (param: XC_MethodHook.MethodHookParam) -> Unit) {

        findAndHookMethod(className, methodName,
                *parameterTypes, beforeHook = beforeHook, afterHook = {})
    }

    fun findAndAfterHookMethod(className: String, methodName: String,
                               vararg parameterTypes: Any,
                               afterHook: (param: XC_MethodHook.MethodHookParam) -> Unit) {

        findAndHookMethod(className, methodName,
                *parameterTypes, beforeHook = {}, afterHook = afterHook)
    }

    fun findAndHookMethod(className: String, methodName: String,
                          vararg parameterTypes: Any,
                          beforeHook: (param: XC_MethodHook.MethodHookParam) -> Unit,
                          afterHook: (param: XC_MethodHook.MethodHookParam) -> Unit) {

        val parameterTypesAndCallback = arrayOf(
                *parameterTypes,
                object : XC_MethodHook() {

                    override fun beforeHookedMethod(param: MethodHookParam) {
                        super.beforeHookedMethod(param)
                        // 直接调用
                        beforeHook(param)
                    }

                    override fun afterHookedMethod(param: MethodHookParam) {
                        super.afterHookedMethod(param)
                        // 直接调用
                        afterHook(param)
                    }
                })

        findAndHookMethod(className, methodName, *parameterTypesAndCallback)
    }

    fun findAndHookMethodReplacement(className: String, methodName: String,
                                     vararg parameterTypes: Any,
                                     replaceHook: (param: XC_MethodHook.MethodHookParam) -> Any?) {

        val parameterTypesAndCallback = arrayOf(
                *parameterTypes,
                object : XC_MethodReplacement() {

                    override fun replaceHookedMethod(param: MethodHookParam): Any? {
                        // 直接调用
                        return replaceHook(param)
                    }
                })

        findAndHookMethod(className, methodName, *parameterTypesAndCallback)
    }

    fun findAndHookMethod(className: String, methodName: String,
                          vararg parameterTypesAndCallback: Any): XC_MethodHook.Unhook {
        return findAndHookMethod(className, param.classLoader, methodName, *parameterTypesAndCallback)
    }

    fun findAndHookMethod(className: String, classLoader: ClassLoader, methodName: String,
                          vararg parameterTypesAndCallback: Any): XC_MethodHook.Unhook {
        return XposedHelpers.findAndHookMethod(className, classLoader, methodName, *parameterTypesAndCallback)
    }

    fun getObjectField(obj: Any, fieldName: String): Any {
        return XposedHelpers.getObjectField(obj, fieldName)
    }

    fun getBooleanField(obj: Any, fieldName: String): Boolean {
        return XposedHelpers.getBooleanField(obj, fieldName)
    }
}