package me.iiasceri.ktcalculator

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_mean_median.workingTV
import java.util.*

class MeanMedianActivity : AppCompatActivity() {

    var mode = ""
    private var endText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mean_median)
        mode = intent.getStringExtra("mode").toString()
        resultsTV.text = mode.uppercase(Locale.getDefault())
    }

    fun onBack(view: android.view.View) {
        finish()
    }

    fun onClear(view: android.view.View) {
        workingTV.text = ""
        resultsTV.text = mode.uppercase(Locale.getDefault())
    }

    fun onEquals(view: android.view.View) {
        resultsTV.text = ""
        val text = workingTV.text

        // Remove any trailing commas and white spaces
        if (text[text.lastIndex] == ' ') {
            trimTrailingWhiteSpaces()
        }

        // Convert string to Double Array
        val nrs = text.split(", ").toTypedArray().map { it.toDouble() }
        val size = nrs.size

        if (mode == "mean") {
            endText = "MEAN: "
            // Calculate Mean
            resultsTV.append(endText + ((nrs.sum() / size).toString()))
        } else if (mode == "median") {
            endText = "MEDIAN: "
            // Calculate Median (2 cases [odd or even])
            val median: Double = if (nrs.size % 2 != 0) {
                nrs[size / 2]
            } else {
                val first = nrs[(size / 2) - 1]
                val second = nrs[size / 2]
                (first + second) / 2
            }
            resultsTV.append(endText + median)
        }
    }

    fun onNumberClick(view: android.view.View) {
        if (view is Button)
            workingTV.append(view.text)
    }

    fun onAddClick(view: android.view.View) {
        if (!workingTV.text.endsWith(" "))
            workingTV.append(", ")
    }

    fun onBackSpaceClick(view: android.view.View) {
        trimTrailingWhiteSpaces(true)
    }

    private fun trimTrailingWhiteSpaces(removeLastChar: Boolean = false) {
        val text = workingTV.text
        var lastIndexOfNumericCharacter = -1
        for (i in text.indices) {
            if (text[i].isDigit()) {
                lastIndexOfNumericCharacter = i
            }
        }
        var addToIndex = 1
        if (removeLastChar)
            addToIndex = 0
        workingTV.text = text.substring(0, lastIndexOfNumericCharacter + addToIndex)
    }
}