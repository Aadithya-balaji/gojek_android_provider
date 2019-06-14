package com.gox.partner.views.manage_bank_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.gox.partner.BR
import com.gox.partner.R
import com.gox.partner.databinding.BankTemplateInflateBinding

class SetupBankTemplateAdapter(private val bankViewModel: ManageBankDetailsViewModel)
    : RecyclerView.Adapter<SetupBankTemplateAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<BankTemplateInflateBinding>(
                layoutInflater,
                R.layout.bank_template_inflate,
                parent,
                false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = bankViewModel.getItemCount()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(bankViewModel, position)
    }

    class ViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bankViewModel: ManageBankDetailsViewModel, position: Int) {
            binding.setVariable(BR.bankViewModel, bankViewModel)
            binding.setVariable(BR.position, position)
            binding.executePendingBindings()
        }
    }
}