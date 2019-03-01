package com.i605.eightball.view

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.app.AppCompatActivity
import android.app.AlertDialog
import android.os.Bundle
import android.support.design.widget.*
import android.widget.*
import android.view.*
import com.i605.eightball.presenter.PresenterSettings
import com.i605.eightball.adapter.RvAdapter
import com.i605.eightball.R
import kotlinx.android.synthetic.main.activity_settings.*

class ActivitySettings : AppCompatActivity() {
    private val presenter = PresenterSettings(this)
    private val adapterRv by lazy { RvAdapter(presenter.list) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        initView()
        setResult(AppCompatActivity.RESULT_CANCELED)
    }

    private fun initView() {
        val adapterSp = ArrayAdapter(this, R.layout.widget_sp_item, presenter.getCategories())
        sp_cat.adapter = adapterSp
        sp_cat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>) {}

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (position >= presenter.getCategories().size - 1) {
                    val padding = (this@ActivitySettings.resources.displayMetrics.density * 16F).toInt()
                    val ll = LinearLayout(this@ActivitySettings)
                    ll.orientation = LinearLayout.VERTICAL
                    ll.setPadding(padding, 0, padding, 0)

                    val etlCat = TextInputLayout(this@ActivitySettings)
                    etlCat.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    val etCat = TextInputEditText(this@ActivitySettings)
                    etCat.hint = getString(R.string.new_category_name)
                    etlCat.addView(etCat)
                    ll.addView(etlCat)

                    val etlName = TextInputLayout(this@ActivitySettings)
                    etlName.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    val etName = TextInputEditText(this@ActivitySettings)
                    etName.hint = getString(R.string.new_item_name)
                    etlName.addView(etName)
                    ll.addView(etlName)

                    val dialog = AlertDialog.Builder(this@ActivitySettings)
                        .setTitle(R.string.new_category)
                        .setView(ll)
                        .setPositiveButton(R.string.ok, null)
                        .setNegativeButton(R.string.cancel) { _, _ ->
                            sp_cat.setSelection(position - 1)
                        }
                        .show()

                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        if (etCat.text!!.isBlank()) {
                            etlCat.isErrorEnabled = true
                            etlCat.error = getString(R.string.empty)
                            return@setOnClickListener
                        } else {
                            etlCat.isErrorEnabled = false
                        }
                        if (etName.text!!.isBlank()) {
                            etlName.isErrorEnabled = true
                            etlName.error = getString(R.string.empty)
                            return@setOnClickListener
                        } else {
                            etlName.isErrorEnabled = false
                        }
                        if (presenter.isCatExist(etCat.text.toString())) {
                            etlCat.isErrorEnabled = true
                            etlCat.error = getString(R.string.category_exists)
                            return@setOnClickListener
                        } else {
                            etlCat.isErrorEnabled = false
                        }

                        presenter.addCat(etCat.text.toString(), etName.text.toString())
                        adapterSp.insert(etCat.text.toString(), position)
                        dialog.dismiss()
                        sp_cat.setSelection(position)
                        presenter.updateRv(etCat.text.toString())
                        adapterRv.notifyDataSetChanged()
                    }

                } else {
                    presenter.updateRv(adapterSp.getItem(position)!!)
                    adapterRv.notifyDataSetChanged()
                    btn_add.isEnabled = presenter.canAdd(sp_cat.selectedItem as String)
                }
            }
        }

        rv.layoutManager = LinearLayoutManager(rv.context)
        rv.adapter = adapterRv
        adapterRv.setOnItemClickListener(object : RvAdapter.OnItemClickListener {
            override fun onItemClick(name: String) {
                if (presenter.canDelete(sp_cat.selectedItem as String)) {
                    AlertDialog.Builder(this@ActivitySettings)
                        .setMessage(getString(R.string.delete, name))
                        .setPositiveButton(R.string.ok) { _, _ ->
                            adapterRv.notifyItemRemoved(presenter.list.indexOf(name))
                            if (presenter.deleteItem(sp_cat.selectedItem as String, name)) {
                                adapterSp.remove(sp_cat.selectedItem as String)
                                sp_cat.setSelection(0)
                            }
                        }
                        .setNegativeButton(R.string.cancel, null)
                        .show()
                } else {
                    AlertDialog.Builder(this@ActivitySettings)
                        .setMessage(getString(R.string.can_not_delete))
                        .setPositiveButton(R.string.ok, null)
                        .show()
                }
            }
        })

        btn_add.setOnClickListener {
            val name = et_name.text.toString()

            if (et_name.text!!.isBlank()) {
                etl_name.isErrorEnabled = true
                etl_name.error = getString(R.string.empty)
                return@setOnClickListener
            } else {
                etl_name.isErrorEnabled = false
            }
            if (presenter.isNameExist(name)) {
                etl_name.isErrorEnabled = true
                etl_name.error = getString(R.string.item_exists)
                return@setOnClickListener
            } else {
                etl_name.isErrorEnabled = false
            }

            presenter.addItem(sp_cat.selectedItem as String, name)
            adapterRv.notifyItemInserted(presenter.list.lastIndex)
            et_name.text!!.clear()
        }
    }
}