package com.sky.xposed.rmad.hook.base

import android.app.ActivityThread
import android.content.Context
import com.sky.xposed.rmad.util.PackageUitl
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by sky on 17-11-1.
 */
abstract class BaseHook : IXposedHookLoadPackage {

    private lateinit var param: XC_LoadPackage.LoadPackageParam

    override fun handleLoadPackage(param: XC_LoadPackage.LoadPackageParam) {
        this.param = param
    }

    fun getSystemContext(): Context {
        return ActivityThread.currentActivityThread().systemContext
    }

    fun getSimplePackageInfo(packageName: String): PackageUitl.SimplePackageInfo? {
        return PackageUitl.getSimplePackageInfo(getSystemContext(), packageName)
    }

    fun findClass(className: String): Class<*> {
        return findClass(className, param.classLoader)
    }

    fun findClass(className: String, classLoader: ClassLoader): Class<*> {
        return XposedHelpers.findClass(className, classLoader)
    }

    fun findAndBeforeHookMethod(className: String, methodName: String,
                          vararg parameterTypes: Any,
                          callback: (param: XC_MethodHook.MethodHookParam) -> Unit) {

        val parameterTypesAndCallback = arrayOf(
                *parameterTypes,
                object : XC_MethodHook() {

            override fun beforeHookedMethod(param: MethodHookParam) {
                super.beforeHookedMethod(param)
                // 直接调用
                callback(param)
            }
        })

        findAndHookMethod(className, methodName, *parameterTypesAndCallback)
    }

    fun findAndAfterHookMethod(className: String, methodName: String,
                                vararg parameterTypes: Any,
                                callback: (param: XC_MethodHook.MethodHookParam) -> Unit) {

        val parameterTypesAndCallback = arrayOf(
                *parameterTypes,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        super.afterHookedMethod(param)
                        // 直接调用
                        callback(param)
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
}