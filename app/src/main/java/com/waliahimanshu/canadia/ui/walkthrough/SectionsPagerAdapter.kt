package com.waliahimanshu.canadia.ui.walkthrough

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

private const val totalSections = 3

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {


    override fun getItem(pos: Int): Fragment {
        return when (pos) {
            0 -> Section0Fragment.newInstance(0)
            1 -> Section1Fragment.newInstance(1)
            else -> Section2Fragment.newInstance(2)
        }
    }

    override fun getCount(): Int {
        return totalSections
    }
}
