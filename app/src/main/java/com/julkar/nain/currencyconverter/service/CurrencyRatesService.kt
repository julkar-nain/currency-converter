package com.julkar.nain.currencyconverter.service

import com.julkar.nain.currencyconverter.domain.CurrencyRatesResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * @author Julkar Nain
 * @since 11 Jun 2020
 */
interface CurrencyRatesService {
    @GET("/live")
    fun getCurrencyRates(@Query("access_key") accessKey: String): Observable<CurrencyRatesResponse>
}