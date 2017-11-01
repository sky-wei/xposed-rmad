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

package com.sky.xposed.load.hook

import android.app.ActivityThread
import android.content.Context
import dalvik.system.PathClassLoader
import de.robv.android.xposed.IXposedHookInitPackageResources
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by sky on 17-11-1.
 */
class Main : IXposedHookLoadPackage, IXposedHookInitPackageResources {

    override fun handleLoadPackage(param: XC_LoadPackage.LoadPackageParam) {

        try {
            val packageName = param.packageName
            val context = ActivityThread.currentActivityThread().systemContext

//            XposedBridge.log(">>>> PackageName: " + packageName)

            handleLoadPackage(context, packageName, param)
        } catch (tr: Throwable) {
            XposedBridge.log(tr)
        }
    }

    override fun handleInitPackageResources(resparam: XC_InitPackageResources.InitPackageResourcesParam) {

    }

    private fun handleLoadPackage(context: Context, packageName: String, param: XC_LoadPackage.LoadPackageParam) {

        if ("com.netease.newsreader.activity" == packageName) {
            // 加载包
            handlerNewsPackage(context, param)
        }
    }

    private fun handlerNewsPackage(context: Context, param: XC_LoadPackage.LoadPackageParam) {

        val classLoader = loadClassLoader(
                context, "com.sky.xposed.rmad", param)

        val mainClass = classLoader.loadClass("com.sky.xposed.rmad.hook.Main")
        val mainAny = mainClass.newInstance()

        // 直接调用
        XposedHelpers.callMethod(mainAny, "handleLoadPackage", param)
    }

    private fun loadClassLoader(context: Context, packageName: String,
                                param: XC_LoadPackage.LoadPackageParam): ClassLoader {

        val packageInfo = context.packageManager.getPackageInfo(packageName, 0)
        val applicationInfo = packageInfo.applicationInfo

        return PathClassLoader(
                applicationInfo.publicSourceDir,
                applicationInfo.nativeLibraryDir,
                Thread.currentThread().contextClassLoader)
    }
}