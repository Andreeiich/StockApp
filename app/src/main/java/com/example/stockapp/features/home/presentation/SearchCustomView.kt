package com.example.stockapp.features.home.presentation

import android.content.Context
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stockapp.R

class SearchCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    var viewListener: CustomViewListener? = null

    private var gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
    private val widthList = ArrayList<Int>()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        var width = 0
        var height = 10

        var lineWidth = 0

        for (i in 0 until childCount) {

            val v = getChildAt(i)

            if (v.visibility == View.GONE) {
                continue
            }

            val lp = v.layoutParams as LayoutParams
            var childWidthType = View.MeasureSpec.EXACTLY
            var childWidth = lp.width

            if (lp.width == ViewGroup.LayoutParams.MATCH_PARENT) {
                childWidthType = View.MeasureSpec.EXACTLY
                childWidth = widthSize - paddingLeft - paddingRight + lp.leftMargin + lp.rightMargin
            } else if (lp.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
                childWidthType = View.MeasureSpec.AT_MOST
                childWidth = widthSize - paddingLeft - paddingRight + lp.leftMargin + lp.rightMargin
            }

            var childHeightType = View.MeasureSpec.UNSPECIFIED
            var childHeight = 0

            if (lp.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
                childHeightType = View.MeasureSpec.AT_MOST
                childHeight =
                    heightSize - paddingTop - paddingBottom + lp.topMargin + lp.bottomMargin
            } else if (lp.height >= 0) {
                childHeightType = View.MeasureSpec.EXACTLY
                childHeight = lp.height
            }

            v.measure(
                View.MeasureSpec.makeMeasureSpec(childWidth, childWidthType),
                View.MeasureSpec.makeMeasureSpec(childHeight, childHeightType)
            )

            val childWidthReal = v.measuredWidth + lp.leftMargin + lp.rightMargin
            val childHeightReal = v.measuredHeight + lp.topMargin + lp.bottomMargin

            if (lineWidth + childWidthReal > widthSize) { // переходим на следующую строку
                width = Math.max(lineWidth, childWidthReal)
                lineWidth = childWidthReal
                height += childHeightReal
            } else {
                lineWidth += childWidthReal
                height = Math.max(height, childHeightReal)
            }
        }

        width = Math.max(lineWidth, width)

        width = if (widthMode == View.MeasureSpec.EXACTLY) widthSize else width
        height = if (heightMode == View.MeasureSpec.EXACTLY) heightSize else height
        setMeasuredDimension(width, height)
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        /*
          * 1) Надо пробежаться по всем детям посчитать максимальную ширину линии и максиманую высоту
          * 2) Исходя из настроек гравити расставить элементы
          */

        var height = 0
        var lineWidth = 0
        val childCount = childCount
        widthList.clear()
        val widthSize = measuredWidth

        for (i in 0 until childCount) {

            val v = getChildAt(i)
            if (v.visibility == View.GONE) {
                continue
            }
            val lp = v.layoutParams as LayoutParams
            val childWidth = v.measuredWidth + lp.leftMargin + lp.rightMargin
            val childHeight = v.measuredHeight + lp.topMargin + lp.bottomMargin

            if (lineWidth + childWidth > widthSize) {// переход на новую линию
                widthList.add(lineWidth)
                lineWidth = childWidth
                height += childHeight
            } else {
                lineWidth += childWidth
                height = Math.max(height, childHeight)
            }
        }
        widthList.add(lineWidth)

        var verticalGravityMargin = 0
        when (gravity and Gravity.VERTICAL_GRAVITY_MASK) {
            Gravity.BOTTOM -> verticalGravityMargin = getHeight() - height
            Gravity.CENTER_VERTICAL -> verticalGravityMargin = (getHeight() - height) / 2
            Gravity.TOP -> {}
        }

        val globalWidth = measuredWidth
        lineWidth = 0
        var top = verticalGravityMargin
        var left = 0
        var currentLineWidth: Int
        var lineCnt = 0
        var t: Int

        for (i in 0 until childCount) {
            val v = getChildAt(i)
            if (v.visibility == View.GONE) {
                continue
            }
            val lp = v.layoutParams as LayoutParams
            val childWidth = v.measuredWidth + lp.leftMargin + lp.rightMargin
            val childHeight = v.measuredHeight + lp.topMargin + lp.bottomMargin

            var l = left

            if (i == 0 || lineWidth + childWidth > widthSize) {//переход на новую строку
                lineWidth = childWidth
                currentLineWidth = widthList[lineCnt]
                ++lineCnt
                var lineHorizontalGravityMargin = 0

                when (gravity and Gravity.HORIZONTAL_GRAVITY_MASK) {
                    Gravity.CENTER_HORIZONTAL -> lineHorizontalGravityMargin =
                        (globalWidth - currentLineWidth) / 2

                    Gravity.RIGHT -> lineHorizontalGravityMargin = globalWidth - currentLineWidth
                    Gravity.LEFT -> {

                    }
                }
                left = lineHorizontalGravityMargin
                l = left
                left += childWidth
                top += childHeight
            } else {
                left += childWidth
                lineWidth += childWidth
            }
            t = top - childHeight
            v.layout(
                l + lp.leftMargin,
                t + lp.topMargin,
                l + childWidth - lp.rightMargin,
                t + childHeight - lp.bottomMargin
            )
        }
    }

    fun setGravity(gravity: Int) {
        if (this.gravity != gravity) {
            this.gravity = gravity
            requestLayout()
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return SearchCustomView.LayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams): ViewGroup.LayoutParams {
        return LayoutParams(p)
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams): Boolean {
        return p is LayoutParams
    }

    class LayoutParams : ViewGroup.MarginLayoutParams {

        constructor(c: Context, attrs: AttributeSet) : super(c, attrs) {}

        constructor(source: ViewGroup.LayoutParams) : super(source) {}

        constructor(width: Int, height: Int) : super(width, height) {}
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event?.action == MotionEvent.ACTION_DOWN) {
            val touchX = event.x.toInt()
            val touchY = event.y.toInt()
            var childIndex = -1

            // Ищем индекс чайлда
            for (i in 0 until childCount) {
                val childView = getChildAt(i)
                val childRect = Rect()
                childView.getHitRect(childRect)
                if (childRect.contains(touchX, touchY)) {
                    childIndex = i
                    break
                }
            }

            if (childIndex != -1) {
                val childView = getChildAt(childIndex)
                val textView = childView.findViewById<TextView>(R.id.searchStockItem)
                val textIn = textView.text.toString()
                viewListener?.transferInfo(textIn)

            }
        }
        return super.onTouchEvent(event)
    }

}
