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
import com.julkar.nain.currencyconverter.util.Utils
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

    private val TAG = javaClass.name

    @Inject
    lateinit var exchangeRateNetworkDataSource: ExchangeRateNetworkDataSource

    @Inject
    lateinit var exchangeRatePersistentDataSource: ExchangeRatePersistentDataSource

    @Inject
    lateinit var communicator: Communicator

    @Inject
    lateinit var utils: Utils

    private val compositeDisposable = CompositeDisposable()


    init {
        val application = appContext.applicationContext as MainApplication
        application.getAppComponent()?.inject(this)
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
                    val formattedMap = utils.prepareFormattedData(it.quotes)
                    communicator.emit(formattedMap)
                    saveExchangeRates(formattedMap)
                }, {
                    Toast.makeText(
                        appContext,
                        "Data not fetched network issue happened",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.d(TAG, it.toString())

                    throw it
                })
        )
    }

    override fun onStopped() {
        super.onStopped()
        compositeDisposable.dispose()
    }
}