package com.petapp.capybara.profiles.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.petapp.capybara.common.BaseViewModel
import com.petapp.capybara.common.RecyclerItems
import com.petapp.capybara.extensions.notifyObserver
import com.petapp.capybara.profiles.domain.Profile
import com.petapp.capybara.profiles.domain.ProfilesRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProfilesViewModel(private val repository: ProfilesRepository) : BaseViewModel() {

    var profiles = MutableLiveData<MutableList<Profile>>()
    var isShowMock = MutableLiveData<Boolean>()

    fun updateProfiles() {
        repository.getProfiles()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    isShowMock.value = it.isEmpty()
                    profiles.value = it
                },
                { Log.d("database", "Get profiles error") }
            ).connect()
    }

    fun insertProfile(profile: Profile) {
        repository.insertProfile(profile)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    updateProfiles()
                    Log.d("database", "${profile.id} insert success")
                },
                { Log.d("database", "${profile.id} insert error") }
            ).connect()
    }

    fun deleteProfile(profileId: Int) {
        repository.deleteProfile(profileId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    updateProfiles()
                    Log.d("database", "$profileId delete success")
                },
                { Log.d("database", "$profileId delete error") }
            ).connect()
    }

    private fun updateProfile(profile: Profile) {
        repository.updateProfile(profile)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    updateProfiles()
                    Log.d("database", "${profile.id} update success")
                },
                { Log.d("database", "${profile.id} update error") }
            ).connect()
    }


    fun setProfileState(profile: Profile) {
        profiles.value?.filter { it.id != profile.id }?.forEach {
            it.isShowEditItem = false
            it.profileEdit.isShowColorsItem = false
        }
        if (!profile.isShowEditItem) updateProfile(profile) else profiles.notifyObserver()

    }

    fun showColorsItem() {
        profiles.notifyObserver()
    }

    fun setColor(parentId: Int, color: Int) {
        profiles.value?.find { it.id == parentId }.apply {
            this?.profileEdit?.profileColor?.chosenColor = color
            this?.color = color
        }
        profiles.notifyObserver()
    }

    fun flatten(items: List<Profile>): List<RecyclerItems> {
        val result = mutableListOf<RecyclerItems>()
        items.forEach { profile ->
            result.add(profile)
            if (profile.isShowEditItem) result.add(profile.profileEdit)
            if (profile.profileEdit.isShowColorsItem) result.add(profile.profileEdit.profileColor)
        }
        return result
    }
}