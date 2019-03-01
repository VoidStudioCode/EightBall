package com.i605.eightball.helper

import android.media.SoundPool
import com.i605.eightball.*

object SoundHelper {
    private val soundPool by lazy { SoundPool.Builder().setMaxStreams(1).build() }
    private var idLong = 0
    private var idShort = 0

    init {
        idLong = soundPool.load(App.getAppContext(), R.raw.l, 1)
        idShort = soundPool.load(App.getAppContext(), R.raw.s, 1)
    }

    fun playLong() {
        soundPool.pause(idLong)
        soundPool.play(idLong, 0.5F, 0.5F, 1, -1, 1F)
    }

    fun playShort() {
        soundPool.pause(idLong)
        soundPool.play(idShort, 0.5F, 0.5F, 1, 0, 1F)
    }
}