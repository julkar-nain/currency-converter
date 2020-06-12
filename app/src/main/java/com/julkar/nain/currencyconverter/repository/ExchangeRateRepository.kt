package com.julkar.nain.currencyconverter.repository

/**
 * @author Julkar Nain
 * @since 12 Jun 2020
 */

interface ExchangeRateRepository {
    fun getExchangeRates(): Map<String, Double>
}