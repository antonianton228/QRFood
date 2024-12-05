package com.example.qrfood

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import androidx.core.graphics.blue
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.qrfood.databinding.SettingsBinding
import com.example.samsungfouth.MainActivityViewModel
import com.google.gson.annotations.SerializedName
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import com.google.gson.Gson
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaType

interface RequestCallback {
    fun onSuccess(response: String)
    fun onFailure(error: String)
}

class ApiService(){
    private val client = OkHttpClient()
    fun makeRequest(callback: RequestCallback, url: String, request_body:String) {
        //val jsonRequest = "your request body"
        val JSON = "application/json; charset=utf-8".toMediaType()
        val body = RequestBody.create(JSON, request_body)
        val request = Request.Builder().url(url).post(body).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(e.toString());
            }
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        throw Exception("Запрос к серверу не был успешен:" +
                                " ${response.code} ${response.message}")
                    }
                    callback.onSuccess(response.body!!.string())
                }
            }
        })
    }
}

object Api {
    val someService : ApiService by lazy {
        ApiService()
    }
}

class HTTPRequester {
    val gson = Gson()
    fun sendGetRequest(alertDialog: AlertDialog, context: Context, observer: Barcode_getter, viewModel: MainActivityViewModel, recycleOfToday: RecyclerView, barcode: String){




        val url = "https://functions.yandexcloud.net/d4e6hh24crdbe9lbk44l/?type=0&barcode=$barcode"

        // Убедимся, что запрос отправляется как JSON
        val jsonMediaType = "application/json; charset=utf-8".toMediaType()

        // Преобразуем строку в тело запроса

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        // Используем OkHttpClient для отправки запроса
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Response", "Error: ${e.message}")
                (context as? Activity)?.runOnUiThread {
                    alertDialog.dismiss()
                    observer.error_loading(viewModel, recycleOfToday)

                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        Log.d("Response", "Unexpected code $response")
                    } else {
                        val responseBody = response.body?.string()
                        if (responseBody != null) {
                            Log.d("Response", response.code.toString())
                            if  (response.code == 202){
                                (context as? Activity)?.runOnUiThread {
                                    alertDialog.dismiss()
                                    observer.no_barcode(viewModel, recycleOfToday, barcode)

                                }
                            }
                            else{
                                val result: Barcode = gson.fromJson(responseBody, Barcode::class.java)
                                (context as? Activity)?.runOnUiThread {
                                    alertDialog.dismiss()
                                    observer.stop_loading(result, viewModel, recycleOfToday)

                                }
                            }



//                            alertDialog.dismiss()
//                            Barcode_getter(context).stop_loading(result)

                        }
                        else{

                        }
                    }
                }
            }
        })
    }

    fun AddBarcode(context: Context, barcode: Barcode){
        val url = "https://functions.yandexcloud.net/d4e6hh24crdbe9lbk44l/?type=1&barcode=${barcode.barcode}&name=${barcode.name}&p1=${barcode.shugar}&p2=${barcode.fats}&p3=${barcode.squirrels}&p4=${barcode.carbohydrates}"
        Log.d("add_bar", url)
        // Убедимся, что запрос отправляется как JSON

        // Преобразуем строку в тело запроса

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        // Используем OkHttpClient для отправки запроса
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Response", "Error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        Log.d("Response", "Unexpected code $response")
                    } else {
                        val responseBody = response.body?.string()
                        if (responseBody != null) {
                            Log.d("Response", response.code.toString())




//                            alertDialog.dismiss()
//                            Barcode_getter(context).stop_loading(result)

                        }
                        else{

                        }
                    }
                }
            }
        })
    }


}
//
//data class Barcode_getter(
//    @SerializedName("barcode") val barcode: Int,
//    @SerializedName("name") val name: String
//    )
data class Barcode(
        @SerializedName("barcode") val barcode: String,
        @SerializedName("name") val name: String,
        @SerializedName("shugar") val shugar: Int,
        @SerializedName("fats") val fats: Int,
        @SerializedName("squirrels") val squirrels: Int,
        @SerializedName("carbohydrates") val carbohydrates: Int,

)
