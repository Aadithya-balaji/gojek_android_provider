package com.gox.partner.views.account_card

interface CardListNavigator {
    fun addCard()
    fun cardPicked(stripeID: String, cardID: String, position: Int)
    fun removeCard()
    fun deselectCard()
    fun showErrorMsg(error: String)
}