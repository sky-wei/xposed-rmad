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

package com.sky.xposed.rmad.hook

import com.sky.xposed.rmad.Constant
import com.sky.xposed.rmad.hook.news.CloudMusicHook
import com.sky.xposed.rmad.hook.news.NewsHook
import de.robv.android.xposed.IXposedHookInitPackageResources
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by sky on 17-11-1.
 */
class Main : IXposedHookLoadPackage, IXposedHookInitPackageResources {

    override fun handleLoadPackage(param: XC_LoadPackage.LoadPackageParam) {

        val packageName = param.packageName

        when(packageName) {
            Constant.News.PACKAGE_NAME -> {
                // 处理网易广告
                val newsHookPackage = NewsHook()
                newsHookPackage.handleLoadPackage(param)
            }
            Constant.CloudMusic.PACKAGE_NAME -> {
                // 处理网易广告
                val cloudMusicHook = CloudMusicHook()
                cloudMusicHook.handleLoadPackage(param)
            }
        }
    }

    override fun handleInitPackageResources(
            param: XC_InitPackageResources.InitPackageResourcesParam) {

    }
}