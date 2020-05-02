package com.petapp.capybara.ui.list

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.petapp.capybara.R

fun profileDelegate(onClick: (Profile) -> Unit) = adapterDelegateLayoutContainer<Profile, RecyclerItems>(R.layout.item_new_profile) {
    itemView.setOnClickListener { onClick(item) }
    bind { }
}

fun colorDelegate(onClick: (Color) -> Unit) = adapterDelegateLayoutContainer<Color, RecyclerItems>(R.layout.item_color) {
    itemView.setOnClickListener { onClick(item) }
    bind { }
}

fun typesDelegate(onClick: (Types) -> Unit) = adapterDelegateLayoutContainer<Types, RecyclerItems>(R.layout.item_survey_type) {
    itemView.setOnClickListener { onClick(item) }
    bind {  }
}