package com.petapp.capybara.widget

import android.content.Context
import android.util.AttributeSet
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.petapp.capybara.R

class DoneToAddMorphView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FloatingActionButton(context, attributeSet, defStyleAttr) {

    private var doneToAdd: AnimatedVectorDrawableCompat? = null

    init {
        doneToAdd = AnimatedVectorDrawableCompat.create(context, R.drawable.avd_done_to_add)
        setImageDrawable(doneToAdd)
    }

    fun showAdd() {
        setImageDrawable(doneToAdd)
        doneToAdd?.start()
    }
}
