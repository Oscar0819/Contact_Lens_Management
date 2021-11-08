package com.eunwoo.contactlensmanagement

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.eunwoo.contactlensmanagement.database.Lens
import com.eunwoo.contactlensmanagement.database.LensDatabase
import com.eunwoo.contactlensmanagement.databinding.ActivityLensInfoBinding
import com.eunwoo.contactlensmanagement.databinding.ActivityMainBinding
import java.util.*

class LensInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLensInfoBinding

    private lateinit var db: LensDatabase

    companion object {
        const val TAG: String = "로그"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLensInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Log.d(TAG, "LensInfoActivity - onCreate() called")

        db = LensDatabase.getInstance(applicationContext)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.calendarButton.setOnClickListener {
            val today = GregorianCalendar()
            val year: Int = today.get(Calendar.YEAR)
            val month: Int = today.get(Calendar.MONTH)
            val date: Int = today.get(Calendar.DATE)

            val dlg = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    binding.calendarButton.setText("${year}-${month+1}-${dayOfMonth}")
                }
            }, year, month, date)
            dlg.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.lens_info_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.save -> {
                save()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun save() {
        binding.run {
            if (this.nameEditText.text.toString().isNotEmpty() and
                    this.calendarButton.text.toString().isNotEmpty() and
                    this.expirationDateButton.text.toString().isNotEmpty()) {
                Thread(Runnable {
                    db!!.lensDao().insert(Lens(null,
                        this.nameEditText.text.toString(),
                        null,
                        this.leftSightEditText.text.toString().toDouble(),
                        this.rightSightEditText.text.toString().toDouble(),
                        this.productNameEditText.text.toString(),
                        this.calendarButton.text.toString(),
                        this.expirationDateButton.text.toString(),
                        this.notificationSwitch.isChecked,
                        this.memoEditText.text.toString()))
                    finish()
                }).start()
            } else {
                Toast.makeText(this@LensInfoActivity, "데이터를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }


}