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

package com.sky.xposed.rmad.helper

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import com.sky.xposed.rmad.Constant
import com.sky.xposed.rmad.util.Alog

class PreferenceHelper(private val mContext: Context) {

    private val mMap = HashMap<String, String>()
    private val mReceiverHelper: ReceiverHelper

    init {

        // 初始化值
        initPreferenceValue()

        // 注册广播
        mReceiverHelper = ReceiverHelper(mContext, { action, intent ->
            onReceive(action, intent)
        }, Constant.Action.REFRESH_PREFERENCE)
        mReceiverHelper.registerReceiver()
    }

    fun getString(key: String, defValue: String): String {
        return mMap[key] ?: defValue
    }

    fun getInt(key: String, defValue: Int): Int {
        return mMap[key]?.toInt() ?: defValue
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return mMap[key]?.toBoolean() ?: defValue
    }

    fun getLong(key: String, defValue: Long): Long {
        return mMap[key]?.toLong() ?: defValue
    }

    fun getFloat(key: String, defValue: Float): Float {
        return mMap[key]?.toFloat() ?: defValue
    }

    fun release() {

        mReceiverHelper.unregisterReceiver()
    }

    fun getContentResolver(): ContentResolver {
        return mContext.contentResolver
    }

    private fun initPreferenceValue() {

        var cursor: Cursor? = null

        try {
            cursor = getContentResolver().query(
                    Uri.parse(Constant.UriString.PREFERENCE_ALL),
                    null, null, null, null)

            if (cursor != null) {

                while (cursor.moveToNext()) {

                    // 保存值
                    mMap[cursor.getString(0)] = cursor.getString(1)
                }
            }
        } catch (tr: Throwable) {
            Alog.e("获取属性信息异常", tr)
        } finally {
            // 关闭
            cursor?.close()
        }
    }

    private fun onReceive(action: String, intent: Intent) {

        if (Constant.Action.REFRESH_PREFERENCE == action) {

            // 获取刷新的值
            val data = intent.getSerializableExtra(Constant.Key.DATA)
                    as ArrayList<Pair<String, String>>

            data.forEach {
                // 重新设置值
                mMap[it.first] = it.second
            }
        }
    }
}