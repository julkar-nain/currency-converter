package com.julkar.nain.currencyconverter.dmain

/**
 * @author Julkar Nain
 * @since 11 Jun 2020
 */
data class CurrencyRatesResponse(
    val success: Boolean,
    val terms: String,
    val privacy: String,
    val timestamp: Long,
    val quotes: Map<String, Double>
)