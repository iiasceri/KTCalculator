package me.iiasceri.ktcalculator

import android.annotation.SuppressLint
import android.util.Log
import java.lang.Exception

object Something {
    fun returnSumOutOfArray(array: ArrayList<Int>): Long {
//        array.reduce { acc, i -> acc += array[i] }.filter.map.

        var sum = 0L
        array.let {
            for (item in it) {
                item.let { sum += item }
            }
        }
        return sum
    }

    @SuppressLint("LongLogTag")
    fun returnMaxOfGivenArray(array: ArrayList<Int>): Int {
        if (array.isNotEmpty()) {
            var max = array[0]
            for (item in array) {
                if (item > max)
                    max = item
            }
            return max
        } else {
            Log.d("you provided empty array", "")
            throw Exception("asd")
        }
    }

    private fun getMaxFromArray(array: ArrayList<Int>, currentIndex: Int, max: Int): Int {
        if (currentIndex == array.size-1) {
            return max
        }
        return if (array[currentIndex] > max)
            getMaxFromArray(array, currentIndex+1, array[currentIndex])
        else
            getMaxFromArray(array, currentIndex+1, max)
    }

    fun getM(array: ArrayList<Int>) {
        getMaxFromArray(array, 0, 0)
    }
}

fun main() {
    val arr = ArrayList<Int>()
    arr.add(23)
    arr.add(3)
    arr.add(2)
    arr.add(-3)
    arr.add(-12)
    println(Something.getM(arr))
}