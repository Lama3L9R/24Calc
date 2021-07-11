package cn.thelama.tfcalc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun findSolution(view: View) {
        val num1Field = findViewById<EditText>(R.id.number1)
        val num2Field = findViewById<EditText>(R.id.number2)
        val num3Field = findViewById<EditText>(R.id.number3)
        val num4Field = findViewById<EditText>(R.id.number4)
        val resultBox = findViewById<TextView>(R.id.resultBox)

        val nums = listOf(num1Field.text.toString().toInt(), num2Field.text.toString().toInt(), num3Field.text.toString().toInt(), num4Field.text.toString().toInt())
        if(nums.any { it > 13 || it < 1 }) {
            resultBox.text = "Illegal Number"
            return
        }
        val arrOut = mutableListOf<MutableList<Operation>>()

        possibles(arrOut, mutableListOf(), nums)
        val finalArr = mutableListOf<MutableList<Operation>>()
        filtrate(finalArr, arrOut)

        var pre: String = ""

        resultBox.text = "arr size: ${finalArr.size}\n${finalArr.joinToString(separator = "") { 
            val str = it.joinToString(separator = " ") { it.num.toString() }.replace("21", "+").replace("22", "-").replace("31", "*").replace("32", "/")
            if(pre != str) {
                pre = str
                str + "\n"
            } else {
                ""
            }
        }}"
    }

    fun possibles(arrOut: MutableList<MutableList<Operation>>, curr: MutableList<MutableList<Operation>>, nums: List<Int>, next: Int = 0) {
        if(next == nums.size) {
            arrOut.clear()
            arrOut.addAll(curr)
            for(i in arrOut.indices) {
                arrOut[i].removeAt(7)
            }
            return
        }

        if(curr.size == 0) {
            nums.forEach {
                curr += mutableListOf(toOp(it), Operation.PLUS)
                curr += mutableListOf(toOp(it), Operation.MINUS)
                curr += mutableListOf(toOp(it), Operation.TIMES)
                curr += mutableListOf(toOp(it), Operation.DIVIDED)
            }
            possibles(arrOut, curr, nums, next + 1)
        } else {
            val nextArr: MutableList<MutableList<Operation>> = mutableListOf()
            curr.forEach {
                val avaNums = nums.clone().toMutableList()
                it.forEach { op ->
                    avaNums.remove(op.num)
                }

                for(i in avaNums) {
                    nextArr += it.clone().apply {
                        add(toOp(i))
                        add(Operation.PLUS)
                    }

                    nextArr += it.clone().apply {
                        add(toOp(i))
                        add(Operation.MINUS)
                    }

                    nextArr += it.clone().apply {
                        add(toOp(i))
                        add(Operation.TIMES)
                    }

                    nextArr += it.clone().apply {
                        add(toOp(i))
                        add(Operation.DIVIDED)
                    }
                }
            }
            possibles(arrOut, nextArr, nums, next + 1)
        }
    }

    fun filtrate(arrOut: MutableList<MutableList<Operation>>, arrIn: MutableList<MutableList<Operation>>) {
        for(i in arrIn) {
            var num = i[0].num
            var index = 1 // 1 / 2 + 3
            while(index < i.size) {
                if(index % 2 != 0) {
                    val nextIndex = index + 1
                    when(i[index]) {
                        Operation.PLUS -> num += i[nextIndex].num
                        Operation.MINUS -> num -= i[nextIndex].num
                        Operation.TIMES -> num *= i[nextIndex].num
                        Operation.DIVIDED -> num /= i[nextIndex].num
                    }
                    index += 2
                }
            }
            if(num == 24) {
                arrOut += i
            }
        }
    }

    fun <T> List<T>.clone(): MutableList<T> {
        val list = mutableListOf<T>()
        this.forEach {
            list += it
        }
        return list
    }

    fun toOp(i: Int): Operation {
        return when(i) {
            1 -> Operation.ONE
            2 -> Operation.TWO
            3 -> Operation.THREE
            4 -> Operation.FOUR
            5 -> Operation.FIVE
            6 -> Operation.SIX
            7 -> Operation.SEVEN
            8 -> Operation.EIGHT
            9 -> Operation.NINE
            10 -> Operation.TEN
            11 -> Operation.ELEVEN
            12 -> Operation.TWELVE
            13 -> Operation.THIRTEEN
            else -> Operation.ONE
        }
    }

    enum class Operation(val num: Int) {
        ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7),
        EIGHT(8), NINE(9), TEN(10), ELEVEN(11), TWELVE(12), THIRTEEN(13),

        PLUS(21), MINUS(22), TIMES(31), DIVIDED(32)
    }
}