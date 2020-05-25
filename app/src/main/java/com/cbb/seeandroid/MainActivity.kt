package com.cbb.seeandroid

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.cbb.seeandroid.aop.AopTest1Activity
import com.cbb.seeandroid.aop.LogTrace

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun aop(view: View) {
        startActivity(Intent(this@MainActivity,AopTest1Activity::class.java))
    }




}
