package com.example.stockapp.features.home.presentation

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.stockapp.R
import kotlin.properties.Delegates

class SearchCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private var transferInfo: CustomViewTransferInfo? = null
    fun setTranfer(activity: MainActivity) {
        transferInfo = activity
    }

    private var gravity = Gravity.LEFT or Gravity.TOP
    private val widthList = ArrayList<Int>()

    private var widthSize by Delegates.notNull<Int>()
    private var heightSize by Delegates.notNull<Int>()
    private var widthMode by Delegates.notNull<Int>()
    private var heightMode by Delegates.notNull<Int>()

    private var childWidthType = View.MeasureSpec.EXACTLY
    private var childWidth: Int = 0
    private var childHeightType = View.MeasureSpec.UNSPECIFIED
    private var childHeight: Int = 0

    private var height: Int = 0
    private var lineWidth: Int = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        getSize(widthMeasureSpec, heightMeasureSpec)
        getMode(widthMeasureSpec, heightMeasureSpec)

        var width = 0
        var height = 10
        var lineWidth = 0

        for (index in 0 until childCount) {
            val currentView = getChildAt(index)
            if (currentView.visibility == View.GONE) {
                continue
            }

            val lp = currentView.layoutParams as LayoutParams
            childWidthType = View.MeasureSpec.EXACTLY
            childWidth = lp.width
            onMeasureWidthOfChild(widthSize, lp)

            childHeightType = View.MeasureSpec.UNSPECIFIED
            childHeight = 0
            onMeasureHeightOfChild(heightSize, lp)

            currentView.measure(
                View.MeasureSpec.makeMeasureSpec(childWidth, childWidthType),
                View.MeasureSpec.makeMeasureSpec(childHeight, childHeightType)
            )

            val childWidthReal = currentView.measuredWidth + lp.leftMargin + lp.rightMargin
            val childHeightReal = currentView.measuredHeight + lp.topMargin + lp.bottomMargin

            // переходим на следующую строку,если общее количество childs не убирается
            if (lineWidth + childWidthReal > widthSize) {
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

    private fun getSize(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        widthSize = MeasureSpec.getSize(widthMeasureSpec)
        heightSize = MeasureSpec.getSize(heightMeasureSpec)
    }

    private fun getMode(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        widthMode = MeasureSpec.getMode(widthMeasureSpec)
        heightMode = MeasureSpec.getMode(heightMeasureSpec)
    }

    private fun onMeasureWidthOfChild(widthSize: Int, lp: LayoutParams) {
        if (lp.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            childWidthType = View.MeasureSpec.EXACTLY
            childWidth = widthSize - paddingLeft - paddingRight + lp.leftMargin + lp.rightMargin
        } else if (lp.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            childWidthType = View.MeasureSpec.AT_MOST
            childWidth = widthSize - paddingLeft - paddingRight + lp.leftMargin + lp.rightMargin
        }
    }

    private fun onMeasureHeightOfChild(heightSize: Int, lp: LayoutParams) {
        if (lp.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            childHeightType = View.MeasureSpec.AT_MOST
            childHeight =
                heightSize - paddingTop - paddingBottom + lp.topMargin + lp.bottomMargin
        } else if (lp.height >= 0) {
            childHeightType = View.MeasureSpec.EXACTLY
            childHeight = lp.height
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        /*
          * 1) Надо пробежаться по всем детям, посчитать максимальную ширину линии и максимальную высоту.
          * 2) Исходя из настроек гравити, расставить элементы.
          */

        val childCount = childCount
        widthList.clear()
        val widthSize = measuredWidth

        for (index in 0 until childCount) {

            val currentView = getChildAt(index)
            if (currentView.visibility == View.GONE) {
                continue
            }
            val lp = currentView.layoutParams as LayoutParams
            setChildSize(lp, currentView)
            measureLineOfChildren()
        }
        widthList.add(lineWidth)

        val verticalGravityMargin = setVerticalGravityMargin()

        val globalWidth = measuredWidth
        lineWidth = 0
        var top = verticalGravityMargin
        var left = 0
        var currentLineWidth: Int
        var lineCnt = 0
        var resultTop: Int

        for (i in 0 until childCount) {
            val currentChild = getChildAt(i)
            if (currentChild.visibility == View.GONE) {
                continue
            }
            val lp = currentChild.layoutParams as LayoutParams
            val childWidth = currentChild.measuredWidth + lp.leftMargin + lp.rightMargin
            val childHeight = currentChild.measuredHeight + lp.topMargin + lp.bottomMargin

            var currentLeft = left

            //переход на новую строку
            if (i == 0 || lineWidth + childWidth > widthSize) {
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
                currentLeft = left
                left += childWidth
                top += childHeight
            } else {
                left += childWidth
                lineWidth += childWidth
            }
            resultTop = top - childHeight
            currentChild.layout(
                currentLeft + lp.leftMargin,
                resultTop + lp.topMargin,
                currentLeft + childWidth - lp.rightMargin,
                resultTop + childHeight - lp.bottomMargin
            )
        }
    }

    private fun setChildSize(lp: LayoutParams, currentView: View) {
        childWidth = currentView.measuredWidth + lp.leftMargin + lp.rightMargin
        childHeight = currentView.measuredHeight + lp.topMargin + lp.bottomMargin
    }

    private fun measureLineOfChildren() {
        // переход на новую линию
        if (lineWidth + childWidth > widthSize) {
            widthList.add(lineWidth)
            lineWidth = childWidth
            height += childHeight
        } else {
            lineWidth += childWidth
            height = Math.max(height, childHeight)
        }
    }

    private fun setVerticalGravityMargin(): Int {
        var verticalGravityMargin: Int = 0
        when (gravity and Gravity.VERTICAL_GRAVITY_MASK) {
            Gravity.BOTTOM -> verticalGravityMargin = getHeight() - height
            Gravity.CENTER_VERTICAL -> verticalGravityMargin = (getHeight() - height) / 2
            Gravity.TOP -> {}
        }
        return verticalGravityMargin
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
            for (index in 0 until childCount) {
                val childView = getChildAt(index)
                val childRect = Rect()
                childView.getHitRect(childRect)
                if (childRect.contains(touchX, touchY)) {
                    childIndex = index
                    break
                }
            }

            if (childIndex != -1) {
                val childView = getChildAt(childIndex)
                val textView = childView.findViewById<TextView>(R.id.searchStockItem)
                val textIn = textView.text.toString()
                transferInfo?.transferInfo(textIn)

            }
        }
        return super.onTouchEvent(event)
    }

}
