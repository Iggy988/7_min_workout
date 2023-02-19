package com.example.sevenminworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sevenminworkout.databinding.ActivityExerciseBinding
import com.example.sevenminworkout.databinding.DialogCustomBackConfirmationBinding
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var binding: ActivityExerciseBinding? = null
    // relaxing time
    private var restTimer : CountDownTimer? = null
    // dokle smo dosli
    private var restProgress = 0

    private var restTimerDuration: Long = 10

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0

    private var exerciseTimerDuration: Long = 30

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null

    private var exerciseAdapter: ExerciseStatusAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //postavljamo action bar iz xml (activity_exercises)
        setSupportActionBar(binding?.toolbarExercise)

        // postavljanje back ikone u action toolbar
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        // pozivanje sa Constant
        exerciseList = Constant.defaultExerciseList()

        tts = TextToSpeech(this, this)

        // postavljanje komande za back po pritisku
        binding?.toolbarExercise?.setNavigationOnClickListener {
            customDialogForBackButton()
        }


        setupRestView()
        setupExerciseStatusRecyclerView()

    }
    // metod za custom dialog za back dugme
    private fun customDialogForBackButton(){
        val customDialog = Dialog(this)
        // pravimo poseban binding za xml dialog_custom_back_confirmation posto nije u okviru activity_main
        // koristimo layout dialog_custom_back_confirmation da inflate elemente(text, button...)
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        //Set the screen content to an explicit view. This view is placed directly into the screen's view hierarchy.
        // It can itself be a complex view hierarchy.
        customDialog.setContentView(dialogBinding.root)
        // da se ne moze cancelati dok se ne izabere jedna od ponudjenih opcija(odnosno ne moze se kliknuti izvan dialoga za cancel)
        customDialog.setCanceledOnTouchOutside(false)

        // klikom na yes, zatvaramo Activity
        dialogBinding.btnYes.setOnClickListener {
            this@ExerciseActivity.finish()
            // moramo ugasiti dialog ili ce ono i dalje stojati
            customDialog.dismiss()
        }
        // klikom na no gasimo custom dialog
        dialogBinding.btnNo.setOnClickListener {
            customDialog.dismiss()
        }
        // za prikaz custom dialoga
        customDialog.show()

    }

    // ubacivanje custom dialog metode u metodu za donje back dugme
    override fun onBackPressed() {
        customDialogForBackButton()
        //super.onBackPressed()
    }

    // metoda za implementaciju RecyclerViewa
    private fun setupExerciseStatusRecyclerView(){
        // layoutManager omogucava nam da izaberemo LayoutManager
        binding?.rvExerciseStatus?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }

    // metoda za resetovanje timera kad se pritisne back
    private fun setupRestView() {

        // ubacivanje mp3 preko Uri.parse(unosimo string lokaciju + lokaciju u resources folderu)
        try {
            val soundURI = Uri.parse(
                "android.resource://com.example.sevenminworkout/" + R.raw.press_start)
            // MediaPlayer klasa koristi create metod koji ima parametre context i lokaciju fajla
            player = MediaPlayer.create(applicationContext, soundURI)
            // necemo da se zvuk ponavlja
            player?.isLooping = false
            player?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }


        // napravimo da se prikazuje
        binding?.flRestView?.visibility = View.VISIBLE
        //get ready vidljiv
        binding?.tvTitle?.visibility = View.VISIBLE
        // naziv vjezbe, nevidljiv dok je rest time
        binding?.tvExerciseName?.visibility = View.INVISIBLE
        // exerciseview, nevidljiv dok je rest time
        binding?.flExerciseView?.visibility = View.INVISIBLE
        // slika nevidljiva dok je restTime
        binding?.ivImage?.visibility = View.INVISIBLE
        // naziv naredne vjezbe vidljiv u rest view
        binding?.tvUpcomingLabel?.visibility = View.VISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.VISIBLE

        if (restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }



        // pribavljanje naziva naredne vjezbe preko exerciseList(Array(class ExerciseModel)) // fun getName vraca String
        binding?.tvUpcomingExerciseName?.text = exerciseList!![currentExercisePosition + 1].getName()

        setRestProgressBar()
    }

    private fun setupExerciseView() {
        // napravimo da se ne prikazuje ali je i dalje tu
        binding?.flRestView?.visibility = View.INVISIBLE
        //kad pocne vjezba tv get ready postaje invisible
        binding?.tvTitle?.visibility = View.INVISIBLE
        // naziv vjezbe, napravimo visible dok traje vjezba
        binding?.tvExerciseName?.visibility = View.VISIBLE
        binding?.flExerciseView?.visibility = View.VISIBLE
        // slika treba da bude vidljiva dok traje vjezba
        binding?.ivImage?.visibility = View.VISIBLE
        // naziv naredne vjezbe nevidljiv u exercise view
        binding?.tvUpcomingLabel?.visibility = View.INVISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.INVISIBLE


        //resetovanje timera
        if (exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
        // pozivanje metode za tekst to speach glasovno izgovaranje naziva vjezbe
        speakOut(exerciseList!![currentExercisePosition].getName())

        // postavljanje  slike u ivImage iz exerciseList(Array(class ExerciseModel)) // fun getImage vraca Int
        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        // postavljanje  naziva vjezbe iz exerciseList(Array(class ExerciseModel)) // fun getName vraca String
        binding?.tvExerciseName?.text = exerciseList!![currentExercisePosition].getName()

        setExerciseProgressBar()
    }

    private fun setRestProgressBar() {
        binding?.progressBar?.progress = restProgress
          //seting timer                          // 10 sekundi, tick je 1 sekunda
        restTimer = object : CountDownTimer(restTimerDuration*1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding?.progressBar?.progress = 10 - restProgress
                binding?.tvTimer?.text = (10 - restProgress).toString()
            }
            override fun onFinish() {

                currentExercisePosition++

                // kad smo na u rest modu postavljamo vizualni prikaz (RecyclerView) koja je trenutna vjezba
                exerciseList!![currentExercisePosition].setIsSelected(true)
                // obavjestavamo adapter da se data promijenila, da bi adapter mogao da poyiva create metode ponovo
                exerciseAdapter!!.notifyDataSetChanged()

                setupExerciseView()
        }
        }.start()
    }



    private fun setExerciseProgressBar() {
        binding?.progressBarExercise?.progress = exerciseProgress

        exerciseTimer = object : CountDownTimer(exerciseTimerDuration*1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding?.progressBarExercise?.progress = 30 - exerciseProgress
                binding?.tvTimerExercise?.text = (30 - exerciseProgress).toString()
            }
            override fun onFinish() {

                if (currentExercisePosition < exerciseList?.size!! -1) {
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    // prikaz da je trenutna vjezba zavrsena
                    exerciseList!![currentExercisePosition].setIsComplited(true)
                    // obavjestavamo adapter da se data promijenila, da bi adapter mogao da poyiva create metode ponovo
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()
                } else {
                   finish()
                   val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                }
            }
        }.start()
    }


    // kad koristimo bainding uvijek treba resetovati binding na nulu preko onDestroy metode
    // da ne bismo imali memory leakages treba  stopirati odredjene procese prilikom gasenja aplikacije
    override fun onDestroy() {
        super.onDestroy()
        if (restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }

        if (exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        if (tts != null) {
            tts?.stop()
            tts?.shutdown()
        }

        if (player != null) {
            player!!.stop()
        }

        binding = null
    }



    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // koristimo jezik za govor
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA
                || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language specified is not supported")
            }
        } else {
            Log.e("TTS", "Initialization Failed!")
        }
    }

    private  fun speakOut(text: String) {
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

}