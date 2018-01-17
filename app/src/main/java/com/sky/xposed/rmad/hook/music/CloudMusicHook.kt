/*
 * Copyright (c) 2018 The sky Authors.
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

package com.sky.xposed.rmad.hook.music

import com.sky.xposed.rmad.hook.base.BaseHook
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by sky on 18-1-16.
 */
class CloudMusicHook : BaseHook() {

    override fun onHandleLoadPackage(param: XC_LoadPackage.LoadPackageParam) {

        val tClass = findClass("com.netease.cloudmusic.module.ad.c\$b")

        findAndAfterHookMethod(
                "com.netease.cloudmusic.module.ad.c", "a", tClass){
            it.result = null
        }

        findAndAfterHookMethod(
                "com.netease.cloudmusic.module.ad.c", "b"){
            it.result = null
        }
    }
}