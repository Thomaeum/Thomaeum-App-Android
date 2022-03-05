package net.informatikag.thomapp.viewables.fragments.settings

import android.os.Bundle
import android.util.Log
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import net.informatikag.thomapp.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        findPreference<Preference>("preferences_substitution_login")?.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                //TODO add Login process here
                Log.d("login", "LOGING IN")
                true
            }
    }
}