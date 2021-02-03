package com.petapp.capybara.presentation.types

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.toRect
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.RecyclerView
import com.petapp.capybara.R
import kotlin.math.min

class SwipeController(
    val context: Context,
    val onEditClick: (RecyclerView.ViewHolder) -> Unit
) : ItemTouchHelper.Callback() {

    private var swipeBack = false
    private var buttonShowedState = ButtonsState.GONE
    private var currentItemViewHolder: RecyclerView.ViewHolder? = null
    private var buttonInstance: RectF? = null

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return makeMovementFlags(0, LEFT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    @Suppress("EmptyFunctionBlock")
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (swipeBack) {
            swipeBack = buttonShowedState != ButtonsState.GONE
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ACTION_STATE_SWIPE) {
            if (buttonShowedState != ButtonsState.GONE) {
                if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
                    super.onChildDraw(
                        canvas,
                        recyclerView,
                        viewHolder,
                        min(dX, -BUTTON_WIDTH),
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            } else {
                setTouchListener(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
        if (buttonShowedState == ButtonsState.GONE) {
            super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
        currentItemViewHolder = viewHolder
    }

    @SuppressLint("ClickableViewAccessibility")
    @Suppress("LongParameterList")
    private fun setTouchListener(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { _, motionEvent ->
            swipeBack = motionEvent.action == MotionEvent.ACTION_CANCEL || motionEvent.action == MotionEvent.ACTION_UP

            if (swipeBack) {
                if (dX < -BUTTON_WIDTH) buttonShowedState = ButtonsState.RIGHT_VISIBLE

                if (buttonShowedState != ButtonsState.GONE) {
                    setTouchDownListener(canvas, recyclerView, viewHolder, dY, actionState, isCurrentlyActive)
                    setItemsClickable(recyclerView, false)
                }
            }

            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Suppress("LongParameterList")
    private fun setTouchDownListener(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                setTouchUpListener(canvas, recyclerView, viewHolder, dY, actionState, isCurrentlyActive)
            }
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Suppress("LongParameterList")
    private fun setTouchUpListener(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                super@SwipeController.onChildDraw(
                    canvas,
                    recyclerView,
                    viewHolder,
                    0f,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                recyclerView.setOnTouchListener { _, _ -> false }
                setItemsClickable(recyclerView, true)
                swipeBack = false
                buttonShowedState = ButtonsState.GONE
            }

            if (buttonShowedState == ButtonsState.RIGHT_VISIBLE &&
                buttonInstance != null &&
                buttonInstance!!.contains(motionEvent.x, motionEvent.y)
            ) {
                onEditClick(viewHolder)
            }

            buttonShowedState = ButtonsState.GONE
            currentItemViewHolder = null
            false
        }
    }

    private fun setItemsClickable(recyclerView: RecyclerView, isClickable: Boolean) {
        for (i in 0 until recyclerView.childCount) {
            recyclerView.getChildAt(i).isClickable = isClickable
        }
    }

    private fun drawEditButton(canvas: Canvas, viewHolder: RecyclerView.ViewHolder) {
        val buttonWidthWithoutPadding: Float = BUTTON_WIDTH - PADDING
        val corners = context.resources.getDimension(R.dimen.widget_corner_radius)
        val itemView: View = viewHolder.itemView
        val paint = Paint()
        paint.color = Color.TRANSPARENT
        val editButton = RectF(
            itemView.right - buttonWidthWithoutPadding,
            itemView.top.toFloat(),
            itemView.right.toFloat(),
            itemView.bottom.toFloat()
        )

        canvas.drawRoundRect(editButton, corners, corners, paint)
        drawBackground(canvas, editButton)
        drawEditIcon(canvas, editButton, paint)
        buttonInstance = editButton
    }

    private fun drawBackground(canvas: Canvas, button: RectF) {
        val background = ContextCompat.getDrawable(context, R.drawable.green_border_bgr_widget)
        background?.bounds = button.toRect()
        background?.draw(canvas)
    }

    private fun drawEditIcon(canvas: Canvas, button: RectF, paint: Paint) {
        paint.color = ContextCompat.getColor(context, R.color.colorAccent)
        val icon = ContextCompat.getDrawable(context, R.drawable.ic_edit)
        val iconRect = RectF(
            button.left + OFFSET_X,
            button.top + OFFSET_Y,
            button.right - OFFSET_X,
            button.bottom - OFFSET_Y
        )
        icon?.bounds = iconRect.toRect()
        icon?.draw(canvas)
    }

    fun onDraw(canvas: Canvas) {
        if (currentItemViewHolder != null) {
            drawEditButton(canvas, currentItemViewHolder!!)
        }
    }

    internal enum class ButtonsState {
        GONE, RIGHT_VISIBLE
    }

    companion object {
        private const val BUTTON_WIDTH = 200F
        private const val OFFSET_Y = 70
        private const val OFFSET_X = 50
        private const val PADDING = 20
    }
}
