package com.sky.xposed.rmad.hook.news

import com.sky.xposed.rmad.hook.base.BaseHook
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by sky on 18-1-16.
 */
class CloudMusicHook : BaseHook() {

    override fun handleLoadPackage(param: XC_LoadPackage.LoadPackageParam) {
        super.handleLoadPackage(param)


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