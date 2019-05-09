package com.test.view.jushi

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class CircleView : View {
    private val paint = Paint()
    private var rects: List<Rect>? = null


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        paint.strokeWidth = 5f
        paint.color = Color.BLUE
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (rects!!.isEmpty()) return
        for (rect in rects!!) {
//            canvas.drawRect(rect, paint)
            val point = getRectMidPoint(rect)
//            canvas.drawCircle(point.x.toFloat(), point.y.toFloat(), getTwoPointLine(rect) / 2, paint)
            val path = Path()
            path.addCircle(point.x.toFloat(), point.y.toFloat(), getTwoPointLine(rect) / 2, Path.Direction.CCW)
            paint.pathEffect = DashPathEffect(getFloat(getCircleLength(getTwoPointLine(rect) / 2).toFloat()), 0f)
            canvas.drawPath(path, paint)

        }
    }

    fun drawCircle(rects: List<Rect>) {
        this.rects = rects
        invalidate()
    }

    /**
     * 获取矩形的中点坐标
     */
    private fun getRectMidPoint(rect: Rect): Point {
        val x = (rect.left + rect.right) / 2
        val y = (rect.top + rect.bottom) / 2
        val point = Point()
        point.set(x, y)
        return point
    }

    /**
     * 获取对焦线的长度的一半
     */
    private fun getTwoPointLine(rect: Rect): Float {
        //两点的对角线的2次方
        val line = (rect.width() * rect.width()) + (rect.height() * rect.height())
        return Math.sqrt(line.toDouble()).toFloat()
    }

    /**
     * 根据半径获取圆的周长
     */
    private fun getCircleLength(r: Float): Double {
        return 2 * Math.PI * Math.PI * r
    }

    /**
     * 根据圆的周长获取需要绘制的虚线
     */
    private fun getFloat(length: Float): FloatArray {
        return floatArrayOf(length / 20, 25f)
    }

}