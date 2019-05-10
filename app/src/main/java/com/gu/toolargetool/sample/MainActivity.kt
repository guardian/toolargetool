package com.gu.toolargetool.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.gu.toolargetool.TooLargeTool

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        TooLargeTool.startLogging(this.application)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString("test", "Hello world!")
    }
}
