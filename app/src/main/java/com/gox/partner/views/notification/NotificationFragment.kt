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


    lateinit var mViewDataBinding: FragmentNotificationBinding
    private lateinit var dashBoardNavigator: DashBoardNavigator


    override fun getLayoutId(): Int = R.layout.fragment_notification

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dashBoardNavigator = context as DashBoardNavigator
    }


    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as FragmentNotificationBinding
        val notificationViewModel = NotificationViewModel()
        notificationViewModel.navigator = this
        mViewDataBinding.notificationviewmodel = notificationViewModel
        mViewDataBinding.setLifecycleOwner(this)

        observeLiveData(notificationViewModel.loadingProgress) {
            loadingObservable.value = it
        }

        notificationViewModel.getNotificationList()

        dashBoardNavigator.setTitle(resources.getString(R.string.title_notification))
        dashBoardNavigator.hideRightIcon(true)
        dashBoardNavigator.showLogo(false)

        observeLiveData(notificationViewModel.notificationResponse) {
            notificationViewModel.loadingProgress.value = false
            //show empty view if no data
            notificationViewModel.showEmptyView.value = it.responseData.notification.data.isNullOrEmpty()
            setNotificationAdapter(it.responseData.notification)

        }

        observeLiveData(notificationViewModel.errorResponse) { errorMessage ->
            run {
                notificationViewModel.loadingProgress.value = false
                notificationViewModel.showEmptyView.value = true
                ViewUtils.showToast(activity!!, errorMessage, false)
            }
        }


    }

    private fun setNotificationAdapter(notificationResponseData: NotificationResponse.ResponseData.Notification) {
        this.mViewDataBinding.notificationRv.adapter = NotificationAdapter(activity, notificationResponseData)

    }

    override fun goToDetailPage() {
    }

}
