package com.example.myapplication

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : BaseActivity() {

//    private var btns:MutableList<Button>=mutableListOf<Button>()

    private var numberArray = ArrayList<String>()
    private var opArray = ArrayList<String>()

    private lateinit var numberHolder: TextView

    private var equalsPressed = false

    private var opPressed = false

    private var input = "0"


    //  set of variables

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        btns.add(findViewById(R.id.btn1))

        numberHolder = findViewById(R.id.numberHolder)

        findViewById<TextView>(R.id.btn1).setOnClickListener{logNumber("1")}
        findViewById<TextView>(R.id.btn2).setOnClickListener{logNumber("2")}
        findViewById<TextView>(R.id.btn3).setOnClickListener{logNumber("3")}
        findViewById<TextView>(R.id.btn4).setOnClickListener{logNumber("4")}
        findViewById<TextView>(R.id.btn5).setOnClickListener{logNumber("5")}
        findViewById<TextView>(R.id.btn6).setOnClickListener{logNumber("6")}
        findViewById<TextView>(R.id.btn7).setOnClickListener{logNumber("7")}
        findViewById<TextView>(R.id.btn8).setOnClickListener{logNumber("8")}
        findViewById<TextView>(R.id.btn9).setOnClickListener{logNumber("9")}
        findViewById<TextView>(R.id.btn0).setOnClickListener{logNumber("0")}

        findViewById<TextView>(R.id.clear).setOnClickListener{reset()}

        findViewById<TextView>(R.id.add).setOnClickListener{handleOperators("+")}
        findViewById<TextView>(R.id.minus).setOnClickListener{handleOperators("-")}
        findViewById<TextView>(R.id.equalsBtn).setOnClickListener{handleEquals()}
        findViewById<TextView>(R.id.multiply).setOnClickListener{handleOperators("x")}
        findViewById<TextView>(R.id.divide).setOnClickListener{handleOperators("/")}

        findViewById<TextView>(R.id.decimal).setOnClickListener{logNumber(".")}

        findViewById<Button>(R.id.settings).setOnClickListener{goToSettings()}
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            INTENT_SETTINGS -> {
                recreate()
            }
        }
    }

    private fun goToSettings() {
        val intent = Intent(this, Settings::class.java)
        startActivityForResult(intent, INTENT_SETTINGS)
    }

    //  applies screen clicks to a numerical or operatic string

    private fun logNumber(number:String){
        if (equalsPressed) {
            numberArray.clear()
            opArray.clear()
        }
        if (input == "0") input = ""
        Log.d("numberOutput",number.toString())
        input += number.toString()
        numberHolder.text = input
        equalsPressed = false
        opPressed = false
    }

    private fun finishSum(){

    }
    private fun handleOperators(operator:String) {
        if (equalsPressed) {
            opArray.add(operator)
        } else {
            if (opPressed){
                if (numberArray.size == 0) return
                opArray[opArray.lastIndex] = operator
            } else {
                numberArray.add(input)
                opArray.add(operator)
            }
        }
        equalsPressed = false
        input = "0"
        output()
        opPressed = true

    }
    private fun handleEquals(){
        if (numberArray.size == 0) return
        numberArray.add(input)
        var total = numberArray[0].toDouble()
        for (i in 1 ..numberArray.size - 1){
            Log.d("NUMBER IN LOOP", i.toString())
            Log.d("VALUE IN LOOP", numberArray[i])
            Log.d("LAST OP IN LOOP", opArray[i - 1])
            if (numberArray.size > 0 && opArray.size == 0){
                total = numberArray[i].toDouble()
            } else {
                when (opArray[i - 1]) {
                    "+" -> total += numberArray[i].toDouble()
                    "-" -> total -= numberArray[i].toDouble()
                    "/" -> total /= numberArray[i].toDouble()
                    "x" -> total *= numberArray[i].toDouble()
                else -> {
                    Toast.makeText(this, "Something has gone wrong", Toast.LENGTH_SHORT).show()
                    numberArray.clear()
                    opArray.clear()
                    equalsPressed = false
                    opPressed = false

                }
                }

//                if (opArray[i - 1] == "+"){
//                    total += numberArray[i].toDouble()
//                }else if (opArray[i - 1] == "-"){
//                    total -= numberArray[i].toDouble()
//                }else if (opArray[i - 1] == "/"){
//                    total /= numberArray[i].toDouble()
//                }else if (opArray[i - 1] == "x"){
//                    total *= numberArray[i].toDouble()
//                }
            }

        }

//        total.toString()
//        if (total.takeLast(2) = ".0")
        numberHolder.text = total.toString()
        numberArray.clear()
        numberArray.add(total.toString())
        opArray.clear()
        input = "0"
        output()
        equalsPressed = true
        opPressed = false

    }

    private fun reset() {
        numberArray.clear()
        opArray.clear()
        numberHolder.text = ""
        input = "0"
        output()
    }

    private fun output(){
        Log.d("TAG",numberArray.toString())
        Log.d("TAG",opArray.toString())
    }

companion object{
    private const val INTENT_SETTINGS = 5
}

}