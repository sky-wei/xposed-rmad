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

/**
 * Created by sky on 17-11-1.
 */
object Support {

    object ClassName {

        var adFragment = "com.netease.nr.biz.ad.AdFragment"

        var newsListFragment = "com.netease.newsreader.newarch.news.list.base.NewarchNewsListFragment"

        var joinPoint = "org.aspectj.lang.JoinPoint"
    }

    object MethodName {

        var onViewCreated = "onViewCreated"

        var newsListAd = "e"
    }

    object FieldName {

        var adHandler = "o"
    }

    object Version {

        private val versionMap = HashMap<String, String>()

        init {
            // 添加版本
            versionMap.put("29.1", "")
        }

        fun isSupport(versionName: String): Boolean {
            return versionMap.containsKey(versionName)
        }
    }
}