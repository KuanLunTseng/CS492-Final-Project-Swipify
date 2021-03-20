package com.kuanluntseng.swipify.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.kuanluntseng.swipify.R

class SettingsFragment : PreferenceFragmentCompat(){
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref)
    }
}