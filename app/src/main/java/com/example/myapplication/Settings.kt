package com.example.myapplication

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Settings : BaseActivity() {

    private lateinit var prefs: SharedPreferences

    private lateinit var nightModeToggle: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        if (intent.extras != null) {
            if (intent.extras!!.containsKey("anyKey")) {
                val anyKey = intent.getIntExtra("anyKey", 0)
                Toast.makeText(this, anyKey.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        findViewById<Button>(R.id.APIButton).setOnClickListener {
            testApi()
        }
        findViewById<Button>(R.id.APIResponseButton).setOnClickListener {
            testApiResponse()
        }
        findViewById<Button>(R.id.submitButton).setOnClickListener {
            confirmRequest()
        }
        prefs = this.getSharedPreferences(PREFS_FILE, 0)
        findViewById<Button>(R.id.save).setOnClickListener {
            save()
            exit()
        }

        findViewById<Button>(R.id.cancel).setOnClickListener { cancel() }
        nightModeToggle = findViewById(R.id.nightMode)
        nightModeToggle.setOnClickListener { toggleTime() }
        if (dNightMode == 2) {
            nightModeToggle.setImageDrawable(getDrawable(R.drawable.ic_morning))
        } else {
            nightModeToggle.setImageDrawable(getDrawable(R.drawable.ic_nighttime))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }

    private fun toggleTime() {
        if (dNightMode == 2) {
            dNightMode = 1
            save()
            recreate()
        } else {
            dNightMode = 2
            save()
            recreate()
        }
    }

    private fun save() {
        prefs.edit().putInt(PREFS_NIGHT_MODE, dNightMode).apply()
    }

    private fun exit() {
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun cancel() {
        val intent = Intent()
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }

    private fun testApi() {
        val retIn = RetrofitInstance.getRetrofitInstance("http://192.168.180.107/")
            .create(RetrofitInterface::class.java)
        retIn.testAPI().enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                connFail()
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                connSuccess(response)
            }

        })
//        retIn.testAPI().enqueue(object:Callback<Void>{
//            override fun onFailure
//        })
    }

    private fun connFail() {
        Toast.makeText(this, "No response from the database", Toast.LENGTH_LONG).show()
    }

    private fun connSuccess(response: Response<Void>) {
        Toast.makeText(this, "Response code " + response.code(), Toast.LENGTH_LONG).show()
        longVibrationPattern()
    }

    private fun testApiResponse() {
        val retIn = RetrofitInstance.getRetrofitInstance("http://192.168.180.107/")
            .create(RetrofitInterface::class.java)
        retIn.testAPIResponse().enqueue(object : Callback<TestResponse> {
            override fun onFailure(call: Call<TestResponse>, t: Throwable) {
                connFail()
            }

            override fun onResponse(call: Call<TestResponse>, response: Response<TestResponse>) {

                val thing: TestResponse = response.body()!!

                val message = thing.getMessage()
                val day = thing.getDay()
                testDayAndMessage(message, day)
                longVibrationPattern()


            }
        })
    }

    private fun testDayAndMessage(message: String?, day: String?) {
        Toast.makeText(this, "Returned data: $message, $day", Toast.LENGTH_LONG).show()
    }

    private fun submitInputNumber() {
        val retIn = RetrofitInstance.getRetrofitInstance("http://192.168.180.107/")
            .create(RetrofitInterface::class.java)
        val number: RequestBody = findViewById<EditText>(R.id.editTextNumber).text.toString()
            .toRequestBody("text/plain".toMediaTypeOrNull())
        retIn.submitNumberAPI(number).enqueue(object : Callback<TestResponse> {
            override fun onFailure(call: Call<TestResponse>, t: Throwable) {
                connFail()
            }

            override fun onResponse(call: Call<TestResponse>, response: Response<TestResponse>) {
                val thing: TestResponse = response.body()!!

                val num = thing.getNumber()
                passNumber(num)
            }
        })
    }

    private fun confirmRequest() {
        val builder = AlertDialog.Builder(this)
        with(builder) {
            setTitle("Confirm Number")
            setMessage("Are you sure this is the number you want to submit?")
            setPositiveButton("Confirm") { _, _ ->
                submitInputNumber()
            }
            setNegativeButton("Cancel", null)
            show()
        }
    }

    private fun passNumber(number: String?) {
        Toast.makeText(this, number, Toast.LENGTH_LONG).show()
    }
}