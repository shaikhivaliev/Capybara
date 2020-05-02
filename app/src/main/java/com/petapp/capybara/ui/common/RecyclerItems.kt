package com.petapp.capybara.ui.list

sealed class RecyclerItems {

    abstract val id: String

    fun isSame(other: RecyclerItems) = id == other.id
}

data class Profile(override val id: String, val items: List<Color> = emptyList(), var isExpanded: Boolean = true) : RecyclerItems()
data class Color(override val id: String, val color: Int) : RecyclerItems()
data class Types(override val id: String, val icon: Int) : RecyclerItems()

fun List<Profile>.flatten(): List<RecyclerItems> {
    val result = mutableListOf<RecyclerItems>()
    this.forEach { profile ->
        result.add(profile)

        if (profile.isExpanded) {
            profile.items.forEach { color ->
                result.add(color)
            }
        }
    }
    return result
}

private fun colors() = listOf(
    Color("red", 0),
    Color("red", 0),
    Color("red", 0),
    Color("red", 0),
    Color("red", 0)
)