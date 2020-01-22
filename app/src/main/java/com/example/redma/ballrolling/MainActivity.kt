package com.example.redma.ballrolling

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() , SensorEventListener{

    private lateinit var sensorManager: SensorManager
    private var sensorX : Float = 0f
    private var sensorY : Float = 0f
    private var sensorZ : Float = 0f

    private var period = 100L
    val handler : Handler = Handler()
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //センサーマネージャーのインスタンスを取得
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        setContentView(R.layout.activity_main)

        timerSet()
    }

    override fun onResume() {
        super.onResume()
        //加速度センサーを取得してからリスナーを登録
        var accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()

        //リスナーを解除
        sensorManager.unregisterListener(this)

        stopTimerTask()
    }

    //加速度センサーの値が変わった時の処理
    override fun onSensorChanged(event: SensorEvent) {
        //各値を取得⇒0はX、1はY、2はZ
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER){
            sensorX = event.values[0]
            sensorY = event.values[1]
            sensorZ = event.values[2]
        }
    }

    private fun timerSet() {

        //別スレッドで行う処理
        runnable = Runnable() {
            run(){
                //ボールの位置が変わるたびに再描画する
                if (canvas.setPositionBall(sensorX, sensorY)){
                    val intent = Intent(this, GameOver::class.java)
                    startActivity(intent)
                }
                                //runメソッドを繰り返し行う処理⇒変数runnnableを0.1秒ごとに行う
                handler.postDelayed(runnable, 100)
            }
        }
        //最初の一回目はこのメソッドで実行宣言
        handler.post(runnable)
    }

    private fun stopTimerTask() {
        handler.removeCallbacks(runnable)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }


}
