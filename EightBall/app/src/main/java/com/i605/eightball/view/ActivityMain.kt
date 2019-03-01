package com.i605.eightball.view

import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import com.i605.eightball.presenter.PresenterMain
import com.i605.eightball.model.Manager
import com.i605.eightball.R
import kotlinx.android.synthetic.main.activity_main.*

class ActivityMain : AppCompatActivity() {
    private val presenter = PresenterMain(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    override fun onResume() {
        super.onResume()
        presenter.registerListener()
    }

    override fun onPause() {
        super.onPause()
        presenter.unregisterListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == AppCompatActivity.RESULT_OK) {
            sp.setSelection(0)
        }
    }

    private fun initView() {
        val adapter = ArrayAdapter(this, R.layout.widget_sp_item, Manager.list.map { item -> item.category })
        sp.adapter = adapter
        sp.setSelection(presenter.getSpPosition())
        sp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>) {}

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                presenter.setSpPosition(position)
                presenter.updateInfo(sp.selectedItem as String)
            }
        }
        btn_about.setOnClickListener { startActivity(Intent(this, ActivityAbout::class.java)) }
        btn_settings.setOnClickListener { startActivityForResult(Intent(this, ActivitySettings::class.java), 0) }
    }

    fun setTvText(msg: String) {
        tv_msg.text = msg
    }

    fun getSpText(): String = sp.selectedItem as String
}