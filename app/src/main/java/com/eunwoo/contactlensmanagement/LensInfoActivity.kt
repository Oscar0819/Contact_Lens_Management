package com.eunwoo.contactlensmanagement

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.eunwoo.contactlensmanagement.databinding.ActivityLensInfoBinding
import com.eunwoo.contactlensmanagement.viewmodel.LensInfoViewModel
import java.util.*

class LensInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLensInfoBinding

    private lateinit var lensInfoViewModel: LensInfoViewModel

    companion object {
        const val TAG: String = "LensInfoActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lens_info)
        binding.lifecycleOwner = this // Use viewLifecycleOwner for fragments
//        binding = ActivityLensInfoBinding.inflate(layoutInflater)
//        val view = binding.root
//        setContentView(view)
        Log.d(TAG, "LensInfoActivity - onCreate() called")

        lensInfoViewModel = ViewModelProvider(this).get(LensInfoViewModel::class.java)
        binding.viewmodel = lensInfoViewModel

        // LiveData를 데이터바인딩으로 사용할 경우 반드시 선언해야함
        binding.lifecycleOwner = this

        lensInfoViewModel.increaseCnt()

        // 인텐트 값을 viewmodel에 전달
        lensInfoViewModel.setCode(
            intent.getIntExtra("code", 99),
        )

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 수정 요청 확인 후 데이터 세트
        if (lensInfoViewModel.code == 1) {
            // 뷰모델 프로퍼티에 저장!
            lensInfoViewModel.setIndex(intent.getSerializableExtra("index").toString().toLong())
            lensInfoViewModel.setId(intent.getSerializableExtra("id").toString().toLong())
            // 수정 모드일 떄 초기화!
            lensInfoViewModel.initModify()
        }

        binding.calendarButton.setOnClickListener {
            // 그레고리력..
            val today = GregorianCalendar()
            val year: Int = today.get(Calendar.YEAR)
            val month: Int = today.get(Calendar.MONTH)
            val date: Int = today.get(Calendar.DATE)

            // 날짜 선택 다이얼로그
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
        // 목적에 따른 텍스트 설정
        if (lensInfoViewModel.code == 0) {
            menu!!.findItem(R.id.save_or_modify).title = "저장"
        } else if (lensInfoViewModel.code == 1) {
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
                if (lensInfoViewModel.code == 0) {
                    lensInfoViewModel.save()
                } else if(lensInfoViewModel.code == 1) {
                    lensInfoViewModel.modify()
                }
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

