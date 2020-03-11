package com.gox.partner.views.adapters

import android.content.Context
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.gox.partner.R
import java.util.*

class PaymentAdapter internal constructor(fm: FragmentManager, context: Context, fragList: Vector<Fragment>) : FragmentStatePagerAdapter(fm) {

    private var title: String? = null
    private var context: Context? = null
    private var fragmentList: Vector<Fragment>? = null

    init {
        this.context = context
        this.fragmentList = fragList
    }

    override fun getItem(position: Int) = fragmentList!!.get(position)

    override fun getCount() = fragmentList!!.size

    override fun getItemPosition(`object`: Any) = super.getItemPosition(`object`)

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) =
            super.destroyItem(container, position, `object`)

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) =
            super.setPrimaryItem(container, position, `object`)

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            0 -> title = context?.resources?.getString(R.string.wallet)
            1 -> title = context?.resources?.getString(R.string.transaction)
        }
        return title.toString()
    }
}