package com.sky.xposed.rmad.hook

import com.sky.xposed.rmad.Constant
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

        if (Constant.News.PACKAGE_NAME == packageName) {
            // 处理网易广告
            val newsHookPackage = NewsHook()
            newsHookPackage.handleLoadPackage(param)
        }
    }

    override fun handleInitPackageResources(
            param: XC_InitPackageResources.InitPackageResourcesParam) {

    }
}