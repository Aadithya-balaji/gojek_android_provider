package com.appoets.gojek.provider.views.home

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.appoets.base.base.BaseFragment
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.FragmentHomePageBinding
import com.appoets.gojek.provider.views.dashboard.DashBoardNavigator
import com.appoets.gojek.provider.views.foodproviderfragment.FoodProviderFragment
import com.appoets.gojek.provider.views.taxiproviderfragment.TaxiProviderFragment
import com.appoets.gojek.provider.views.xuberServicesProviderFragment.XuberServicesProviderFragment

class HomeFragment : BaseFragment<com.appoets.gojek.provider.databinding.FragmentHomePageBinding>(), Home_Navigator {

    private lateinit var mHomeDataBinding: com.appoets.gojek.provider.databinding.FragmentHomePageBinding
    override fun getLayoutId(): Int = R.layout.fragment_home_page
    private lateinit var dashBoardNavigator: DashBoardNavigator



    override fun initView(mRootView: View, mViewDataBinding: ViewDataBinding?) {
        mHomeDataBinding = mViewDataBinding as FragmentHomePageBinding
        val mHomeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java);
        mHomeDataBinding.homemodel = mHomeViewModel
        activity?.supportFragmentManager?.beginTransaction()?.add(R.id.provider_container, TaxiProviderFragment())?.commit()
        mHomeViewModel!!.setNavigator(this);

    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        dashBoardNavigator = context as DashBoardNavigator
    }


    override fun gotoTaxiModule() {

        mHomeDataBinding.foodieAppTv.background = ContextCompat.getDrawable(this!!.activity!!, R.drawable.bg_service_unselected)
        mHomeDataBinding.foodieAppTv.setTextColor(ContextCompat.getColor(this.activity!!, R.color.black))
        mHomeDataBinding.servicesAppTv.background = ContextCompat.getDrawable(this!!.activity!!, R.drawable.bg_service_unselected)
        mHomeDataBinding.servicesAppTv.setTextColor(ContextCompat.getColor(this.activity!!, R.color.black))
        mHomeDataBinding.taxiAppTv.background = ContextCompat.getDrawable(this!!.activity!!, R.drawable.bg_service_selected)
        mHomeDataBinding.taxiAppTv.setTextColor(ContextCompat.getColor(this.activity!!, R.color.selected_provider_tc))

        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.provider_container, TaxiProviderFragment())?.commit()

    }

    override fun gotoFoodieModule() {


        mHomeDataBinding.foodieAppTv.background = ContextCompat.getDrawable(this!!.activity!!, R.drawable.bg_service_selected)
        mHomeDataBinding.foodieAppTv.setTextColor(ContextCompat.getColor(this.activity!!, R.color.selected_provider_tc))
        mHomeDataBinding.servicesAppTv.background = ContextCompat.getDrawable(this!!.activity!!, R.drawable.bg_service_unselected)
        mHomeDataBinding.servicesAppTv.setTextColor(ContextCompat.getColor(this.activity!!, R.color.black))
        mHomeDataBinding.taxiAppTv.background = ContextCompat.getDrawable(this!!.activity!!, R.drawable.bg_service_unselected)
        mHomeDataBinding.taxiAppTv.setTextColor(ContextCompat.getColor(this.activity!!, R.color.black))

        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.provider_container, FoodProviderFragment())?.commit()

    }

    override fun gotoXuberModule() {

        mHomeDataBinding.foodieAppTv.background = ContextCompat.getDrawable(this!!.activity!!, R.drawable.bg_service_unselected)
        mHomeDataBinding.foodieAppTv.setTextColor(ContextCompat.getColor(this.activity!!, R.color.black))
        mHomeDataBinding.servicesAppTv.background = ContextCompat.getDrawable(this!!.activity!!, R.drawable.bg_service_selected)
        mHomeDataBinding.servicesAppTv.setTextColor(ContextCompat.getColor(this.activity!!, R.color.selected_provider_tc))
        mHomeDataBinding.taxiAppTv.background = ContextCompat.getDrawable(this!!.activity!!, R.drawable.bg_service_unselected)
        mHomeDataBinding.taxiAppTv.setTextColor(ContextCompat.getColor(this.activity!!, R.color.black))

        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.provider_container, XuberServicesProviderFragment())?.commit()

    }


}
