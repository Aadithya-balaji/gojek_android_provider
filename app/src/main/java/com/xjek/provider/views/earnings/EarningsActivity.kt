package com.xjek.provider.views.earnings

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.xjek.base.base.BaseActivity
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.extensions.readPreferences
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityEarningsBinding
import kotlinx.android.synthetic.main.activity_earnings.*

class EarningsActivity : BaseActivity<ActivityEarningsBinding>(), EarningsNavigator {

    private lateinit var mBinding: ActivityEarningsBinding
    private lateinit var mViewModel: EarningsViewModel

    override fun getLayoutId(): Int = R.layout.activity_earnings

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mBinding = mViewDataBinding as ActivityEarningsBinding
        mViewModel = ViewModelProviders.of(this).get(EarningsViewModel::class.java)
        mViewModel.navigator = this
        mViewModel.loadingProgress = loadingObservable as MutableLiveData<Boolean>

        mBinding.viewModel = mViewModel
        mBinding.viewPagerAdapter = EarningsPagerAdapter(this.supportFragmentManager)

        vpEarnings.setPageTransformer(false, mBinding.viewPagerAdapter)
        vpEarnings.currentItem = EarningsPagerAdapter.FIRST_PAGE
        vpEarnings.offscreenPageLimit = 3
        vpEarnings.pageMargin = -400

        vpEarnings.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
//                println("RRR :: state = [$state]= [${vpEarnings.currentItem}]")
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                println("RRR :: position = [$position]")
            }
        })

        vpEarnings.currentItem

        observeLiveData(mViewModel.earningsMonth) {
            if (mViewModel.earningsMonth.value != null) {

            }
        }

        observeLiveData(mViewModel.earningsWeek) {
            if (mViewModel.earningsWeek.value != null) {

            }
        }

        observeLiveData(mViewModel.earningsDay) {
            if (mViewModel.earningsDay.value != null) {

            }
        }



        mViewModel.earnings(readPreferences(PreferencesKey.PROVIDER_ID))
    }
}