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

package com.sky.xposed.rmad.util

import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * Created by sky on 17-11-7.
 */
object ReflectUtil {

    private val TAG: String = "ReflectUtil"

    fun getContextClassLoader(): ClassLoader {
        return Thread.currentThread().contextClassLoader
    }

    fun loadClassQuietly(classLoader: ClassLoader, className: String): Class<*>? {
        try {
            return classLoader.loadClass(className)
        } catch (e: Exception) {
            Alog.e(TAG, "LoadClass Exception", e)
        }
        return null
    }

    fun loadClassQuietly(className: String): Class<*>? {
        return loadClassQuietly(getContextClassLoader(), className)
    }

    fun newInstance(tClass: Class<*>?): Any? {
        return newInstance(tClass, null, null)
    }

    fun newInstance(tClass: Class<*>?, parameterTypes: Array<Class<*>>?, args: Array<Any>?): Any? {

        if (tClass == null) return null

        try {
            // 获取类的构造方法
            val tParameterTypes = parameterTypes ?: arrayOf()
            val tArgs = args ?: arrayOf()

            val constructor = tClass.getConstructor(*tParameterTypes)
            constructor.isAccessible = true

            // 创建类的实例
            return constructor.newInstance(*tArgs)
        } catch (e: Exception) {
            Alog.e(TAG, "newInstance Exception", e)
        }
        return null
    }

    fun findField(tClass: Class<*>, name: String): Field? {

        if (tClass == null) return null

        try {
            val field = tClass.getDeclaredField(name)
            field.isAccessible = true
            return field
        } catch (e: NoSuchFieldException) {
            Alog.e(TAG, "NoSuchFieldException", e)
        }

        return findField(tClass.superclass, name)
    }

    fun findMethod(tClass: Class<*>?, name: String, vararg parameterTypes: Class<*>): Method? {

        if (tClass == null) return null

        try {
            val method = tClass.getDeclaredMethod(name, *parameterTypes)
            method.isAccessible = true
            return method
        } catch (e: NoSuchMethodException) {
            Alog.e(TAG, "NoSuchMethodException", e)
        }

        return findMethod(tClass.superclass, name, *parameterTypes)
    }

    fun getValueQuietly(field: Field?, `object`: Any?): Any? {

        if (field == null) return null

        try {
            field.isAccessible = true
            return field.get(`object`)
        } catch (e: IllegalAccessException) {
            Alog.e(TAG, "IllegalAccessException", e)
        }

        return null
    }

    fun setValueQuietly(field: Field?, `object`: Any?, value: Any) {

        if (field == null) return

        try {
            field.isAccessible = true
            field.set(`object`, value)
        } catch (e: IllegalAccessException) {
            Alog.e(TAG, "IllegalAccessException", e)
        }
    }

    fun getFieldValue(tClass: Class<*>, name: String): Any? {
        return getValueQuietly(findField(tClass, name), null)
    }

    fun getFieldValue(`object`: Any?, name: String): Any? {

        return if (`object` == null) null else getValueQuietly(findField(`object`.javaClass, name), `object`)

    }

    fun setFieldValue(tClass: Class<*>, name: String, value: Any) {
        setValueQuietly(findField(tClass, name), null, value)
    }

    fun setFieldValue(`object`: Any?, name: String, value: Any) {

        if (`object` == null) return

        setValueQuietly(findField(`object`.javaClass, name), `object`, value)
    }

    @Throws(InvocationTargetException::class, IllegalAccessException::class)
    operator fun invoke(receiver: Any?, tClass: Class<*>?, name: String?,
                        parameterTypes: Array<Class<*>>?, args: Array<Any>?): Any? {

        if (tClass == null || name == null) return null

        val tParameterTypes = parameterTypes ?: arrayOf()
        val tArgs = args ?: arrayOf()

        val method = findMethod(tClass, name, *tParameterTypes)

        return method?.invoke(receiver, *tArgs)
    }

    fun invokeQuietly(receiver: Any?, tClass: Class<*>, name: String, parameterTypes: Array<Class<*>>?, args: Array<Any>?): Any? {

        try {
            return invoke(receiver, tClass, name, parameterTypes, args)
        } catch (e: Exception) {
            Alog.e(TAG, "Invoke Exception", e)
        }

        return null
    }

    fun invokeQuietly(receiver: Any, name: String): Any? {
        return invokeQuietly(receiver, name, null, null)
    }

    fun invokeQuietly(receiver: Any?, name: String, parameterTypes: Array<Class<*>>?, args: Array<Any>?): Any? {

        return if (receiver == null) null else invokeQuietly(receiver, receiver.javaClass, name, parameterTypes, args)

    }

    fun invokeQuietly(tClass: Class<*>, name: String, parameterTypes: Array<Class<*>>, args: Array<Any>): Any? {
        return invokeQuietly(null, tClass, name, parameterTypes, args)
    }

    fun invokeQuietly(tClass: Class<*>, name: String): Any? {
        return invokeQuietly(null, tClass, name, null, null)
    }
}