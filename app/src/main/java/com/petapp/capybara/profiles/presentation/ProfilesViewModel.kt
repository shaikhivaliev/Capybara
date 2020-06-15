package com.petapp.capybara.profiles.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.petapp.capybara.common.BaseItem
import com.petapp.capybara.common.BaseViewModel
import com.petapp.capybara.extensions.notifyObserver
import com.petapp.capybara.profiles.domain.Profile
import com.petapp.capybara.profiles.domain.ProfilesRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProfilesViewModel @Inject constructor(private val repository: ProfilesRepository) : BaseViewModel() {

    private val _profiles = MutableLiveData<MutableList<Profile>>()
    val profiles: LiveData<MutableList<Profile>> get() = _profiles
    private val _isShowMock = MutableLiveData<Boolean>()
    val isShowMock: LiveData<Boolean> get() = _isShowMock

    fun updateProfiles() {
        repository.getProfiles()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _isShowMock.value = it.isEmpty()
                    _profiles.value = it
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


    fun changeProfileItemState(profile: Profile) {
        profiles.value?.filter { it.id != profile.id }?.forEach {
            it.isShowEditItem = false
            it.profileEdit.isShowColorsItem = false
        }

        if (profile.isShowEditItem) {
            _profiles.notifyObserver()
        } else {
            updateProfile(profile)
        }

    }

    fun showColorsItem() {
        _profiles.notifyObserver()
    }

    fun setColor(parentId: Int, color: Int) {
        profiles.value?.find { it.id == parentId }?.apply {
            this.profileEdit.profileColor.chosenColor = color
            this.color = color
        }
        _profiles.notifyObserver()
    }

    fun mapToBaseItem(items: List<Profile>): List<BaseItem> {
        val result = mutableListOf<BaseItem>()
        items.forEach { profile ->
            result.add(profile)
            if (profile.isShowEditItem) result.add(profile.profileEdit)
            if (profile.profileEdit.isShowColorsItem) result.add(profile.profileEdit.profileColor)
        }
        return result
    }
}