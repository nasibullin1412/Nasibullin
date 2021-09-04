package com.homework.nasibullintinkoff.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.homework.nasibullintinkoff.R
import com.homework.nasibullintinkoff.viewmodel.MainViewModel
import androidx.activity.viewModels
import androidx.core.widget.NestedScrollView
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
    private lateinit var nsvNormalConnection: NestedScrollView
    private lateinit var nsvErrorConnection: NestedScrollView
    private lateinit var btnBack: Button
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
        setupButtons()
        setupObserver()
        viewModel.doGetLocalData()
    }

    private fun setupButtons(){
        btnBack.setOnClickListener{
            previousPost()
        }
        findViewById<Button>(R.id.btnNext).setOnClickListener {
            viewModel.increaseCurrentPostIndex()
            viewModel.doGetLocalData()
        }
        findViewById<Button>(R.id.btnReset).setOnClickListener {
            viewModel.doDeleteAllCache()
            viewModel.doGetLocalData()
        }
    }

    private fun previousPost(){
        if (viewModel.decreaseCurrentPostIndex().not()){
            return
        }
        viewModel.doGetLocalData()
    }

    private fun initView(){
        imgGif = findViewById(R.id.imgGif)
        tvDescription = findViewById(R.id.tvDescription)
        nsvNormalConnection = findViewById(R.id.nsvNormalConnection)
        nsvErrorConnection = findViewById(R.id.nsvErrorConnection)
        btnBack = findViewById(R.id.btnBack)
    }

    private fun setupObserver(){
        viewModel.signalBackButton.observe(
            this, {
                if (it){
                    btnBack.visibility = View.VISIBLE
                }
                else{
                    btnBack.visibility = View.GONE
                }
            }
        )
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
                        unsuccessfulGetData("$ERROR_MESSAGE: ${it.message}")
                    }
                    Resource.Status.FAILURE -> {
                        unsuccessfulGetData("$FAILED_MESSAGE: ${it.message}")
                    }
                }
            }
        )
    }

    private fun unsuccessfulGetData(message:String){
        if (viewModel.isFromLocal){
            viewModel.doGetRemoteData()
        }
        else{
            setErrorView(message)
        }
    }

    private fun setErrorView(message: String){
        Utility.showToast(message, App.appContext)
        nsvNormalConnection.visibility = View.GONE
        nsvErrorConnection.visibility = View.VISIBLE
    }

    private fun successGetData(postDto: PostDto){
        nsvNormalConnection.visibility = View.VISIBLE
        nsvErrorConnection.visibility = View.GONE
        Glide.with(this).asGif().load(postDto.urlGif.toString()).into(imgGif)
        "${postDto.author}: ${postDto.description}".also { tvDescription.text = it }
        if (viewModel.isFromLocal.not()){
            viewModel.doInsertDatabase(postDto)
        }
    }
}