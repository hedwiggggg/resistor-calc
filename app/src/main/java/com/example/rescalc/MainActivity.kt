package com.example.rescalc

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var currentBandTuple: Pair<Button, Band>;
    private var currentBandCount = BandCount.FOUR
    private var validColorChange = false

    private val colorBandTuples: Array<Pair<Button, Band>>
        get() = arrayOf(
            Pair(findViewById(R.id.color_1), Band.ONE),
            Pair(findViewById(R.id.color_2), Band.TWO),
            Pair(findViewById(R.id.color_3), Band.THREE),
            Pair(findViewById(R.id.color_4), Band.FOUR),
            Pair(findViewById(R.id.color_5), Band.FIVE),
            Pair(findViewById(R.id.color_6), Band.SIX)
        )

    private val colorBandButtons = object {
        val fifth: Button
            get() = colorBandTuples[4].first

        val sixth: Button
            get() = colorBandTuples[5].first
    }

    private val resistorChangeTuples: Array<Pair<Button, BandCount>>
        get() = arrayOf(
            Pair(findViewById(R.id.colors_4), BandCount.FOUR),
            Pair(findViewById(R.id.colors_5), BandCount.FIVE),
            Pair(findViewById(R.id.colors_6), BandCount.SIX)
        )

    private val resistorChangeButtons = object {
        val fourColors: Button
            get() = resistorChangeTuples[0].first

        val fiveColors: Button
            get() = resistorChangeTuples[1].first

        val sixColors: Button
            get() = resistorChangeTuples[2].first
    }

    private val resistorMeta = object {
        val resistance: TextView
            get() = findViewById(R.id.resistance)

        val tolerance: TextView
            get() = findViewById(R.id.tolerance)

        val coefficient: TextView
            get() = findViewById(R.id.coefficient)
    }

    private val colorSelector: Spinner
        get() = findViewById(R.id.color_selector)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupColorSelector()
        setupColorBands()
        setupResistorTypeChange()

        // set initial resistor
        setBandColor(colorBandTuples[0], Colors.YELLOW)
        setBandColor(colorBandTuples[1], Colors.RED)
        setBandColor(colorBandTuples[2], Colors.BLACK)
        setBandColor(colorBandTuples[3], Colors.SILVER)
        setBandColor(colorBandTuples[4], Colors.SILVER)
        setBandColor(colorBandTuples[5], Colors.BROWN)

        updateResistorInformation()
    }

    private fun setupColorSelector() {
        // fill spinner with data from R.array.resistor_colors
        ArrayAdapter.createFromResource(
            this,
            R.array.resistor_colors,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            colorSelector.adapter = adapter
        }

        // add item select listener
        colorSelector.onItemSelectedListener = handleColorSelected
    }

    private val handleColorSelected = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
            // not necessary, but has to be implemented
        }

        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            if (::currentBandTuple.isInitialized && validColorChange) {
                val colorName = colorSelector.getItemAtPosition(position).toString()
                val color = Colors.fromColorName(colorName)

                setBandColor(currentBandTuple, color)
                updateResistorInformation()

                /**
                 * validColorChange + selection reset was necessary,
                 * because selecting the same color 2x didn't trigger
                 * the onItemSelected handler, and setSelection triggers
                 * it unwanted.
                 */

                validColorChange = false
                colorSelector.setSelection(0)
            }
        }
    }

    fun setBandColor(band: Pair<Button, Band>, color: Colors) {
        val (button, type) = band
        val resource = Colors.getResource(color)

        CurrentResistor.bands.setBand(type, color)
        button.setBackgroundResource(resource)
    }

    private fun setupColorBands() {
        colorBandTuples.forEach {
            val (button, type) = it

            button.setOnClickListener {
                changeCurrentBand(button, type)
            }
        }
    }

    private fun changeCurrentBand(button: Button, type: Band) {
        currentBandTuple = Pair(button, type)
        validColorChange = true
        colorSelector.performClick()
    }

    private fun setupResistorTypeChange() {
        resistorChangeTuples.forEach {
            val (button, type) = it

            button.setOnClickListener {
                selectResistorType(type)
            }
        }
    }

    private fun selectResistorType(type: BandCount) {
        currentBandCount = type

        when (type) {
            BandCount.FOUR -> {
                resistorChangeButtons.fourColors.setBackgroundResource(R.drawable.resistor_type_chooser_selected)
                resistorChangeButtons.fiveColors.setBackgroundResource(R.drawable.resistor_type_chooser)
                resistorChangeButtons.sixColors.setBackgroundResource(R.drawable.resistor_type_chooser)

                colorBandButtons.fifth.visibility = View.GONE
                colorBandButtons.sixth.visibility = View.GONE
            }

            BandCount.FIVE -> {
                resistorChangeButtons.fourColors.setBackgroundResource(R.drawable.resistor_type_chooser)
                resistorChangeButtons.fiveColors.setBackgroundResource(R.drawable.resistor_type_chooser_selected)
                resistorChangeButtons.sixColors.setBackgroundResource(R.drawable.resistor_type_chooser)

                colorBandButtons.fifth.visibility = View.VISIBLE
                colorBandButtons.sixth.visibility = View.GONE
            }

            BandCount.SIX -> {
                resistorChangeButtons.fourColors.setBackgroundResource(R.drawable.resistor_type_chooser)
                resistorChangeButtons.fiveColors.setBackgroundResource(R.drawable.resistor_type_chooser)
                resistorChangeButtons.sixColors.setBackgroundResource(R.drawable.resistor_type_chooser_selected)

                colorBandButtons.fifth.visibility = View.VISIBLE
                colorBandButtons.sixth.visibility = View.VISIBLE
            }
        }

        updateResistorInformation()
    }

    @SuppressLint("SetTextI18n")
    private fun updateResistorInformation() {
        fun failed(message: String) {
            resistorMeta.resistance.text = "resistance   | NaN Ω"
            resistorMeta.tolerance.text = "tolerance    | NaN %"
            resistorMeta.coefficient.text = "coefficient  | NaN"

            Toast.makeText(
                this@MainActivity,
                message,
                Toast.LENGTH_SHORT
            ).show()
        }

        fun formatOhms(ohms: Float): String {
            val stringOhms = ohms.roundToInt().toString()

            return when (stringOhms.length) {
                4, 5, 6 -> (ohms / 1000).toString() + "K"
                7, 8, 9 -> (ohms / 1000000).toString() + "M"
                10, 11, 12 -> (ohms / 1000000000).toString() + "G"
                else -> ohms.toString()
            }
        }

        try {
            resistorMeta.resistance.text = "resistance   | ${formatOhms(getResistance)} Ω"
            resistorMeta.tolerance.text = "tolerance    | $getTolerance %"
            resistorMeta.coefficient.text = "coefficient  | $getCoefficient"
        } catch (e: GetResistanceError) {
            failed("Couldn't get resistance value, your setting seems wrong..")
        } catch (e: GetMultiplicandError) {
            failed("Couldn't get multiplicand value, your setting seems wrong..")
        } catch (e: GetToleranceError) {
            failed("Couldn't get tolerance value, your setting seems wrong..")
        } catch (e: GetCoefficientError) {
            failed("Couldn't get coefficient value, your setting seems wrong..")
        }
    }

    private val getResistance: Float
        get() = when (currentBandCount) {
            BandCount.FOUR -> {
                var resistance = 0f
                resistance += Colors.getResistance(CurrentResistor.bands.first) * 10
                resistance += Colors.getResistance(CurrentResistor.bands.second) * 1
                resistance *= Colors.getMultiplicand(CurrentResistor.bands.third)
                resistance
            }

            else -> {
                var resistance = 0f
                resistance += Colors.getResistance(CurrentResistor.bands.first) * 100
                resistance += Colors.getResistance(CurrentResistor.bands.second) * 10
                resistance += Colors.getResistance(CurrentResistor.bands.third) * 1
                resistance *= Colors.getMultiplicand(CurrentResistor.bands.fourth)
                resistance
            }
        }

    private val getTolerance: Float
        get() = when (currentBandCount) {
            BandCount.FOUR -> Colors.getTolerance(CurrentResistor.bands.fourth)
            else -> Colors.getTolerance(CurrentResistor.bands.fifth)
        }

    private val getCoefficient: Float
        get() = when (currentBandCount) {
            BandCount.SIX -> Colors.getCoefficient(CurrentResistor.bands.sixth)
            else -> Float.NaN
        }

}
