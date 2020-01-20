package com.example.redma.ballrolling

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.activity_main.view.*
import java.util.jar.Attributes
import android.graphics.Canvas
import androidx.annotation.IntegerRes

//カスタムビュー
class CanvasView(context : Context, attbs : AttributeSet) : View(context, attbs) {
    private var paint: Paint = Paint()
    private var bmp : Bitmap? = null
    private var xpos : Float = 0f
    private var ypos : Float = 0f
    private var preX : Float = 0f
    private var preY : Float = 0f
    // 描画するラインの太さ
    private val lineStrokeWidth = 20f
    private val strokeW1 = 20f
    private val strokeW2 = 40f

    //隕石が下に落ちる速度
    private var rakka = 3.0f

    //コンストラクタ
    init {
        //ボールの画像を取得
        bmp = BitmapFactory.decodeResource(resources, R.drawable.ball)
    }

    override fun onDraw(canvas: Canvas) {
        //画面縦、横の真ん中の座標を取得
        val xc = (canvas.width / 2).toFloat()
        val yc = (canvas.height / 2).toFloat()

        //ボールを画面に表示させる処理⇒getWidth/2は画面横の真ん中の値
        //canvas.drawBitmap(bmp!!, xc +xpos, yc+ypos,paint)


        //円の書式設定
        paint.color = Color.argb(255, 125, 125, 255)
        paint.strokeWidth = strokeW1.toFloat()
        paint.isAntiAlias = true
        // 円を投入⇒(中心x1座標, 中心y1座標, r半径, point)⇒書式設定みたいなもの
        canvas.drawCircle(canvas.getWidth()/2+xpos,  canvas.getHeight()/2+ypos, xc / 8, paint)



        //隕石の投入
        canvas.drawCircle(xc-xc/2, 0 + rakka, xc / 4, paint)
        setPositionMeteo()
    }

    //ボールの位置を再描画
    public fun setPositionBall(xp : Float, yp : Float){
        var dT : Float = 0.8f

        //感度みたいなのもの⇒数字の部分を大きくすれば少し傾けただけで大きく動くようになる
        val ax : Float = -xp*2
        val ay : Float = yp*2

        xpos += preX*dT + ax*dT*dT
        preX += ax*dT

        ypos += preY*dT + ay*dT*dT
        preY += ay*dT

        //再描画
        invalidate()
    }

    public fun setPositionMeteo() {
        rakka += (Math.floor(Math.random () * 10) + 3).toFloat()
        }
    }