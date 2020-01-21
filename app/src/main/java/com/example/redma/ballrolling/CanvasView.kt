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
    // 描画するラインの太さ
    private val lineStrokeWidth = 20f
    private val strokeW1 = 20f
    private val strokeW2 = 40f

    //隕石が下に落ちる速度
    private var rakka = 0f
    private var rakka2 = 5f

    //画面縦、横の真ん中の座標を取得
    val xc = (canvas.width / 2).toFloat()
    val yc = (canvas.height / 2).toFloat()


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

        // 円を描画⇒(中心x1座標, 中心y1座標, r半径, point)⇒書式設定みたいなもの
        canvas.drawCircle(
            xc + xpos,
            yc + ypos,
            xc / 4,
            paint
        )
        //隕石の描画
        if (rakka2 < canvas.getHeight().toFloat() + xc / 4) {
            canvas.drawCircle(xc - xc / 2, 0f + rakka2, xc / 4, paint)
        } else {
            rakka2 = 0f
            canvas.drawCircle(xc - xc / 2, 0f + rakka2, xc / 4, paint)
        }
    }

    //ボールの位置を取得
    public fun setPositionBall(xp: Float, yp: Float): Boolean {
        if (hitChecked()) {
            return true
        } else{
        var dT: Float = 0.8f
        //感度みたいなのもの⇒数字の部分を大きくすれば少し傾けただけで大きく動くようになる
        val ax: Float = -xp * 2
        val ay: Float = yp * 2
        xpos += preX * dT + ax * dT * dT
        preX += ax * dT
        ypos += preY * dT + ay * dT * dT
        preY += ay * dT

        //隕石の位置
        rakka += (Math.floor(Math.random() * 10) + 3).toFloat()
        rakka2 += 20f

        //再描画
        invalidate()
        return false
    }
}

    //隕石の次の位置を取得
    public fun setPositionMeteo() {
        //再描画
        invalidate()
    }

    public fun hitChecked():Boolean{
        //ボールの中心を取得
        var ballCenterX = canvas.getWidth() / 2 + xpos
        var ballCenterY = canvas.getHeight() / 2 +ypos

        //隕石の中心を取得
        var meteoCenterX = (canvas.getWidth() - canvas.getWidth() / 2).toFloat()
        var meteoCenterY =  0 + rakka

        var dx = ballCenterX - meteoCenterX //ボールと隕石の距離X
        var dy = ballCenterY - meteoCenterY //ボールと隕石の距離Y
        //if 衝突したときの処理
        if (xpos  > 500){
            return true
        }
            return false
    }
}