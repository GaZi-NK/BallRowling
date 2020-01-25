package com.example.redma.ballrolling

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.activity_main.view.*

//カスタムビュー
class CanvasView(context: Context, attbs: AttributeSet) : View(context, attbs) {
    var score = 0 //スコア
    private var paint: Paint = Paint()
    private var bmp: Bitmap? = null

    private var radias : Float = 50f
    private var xpos: Float = 0f //ボールの現在のx位置
    private var ypos: Float = 0f //ボールの現在のy位置
    private var preX: Float = 0f
    private var preY: Float = 0f

    var meteo = arrayOfNulls<Ball>(5) //隕石の数分のインスタンスを生成するよう

    //隕石の位置をリセット
    private var rakka2 = 0f

    //画面縦、横の真ん中の座標を取得
    val xc = (canvas.width / 2).toFloat()
    val yc = (canvas.height / 2).toFloat()

    var player = Ball() //ボールのインスタンスを生成

    //コンストラクタ
    init {
        //ボールの画像を取得
        bmp = BitmapFactory.decodeResource(resources, R.drawable.ball)
        generateMetoro()
    }

    //隕石のプロパティを決める
    fun generateMetoro() {
        for (i in meteo.indices) {
            meteo[i] = Ball()
            meteo[i]?.x = (canvas.width / 6 * i) + (canvas.width / 6 / 2).toFloat()
            meteo[i]?.y = (Math.random() * (0 + canvas.height) - canvas.height).toFloat()
            meteo[i]?.rabius = (Math.random() * 50).toFloat()
            meteo[i]?.sp = (Math.random() * (15 - 1) + 1).toFloat()
        }
    }

    override fun onDraw(canvas: Canvas) {
        var scoreText = "スコア：${score}" //スコアを表示するテキスト
        val strokeW1 = 20f
        val xc = (canvas.width / 2).toFloat()
        val yc = (canvas.height / 2).toFloat()

        //スコアの表示
        paint.color = Color.BLACK
        paint.textSize = 20f
        canvas.drawText(scoreText,50f,50f,paint)

        //円の書式設定
        paint.color = Color.GREEN
        paint.strokeWidth = strokeW1
        paint.isAntiAlias = true

        //ボールが画面外に出ないように
         if(xc + xpos- radias < 0 && preX < 0){  // Xが左端に出ないように＋壁にぶつかったら減速させる処理
                preX = -preX / 1.5f //加速度をマイナス方向に変換⇒ボールが壁に当たって反対へ行く
         }else if(xc + xpos + radias > canvas.width && preX > 0){
                preX = -preX / 1.5f
         }

        if (yc - ypos - radias < 0 && preY < 0){
            preY = -preY / 1.5f
        }else if (yc - ypos + radias > canvas.height && preY > 0){
            preY = -preY /1.5f
        }
        // 円を描画⇒(中心x1座標, 中心y1座標, r半径, point)⇒書式設定みたいなもの
        canvas.drawCircle(xc + xpos, yc + ypos, radias, paint)

        //隕石の描画
        paint.color = Color.BLUE
        for (i in meteo.indices) {
            //画面内の描画
            if (meteo[i]!!.y < canvas.height.toFloat()) {
                meteo[i]!!.y += meteo[i]!!.sp
                canvas.drawCircle(canvas.width.toFloat() / 6 * (i + 1) , meteo[i]!!.y , meteo[i]!!.rabius, paint)
                //画面外に行ったときにまた上から落ちてくるように処理
            } else {
                score += 1
                meteo[i]!!.y = 0f
                canvas.drawCircle(canvas.width.toFloat() / 6 * (i + 1) , rakka2, meteo[i]!!.rabius, paint)
            }
        }
    }

    //ボールの位置を取得
    public fun setPositionBall(xp: Float, yp: Float) {
        var dT: Float = 0.8f
        //感度みたいなのもの⇒数字の部分を大きくすれば少し傾けただけで大きく動くようになる
        val ax: Float = -xp * 2
        val ay: Float = yp * 2

        //ボールの位置を更新
        xpos += preX * dT + ax * dT * dT
        ypos += preY * dT + ay * dT * dT

        //現在のボールの加速度を更新
        preX += ax * dT
        preY += ay * dT

            //再描画
            invalidate()
    }

    public fun hitChecked() {
        //ボールの中心を取得
        var ballCenterX = canvas.getWidth() / 2 + xpos
        var ballCenterY = canvas.getHeight() / 2 + ypos
    }
}
