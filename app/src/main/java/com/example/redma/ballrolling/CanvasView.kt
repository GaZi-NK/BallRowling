package com.example.redma.ballrolling

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import java.util.jar.Attributes

//カスタムビュー
class CanvasView(context : Context, attbs : AttributeSet) : View(context, attbs) {
    private lateinit var paint : Paint
    private var bmp : Bitmap? = null
    private var xpos : Float = 0f
    private var ypos : Float = 0f
    private var preX : Float = 0f
    private var preY : Float = 0f

    //コンストラクタ
    init {
        //ポイントのインスタンスを取得
        paint = Paint()

        //ボールの画像を取得
        bmp = BitmapFactory.decodeResource(resources, R.drawable.ball)
    }

    override fun onDraw(canvas: Canvas?) {
        //背景色
        canvas?.drawColor(Color.argb(125, 0, 0, 255))
        //透明
        canvas?.drawBitmap(bmp!!, canvas?.getWidth()/2+xpos, canvas?.getHeight()/2+ypos,paint)
    }

    public fun setPosition(xp : Float, yp : Float){
        var dT : Float = 0.8f

        val ax : Float = -xp*2
        val ay : Float = yp*2

        xpos += preX*dT + ax*dT*dT
        preX += ax*dT

        ypos += preY*dT + ay*dT*dT
        preY += ay*dT

        //再描画
        invalidate()
    }

}