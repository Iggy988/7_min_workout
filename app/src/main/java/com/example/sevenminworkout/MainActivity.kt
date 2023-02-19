package com.example.sevenminworkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.FrameLayout
import android.widget.Toast
import com.example.sevenminworkout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //Variable for timer which will be initialized later
    private var countDownTimer: CountDownTimer? = null
    //The duration of the timer in milliseconds
    private var timeDuration: Long = 60000
    //pauseOffset = timerDuration - time left
    private var pauseOffset: Long = 0


    // kreiranje varijable binding i dodjeljivanje nullte vrijednosti
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        // koristi root layout - activity main, constraint layout
        setContentView(binding?.root)


        //val flStartButton: FrameLayout = findViewById(R.id.fl_start)
        binding?.flStart?.setOnClickListener {
            // otvaranje novog activityja
            val intent = Intent(this, ExerciseActivity::class.java)
            startActivity(intent)

        }

        // pravimo da se klikom na fl otvara BMI Activity
        binding?.flBMI?.setOnClickListener {
            // otvaranje novog activityja
            val intent = Intent(this, BMIActivity::class.java)
            startActivity(intent)

        }

        // pravimo da se klikom na fl otvara History Activity
        binding?.flHistory?.setOnClickListener {
            // otvaranje novog activityja
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)

        }

    }










    // kad koristimo bainding uvijek treba resetovati binding na nulu preko onDestroy metode
    override fun onDestroy() {
        super.onDestroy()
        // uvijek resetovati binding na null
        binding = null
    }
}