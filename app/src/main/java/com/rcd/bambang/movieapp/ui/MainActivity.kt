package com.rcd.bambang.movieapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.rcd.bambang.movieapp.R
import com.rcd.bambang.movieapp.ui.movie_list.MovieListActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@MainActivity, MovieListActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}