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