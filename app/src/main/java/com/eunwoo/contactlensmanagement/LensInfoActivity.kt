package com.eunwoo.contactlensmanagement

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.eunwoo.contactlensmanagement.database.Lens
import com.eunwoo.contactlensmanagement.database.LensDatabase
import com.eunwoo.contactlensmanagement.databinding.ActivityLensInfoBinding
import com.eunwoo.contactlensmanagement.dataclass.LensInfo
import com.eunwoo.contactlensmanagement.viewmodel.LensInfoViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import java.lang.Runnable
import java.util.*

class LensInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLensInfoBinding

    private lateinit var db: LensDatabase

    private lateinit var lensInfoViewModel: LensInfoViewModel

    companion object {
        const val TAG: String = "LensInfoActivity"
    }

    // code는 저장, 수정 관련 신호 0 = 저장, 1 = 수정
    // 기본 디폴트 값
    private var code: Int = -1
    private var index: Long = -1
    private var id: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLensInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Log.d(TAG, "LensInfoActivity - onCreate() called")

        db = LensDatabase.getInstance(applicationContext)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        code = intent.getIntExtra("code", 99)


        // 수정 요청 확인 후 데이터 세트
        if (intent.getSerializableExtra("code") == 1) {
            index = intent.getSerializableExtra("index").toString().toLong()
            id = intent.getSerializableExtra("id").toString().toLong()
            initModify()
        }

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
        if (code == 0) {
            menu!!.findItem(R.id.save_or_modify).title = "저장"
        } else if (code == 1) {
            menu!!.findItem(R.id.save_or_modify).title = "수정"
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.save_or_modify -> {
                if (code == 0) {
                    save()
                } else if(code == 1) {
                    CoroutineScope(IO).launch {
                        modify()
                    }
                }

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private suspend fun modify() {
        Log.d(TAG, "mINDEX : ${index}")
        binding.apply {
            val lens: Lens = Lens(
                id,
                nameEditText.text.toString(),
                leftSightEditText.text.toString().toDouble(),
                rightSightEditText.text.toString().toDouble(),
                productNameEditText.text.toString(),
                calendarButton.text.toString(),
                expirationDateButton.text.toString(),
                notificationSwitch.isChecked,
                memoEditText.text.toString()
                )
            db.lensDao().update(lens)
            finish()
        }
    }

    private fun save() {
        binding.run {
            if (this.nameEditText.text.toString().isNotEmpty() and
                    this.calendarButton.text.toString().isNotEmpty() and
                    this.expirationDateButton.text.toString().isNotEmpty()) {
                Thread(Runnable {
                    db!!.lensDao().insert(Lens(null,
                        this.nameEditText.text.toString(),
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

    private fun initModify() {
        Log.d(TAG, "INDEX : ${index}")
        lensInfoViewModel = ViewModelProvider(this).get(LensInfoViewModel::class.java)


        CoroutineScope(Default).launch {
            val lensData: Lens = db.lensDao().getList()[index.toInt()]
            var lensInfo: LensInfo = LensInfo(lensData.name!!,
                                    lensData.leftSight!!,
                                    lensData.rightSight!!,
                                    lensData.productName!!,
                                    lensData.initialDate!!,
                                    lensData.expirationDate!!,
                                    lensData.pushCheck!!,
                                    lensData.memo!!)
            CoroutineScope(Main).launch{
            lensInfoViewModel.updateLensInfo(lensInfo)
            lensInfoViewModel.getLensInfo().observe(this@LensInfoActivity, androidx.lifecycle.Observer {
                    binding.apply {
                        nameEditText.setText(lensInfo.name)
                        leftSightEditText.setText(lensInfo.leftSight.toString())
                        rightSightEditText.setText(lensInfo.rightSight.toString())
                        productNameEditText.setText(lensInfo.productName)
                        calendarButton.setText(lensInfo.initialDate)
                        expirationDateButton.setText(lensInfo.expirationDate)
                        memoEditText.setText(lensInfo.memo)
                        notificationSwitch.isChecked = lensInfo.pushCheck!!
                    }
            })
        }
//            binding.apply {
//                nameEditText.setText(lensData.name)
//                leftSightEditText.setText(lensData.leftSight.toString())
//                rightSightEditText.setText(lensData.rightSight.toString())
//                productNameEditText.setText(lensData.productName)
//                calendarButton.setText(lensData.initialDate)
//                expirationDateButton.setText(lensData.expirationDate)
//                memoEditText.setText(lensData.memo)
//                runOnUiThread {
//                    notificationSwitch.isChecked = lensData.pushCheck!!
//                }
//            }
        }
    }

}

