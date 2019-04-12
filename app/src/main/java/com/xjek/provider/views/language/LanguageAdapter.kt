package com.xjek.provider.views.language

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.xjek.provider.BR
import com.xjek.provider.R
import com.xjek.provider.databinding.LanguageInflateBinding

class LanguageAdapter(private val languageViewModel: LanguageViewModel): RecyclerView.Adapter<LanguageAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<LanguageInflateBinding>(
                layoutInflater,
                R.layout.language_inflate,
                parent,
                false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = languageViewModel.getLanguages().size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(languageViewModel,position)
    }


    class ViewHolder(private val binding: ViewDataBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(languageViewModel: LanguageViewModel, position: Int) {
            binding.setVariable(BR.languageViewModel, languageViewModel)
            binding.setVariable(BR.position, position)
            binding.executePendingBindings()
        }
    }
}