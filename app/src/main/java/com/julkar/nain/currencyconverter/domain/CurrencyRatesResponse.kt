package com.julkar.nain.currencyconverter.domain

import com.google.gson.annotations.SerializedName

/**
 * @author Julkar Nain
 * @since 11 Jun 2020
 */
data class CurrencyRatesResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("terms")val terms: String,
    @SerializedName("privacy")val privacy: String,
    @SerializedName("timestamp")val timestamp: Long,
    @SerializedName("quotes")val quotes: MutableMap<String, Double>
)