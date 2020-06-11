package com.julkar.nain.currencyconverter.service

import com.julkar.nain.currencyconverter.dmain.CurrencyRatesResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*


/**
 * @author Julkar Nain
 * @since 11 Jun 2020
 */
interface CurrencyRatesService {
    @GET("/live")
    fun getCurrencyRates(@Query("access_key") accessKey: String): Observable<CurrencyRatesResponse>
}