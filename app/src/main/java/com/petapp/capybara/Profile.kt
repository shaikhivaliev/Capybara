package com.petapp.capybara

import com.petapp.capybara.common.RecyclerItems
import java.util.concurrent.atomic.AtomicInteger

class Profile(
    override var id: Int = UniqueId.id,
    val title: String,
    var color: Int = android.R.color.white,
    var editProfile: EditProfile = EditProfile(
        parentId = id
    ),
    var isExpandedEdit: Boolean = false,
    var isEditMode: Boolean = false
) : RecyclerItems()

fun List<Profile>.flatten(): List<RecyclerItems> {
    val result = mutableListOf<RecyclerItems>()
    this.forEach { profile ->
        result.add(profile)
        if (profile.isExpandedEdit) result.add(profile.editProfile)
        if (profile.editProfile.isExpandedColor) result.add(profile.editProfile.color)
    }
    return result
}

class UniqueId {
    companion object {
        private val c = AtomicInteger(0)
        val id: Int
            get() = c.incrementAndGet()
    }
}
