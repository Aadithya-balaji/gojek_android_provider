package com.xjek.xuberservice.ratingFragment

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import com.appoets.base.base.BaseFragment
import com.xjek.xuberservice.R
import com.xjek.xuberservice.databinding.FragmentRatingLayoutBinding
import com.xjek.xuberservice.xuberMainActivity.CommunicationListener
import com.xjek.xuberservice.xuberMainActivity.XuberMainActivity

class RatingFragment : BaseFragment<FragmentRatingLayoutBinding>(), RatingNavigator {

    private lateinit var mCommunicationListener: CommunicationListener

    companion object {
        fun newInstance(): RatingFragment {
            return RatingFragment()
        }
    }


    lateinit var mViewDataBinding: FragmentRatingLayoutBinding
    override fun getLayoutId(): Int = R.layout.fragment_rating_layout

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as FragmentRatingLayoutBinding
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is XuberMainActivity) {
            mCommunicationListener = context
        }
    }

    override fun submitRating() {

        mCommunicationListener.onMessage("rating")
    }


}
