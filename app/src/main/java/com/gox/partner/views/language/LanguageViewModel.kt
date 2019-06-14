package com.gox.partner.views.language

import com.gox.base.base.BaseViewModel
import com.gox.partner.models.Language
import com.gox.partner.utils.Constant

class LanguageViewModel : BaseViewModel<LanguageNavigator>() {

    private lateinit var languages: List<Language>
    private lateinit var adapter: LanguageAdapter
    private lateinit var currentLanguage: String

    fun setLanguage(currentLanguage: String) {
        this.currentLanguage = currentLanguage
        languages = Constant.languages
        adapter = LanguageAdapter(this)
    }

    fun getAdapter(): LanguageAdapter = adapter

    fun getLanguages(): List<Language> = languages

    fun getLanguage(position: Int): Language = languages[position]

    fun getCurrentLanguage(): String = currentLanguage

    fun checkedChangeListener(checked: Boolean, position: Int) {
        val selectedLanguage = languages[position].key
        if (checked && selectedLanguage != currentLanguage) {
            currentLanguage = selectedLanguage
            adapter.setNewLocale(selectedLanguage)
        }
    }

    fun onSaveClicked() = navigator.onLanguageChanged()

}