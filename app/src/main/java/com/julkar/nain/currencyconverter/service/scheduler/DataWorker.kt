package com.julkar.nain.currencyconverter.service.scheduler

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.julkar.nain.currencyconverter.application.MainApplication
import com.julkar.nain.currencyconverter.database.entity.ExchangeRate
import com.julkar.nain.currencyconverter.repository.ExchangeRateNetworkDataSource
import com.julkar.nain.currencyconverter.repository.ExchangeRatePersistentDataSource
import com.julkar.nain.currencyconverter.service.Communicator.Communicator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Julkar Nain
 * @since 12 Jun 2020
 */
class DataWorker(val appContext: Context, params: WorkerParameters) :
    Worker(appContext, params) {

    @Inject
    lateinit var exchangeRateNetworkDataSource: ExchangeRateNetworkDataSource

    @Inject
    lateinit var exchangeRatePersistentDataSource: ExchangeRatePersistentDataSource

    @Inject
    lateinit var communicator: Communicator
    private val compositeDisposable = CompositeDisposable()
    private val KEY_USA = "USDUSD"

    private val TAG = javaClass.name

    init {
        val application = appContext.applicationContext as MainApplication
        application.getAppComponent()?.getMainSubComponent()?.create()?.inject(this)
    }

    override fun doWork(): Result {
        return try {
            fetchNetworkData()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun saveExchangeRates(ratesMap: Map<String, Double>) =
        CoroutineScope(Dispatchers.IO).launch {
            val rateList = ratesMap.map {
                ExchangeRate(it.key, it.value)
            }.toList()

            exchangeRatePersistentDataSource.saveExchangeRates(rateList)
        }

    private fun fetchNetworkData() {
        compositeDisposable.add(
            exchangeRateNetworkDataSource.getExchangeRates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val map = it.quotes
                    map[KEY_USA] = 1.0
                    val formattedMap = map.mapKeys { it.key.removeRange(0, 3) }

                    communicator.emit(formattedMap)

                    saveExchangeRates(formattedMap)
                }, {
                    Toast.makeText(
                        appContext,
                        "Data not fetched network issue happened",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.d(TAG, it.toString())
                })
        )
    }

    override fun onStopped() {
        super.onStopped()
        compositeDisposable.dispose()
    }
}