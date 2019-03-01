package com.samwoodall.mvvmlivedatarx.PureRx

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.samwoodall.mvvmlivedatarx.LiveDataRx.MainViewModelData
import com.samwoodall.mvvmlivedatarx.R
import kotlinx.android.synthetic.main.activity_main.*

class PureRxActivity : AppCompatActivity() {

    private lateinit var viewModel: PureRxViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(PureRxViewModel::class.java)
        lifecycle.addObserver(viewModel)

//        viewModel.getMainViewModel()
    }

    private fun loading() {}
    private fun complete(complete: MainViewModelData.Complete) {
        hello.text = complete.userDesc
    }
}