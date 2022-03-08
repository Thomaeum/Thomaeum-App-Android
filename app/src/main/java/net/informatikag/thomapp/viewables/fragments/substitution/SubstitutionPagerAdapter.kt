package net.informatikag.thomapp.viewables.fragments.substitution

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class SubstitutionPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm){
    override fun getCount(): Int = 3

    override fun getItem(position: Int): Fragment {
        val fragment = SubstitutionListFragment()
        fragment.arguments = Bundle().apply {
            putInt("day", position)
        }
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence {
        return position.toString()
    }
}