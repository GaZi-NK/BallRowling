package com.example.redma.ballrolling

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.activity_main.view.*

//カスタムビュー
class CanvasView(context: Context, attbs: AttributeSet) : View(context, attbs) {
    var score = 0 //スコア
    private var paint: Paint = Paint()
    private var bmp: Bitmap? = null

    private var radias : Float = 100f //ボールの半径
    private var xpos: Float = 0f //ボールの現在のx位置
    private var ypos: Float = 0f //ボールの現在のy位置
    private var preX: Float = 0f //現在の加速度
    private var preY: Float = 0f //現在の加速度

    var meteo = arrayOfNulls<Ball>(5) //隕石の数分のインスタンスを生成するよう

    //隕石の位置をリセット
    private var rakka2 = 0f

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
             xpos = radias - xc + 1f  //はみ出ないようにする。⇒ボール位置は画面中央+xposできまるのでxposを画面端から半径分でないように調整する
         }else if(xc + xpos + radias > canvas.width && preX > 0){
                preX = -preX / 1.5f
             xpos = xc - radias + 1f
         }

        if (yc + ypos - radias < 0 && preY < 0){
            preY = -preY / 1.5f
            ypos = radias - yc + 1f
        }else if (yc + ypos + radias > canvas.height - 0 && preY > 0){
            preY = -preY /1.5f
            ypos = yc - radias - 1f
        }

        // 円を描画⇒(中心x1座標, 中心y1座標, r半径, point)⇒書式設定みたいなもの
        canvas.drawCircle(xc + xpos, yc + ypos, radias, paint)

        //隕石の色
        paint.color = Color.BLUE
        //隕石の描画　画面内に隕石があるときはそのままで画面外にいったら上から降ってくるように処理
        for (i in meteo.indices) {
            //画面内の描画
            if (meteo[i]!!.y < canvas.height.toFloat()) { //隕石が画面内にある時の処理
                if (meteo[i]?.x == 0f && meteo[i]?.y == 0f) { //隕石の初期生成じに隕石の位置、半径、落ちる速度を決める
                    meteo[i]?.x = canvas.width.toFloat() / 6 * (i + 1) //隕石のxを設定
                    meteo[i]?.y = (Math.random() * 10 + 0).toFloat()   //隕石のyを設定
                    meteo[i]?.rabius = (Math.random() * 70).toFloat()  //隕石の半径を設定
                    meteo[i]?.sp = (Math.random() * (20 - 1) + 1).toFloat() //隕石の落下速度を設定
                }
                meteo[i]!!.y += meteo[i]!!.sp //現在の位置+隕石の落下速度
                canvas.drawCircle(meteo[i]!!.x , meteo[i]!!.y , meteo[i]!!.rabius, paint) //再描画
                //画面外に行ったときにまた上から落ちてくるように処理
            } else {        //隕石が画面外に行った時の処理
                meteo[i]?.x = 0f
                meteo[i]?.y = 0f
                score += 1  //隕石をよけれたと判定しスコア1アップ
                meteo[i]!!.y = 0f //隕石を画面上にセット
                canvas.drawCircle(canvas.width.toFloat() / 6 * (i + 1) , rakka2, meteo[i]!!.rabius, paint) //再描画
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

    //ボールの衝突判定
    public fun hitChecked(): Boolean {
        //ボールの中心を取得
        var ballCenterX = 600 + xpos
        var ballCenterY = 828 + ypos

        for (i in meteo.indices){
            if (ballCenterX - radias < meteo[i]!!.x + meteo[i]!!.rabius && ballCenterX + radias > meteo[i]!!.x - meteo[i]!!.rabius
                && ballCenterY - radias < meteo[i]!!.y + meteo[i]!!.rabius && ballCenterY + radias > meteo[i]!!.y - meteo[i]!!.rabius){
                return true
            }
        }
        return false
    }
}
