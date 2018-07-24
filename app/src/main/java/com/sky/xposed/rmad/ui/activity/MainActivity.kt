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

package com.sky.xposed.rmad.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.sky.xposed.rmad.BuildConfig
import com.sky.xposed.rmad.R
import com.sky.xposed.rmad.ui.view.ItemMenu
import com.sky.xposed.rmad.util.VToast

class MainActivity : Activity() {

    private lateinit var imVersion: ItemMenu
    private lateinit var tvSupportVersion: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 初始化
        VToast.getInstance().init(applicationContext)

        imVersion = findViewById(R.id.im_version)
        tvSupportVersion = findViewById(R.id.tv_support_version)

        imVersion.setDesc("v${BuildConfig.VERSION_NAME}")
    }

    fun onClick(view: View) {

        when(view.id) {
            R.id.im_source -> {
                openUrl("https://github.com/sky-wei/xposed-rmad")
            }
            R.id.im_download -> {
                openUrl("http://repo.xposed.info/module/com.sky.xposed.rmad")
            }
        }
    }

    private fun openUrl(url: String) {

        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)

            // 调用系统浏览器打开
            startActivity(intent)
        } catch (tr: Throwable) {
            VToast.show("打开浏览器异常")
        }
    }
}
