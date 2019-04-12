package com.xjek.provider.views.language

import com.xjek.base.base.BaseViewModel
import com.xjek.provider.models.ConfigResponseModel.ResponseData.AppSetting.Language
import com.xjek.provider.utils.Constant

class LanguageViewModel : BaseViewModel<LanguageNavigator>(){

    private lateinit var languages:List<Language>
    private lateinit var adapter: LanguageAdapter

    fun setLanguage(){
        languages = Constant.languages
        adapter = LanguageAdapter(this)
    }

    fun getAdapter():LanguageAdapter = adapter

    fun getLanguages():List<Language> = languages


    fun getLanguage(position:Int):String = languages[position].name

}