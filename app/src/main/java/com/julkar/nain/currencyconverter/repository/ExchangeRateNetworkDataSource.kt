package com.julkar.nain.currencyconverter.repository

import com.julkar.nain.currencyconverter.domain.CurrencyRatesResponse
import com.julkar.nain.currencyconverter.service.CurrencyRatesService
import com.julkar.nain.currencyconverter.util.ACCESS_KEY
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Julkar Nain
 * @since 12 Jun 2020
 */
@Singleton
class ExchangeRateNetworkDataSource @Inject constructor(private val currencyRatesService: CurrencyRatesService) :
    ExchangeRateRepository<Single<CurrencyRatesResponse>> {

    override fun getExchangeRates(): Single<CurrencyRatesResponse> {
        return currencyRatesService.getCurrencyRates(ACCESS_KEY)
    }
}