package com.petapp.capybara.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toRectF
import com.petapp.capybara.R
import com.petapp.capybara.extensions.dpTpPx

class ProfileCustomView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attributeSet, defStyleAttr) {

    companion object {
        private const val DEFAULT_SIZE_DP = 56
        private const val DEFAULT_BORDER_WIDTH_DP = 6
        private const val TAG = "custom_view"
    }

    private val avatarPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val initialsPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    // главная область рисования
    private val viewRect = Rect()
    // для рисования каемки
    private var borderRect = Rect()
    // рисуем либо инициалы, либо изображение (isAvatarMode = true)
    private var isAvatarMode = true
    private var initials = ""

    init {
        scaleType = ScaleType.CENTER_CROP
        setup()
    }

    private fun setup() {
        with(borderPaint) {
            strokeWidth = context.dpTpPx(DEFAULT_BORDER_WIDTH_DP)
            color = Color.WHITE
            style = Paint.Style.STROKE
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.UNSPECIFIED -> {
                val defaultSize = context.dpTpPx(DEFAULT_SIZE_DP).toInt()
                setMeasuredDimension(defaultSize, defaultSize)
                Log.d(TAG, "onMeasure UNSPECIFIED")
            }
            MeasureSpec.AT_MOST -> {
                setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
                Log.d(TAG, "onMeasure AT_MOST")
            }
            MeasureSpec.EXACTLY -> {
                setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
                Log.d(TAG, "onMeasure EXACTLY")
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.d(TAG, "onSizeChanged")
        if (w == 0) return
        with(viewRect) {
            left = 0
            top = 0
            right = w
            bottom = h
        }
        borderRect = viewRect.apply {
            val half = (context.dpTpPx(DEFAULT_BORDER_WIDTH_DP) / 2).toInt()
            this.inset(half, half)
        }
        prepareShader(w, h)
    }

    override fun onDraw(canvas: Canvas) {
        if (drawable != null && isAvatarMode) {
            drawAvatar(canvas)
        } else {
            drawInitials(canvas)
        }
        canvas.drawOval(borderRect.toRectF(), borderPaint)
        Log.d(TAG, "onDraw")
        super.dispatchDraw(canvas)
    }

    private fun prepareShader(width: Int, height: Int) {
        if (width == 0 || drawable == null) return
        val srcBitmap = drawable.toBitmap(width, height, Bitmap.Config.ARGB_8888)
        avatarPaint.shader = BitmapShader(srcBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    }

    private fun drawAvatar(canvas: Canvas) {
        canvas.drawOval(viewRect.toRectF(), avatarPaint)
    }

    private fun drawInitials(canvas: Canvas) {
        initialsPaint.color = ContextCompat.getColor(context, R.color.light_gray)
        canvas.drawOval(viewRect.toRectF(), initialsPaint)
        with(initialsPaint) {
            color = ContextCompat.getColor(context, R.color.light_black)
            textAlign = Paint.Align.CENTER
            textSize = height * 0.33F
        }
        val offsetY = (initialsPaint.descent() + initialsPaint.ascent()) / 2
        canvas.drawText(initials, viewRect.exactCenterX(), viewRect.exactCenterY() - offsetY, initialsPaint)
        Log.d(TAG, "drawInitials")
    }

    fun setColor(color: Int) {
        borderPaint.color = ContextCompat.getColor(context, color)
        invalidate()
    }

    fun setInitials(name: String) {
        initials = name.first().toString()
        invalidate()
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        if (isAvatarMode) prepareShader(width, height)
        Log.d(TAG, "setImageBitmap")
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        if (isAvatarMode) prepareShader(width, height)
        Log.d(TAG, "setImageDrawable")
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        if (isAvatarMode) prepareShader(width, height)
        Log.d(TAG, "setImageResource")
    }
}