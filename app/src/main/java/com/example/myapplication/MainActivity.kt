package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

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

        findViewById<TextView>(R.id.btn1).setOnClickListener{numPadInput("1")}
        findViewById<TextView>(R.id.btn2).setOnClickListener{numPadInput("2")}
        findViewById<TextView>(R.id.btn3).setOnClickListener{numPadInput("3")}
        findViewById<TextView>(R.id.btn4).setOnClickListener{numPadInput("4")}
        findViewById<TextView>(R.id.btn5).setOnClickListener{numPadInput("5")}
        findViewById<TextView>(R.id.btn6).setOnClickListener{numPadInput("6")}
        findViewById<TextView>(R.id.btn7).setOnClickListener{numPadInput("7")}
        findViewById<TextView>(R.id.btn8).setOnClickListener{numPadInput("8")}
        findViewById<TextView>(R.id.btn9).setOnClickListener{numPadInput("9")}
        findViewById<TextView>(R.id.btn0).setOnClickListener{numPadInput("0")}

        findViewById<TextView>(R.id.clear).setOnClickListener{reset()}

        findViewById<TextView>(R.id.add).setOnClickListener{handleOperators("+")}
        findViewById<TextView>(R.id.minus).setOnClickListener{handleOperators("-")}
        findViewById<TextView>(R.id.equalsBtn).setOnClickListener{handleEquals()}
        findViewById<TextView>(R.id.multiply).setOnClickListener{handleOperators("x")}
        findViewById<TextView>(R.id.divide).setOnClickListener{handleOperators("/")}

        findViewById<TextView>(R.id.decimal).setOnClickListener{numPadInput(".")}

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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            CAM_PERM_REQUEST -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goToSettings()
                } else {
                    Toast.makeText(this, "Without permission you cannot access this function", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun goToSettings() {

        if (canUseCam()) {

            val intent = Intent(this, Settings::class.java)
            intent.putExtra("anyKey", 9)
            startActivityForResult(intent, INTENT_SETTINGS)

        } else {

            requestPerm()

        }

    }

    private fun canUseCam():Boolean {

        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        return permission == PackageManager.PERMISSION_GRANTED

    }

    private fun requestPerm() {

        requestPermissions(arrayOf(Manifest.permission.CAMERA), CAM_PERM_REQUEST)

    }

    //  applies screen clicks to a numerical or operatic string

    private fun numPadInput(inp:String) {

        onClickVibration()

        if (equalsPressed) {
            clearArrays()
        }

        if (input == "0") input = ""

        Log.d("numberOutput",inp)

        input += inp
        numberHolder.text = input
        equalsPressed = false
        opPressed = false

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
                    clearArrays()
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
        clearArrays()
        numberArray.add(total.toString())
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

    private fun clearArrays() {
        numberArray.clear()
        opArray.clear()
    }

companion object{
    private const val INTENT_SETTINGS = 5
    private const val CAM_PERM_REQUEST = 7
}

}