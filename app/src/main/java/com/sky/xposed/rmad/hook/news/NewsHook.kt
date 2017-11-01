package com.sky.xposed.rmad.hook.news

import android.os.Bundle
import android.os.Handler
import android.view.View
import com.sky.xposed.rmad.Constant
import com.sky.xposed.rmad.hook.base.BaseHook
import com.sky.xposed.rmad.util.Alog
import com.sky.xposed.rmad.util.PackageUitl
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by sky on 17-11-1.
 */
class NewsHook : BaseHook() {

    override fun handleLoadPackage(param: XC_LoadPackage.LoadPackageParam) {
        super.handleLoadPackage(param)

        if (!checkVersion(getSimplePackageInfo(Constant.News.PACKAGE_NAME))) {
            Alog.d("不支持当前版本")
            return
        }

        val fragmentClass = findClass(
                Support.ClassName.newsListFragment)

        val joinPointClass = findClass(
                Support.ClassName.joinPoint)

        findAndBeforeHookMethod(
                Support.ClassName.newsListFragment,
                Support.MethodName.newsListAd,
                fragmentClass, List::class.java, joinPointClass) {

            // 置空即可不添加广告
            it.args[1] = null
        }

        findAndAfterHookMethod(
                Support.ClassName.adFragment,
                Support.MethodName.onViewCreated,
                View::class.java, Bundle::class.java) {

            // 跳过启动广告
            val handler = getObjectField(
                    it.thisObject,
                    Support.FieldName.adHandler) as Handler
            handler.sendEmptyMessageDelayed(3, 1)
        }
    }

    private fun checkVersion(info: PackageUitl.SimplePackageInfo?): Boolean {

        if (info == null) return false

        return Support.Version.isSupport(info.versionName)
    }
}