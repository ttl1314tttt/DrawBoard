package com.example.drawboard

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class Draw:View {
    var mColor = Color.BLACK
         set(value) {
             field = value
             mPaint.color = value
         }
    var mStrokeWidth = this.context.dp2px(5)
        set(value){
            field = value
            mPaint.strokeWidth = value
        }
    var mPath:Path? = Path()
    var mPaint = Paint().apply {
        color = mColor
        strokeWidth = mStrokeWidth
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }
    var pathModelList = arrayListOf<PathModel>()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas?) {
        pathModelList.forEach { model->
            canvas?.drawPath(model.path,model.paint)
        }
        mPath?.let {
            canvas?.drawPath(mPath!!,mPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN->{
                //创建新的path
                mPath = Path()
                //确定落点
                mPath!!.moveTo(event.x,event.y)
            }
            MotionEvent.ACTION_MOVE->{
                //从path的最后一个点到当前点画一条线
                mPath!!.lineTo(event.x,event.y)
                //刷新
                invalidate()
            }
            MotionEvent.ACTION_UP->{
                var paint = Paint().apply {
                    color = mColor
                    strokeWidth = mStrokeWidth
                    style = Paint.Style.STROKE
                    strokeCap = Paint.Cap.ROUND
                    strokeJoin = Paint.Join.ROUND
                }
                //保存当前路径
                pathModelList.add(PathModel(paint,mPath!!))


                mPath = null
            }
        }
        return true
    }

    fun undo(){
        if (pathModelList.size > 0){
            pathModelList.removeLast()
            invalidate()
        }
    }
}


















