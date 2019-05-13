package com.xjek.provider.views.earnings

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.xjek.provider.R

class EarningsPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager), ViewPager.PageTransformer {

    override fun getItem(position: Int): Fragment {
        val mScale: Float = if (position == FIRST_PAGE) BIG_SCALE
        else SMALL_SCALE
//        val data = mList[position]
//        val earnings = when (position) {
//            0 -> "&$data"
//            1 -> "&$data"
//            2 -> "&$data"
//            else -> "&0"
//        }
        return EarningsItemFragment.newInstance("@0", mScale)
    }

    override fun getCount() = PAGES

    override fun transformPage(page: View, position: Float) {
        val myLinearLayout = page.findViewById<EarningsItemLayout>(R.id.item_root)
        var scale = BIG_SCALE
        if (position > 0) scale -= position * DIFF_SCALE else scale += position * DIFF_SCALE
        if (scale < 0) scale = 0f
        myLinearLayout.setScaleBoth(scale)
    }

    companion object {
        const val PAGES = 3
        const val FIRST_PAGE = 0
        const val BIG_SCALE = 1.0f
        const val SMALL_SCALE = 0.7f
        const val DIFF_SCALE = BIG_SCALE - SMALL_SCALE
    }
}