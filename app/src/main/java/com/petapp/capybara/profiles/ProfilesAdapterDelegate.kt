package com.petapp.capybara.profiles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.petapp.capybara.R
import com.petapp.capybara.data.model.Profile
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_profile.view.*

class ProfilesAdapterDelegate(
    private val itemClick: (Profile, ImageView) -> Unit
) : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_profile, parent, false))
    }

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean {
        return items[position] is Profile
    }

    override fun onBindViewHolder(items: MutableList<Any>, position: Int, holder: RecyclerView.ViewHolder, payloads: MutableList<Any>) {
        (holder as ViewHolder).bind(items[position] as Profile)
    }

    private inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        private lateinit var profile: Profile

        init {
            containerView.setOnClickListener { itemClick(profile, containerView.photo) }
        }

        fun bind(profile: Profile) {
            this.profile = profile
            containerView.profile_name.text = profile.name
            containerView.photo.setColor(profile.color)
            containerView.photo.setInitials(profile.name)
            containerView.photo.transitionName = profile.name
            Glide.with(containerView)
                .load(profile.photo)
                .into(containerView.photo)
        }
    }
}