package com.example.sevenminworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.sevenminworkout.databinding.ActivityFinishBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FinishActivity : AppCompatActivity() {

    private var binding: ActivityFinishBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarFinishActivity)

        // postavljanje back ikone u action toolbar
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        //pritisak na back button
        binding?.toolbarFinishActivity?.setNavigationOnClickListener {
            onBackPressed()
        }

        binding?.btnFinish?.setOnClickListener {
            finish()
        }


        val dao = (application as WorkOutApp).db?.historyDao()

        if (dao != null) {
            addDateToDatabase(dao)
        }


    }
    // metoda za spremanje data u database
    private fun addDateToDatabase(historyDao: HistoryDao){

        // dodavanje kalendara radi pristupanja datumu
        val c = Calendar.getInstance()
        val dateTime = c.time
        Log.e("Date", "" +dateTime)

        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        // napravi data od sdf koji ce biti string
        val date = sdf.format(dateTime)
        Log.e("Formatted Date", "" +dateTime)


        lifecycleScope.launch {
            historyDao.insert(HistoryEntity(date))
            Log.e("Date: ", "Added...")
        }
    }
}