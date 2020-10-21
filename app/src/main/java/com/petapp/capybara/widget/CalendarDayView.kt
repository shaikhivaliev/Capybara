package com.petapp.capybara.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.petapp.capybara.R

class CalendarDayView(
    context: Context,
    attributeSet: AttributeSet
) : View(context, attributeSet) {

    private val mainBounds: RectF = RectF()

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textBounds: Rect = Rect()
    private var textWidth: Float = 0.0F

    private val indicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var dayName: String = ""
    private val sectors: ArrayList<Sector> = arrayListOf()

    init {
        val typedArray: TypedArray = context.obtainStyledAttributes(attributeSet, R.styleable.CalendarDayView)
        val viewSize =
            typedArray.getDimensionPixelSize(R.styleable.CalendarDayView_viewSize, DEFAULT_VIEW_SIZE).toFloat()
        typedArray.recycle()

        with(textPaint) {
            color = Color.BLACK
            textSize = viewSize
            textAlign = Paint.Align.CENTER
        }

        with(indicatorPaint) {
            style = Paint.Style.STROKE
            strokeWidth = INDICATOR_STROKE
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textWidth = textPaint.measureText("100.0 %")
        textPaint.getTextBounds("1", 0, 1, textBounds)
        val diameter = (textWidth * REDUCE_VIEW_SIZE).toInt()
        val measureWidth = resolveSize(diameter, widthMeasureSpec)
        val measureHeight = resolveSize(diameter, heightMeasureSpec)
        mainBounds.set(
            +MAIN_BOUNDS_SIZE,
            +MAIN_BOUNDS_SIZE,
            measureWidth - MAIN_BOUNDS_SIZE,
            measureHeight - MAIN_BOUNDS_SIZE
        )
        setMeasuredDimension(measureWidth, measureHeight)
    }

    override fun onDraw(canvas: Canvas) {
        val cx = width / 2
        val cy = height / 2

        canvas.drawText(dayName, cx.toFloat(), cy + textBounds.height() / 2.toFloat(), textPaint)

        val indicatorsCount = sectors.size

        if (indicatorsCount != 0) {
            var startAngle = (WHOLE_CIRCLE / indicatorsCount).toFloat()
            for (i in 0 until indicatorsCount) {
                with(indicatorPaint) {
                    color = ContextCompat.getColor(context, sectors[i].color)
                }
                startAngle = sectors[i].draw(canvas, mainBounds, startAngle, indicatorPaint)
            }
        }
    }

    fun createIndicators(colors: List<Int>) {
        for (color in colors) {
            sectors.add(Sector(colors.size, color))
        }
    }

    fun setDayName(day: String) {
        dayName = day
    }

    inner class Sector(private val indicatorsCount: Int, val color: Int) {

        fun draw(canvas: Canvas, bounds: RectF, startAngle: Float, paint: Paint): Float {
            val sweepAngle: Float = (WHOLE_CIRCLE / indicatorsCount).toFloat()
            val endAngle = startAngle + sweepAngle
            canvas.drawArc(bounds, startAngle, sweepAngle, false, paint)
            return endAngle
        }
    }

    companion object {
        private const val INDICATOR_STROKE = 6F
        private const val WHOLE_CIRCLE = 360
        private const val DEFAULT_VIEW_SIZE = 24
        private const val REDUCE_VIEW_SIZE = 0.9
        private const val MAIN_BOUNDS_SIZE = 10F
    }
}
