package com.example.discoveryicetask

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Details : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val userName : TextView = findViewById(R.id.txtName)
        val userSurname : TextView = findViewById(R.id.txtSurname)
        val planAmount : TextView = findViewById(R.id.txtAmount)

        val searchResults = intent.getStringExtra("searchResults")


        if (!searchResults.isNullOrBlank()) {
            val lines = searchResults.split("\n")
            if (lines.size >= 3) {
                val name = lines[0].substringAfter(":").trim()
                val surname = lines[1].substringAfter(":").trim()
                val amount = lines[2].substringAfter(":").trim()

                userName.text = name
                userSurname.text = surname
                planAmount.text = amount
            }
        }
        val backButton : Button = findViewById(R.id.btnBack)
        backButton.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}