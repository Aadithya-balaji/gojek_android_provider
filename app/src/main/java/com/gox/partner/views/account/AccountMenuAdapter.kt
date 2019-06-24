package com.gox.partner.views.account

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.gox.partner.BR
import com.gox.partner.R
import com.gox.partner.databinding.LayoutAccountMenuItemBinding

class AccountMenuAdapter(private val mViewModel: AccountViewModel) :
        RecyclerView.Adapter<AccountMenuAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(DataBindingUtil.inflate<LayoutAccountMenuItemBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.layout_account_menu_item,
                    parent,
                    false)
            )

    override fun getItemCount() = mViewModel.getAccountMenus().size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
            holder.bind(mViewModel, position)

    class ViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(accountViewModel: AccountViewModel, position: Int) {
            binding.setVariable(BR.accountViewModel, accountViewModel)
            binding.setVariable(BR.position, position)
            binding.executePendingBindings()
        }
    }
}