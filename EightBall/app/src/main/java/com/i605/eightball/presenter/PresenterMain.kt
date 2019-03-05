package com.i605.eightball.presenter

import android.preference.PreferenceManager
import android.content.Context
import android.os.Handler
import android.util.Log
import android.hardware.*
import kotlin.random.Random
import com.i605.eightball.helper.SoundHelper
import com.i605.eightball.view.ActivityMain
import com.i605.eightball.model.Manager
import com.i605.eightball.R

class PresenterMain(private val activity: ActivityMain) {
    private val manager by lazy { activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    private val accelerometer by lazy { manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }
    private val sensorListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

        override fun onSensorChanged(event: SensorEvent) {
            shake(event)
        }
    }
    private var isReadyToShake = true
    private var lastTime = 0L

    init {
        SoundHelper
    }

    fun registerListener() {
        manager.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun unregisterListener() {
        manager.unregisterListener(sensorListener)
    }

    fun getSpPosition(): Int {
        val sp = PreferenceManager.getDefaultSharedPreferences(activity)
        return sp.getInt("spPosition", 0)
    }

    fun setSpPosition(position: Int) {
        val sp = PreferenceManager.getDefaultSharedPreferences(activity)
        sp.edit().putInt("spPosition", position).apply()
    }

    fun updateInfo(cat: String) {
        if (cat == activity.getString(R.string.fortune_telling)) {
            activity.setTvText(activity.getString(R.string.think_and_give_it_a_shake))
        } else {
            activity.setTvText(activity.getString(R.string.give_it_a_shake))
        }
    }

    private fun shake(event: SensorEvent) {
        if (isReadyToShake) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val delta = Math.sqrt(Math.pow((x + y + z - SensorManager.GRAVITY_EARTH).toDouble(), 2.0)).toFloat()
            if (delta > 15) {
                Log.i("delta: ", delta.toString())
                if (lastTime == 0L) {
                    lastTime = System.currentTimeMillis()
                    handler.sendEmptyMessage(0)
                }
                if (System.currentTimeMillis() - lastTime > 10) {
                    handler.removeMessages(1)
                    handler.sendEmptyMessageDelayed(1, 500)
                    handler.removeMessages(2)
                    handler.sendEmptyMessageDelayed(2, 1000)
                }
            }
        }
    }

    private val handler = Handler {
        when (it.what) {
            0 -> {
                SoundHelper.playLong()
                activity.setTvText("")
            }
            1 -> SoundHelper.playShort()
            2 -> dice()
            3 -> isReadyToShake = true
        }
        true
    }

    private fun dice() {
        isReadyToShake = false
        lastTime = 0
        val random = Random(System.currentTimeMillis())
        val item = Manager.list.first { item -> item.category == activity.getSpText() }
        val name = item.list[random.nextInt(item.list.size)]
        activity.setTvText(name)
        handler.sendEmptyMessageDelayed(3, 1000)
    }
}