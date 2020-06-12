package com.julkar.nain.currencyconverter.repository

import com.julkar.nain.currencyconverter.application.di.ActivityScope
import com.julkar.nain.currencyconverter.database.dao.ExchangeRateDao
import com.julkar.nain.currencyconverter.database.entity.ExchangeRate
import javax.inject.Inject

/**
 * @author Julkar Nain
 * @since 12 Jun 2020
 */
@ActivityScope
class ExchangeRatePersistentDataSource @Inject constructor(private val exchangeRateDao: ExchangeRateDao) {

    suspend fun getExchangeRates(): List<ExchangeRate> {
        return exchangeRateDao.getExchangeRates()
    }

    suspend fun saveExchangeRates(rates: List<ExchangeRate>) {
        exchangeRateDao.insert(rates)
    }
}