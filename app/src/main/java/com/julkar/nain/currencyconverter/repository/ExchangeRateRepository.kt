package com.julkar.nain.currencyconverter.repository

import androidx.lifecycle.LiveData
import io.reactivex.Single

/**
 * @author Julkar Nain
 * @since 12 Jun 2020
 */

interface ExchangeRateRepository<T> {
    fun getExchangeRates(): T
}