package com.julkar.nain.currencyconverter.main.vm

import android.content.Context
import android.text.TextUtils.isEmpty
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
import com.julkar.nain.currencyconverter.util.Action
import com.julkar.nain.currencyconverter.util.WORK_NAME
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

class MainViewModel constructor(
    private val appContext: Context,
    private val communicator: Communicator,
    private val exchangeRatePersistentDataSource: ExchangeRatePersistentDataSource
) :
    ViewModel() {

    private val TAG = javaClass.toString()

    private val compositeDisposable = CompositeDisposable()
    private var countryNameFrom: String = "USD"
    private var countryNameTo: String = "USD"

    val currencyRatesMap = MutableLiveData<Map<String, Double>>()
    val textFrom = MutableLiveData<String>()
    val textTo = MutableLiveData<String>()

    fun fetchCurrencyData(): LiveData<Map<String, Double>> {
        viewModelScope.launch(Dispatchers.IO) {
            val rates = exchangeRatePersistentDataSource.getExchangeRates()
            currencyRatesMap.postValue(rates.map { it.countryName to it.exchangeRate }.toMap())
        }

        return currencyRatesMap
    }

    fun setCurrencyRate(action: Action, position: Int) {
        if (action == Action.FROM) {
            countryNameFrom = currencyRatesMap.value?.keys?.elementAt(position)!!
        } else {
            countryNameTo = currencyRatesMap.value?.keys?.elementAt(position)!!
        }

        textFrom.value?.let { convertRates(Action.FROM, it) }
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

    fun convertRates(action: Action, amountText: String) {
        val amountFrom = amountText.replace(",", "").toDoubleOrNull()

        if (action == Action.FROM) {
            postExchangeRate(textFrom, textTo, countryNameFrom, countryNameTo, amountFrom)
        } else {
            postExchangeRate(textTo, textFrom, countryNameTo, countryNameFrom, amountFrom)
        }
    }

    fun dispose() {
        compositeDisposable.dispose()
        try {
            WorkManager.getInstance(appContext).cancelUniqueWork(WORK_NAME)
        }catch (e: Throwable){
            e.printStackTrace()
        }

    }

    fun registerScheduler() {
        observeScheduler()

        CoroutineScope(Dispatchers.IO).launch {
            setupRecurringWork()
        }
    }

    private fun postExchangeRate(
        from: MutableLiveData<String>,
        to: MutableLiveData<String>,
        nameFrom: String,
        nameTo: String,
        amountFrom: Double?
    ) {
        amountFrom?.let {
            from.postValue(DecimalFormat("#,###").format(amountFrom))
            to.postValue(
                DecimalFormat("#,###.00").format(
                    getExchangedAmount(
                        amountFrom.toString().toDouble(),
                        from = nameFrom,
                        to = nameTo
                    )
                ).toString()
            )
        } ?: if (!isEmpty(to.value)) {
            to.postValue("")
        }
    }

    private fun observeScheduler() {
        communicator.observe(Map::class.java).subscribe {
            try {
                val map = it as Map<String, Double>
                currencyRatesMap.postValue(map)
            } catch (e: Exception) {
                Log.v(TAG, e.toString())
            }
        }
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
