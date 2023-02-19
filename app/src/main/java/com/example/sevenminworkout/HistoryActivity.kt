package com.example.sevenminworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sevenminworkout.databinding.ActivityHistoryBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private var binding: ActivityHistoryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // postavljanje ActionBara
        setSupportActionBar(binding?.toolbarHistoryActivity)
        // postavljanje back ikone u action toolbar
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            // mozemo mijenjati naslov u elementima, konkretno u ovom slucaju u ActionBAru
            supportActionBar?.title = "ISTORIJA"
        }
        // postavljanje komande za back po pritisku
        binding?.toolbarHistoryActivity?.setNavigationOnClickListener {
            onBackPressed()
        }

        val dao = (application as WorkOutApp).db?.historyDao()
        if (dao != null) {
            getAllCompletedDates(dao)
        }
    }

    // metoda za pristup svim unosima
    private fun getAllCompletedDates(historyDao: HistoryDao){
        lifecycleScope.launch {
            historyDao.fetchAllDates().collect { allCompletedDates ->

                if (allCompletedDates.isNotEmpty()) {
                    binding?.tvHistory?.visibility = View.VISIBLE
                    binding?.rvHistory?.visibility = View.VISIBLE
                    binding?.tvNoDataAvailable?.visibility = View.INVISIBLE

                    binding?.rvHistory?.layoutManager = LinearLayoutManager(this@HistoryActivity)

                    // unosimo datume
                    val dates = ArrayList<String>()
                    for (date in allCompletedDates) {
                        // unosimo data iz HistoryEntity
                        dates.add(date.date)
                    }

                    val historyAdapter = HistoryAdapter(dates)

                    // zakacimo adapter za rv
                    binding?.rvHistory?.adapter = historyAdapter

                } else {
                    binding?.tvHistory?.visibility = View.GONE
                    binding?.rvHistory?.visibility = View.GONE
                    binding?.tvNoDataAvailable?.visibility = View.VISIBLE
                }

            }
        }
    }








    // kad koristimo bainding uvijek treba resetovati binding na nulu preko onDestroy metode
    override fun onDestroy() {
        super.onDestroy()
        // uvijek resetovati binding na null
        binding = null
    }
}