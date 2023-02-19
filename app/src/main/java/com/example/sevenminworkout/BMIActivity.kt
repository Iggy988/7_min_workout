package com.example.sevenminworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import com.example.sevenminworkout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    companion object {
        private const val METRIC_UNIT_VIEW = "METRIC_UNIT_VIEW" // Metric Unit View
        private const val US_UNIT_VIEW = "US_UNIT_VIEW" // US Unit View
    }

    private var binding: ActivityBmiBinding? = null

    // a variable to hold a value to make a selected view visible
    private var currentVisibleView: String = METRIC_UNIT_VIEW


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

    // postavljanje ActionBara
        setSupportActionBar(binding?.toolbarBmiActivity)
        // postavljanje back ikone u action toolbar
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            // moyemo mijenjati naslov u elementima, konkretno u ovom slucaju u ActionBAru
            supportActionBar?.title = "IZRAČUNAJ BMI"
        }
        // postavljanje komande za back po pritisku
        binding?.toolbarBmiActivity?.setNavigationOnClickListener {
            onBackPressed()
        }

        makeVisibleMetricUnitsView()

        // registrujemo izbor kad pritisnemo radio button
        // kad koristimo radio grupu- koristimo setOnCheckedChangeListener (RadioGroup, Int)
        binding?.rgUnits?.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbMetricUnits) {
                makeVisibleMetricUnitsView()
            } else {
                makeVisibleUSUnitsView()
            }
        }


        // pritiskom na dugme CALCULATE:
        binding?.btnCalculateUnits?. setOnClickListener {

            // unos metode za obracun mjernih jedinica
            calculateUnits()

        }
    }

    // metoda za prikazivanje metrickih jedinica
    private fun makeVisibleMetricUnitsView(){
        currentVisibleView = METRIC_UNIT_VIEW
        // Metric Weight and Height Visible
        binding?.tilMetricUnitWeight?.visibility = View.VISIBLE
        binding?.tilMetricUnitHeight?.visibility = View.VISIBLE

        // US Weight and Height Gone
        binding?.tilUsUnitWeight?.visibility = View.GONE
        binding?.tilUsUnitHeightFeet?.visibility = View.GONE
        binding?.tilUsUnitHeightInch?.visibility = View.GONE

        // clear value if it is added (ako je unesena vrijednost, brise se ako mijenjamo metricki kalkulator)
        binding?.etMetricUnitHeight?.text!!.clear()
        binding?.etMetricUnitWeight?.text!!.clear()

        // kad se klikne na drugi kalkulator(metricki), brise se prethodno izracunata vrijednost
        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE
    }

    // metoda za prikazivanje Us jedinica
    private fun makeVisibleUSUnitsView(){
        currentVisibleView = US_UNIT_VIEW // Current View is updated here
        // Metric Weight and Height Gone
        binding?.tilMetricUnitWeight?.visibility = View.GONE
        binding?.tilMetricUnitHeight?.visibility = View.GONE

        // US Weight and Height Visible
        binding?.tilUsUnitWeight?.visibility = View.VISIBLE
        binding?.tilUsUnitHeightFeet?.visibility = View.VISIBLE
        binding?.tilUsUnitHeightInch?.visibility = View.VISIBLE

        // clear value if it is added (ako je unesena vrijednost, brise se ako mijenjamo metricki kalkulator)
        binding?.etUsUnitWeight?.text!!.clear()
        binding?.etUsUnitHeightFeet?.text!!.clear()
        binding?.etUsUnitHeightInch?.text!!.clear()

        // kad se klikne na drugi kalkulator(metricki), brise se prethodno izracunata vrijednost
        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE
    }


    // metoda za prikazivanje BMI rezultata
    private fun displayBMIResult(bmi: Float){

        val bmiLabel: String
        val bmiDescription: String

        /**
         * Compares this value with the specified value for order.
         * Returns zero if this value is equal to the specified other value,
         * a negative number if it's less than other, or a positive number if it's greater than other.
         */
        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Vrlo teška pothranjenost"
            bmiDescription = "Ups! Stvarno se trebate bolje brinuti o sebi! Jedi više!!!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0) {
            bmiLabel = "Ozbiljna pothranjenost"
            bmiDescription = "Ups! Stvarno se trebate bolje brinuti o sebi! Jedi više!!!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0) {
            bmiLabel = "Nedovoljna težina"
            bmiDescription = "Ups! Stvarno se trebate bolje brinuti o sebi! Jedi više!!!"
        }else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0) {
            bmiLabel = "Normalna težina"
            bmiDescription = "Čestitamo! U dobroj ste formi!"
        }else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0) {
            bmiLabel = "Preteški ste"
            bmiDescription = "Ups! Stvarno se trebate bolje brinuti o sebi! Vježbajte!"
        }else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0) {
            bmiLabel = "Pretili ste | (Umjerena pretilost)"
            bmiDescription = "Ups! Stvarno se trebate bolje brinuti o sebi! Vježbajte!!"
        }else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0) {
            bmiLabel = "Pretili ste || (Jaka pretilost)"
            bmiDescription = "OMG! U vrlo ste opasnom stanju! Djelujte odmah!"
        }else {
            bmiLabel = "Pretili ste ||| (Vrlo teška pretilost pretilost)"
            bmiDescription = "OMG! U vrlo ste opasnom stanju! Djelujte odmah!!"
        }

        // pravimo da linearLayout bude visible
        binding?.llDisplayBMIResult?.visibility = View.VISIBLE

        // This is used to round of the result value to 2 decimal values after "."
        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        binding?.tvBMIValue?.text = bmiValue
        binding?.tvBMIType?.text = bmiLabel
        binding?.tvBMIDescription?.text = bmiDescription

    }


    //metoda za utvrdjivanje da li je izvrsen u unos brojeva u polja
    private fun validateMetricUnits(): Boolean {
        var isValid = true

        // ako je polje ya unos vrijednosti za tezinu prazno
        // varijabla isValid vraca false
        if (binding?.etMetricUnitWeight?.text.toString().isEmpty()){
            isValid = false
        } else if (binding?.etMetricUnitHeight?.text.toString().isEmpty()) {
            isValid = false
        }
        // vraca true ili false zavisno da li je izvrsen unos
        return isValid
    }

    // metoda za obracun mjera
    private fun calculateUnits() {
        if (currentVisibleView == METRIC_UNIT_VIEW) {
                // ukoliko je unesena vrijednost
                if (validateMetricUnits()) {
                    // The height value in converted to float value and divided by 100 to convert it to meter.
                    val heightValue: Float = binding?.etMetricUnitHeight?.text.toString().toFloat() / 100

                    val weightValue: Float = binding?.etMetricUnitWeight?.text.toString().toFloat()

                    // formula za izracunavanje bmi
                    val bmi = weightValue/(heightValue*heightValue)
                    // metoda prikayuje rezultat
                    displayBMIResult(bmi)

                    // ukoliko nije unesena vrijednost
                } else {
                    Toast.makeText(this@BMIActivity, "Unesite validnje mjere", Toast.LENGTH_SHORT).show()
                }
        } else {
            if (validateUsUnits()){
                val usUnitHeightValueFeet: String = binding?.etUsUnitHeightFeet?.text.toString()
                val usUnitHeightValueInch: String = binding?.etUsUnitHeightInch?.text.toString()
                val usUnitWeightValue: Float = binding?.etUsUnitWeight?.text.toString().toFloat()

                // Height Feet and Inch values are merged and multiplied by 12 for converting
                val heightValue = usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12

                val bmi = 703 * (usUnitWeightValue / (heightValue*heightValue))

                displayBMIResult(bmi)
            }
            // ukoliko nije unesena vrijednost
            else {
                Toast.makeText(this@BMIActivity, "Unesite validnje mjere", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // pomocna metoda za obracun US mjernih jedinica
    private fun validateUsUnits(): Boolean {
        var isValid = true

        // kad je polje unosa texta prazno
        when {
            binding?.etUsUnitWeight?.text.toString().isEmpty() -> {
                isValid = false
            }
            binding?.etUsUnitHeightFeet?.text.toString().isEmpty() -> {
                isValid = false
            }
            binding?.etUsUnitHeightInch?.text.toString().isEmpty() -> {
                isValid = false
            }
        }
        return isValid
    }

}