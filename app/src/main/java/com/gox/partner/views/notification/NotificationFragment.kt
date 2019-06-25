package com.gox.partner.views.notification

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import com.gox.base.base.BaseFragment
import com.gox.base.extensions.observeLiveData
import com.gox.base.utils.ViewUtils
import com.gox.partner.R
import com.gox.partner.databinding.FragmentNotificationBinding
import com.gox.partner.models.NotificationResponse
import com.gox.partner.views.adapters.NotificationAdapter
import com.gox.partner.views.dashboard.DashBoardNavigator

class NotificationFragment : BaseFragment<FragmentNotificationBinding>(), NotificationNavigator {

    lateinit var mBinding: FragmentNotificationBinding
    private lateinit var mNavigator: DashBoardNavigator

    override fun getLayoutId() = R.layout.fragment_notification

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mNavigator = context as DashBoardNavigator
    }

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        this.mBinding = mViewDataBinding as FragmentNotificationBinding
        val mViewModel = NotificationViewModel()
        mViewModel.navigator = this
        mViewDataBinding.notificationviewmodel = mViewModel
        mViewDataBinding.setLifecycleOwner(this)

        observeLiveData(mViewModel.showLoading) {
            loadingObservable.value = it
        }

        mViewModel.getNotificationList()

        mNavigator.setTitle(resources.getString(R.string.title_notification))
        mNavigator.hideRightIcon(true)
        mNavigator.showLogo(false)

        observeLiveData(mViewModel.notificationResponse) {
            mViewModel.showLoading.value = false
            mViewModel.showEmptyView.value = it.responseData.notification.data.isNullOrEmpty()
            setNotificationAdapter(it.responseData.notification)
        }

        observeLiveData(mViewModel.errorResponse) { errorMessage ->
            run {
                mViewModel.showLoading.value = false
                mViewModel.showEmptyView.value = true
                ViewUtils.showToast(activity!!, errorMessage, false)
            }
        }
    }

    private fun setNotificationAdapter(notificationResponseData: NotificationResponse.ResponseData.Notification) {
        this.mBinding.notificationRv.adapter = NotificationAdapter(activity, notificationResponseData)
    }
}
