package com.xjek.provider.views.onboard

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.xjek.base.base.BaseActivity
import com.xjek.base.base.BaseFragment
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityOnBoardBinding
import com.xjek.provider.views.signin.SignInActivity
import com.xjek.provider.views.signup.SignupActivity
import com.novoda.spritz.Spritz
import com.novoda.spritz.SpritzStep
import java.util.concurrent.TimeUnit


class OnBoardActivityK : BaseActivity<com.xjek.provider.databinding.ActivityOnBoardBinding>(), OnBoardNavigator {

    private lateinit var mViewDataBinding: com.xjek.provider.databinding.ActivityOnBoardBinding
    private lateinit var spritz: Spritz
    private lateinit var viewPager: ViewPager
    private lateinit var ivBack: ImageView
    private lateinit var tbrRightTitle: TextView
    private lateinit var ivLogo: ImageView
    private lateinit var ivRight: ImageView

    override fun getLayoutId(): Int {
        return R.layout.activity_on_board
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityOnBoardBinding
        val onBoardViewModel = OnBoardViewModel(this)
        mViewDataBinding.viewModel = onBoardViewModel
        viewPager = mViewDataBinding.viewpagerOnboard
        viewPager.adapter = ScreenSlidePagerAdapter(supportFragmentManager)
        initSpritz()

        //InitView

    }

    override fun goToSignIn() {
        openNewActivity(this@OnBoardActivityK, SignInActivity::class.java, false)
    }

    override fun goToSignUp() {
        openNewActivity(this@OnBoardActivityK, SignupActivity::class.java, false)
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

class AnimationFragment : BaseFragment<com.xjek.provider.databinding.FragmentAnimationpageBinding>() {
    override fun getLayoutId(): Int = R.layout.fragment_animationpage

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
    }

}
