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

package com.sky.xposed.rmad.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import com.sky.xposed.rmad.Constant
import com.sky.xposed.rmad.R
import com.sky.xposed.rmad.helper.ReceiverHelper

/**
 * Created by sky on 17-11-1.
 */
class SettingsFragment :
        PreferenceFragment(),
        Preference.OnPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.setting_preferences)

        findPreference(Constant.Preference.NEWS_ALL_AD).onPreferenceChangeListener = this
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {

        val data = arrayListOf(Pair<String, String>(preference.key, newValue.toString()))

        val intent = Intent(Constant.Action.REFRESH_PREFERENCE)
        intent.putExtra(Constant.Key.DATA, data)

        // 发送广播
        ReceiverHelper.sendBroadcastReceiver(activity, intent)

        return true
    }
}