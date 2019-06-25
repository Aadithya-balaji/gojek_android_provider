package com.gox.partner.views.earnings

import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.gox.base.base.BaseActivity
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.partner.R
import com.gox.partner.databinding.ActivityEarningsBinding
import kotlinx.android.synthetic.main.activity_earnings.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*

class EarningsActivity : BaseActivity<ActivityEarningsBinding>(), EarningsNavigator {

    private lateinit var mBinding: ActivityEarningsBinding
    private lateinit var mViewModel: EarningsViewModel

    override fun getLayoutId(): Int = R.layout.activity_earnings

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mBinding = mViewDataBinding as ActivityEarningsBinding
        mViewModel = ViewModelProviders.of(this).get(EarningsViewModel::class.java)
        mViewModel.navigator = this
        mViewModel.showLoading = loadingObservable

        mBinding.viewModel = mViewModel

        mViewDataBinding.toolbarLayout.title_toolbar.setTitle(R.string.title_earnings)
        mViewDataBinding.toolbarLayout.toolbar_back_img.setOnClickListener { finish() }

        vpEarnings.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                println("RRR :: position = [$position]")
                when (position) {
                    0 -> {
                        bToday.background = ContextCompat.getDrawable(this@EarningsActivity, R.drawable.bg_blue_corner)
                        bToday.setTextColor(ContextCompat.getColor(this@EarningsActivity, R.color.white))
                        btThisWeek.background = ContextCompat.getDrawable(this@EarningsActivity, R.drawable.bg_white)
                        btThisWeek.setTextColor(ContextCompat.getColor(this@EarningsActivity, R.color.darkgrey))
                        bThisMonth.background = ContextCompat.getDrawable(this@EarningsActivity, R.drawable.bg_white)
                        bThisMonth.setTextColor(ContextCompat.getColor(this@EarningsActivity, R.color.darkgrey))
                    }
                    1 -> {
                        btThisWeek.background = ContextCompat.getDrawable(this@EarningsActivity, R.drawable.bg_blue_corner)
                        btThisWeek.setTextColor(ContextCompat.getColor(this@EarningsActivity, R.color.white))
                        bToday.background = ContextCompat.getDrawable(this@EarningsActivity, R.drawable.bg_white)
                        bToday.setTextColor(ContextCompat.getColor(this@EarningsActivity, R.color.darkgrey))
                        bThisMonth.background = ContextCompat.getDrawable(this@EarningsActivity, R.drawable.bg_white)
                        bThisMonth.setTextColor(ContextCompat.getColor(this@EarningsActivity, R.color.darkgrey))
                    }
                    2 -> {
                        bThisMonth.background = ContextCompat.getDrawable(this@EarningsActivity, R.drawable.bg_blue_corner)
                        bThisMonth.setTextColor(ContextCompat.getColor(this@EarningsActivity, R.color.white))
                        btThisWeek.background = ContextCompat.getDrawable(this@EarningsActivity, R.drawable.bg_white)
                        btThisWeek.setTextColor(ContextCompat.getColor(this@EarningsActivity, R.color.darkgrey))
                        bToday.background = ContextCompat.getDrawable(this@EarningsActivity, R.drawable.bg_white)
                        bToday.setTextColor(ContextCompat.getColor(this@EarningsActivity, R.color.darkgrey))
                    }
                }
            }
        })

        bToday.setOnClickListener {
            bToday.background = ContextCompat.getDrawable(this@EarningsActivity, R.drawable.bg_blue_corner)
            bToday.setTextColor(ContextCompat.getColor(this@EarningsActivity, R.color.white))
            btThisWeek.background = ContextCompat.getDrawable(this@EarningsActivity, R.drawable.bg_white)
            btThisWeek.setTextColor(ContextCompat.getColor(this@EarningsActivity, R.color.darkgrey))
            bThisMonth.background = ContextCompat.getDrawable(this@EarningsActivity, R.drawable.bg_white)
            bThisMonth.setTextColor(ContextCompat.getColor(this@EarningsActivity, R.color.darkgrey))
            vpEarnings.currentItem = 0
        }

        btThisWeek.setOnClickListener {
            btThisWeek.background = ContextCompat.getDrawable(this@EarningsActivity, R.drawable.bg_blue_corner)
            btThisWeek.setTextColor(ContextCompat.getColor(this@EarningsActivity, R.color.white))
            bToday.background = ContextCompat.getDrawable(this@EarningsActivity, R.drawable.bg_white)
            bToday.setTextColor(ContextCompat.getColor(this@EarningsActivity, R.color.darkgrey))
            bThisMonth.background = ContextCompat.getDrawable(this@EarningsActivity, R.drawable.bg_white)
            bThisMonth.setTextColor(ContextCompat.getColor(this@EarningsActivity, R.color.darkgrey))
            vpEarnings.currentItem = 1
        }

        bThisMonth.setOnClickListener {
            bThisMonth.background = ContextCompat.getDrawable(this@EarningsActivity, R.drawable.bg_blue_corner)
            bThisMonth.setTextColor(ContextCompat.getColor(this@EarningsActivity, R.color.white))
            btThisWeek.background = ContextCompat.getDrawable(this@EarningsActivity, R.drawable.bg_white)
            btThisWeek.setTextColor(ContextCompat.getColor(this@EarningsActivity, R.color.darkgrey))
            bToday.background = ContextCompat.getDrawable(this@EarningsActivity, R.drawable.bg_white)
            bToday.setTextColor(ContextCompat.getColor(this@EarningsActivity, R.color.darkgrey))
            vpEarnings.currentItem = 2
        }

        mViewModel.earnings.observe(this, Observer {
            if (it != null) {
                mBinding.viewPagerAdapter = EarningsPagerAdapter(this.supportFragmentManager, it.responseData, this)
                vpEarnings.setPageTransformer(false, mBinding.viewPagerAdapter)
                vpEarnings.currentItem = EarningsPagerAdapter.FIRST_PAGE
                vpEarnings.offscreenPageLimit = 3
                vpEarnings.pageMargin = -250
            }
        })

        mViewModel.earnings(readPreferences(PreferencesKey.PROVIDER_ID))
    }
}