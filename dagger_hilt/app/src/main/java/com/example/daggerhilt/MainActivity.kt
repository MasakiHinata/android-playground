package com.example.daggerhilt

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.daggerhilt.assistedinject.AssistedInjectViewModel
import com.example.daggerhilt.assistedinject.viewModelProviderFactoryOf
import com.example.daggerhilt.calculator.Calculator
import com.example.daggerhilt.car.Car
import com.example.daggerhilt.database.DatabaseInterface
import com.example.daggerhilt.fruits.FruitsApplication
import com.example.daggerhilt.logger.MyLogger
import com.example.daggerhilt.phone.Phone
import com.example.daggerhilt.viewmodel.MainActivityViewModel
import com.example.daggerhilt.workmanager.GreetWorker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    // コンストラクタインジェクション
    @Inject
    lateinit var calculator: Calculator

    // @Bindsを使用した注入
    @Inject
    lateinit var database: DatabaseInterface

    // @Providesを使用した注入
    @Inject
    lateinit var car: Car

    // アノテーションを利用して区別
    @Inject
    lateinit var phone: Phone

    // 事前定義された修飾子を利用
    @Inject
    lateinit var logger: MyLogger

    private val viewModel: MainActivityViewModel by viewModels()

    // Assisted Injectを利用したViewModelのFactory
    @Inject
    lateinit var factory: AssistedInjectViewModel.Factory

    private val assistedInjectViewModel: AssistedInjectViewModel by viewModels {
        viewModelProviderFactoryOf { factory.create("Alice") }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        Toast.makeText(this, "watch log!", Toast.LENGTH_LONG).show()
        Log.d(TAG, "calculator: 10+1=${calculator.add(1, 10)}")
        Log.d(TAG, "database: ${database.loadMessage()}")
        Log.d(TAG, "car: ${car.drive()}")
        Log.d(TAG, "phone: ${phone.batteryLevel()}")
        Log.d(TAG, "calculator(view model): 10+1=${viewModel.add(1, 10)}")
        logger.log("Hello")
        Log.d(TAG, "assisted inject view model: ${assistedInjectViewModel.userName}")

        val workManager = WorkManager.getInstance(this)
        val request = OneTimeWorkRequestBuilder<GreetWorker>().build()
        workManager.enqueue(request)

        val fruitsApplication = FruitsApplication()
        fruitsApplication.showFruits(applicationContext)
    }
}