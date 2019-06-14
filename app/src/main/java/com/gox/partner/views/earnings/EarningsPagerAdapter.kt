package com.gox.partner.views.earnings

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.gox.base.data.PreferencesHelper
import com.gox.base.data.PreferencesKey
import com.gox.partner.R
import com.gox.partner.models.EarningsResponseData

class EarningsPagerAdapter(fragmentManager: FragmentManager,
                           private val response: EarningsResponseData,
                           private val activity: EarningsActivity)
    : FragmentPagerAdapter(fragmentManager), ViewPager.PageTransformer {

    override fun getItem(position: Int): Fragment {
        val mScale: Float = if (position == FIRST_PAGE) BIG_SCALE
        else SMALL_SCALE

        var earnings = ""
        var earningsTitle = activity.getString(R.string.today_target)
        val symbol = PreferencesHelper.get(PreferencesKey.CURRENCY_SYMBOL, "₹")
        when (position) {
            0 -> {
                earnings = "$symbol ${response.today}"
                earningsTitle = activity.getString(R.string.today_target)
            }
            1 -> {
                earnings = "$symbol ${response.week}"
                earningsTitle = activity.getString(R.string.weekly_target)
            }
            2 -> {
                earnings = "$symbol ${response.month}"
                earningsTitle = activity.getString(R.string.monthly_target)
            }
        }
        return EarningsItemFragment.newInstance(earnings, earningsTitle, mScale)
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