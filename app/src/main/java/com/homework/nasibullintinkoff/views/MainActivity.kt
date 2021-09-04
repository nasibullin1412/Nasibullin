package com.homework.nasibullintinkoff.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.homework.nasibullintinkoff.R
import com.homework.nasibullintinkoff.viewmodel.MainViewModel
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.homework.nasibullintinkoff.App
import com.homework.nasibullintinkoff.data.PostDto
import com.homework.nasibullintinkoff.utils.Resource
import com.homework.nasibullintinkoff.utils.Utility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var imgGif: ImageView
    private lateinit var tvDescription: TextView
    private val viewModel: MainViewModel by viewModels()

    companion object{
        private const val ERROR_MESSAGE = "Error get data"
        private const val FAILED_MESSAGE = "Fail get data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init(){
        initView()
        setupObserver()
        viewModel.doGetRemoteData()
    }

    private fun initView(){
        imgGif = findViewById(R.id.imgGif)
        tvDescription = findViewById(R.id.tvDescription)
    }

    private fun setupObserver(){
        viewModel.postDto.observe(
            this, {
                when(it.status){
                    Resource.Status.SUCCESS -> {
                        if (it.data != null) {
                            successGetData(it.data)
                        } else{
                            Utility.showToast(FAILED_MESSAGE, App.appContext)
                        }
                    }
                    Resource.Status.ERROR -> {
                        Utility.showToast("$ERROR_MESSAGE: ${it.message}", App.appContext)
                    }
                    Resource.Status.FAILURE -> {
                        Utility.showToast("$FAILED_MESSAGE: ${it.message}", App.appContext)
                    }
                }
            }
        )
    }

    private fun successGetData(postDto: PostDto){
        Glide.with(this).asGif().load(postDto.urlGif.toString()).into(imgGif)
        tvDescription.text = postDto.description
    }
}