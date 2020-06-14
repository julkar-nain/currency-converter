package com.julkar.nain.currencyconverter.main.vm

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.julkar.nain.currencyconverter.repository.ExchangeRatePersistentDataSource
import com.julkar.nain.currencyconverter.service.Communicator.Communicator
import com.julkar.nain.currencyconverter.service.scheduler.DataWorker
import com.julkar.nain.currencyconverter.util.WORK_NAME
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

class MainViewModel constructor(
    private val appContext: Context,
    communicator: Communicator,
    private val exchangeRatePersistentDataSource: ExchangeRatePersistentDataSource
) :
    ViewModel() {

    private val TAG = javaClass.toString()

    private val currencyRatesMap = MutableLiveData<Map<String, Double>>()
    private val compositeDisposable = CompositeDisposable()

    init {
        communicator.observe(Map::class.java).subscribe {
            try {
                val map = it as Map<String, Double>
                currencyRatesMap.postValue(map)
            } catch (e: Exception) {
                Log.v(TAG, e.toString())
            }
        }
        registerScheduler()
    }

    fun fetchCurrencyData(): LiveData<Map<String, Double>> {
        viewModelScope.launch(Dispatchers.IO) {
            val rates = exchangeRatePersistentDataSource.getExchangeRates()
            currencyRatesMap.postValue(rates.map { it.countryName to it.exchangeRate }.toMap())
        }

        return currencyRatesMap
    }

    fun getCurrencyRate(position: Int): String {
        return currencyRatesMap.value?.keys?.elementAt(position)!!
    }

    fun getExchangedAmount(unExchanged: Double, from: String, to: String): Double {
        val exchangedAmount =
            (1 / currencyRatesMap.value!![from]!!) * currencyRatesMap.value!![to]!! * unExchanged

        return exchangedAmount
    }

    fun getExchangeRates(): List<String> {
        return currencyRatesMap.value?.map { it.key + " : " + DecimalFormat("000.00").format(it.value) }
            ?.toList()!!
    }

    fun registerScheduler() {
        CoroutineScope(Dispatchers.IO).launch {
            setupRecurringWork()
        }
    }

    fun dispose() {
        compositeDisposable.dispose()
        WorkManager.getInstance(appContext).cancelUniqueWork(WORK_NAME)
    }

    private fun setupRecurringWork() {
        val repeatingRequest = PeriodicWorkRequestBuilder<DataWorker>(
            repeatInterval = 30,
            repeatIntervalTimeUnit = TimeUnit.SECONDS
        ).build()

        WorkManager.getInstance(appContext).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            repeatingRequest
        )
    }
}