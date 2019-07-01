package com.gox.partner.views.earnings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.gox.partner.R
import com.gox.partner.databinding.ItemEarningsBinding

class EarningsItemFragment : Fragment() {

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View? {
        if (c == null) return null

        val binding: ItemEarningsBinding =
                DataBindingUtil.inflate(i, R.layout.item_earnings, c, false)
        val myView: View = binding.root

        binding.earningsAmt = this.arguments!!.getString("earnings")
        binding.itemText.text = this.arguments!!.getString("earningsTitle")
        binding.itemRoot.setScaleBoth(this.arguments!!.getFloat("scale"))
        return myView

    }

    companion object {
        fun newInstance(earnings: String, earningsTitle: String, scale: Float): Fragment {
            val bundle = Bundle()
            bundle.putString("earnings", earnings)
            bundle.putString("earningsTitle", earningsTitle)
            bundle.putFloat("scale", scale)
            val f = EarningsItemFragment()
            f.arguments = bundle
            return f
        }
    }
}
