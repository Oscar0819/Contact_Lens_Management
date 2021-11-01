package com.eunwoo.contactlensmanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.eunwoo.contactlensmanagement.databinding.ActivityLensInfoBinding
import com.eunwoo.contactlensmanagement.databinding.ActivityMainBinding

class LensInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLensInfoBinding

    companion object {
        const val TAG: String = "로그"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLensInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Log.d(TAG, "LensInfoActivity - onCreate() called")

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}