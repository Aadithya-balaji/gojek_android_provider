package com.appoets.xjek.ui.onboard

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.appoets.base.base.BaseActivity
import com.appoets.base.base.BaseFragment
import com.novoda.spritz.Spritz
import com.novoda.spritz.SpritzStep
import java.util.concurrent.TimeUnit
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.ActivityOnBoardBinding
import com.appoets.gojek.provider.views.onboard.OnBoardNavigator
import com.appoets.gojek.provider.views.onboard.OnBoardViewModel
import com.appoets.gojek.provider.views.signin.SignInActivity
import com.appoets.xjek.ui.signup.SignupActivity


class OnBoardActivityK :BaseActivity<com.appoets.gojek.provider.databinding.ActivityOnBoardBinding>(), OnBoardNavigator {

    lateinit var mViewDataBinding: com.appoets.gojek.provider.databinding.ActivityOnBoardBinding
    lateinit var spritz: Spritz
    lateinit var viewPager: ViewPager

    override fun getLayoutId(): Int {
        return R.layout.activity_on_board
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityOnBoardBinding
        val onBoardViewModel = OnBoardViewModel()
        onBoardViewModel.setNavigator(this)
        mViewDataBinding.viewModel = onBoardViewModel
        viewPager = mViewDataBinding.viewpagerOnboard
        viewPager.adapter = ScreenSlidePagerAdapter(supportFragmentManager)
        initSpritz()
    }

    override fun goToSignIn() {
        openNewActivity(this@OnBoardActivityK, SignInActivity::class.java,true)
    }

    override fun goToSignUp() {
       openNewActivity(this@OnBoardActivityK,SignupActivity::class.java,true)
    }



    private fun initSpritz() {
        spritz = Spritz.with(mViewDataBinding.animationView)
                .withSteps(SpritzStep.Builder()
                        .withAutoPlayDuration(0, TimeUnit.SECONDS)
                        .withSwipeDuration(5, TimeUnit.SECONDS)
                        .build(),
                        SpritzStep.Builder()
                                .withAutoPlayDuration(3, TimeUnit.SECONDS)
                                .withSwipeDuration(2, TimeUnit.SECONDS)
                                .build(),
                        SpritzStep.Builder()
                                .withAutoPlayDuration(3, TimeUnit.SECONDS)
                                .withSwipeDuration(4, TimeUnit.SECONDS)
                                .build()).build()
    }

    override fun onStart() {
        super.onStart()
        spritz.attachTo(viewPager)
        spritz.startPendingAnimations()
    }

    override fun onStop() {
        super.onStop()
        spritz.detachFrom(viewPager)
    }


}

class ScreenSlidePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(p0: Int): Fragment {
        return AnimationFragment()
    }


    override fun getCount(): Int = 3

}

class AnimationFragment : BaseFragment<com.appoets.gojek.provider.databinding.FragmentAnimationpageBinding>() {
    override fun getLayoutId(): Int = R.layout.fragment_animationpage

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
    }

}
