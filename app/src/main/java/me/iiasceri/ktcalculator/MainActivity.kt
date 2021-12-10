package me.iiasceri.ktcalculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity() {

    private var canAddOperation = false
    private var canAddDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // Any numeric symbols [1-9] and ['-', '.'] are accepted
    fun onNumberClick(view: android.view.View) {
        if (view is Button) {
            if (view.text == ".") {
                if (canAddDecimal) {
                    workingTV.append(view.text)
                    canAddOperation = false
                }
                canAddDecimal = false
            } else {
                workingTV.append(view.text)
                canAddOperation = true
            }
        }
    }

    // Pressed when user wants to get the result
    fun onOperationClick(view: android.view.View) {
        if (view is Button && canAddOperation) {
            workingTV.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }

    // Just wipes TVs
    fun onClearClick(view: android.view.View) {
        workingTV.text = ""
        resultsTV.text = ""
    }

    // Remove last character
    fun onBackSpaceClick(view: android.view.View) {
        val length = workingTV.length()
        if (length > 0)
            workingTV.text = workingTV.text.subSequence(0, length - 1)
    }

    fun onEquals(view: android.view.View) {
        val digitsOperators = digitsOperators()
        if (digitsOperators.isEmpty()) {
            resultsTV.text = ""
            return
        }

        val timesDivision = timesDivisionCalculate(digitsOperators)
        if (timesDivision.isEmpty()) {
            resultsTV.text = ""
            return
        }

        val result = addSubCalculate(timesDivision)
        resultsTV.text = result.toString()
    }

    private fun addSubCalculate(passedList: MutableList<Any>): Double {
        var result = passedList[0] as Double
        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex) {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Double
                if (operator == '+')
                    result += nextDigit
                if (operator == '-')
                    result -= nextDigit
            }
        }
        return result
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {

        var list = passedList
        while (list.contains('x') || list.contains('/')) {
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()

        var restartIndex = passedList.size

        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex) {
                val operator = passedList[i]
                val previousDigit = passedList[i - 1] as Double
                val nextDigit = passedList[i + 1] as Double
                when (operator) {
                    'x' -> {
                        newList.add(previousDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' -> {
                        newList.add(previousDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else -> {
                        newList.add(previousDigit)
                        newList.add(operator)
                    }
                }
            }
            if (i > restartIndex)
                newList.add(passedList[i])
        }
        return newList
    }

    private fun digitsOperators(): MutableList<Any> {
        val list = mutableListOf<Any>()
        try {
            var currentDigit = ""
            for (character in workingTV.text) {
                if (character.isDigit() || character == '.') {
                    currentDigit += character
                } else {
                    list.add(currentDigit.toDouble())
                    currentDigit = ""
                    list.add(character)
                }
            }

            if (currentDigit != "")
                list.add(currentDigit.toDouble())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    // Go to second screen
    fun onMeanMedianClick(view: android.view.View) {
        var mode = ""
        if (view is Button)
            mode = view.text.toString().lowercase(Locale.getDefault())
        val intent = Intent(this, MeanMedianActivity::class.java)
        intent.putExtra("mode", mode)
        startActivity(intent)
    }
}