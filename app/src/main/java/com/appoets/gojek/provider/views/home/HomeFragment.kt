package com.appoets.gojek.provider.views.home

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appoets.basemodule.base.BaseFragment
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.views.adapters.HomeAdapter
import com.appoets.gojek.provider.views.dashboard.DashBoardNavigator

class HomeFragment:BaseFragment<com.appoets.gojek.provider.databinding.FragmentHomePageBinding>(){
    private  lateinit var mHomeDataBinding:com.appoets.gojek.provider.databinding.FragmentHomePageBinding
    private  var mHomeViewModel:HomeViewModel?=null
    private  lateinit var   rvHome:RecyclerView
    private  var  rvHomeAdapter:HomeAdapter?=null
    private  var  linearLayoutManager:LinearLayoutManager?=null
    private var appCompatActivity: AppCompatActivity? = null
    private var dashBoardNavigator: DashBoardNavigator? = null
    override fun getLayoutId(): Int {
        return  R.layout.fragment_home_page
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        dashBoardNavigator = context as DashBoardNavigator
    }

    override fun initView(mRootView: View, mViewDataBinding: ViewDataBinding?) {
        mHomeDataBinding=mViewDataBinding as com.appoets.gojek.provider.databinding.FragmentHomePageBinding
        mHomeViewModel =HomeViewModel()
        mHomeDataBinding.homemodel=mHomeViewModel
        rvHome=mRootView.findViewById(R.id.rv_home)
        linearLayoutManager= LinearLayoutManager(activity)
        appCompatActivity = if (dashBoardNavigator?.getInstance() != null) dashBoardNavigator?.getInstance() else activity as AppCompatActivity
        rvHomeAdapter= HomeAdapter(appCompatActivity!!)
        rvHome.adapter=rvHomeAdapter
        rvHome.layoutManager=linearLayoutManager
    }

}
