package com.example.redma.ballrolling

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.os.postDelayed

class MainActivity : AppCompatActivity() , SensorEventListener{

    private lateinit var sensorManager: SensorManager
    private lateinit var canvasView: CanvasView
    private var sensorX : Float = 0f
    private var sensorY : Float = 0f
    private var sensorZ : Float = 0f

    private var period = 100L
    private val handler : Handler = Handler()
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerSet()
    }

    override fun onResume() {
        super.onResume()
        var accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this,accel, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()

        //リスナーを解除
        sensorManager.unregisterListener(this)
        stopTimerTask()
    }



    override fun onSensorChanged(event: SensorEvent?) {

        if (event!!.sensor.type == Sensor.TYPE_ACCELEROMETER){
            sensorX = event.values[0]
            sensorY = event.values[1]
            sensorZ = event.values[2]
        }
    }

    private fun timerSet() {
        runnable = Runnable {
            kotlin.run{
                canvasView.setPosition(sensorX, sensorY)
                handler.postDelayed(runnable, period)
            }
        }
        handler.post(runnable)
    }

    private fun stopTimerTask() {
        handler.removeCallbacks(runnable)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }


}
