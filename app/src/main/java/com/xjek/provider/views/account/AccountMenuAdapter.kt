package com.xjek.provider.views.account

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.xjek.provider.BR
import com.xjek.provider.R
import com.xjek.provider.databinding.LayoutAccountMenuItemBinding

class AccountMenuAdapter(private val accountViewModel: AccountViewModel) :
        RecyclerView.Adapter<AccountMenuAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<LayoutAccountMenuItemBinding>(
                layoutInflater,
                R.layout.layout_account_menu_item,
                parent,
                false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return accountViewModel.getAccountMenus().size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(accountViewModel, position)
    }

    class ViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(accountViewModel: AccountViewModel, position: Int) {
            binding.setVariable(BR.accountViewModel, accountViewModel)
            binding.setVariable(BR.position, position)
            binding.executePendingBindings()
        }
    }
}