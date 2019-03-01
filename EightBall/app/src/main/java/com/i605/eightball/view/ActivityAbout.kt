package com.i605.eightball.view

import android.support.v7.app.AppCompatActivity
import android.app.AlertDialog
import android.os.Bundle
import com.i605.eightball.presenter.PresenterAbout
import com.i605.eightball.R
import kotlinx.android.synthetic.main.activity_about.*

class ActivityAbout : AppCompatActivity() {
    private val presenter = PresenterAbout(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        presenter.init()
        btn_disclaimer.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(R.string.open_source_licenses)
                .setMessage(presenter.disclaimer())
                .setPositiveButton(android.R.string.ok, null)
                .show()
        }
    }

    fun setVersion(version: String) {
        tv_version.text = version
    }

    fun setUpdateHistory(updateHistory: String) {
        tv_update.text = updateHistory
    }
}