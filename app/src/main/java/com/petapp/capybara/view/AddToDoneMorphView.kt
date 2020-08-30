package com.petapp.capybara.view

import android.content.Context
import android.util.AttributeSet
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.petapp.capybara.R

class AddToDoneMorphView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FloatingActionButton(context, attributeSet, defStyleAttr) {

    private var addToDone: AnimatedVectorDrawableCompat? = null

    init {
        addToDone = AnimatedVectorDrawableCompat.create(context, R.drawable.avd_add_to_done)
        setImageDrawable(addToDone)
    }

    fun showDone() {
        setImageDrawable(addToDone)
        addToDone?.start()
    }
}