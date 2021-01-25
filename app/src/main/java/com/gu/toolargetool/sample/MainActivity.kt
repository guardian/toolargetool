package com.gu.toolargetool.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.gu.toolargetool.TooLargeTool

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.container, TestFragment())
                    .commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("MainActivity.test", "MainActivity put this string here.")
    }
}
