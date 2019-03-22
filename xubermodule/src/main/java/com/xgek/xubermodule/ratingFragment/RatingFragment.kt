package com.xgek.xubermodule.ratingFragment

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import com.appoets.basemodule.base.BaseFragment
import com.xgek.xubermodule.R
import com.xgek.xubermodule.databinding.FragmentRatingLayoutBinding
import com.xgek.xubermodule.startservice_fragment.StartServiceFragment
import com.xgek.xubermodule.xuberMainActivity.CommunicationListener
import com.xgek.xubermodule.xuberMainActivity.XuberMainActivity

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

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is XuberMainActivity) {
            mCommunicationListener = context
        }
    }

    override fun submitRating() {

        mCommunicationListener.onMessage("rating")
    }


}
