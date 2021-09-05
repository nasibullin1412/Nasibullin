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
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.tabs.TabLayout
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
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private val viewModel: MainViewModel by viewModels()

    companion object{
        private const val ERROR_MESSAGE = "Error get data"
        private const val FAILED_MESSAGE = "Fail get data"
        private const val EMPTY_DATA = "Нет данных"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init(){
        initView()
        setupButtons()
        setupTabLayout()
        setupObservers()
        viewModel.doGetLocalData()
    }

    /**
     * setup buttons listeners
     */
    private fun setupButtons() {
        btnBack.setOnClickListener {
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
        findViewById<TextView>(R.id.tvRepeat).setOnClickListener {
            viewModel.doGetLocalData()
        }
    }
    private fun previousPost() {
        if (viewModel.decreaseCurrentPostIndex().not()) {
            return
        }
        viewModel.doGetLocalData()
    }

    /**
     * setup tab layout listener
     */
    private fun setupTabLayout(){
        findViewById<TabLayout>(R.id.tabLayout).addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewModel.changeCategory(tab.position)
                viewModel.doGetLocalData()
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {
                return
            }
            override fun onTabReselected(tab: TabLayout.Tab) {
                return
            }
        })
    }

    private fun initView(){
        imgGif = findViewById(R.id.imgGif)
        tvDescription = findViewById(R.id.tvDescription)
        nsvNormalConnection = findViewById(R.id.nsvNormalConnection)
        nsvErrorConnection = findViewById(R.id.nsvErrorConnection)
        btnBack = findViewById(R.id.btnBack)
        shimmerFrameLayout = findViewById(R.id.sflPost)
    }

    /**
     * setup LiveData observers
     */
    private fun setupObservers(){
        setupShimmerObserver()
        setupBackButtonObserver()
        setupDataObserver()
    }

    /**
     * setup shimmer observer.
     * Change visibility of layouts after signaling
     */
    private fun setupShimmerObserver(){
        viewModel.signalShimmer.observe(
            this, {
                if (nsvErrorConnection.visibility != View.VISIBLE) {
                    if (it) {
                        nsvNormalConnection.visibility = View.GONE
                        shimmerFrameLayout.visibility = View.VISIBLE
                        shimmerFrameLayout.startShimmer()
                    } else {
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE
                        nsvNormalConnection.visibility = View.VISIBLE
                    }
                }
            }
        )
    }

    /**
     * setup back button observer.
     * Change visibility of button after signaling
     */
    private fun setupBackButtonObserver(){
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
    }

    /**
     * setup data update observer.
     * View new output data after updating
     */
    private fun setupDataObserver(){
        viewModel.postDto.observe(
            this, {
                when(it.status){
                    Resource.Status.SUCCESS -> {
                        if (!it.data.isNullOrEmpty()) {
                            successGetData(it.data)
                        } else{
                            unsuccessfulGetData(EMPTY_DATA)
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

    /**
     * handle unsuccessful get data
     * @param message is string with the reason for the unsuccessful
     */
    private fun unsuccessfulGetData(message:String){
        if (viewModel.isFromLocal){
            viewModel.doGetRemoteData()
        }
        else{
            setErrorView(message)
        }
    }

    /**
     * setup error view
     * @param message is string with the reason for the error
     */
    private fun setErrorView(message: String){
        Utility.showToast(message, App.appContext)
        if (message == EMPTY_DATA){
            findViewById<TextView>(R.id.tvErrorMessage)
                .text = EMPTY_DATA
        }
        else{
            findViewById<TextView>(R.id.tvErrorMessage)
                .text = resources.getText(R.string.error_message)
        }
        nsvNormalConnection.visibility = View.GONE
        shimmerFrameLayout.visibility = View.GONE
        nsvErrorConnection.visibility = View.VISIBLE

    }

    /**
     * setup view after success get new output data
     * @param postDtoList is list with new data
     */
    private fun successGetData(postDtoList: List<PostDto>){
        if (nsvErrorConnection.visibility == View.VISIBLE) {
            nsvNormalConnection.visibility = View.VISIBLE
            nsvErrorConnection.visibility = View.GONE
        }
        Glide.with(this).asGif().load(postDtoList[0].urlGif.toString()).into(imgGif)
        "${postDtoList[0].author}: ${postDtoList[0].description}".also { tvDescription.text = it }
        if (viewModel.isFromLocal.not()){
            viewModel.doInsertDatabase(postDtoList)
        }
    }
}