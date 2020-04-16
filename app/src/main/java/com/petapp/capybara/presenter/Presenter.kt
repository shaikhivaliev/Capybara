package com.petapp.capybara.presenter

interface Presenter {

    /*лучше использовать lifecycle*/
    fun onDateClicked(id: Int)
    fun onDestroy()

}