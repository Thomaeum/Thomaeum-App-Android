package net.informatikag.thomapp.viewables.fragments.substitution

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import net.informatikag.thomapp.R

class SubstitutionPagerAdapter(fm: FragmentManager, context: Context): FragmentStatePagerAdapter(fm){

    private val context: Context = context

    override fun getCount(): Int = 3

    override fun getItem(position: Int): Fragment {
        val fragment = SubstitutionListFragment()
        fragment.arguments = Bundle().apply {
            putInt("day", position)
        }
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position){
            0 -> context.getString(R.string.substitution_tab_title_0)
            1 -> context.getString(R.string.substitution_tab_title_1)
            2 -> context.getString(R.string.substitution_tab_title_2)
            else -> context.getString(R.string.substitution_tag_title_default, position)
        }
    }
}