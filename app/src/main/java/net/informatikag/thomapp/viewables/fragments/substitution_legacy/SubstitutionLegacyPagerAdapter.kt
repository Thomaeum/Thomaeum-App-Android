package net.informatikag.thomapp.viewables.fragments.substitution_legacy

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import net.informatikag.thomapp.R

class SubstitutionLegacyPagerAdapter(fm: FragmentManager, context: Context): FragmentStatePagerAdapter(fm){

    private val context: Context = context
    private val URL_KEY = "url"

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        val fragment = SubstitutionLegacyChildFragment()
        fragment.arguments = Bundle().apply {
            putString(URL_KEY, when(position){
                0 -> "preferences_substitution_legacy_url_unterstufe"
                1 -> "preferences_substitution_legacy_url_oberstufe"
                else -> ""
            })
        }
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position){
            0 -> context.getString(R.string.substitution_legacy_tab_title_0)
            1 -> context.getString(R.string.substitution_legacy_tab_title_1)
            else -> "Fehler"
        }
    }
}