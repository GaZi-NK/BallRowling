package com.example.redma.ballrolling

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.activity_main.view.*

//カスタムビュー
class CanvasView(context: Context, attbs: AttributeSet) : View(context, attbs) {
    private var paint: Paint = Paint()
    private var bmp: Bitmap? = null
    private var xpos: Float = 0f
    private var ypos: Float = 0f
    private var preX: Float = 0f
    private var preY: Float = 0f

    var meteo = arrayListOf<Ball>() //隕石の数分のインスタンスを生成するよう

    //隕石が下に落ちる速度
    private var rakka2 = 5f

    //画面縦、横の真ん中の座標を取得
    val xc = (canvas.width / 2).toFloat()
    val yc = (canvas.height / 2).toFloat()

    var paryer = Ball() //ボールのインスタンスを生成

    //コンストラクタ
    init {
        //ボールの画像を取得
        bmp = BitmapFactory.decodeResource(resources, R.drawable.ball)
    }

    //隕石のプロパティを決める
    fun generateMetoro() {
        for (i in 0..4) {
            meteo[i] = Ball()
            meteo[i].x = (canvas.width / 6 * i) + (canvas.width / 6 / 2).toFloat()
            meteo[i].y = (Math.random() * (0 + canvas.height) - canvas.height).toFloat()
            meteo[i].rabius = (Math.random() * (canvas.width / 6 / 2 - 10) + 10).toFloat()
            meteo[i].sp = (Math.random() * 10).toFloat()
        }
    }

    override fun onDraw(canvas: Canvas) {
        val strokeW1 = 20f

        //円の書式設定
        paint.color = Color.argb(255, 125, 125, 255)
        paint.strokeWidth = strokeW1.toFloat()
        paint.isAntiAlias = true
        // 円を描画⇒(中心x1座標, 中心y1座標, r半径, point)⇒書式設定みたいなもの
        canvas.drawCircle(
            paryer.x + xpos,
            paryer.y + ypos,
            xc / 4,
            paint
        )
        //隕石の描画
        for (i in 0..4) {
            if (rakka2 < canvas.getHeight().toFloat() + xc / 4) {
                meteo[i].y += meteo[i].sp
            } else {
                rakka2 = 0f
                canvas.drawCircle(meteo[i].x, rakka2, meteo[i].rabius, paint)
            }
        }
    }

    //ボールの位置を取得
    public fun setPositionBall(xp: Float, yp: Float): Boolean {
            var dT: Float = 0.8f
            //感度みたいなのもの⇒数字の部分を大きくすれば少し傾けただけで大きく動くようになる
            val ax: Float = -xp * 2
            val ay: Float = yp * 2
            xpos += preX * dT + ax * dT * dT
            preX += ax * dT
            ypos += preY * dT + ay * dT * dT
            preY += ay * dT

            //再描画
            invalidate()
            return false
    }

    public fun hitChecked() {
        //ボールの中心を取得
        var ballCenterX = canvas.getWidth() / 2 + xpos
        var ballCenterY = canvas.getHeight() / 2 + ypos
    }
}

class Ball {
    var x: Float = 0f //x座標の初期値
    var y: Float = 0f //y座標の初期値
    var rabius: Float = 0f //半径の初期値
    var sp: Float = 0f //速さの係数
}