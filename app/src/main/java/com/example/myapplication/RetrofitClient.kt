package com.example.myapplication

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.http.Headers
import java.util.*
import kotlin.collections.ArrayList

interface RetrofitInterface {
    @Headers("Content-Type:application/json; charset=utf-8")
    @POST("test.php")
    fun testAPI(): Call<Void>

    @Headers("Content-Type:application/json; charset=utf-8")
    @POST("test.php")
    fun testAPIResponse(): Call<TestResponse>

    @Multipart
    @POST("test.php")
    fun submitNumberAPI(@Part("number") value:RequestBody): Call<TestResponse>
}

class TestResponse {
    @Expose
    @SerializedName("message")
    private var message:String? = null
    fun getMessage():String? {
        return message
    }
    @Expose
    @SerializedName("day")
    private var day:String? = null
    fun getDay():String?{
        return day
    }
    @Expose
    @SerializedName("number")
    private var number:String? = null
    fun getNumber():String?{
        return number
    }
}

class RetrofitInstance {
    companion object {
        private const val SHOW_LISTENER = true
        private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
        private val authHead = Interceptor { chain ->
            val request: Request = chain.request().newBuilder()
                .addHeader("Authorization", "abc123")
                .build()
            chain.proceed(request)
        }
        private val client: OkHttpClient = OkHttpClient.Builder().apply {
            if (SHOW_LISTENER) {
                this.addInterceptor(interceptor)
            }
            this.addInterceptor(authHead)
        }.build()
        fun getRetrofitInstance(url: String): Retrofit {
            return Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}