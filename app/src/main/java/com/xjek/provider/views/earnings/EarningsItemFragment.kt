package com.xjek.provider.views.earnings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.xjek.provider.R

class EarningsItemFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (container == null) return null

        val binding: com.xjek.provider.databinding.ItemEarningsBinding = DataBindingUtil.inflate(inflater, R.layout.item_earnings, container, false)
        val myView: View = binding.root

        binding.earningsAmt = this.arguments!!.getString("data")
        binding.itemRoot.setScaleBoth(this.arguments!!.getFloat("scale"))

        return myView
    }

    companion object {

        fun newInstance(position: String, scale: Float): Fragment {
            val bundle = Bundle()
            bundle.putString("data", position)
            bundle.putFloat("scale", scale)
            val f = EarningsItemFragment()
            f.arguments = bundle
            return f
        }
    }
}
