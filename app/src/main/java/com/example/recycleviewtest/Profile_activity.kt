package com.example.recycleviewtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.core.widget.TextViewCompat

class Profile_activity : AppCompatActivity() {
    companion object{
        const val TOTAL_COUNT = "total_count"
    }

    private lateinit var textView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        textView = findViewById(R.id.repositoriesUrlTxt)

        val html_url:String? = intent.getStringExtra(TOTAL_COUNT)

        val userRepositories: String = html_url + "?tab=repositories"

        textView.setText(userRepositories)
    }
}