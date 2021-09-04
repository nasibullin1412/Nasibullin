package com.homework.nasibullintinkoff.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.homework.nasibullintinkoff.App
import com.homework.nasibullintinkoff.R
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.request.target.Target


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val imageView = findViewById<ImageView>(R.id.imgGif)
        Glide.with(this).asGif().load("https://static.devli.ru/public/images/gifs/202105/338eec95-f956-4aa6-8844-219166979cc2.gif").into(imageView)
    }
}