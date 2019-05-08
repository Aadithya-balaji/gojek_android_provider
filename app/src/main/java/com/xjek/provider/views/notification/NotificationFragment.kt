package com.xjek.provider.views.notification

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseFragment
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.utils.ViewUtils
import com.xjek.provider.R
import com.xjek.provider.models.NotificationResponseData
import com.xjek.provider.views.adapters.NotificationAdapter
import com.xjek.provider.views.dashboard.DashBoardNavigator

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
            notificationViewModel.showEmptyView.value = it.responseData.data.isNullOrEmpty()
            setNotificationAdapter(it.responseData)

        }

        observeLiveData(notificationViewModel.errorResponse) { errorMessage ->
            run {
                notificationViewModel.loadingProgress.value = false
                notificationViewModel.showEmptyView.value = true
                ViewUtils.showToast(activity!!, errorMessage, false)
            }
        }


    }

    private fun setNotificationAdapter(notificationResponseData: NotificationResponseData) {
        this.mViewDataBinding.notificationRv.adapter = NotificationAdapter(activity, notificationResponseData)

    }

    override fun goToDetailPage() {
    }

}
