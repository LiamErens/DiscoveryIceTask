package com.example.discoveryicetask

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telecom.Call
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson
import java.net.URL
import java.util.concurrent.Executors

data class PostUser(var ID: String, var Amount:String, var PracticeID:String)

data class Received(var received_ID: String, var received_Amount:String, var received_PracticeID:String, var message:String)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Read()
        Post()
    }
    fun Read()
    {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute{
            val url = URL("https://opsc.azurewebsites.net/Dis.php")
            val json = url.readText()
            val planList = Gson().fromJson(json, Array<Plan>::class.java).toList()

            Handler(Looper.getMainLooper()).post{
                Log.d("AddNewPlan", "Plain Json Vars: $json")
                Log.d("AddNewPlan", "Converted Json: $planList")
            }
            var getData : Button = findViewById(R.id.btnSearch)
            getData.setOnClickListener() {
                val id: EditText = findViewById(R.id.planId)
                val userId = id.text.toString().toInt()

                val plans = planList.filter { it.PlanID == userId }

                if (plans.isNotEmpty()) {
                    val resultText = StringBuilder()
                    for (data in plans) {
                        resultText.append("Name: ${data.Name}\n")
                        resultText.append("Surname: ${data.Surname}\n")
                        resultText.append("Amount: R${data.Amount}\n\n")
                    }
                    val intent = Intent(this, Call.Details::class.java)
                    intent.putExtra("searchResults", resultText.toString())
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, "Plan not found", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    fun Post()
    {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute{
            val user = PostUser("15", "20000", "123456789")

            val (_,_,result) = "https://opsc.azurewebsites.net/Add.php".httpPost()
                .jsonBody(Gson().toJson(user).toString())
                .responseString()
            val Json = "["+ result.component1() +"]"
            val userList = Gson().fromJson(Json, Array<Received>::class.java).toList()

            Handler(Looper.getMainLooper()).post{
                var Text = findViewById<TextView>(R.id.txtOutput)
                Text.setText(userList.toString())
            }
        }
    }
}